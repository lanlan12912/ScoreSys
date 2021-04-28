package com.yelanlan.scoremanagersystem.ServiceImpl;

import com.yelanlan.scoremanagersystem.DAO.DepartmentDAO;
import com.yelanlan.scoremanagersystem.DAO.UserDAO;
import com.yelanlan.scoremanagersystem.Enum.DepartEnum;
import com.yelanlan.scoremanagersystem.RepositoryIface.Common.IMessage;
import com.yelanlan.scoremanagersystem.RepositoryImpl.Common.Message;
import com.yelanlan.scoremanagersystem.RepositoryImpl.Department;
import com.yelanlan.scoremanagersystem.RepositoryImpl.User;
import com.yelanlan.scoremanagersystem.ServiceIface.IDepartmentService;
import com.yelanlan.scoremanagersystem.ServiceIface.IUserService;
import com.yelanlan.scoremanagersystem.Utils.DateUtils;
import com.yelanlan.scoremanagersystem.Utils.ParamUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class DepartmentService implements IDepartmentService {

    @Autowired
    private DepartmentDAO departmentDAO;
    @Autowired
    private IUserService userService;
    @Autowired
    private UserDAO userDAO;

    /**
     * 更新院系信息
     * @param map
     * @return
     * */
    @Override
    public IMessage updateDepart(Map<String, String> map) {
        if(!ParamUtils.allNotNull(map.get("id"))){
            return new Message(false,"找不到院系信息");
        }
        Department department = departmentDAO.findDepartById(String.valueOf(map.get("id")));
        //先将已存在的院系删除
        departmentDAO.delete(department);
        department.setDepartName(String.valueOf(map.get("departName")));
        if(ParamUtils.allNotNull(map.get("departDesc"))){
            department.setDepartDesc(map.get("departDesc"));
        }
        //上级院系与创建人不能更改，无需处理
        departmentDAO.save(department);//将更新的院系信息保存
        return new Message(true,"修改成功");
    }

    /**
     * 新增院系信息
     * @param map
     * @return
     * */
    @Override
    public IMessage saveDepart(Map<String, String> map) {
        try {
            //判断 是否已经存在院系信息，若不存在生成id
            String id = UUID.randomUUID().toString().replaceAll("-", "");
            String parentId = null;
            if(ParamUtils.allNotNull(map.get("parentId"))){
                parentId = map.get("parentId");
            }
            User currentUser = userService.getCurrentUser();
            //创建时间
            Timestamp crtDate = DateUtils.getCurrentTime();
            Department department = new Department(id,parentId,DepartEnum.valueOf(map.get("departType")).getName(),
                    String.valueOf(map.get("departName")),String.valueOf(map.get("departDesc")), currentUser.getUserNumber(),crtDate);
            departmentDAO.save(department);//保存院系信息
            return new Message(true,"保存成功");
        }catch (Exception e ){
            e.printStackTrace();
            return new Message(false,"系统异常");
        }
    }

    /**
     * 删除院系信息
     * @param id
     * @return
     * */
    @Override
    public IMessage delDepartment(String id){
        try{
            Department department = departmentDAO.findDepartById(id);
            if(null == department){
                return new Message(false,"该信息已被删除，无法再操作");
            }
            //属于院的用户
            List<User> collegeusers = userDAO.findAllByCollegeId(department.getId());
            //属于系的用户
            List<User> departUser = userDAO.findAllByDepartmentId(department.getId());
            if(collegeusers.size()>0 || departUser.size()>0){
                return new Message(false,"该院/系已有用户存在，无法删除");
            }
            //删除院信息时，查同时删除其下的系信息
            if(DepartEnum.valueOf(department.getDepartType()) == DepartEnum.COLLEGE){
               departmentDAO.deleteAllByParentId(department.getId());
            }
            departmentDAO.delete(department);
            return new Message(true,"删除成功");
        }catch (Exception e){
            e.printStackTrace();
            return new Message(false,"系统异常");
        }
    }

}
