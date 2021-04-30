package com.yelanlan.scoremanagersystem.ServiceImpl;

import com.yelanlan.scoremanagersystem.DAO.DepartmentDAO;
import com.yelanlan.scoremanagersystem.DAO.UserDAO;
import com.yelanlan.scoremanagersystem.Enum.DepartEnum;
import com.yelanlan.scoremanagersystem.RepositoryIface.Common.IMessage;
import com.yelanlan.scoremanagersystem.RepositoryImpl.Common.Message;
import com.yelanlan.scoremanagersystem.RepositoryImpl.DTO.DepartMentDTO;
import com.yelanlan.scoremanagersystem.RepositoryImpl.DTO.MenuTreeDTO;
import com.yelanlan.scoremanagersystem.RepositoryImpl.Department;
import com.yelanlan.scoremanagersystem.RepositoryImpl.User;
import com.yelanlan.scoremanagersystem.ServiceIface.IDepartmentService;
import com.yelanlan.scoremanagersystem.ServiceIface.IUserService;
import com.yelanlan.scoremanagersystem.Utils.DateUtils;
import com.yelanlan.scoremanagersystem.Utils.ParamUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

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
            Department department = new Department(id,parentId,map.get("departType"),map.get("departName"),map.get("departDesc"), currentUser.getUserNumber(),crtDate);
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
            List<Department> childDepart = departmentDAO.findAllByParentId(id);
            List<String> departIds = childDepart.stream().map(Department::getId).collect(Collectors.toList());
            departIds.add(id);
            if(userDAO.findAllByCollegeAndDepart(departIds,departIds).size()>0){
                return new Message(false,"该院/系已有用户存在，无法删除");
            }
            if(DepartEnum.valueOf(department.getDepartType()) == DepartEnum.CLASS){//如果是班信息，直接删除
                departmentDAO.delete(department);
                return new Message(true,"删除成功");
            }else {//如果是院信息，先验证其下的系是否已经被使用，再进行删除
                if(childDepart.size()>0){
                    Message message = delChildDepart(childDepart);
                    if(!message.isSuccess()){
                        return new Message(false,message.getMsg());
                    }
                }
                //最后删除父菜单
                departmentDAO.delete(department);
                return new Message(true,"删除成功");
            }
        }catch (Exception e){
            e.printStackTrace();
            return new Message(false,"系统异常");
        }
    }

    /**
     * 删除子院/系信息
     * @param departList
     * @return
     * */
    public Message delChildDepart(List<Department> departList){
        for(Department department : departList){
                List<Department> childDepart = departmentDAO.findAllByParentId(department.getId());//子菜单/子模块
                List<String> departIds = childDepart.stream().map(Department::getId).collect(Collectors.toList());
                departIds.add(department.getId());
                if (userDAO.findAllByCollegeAndDepart(departIds,departIds).size()>0){
                    return new Message(false,"该院/系已有用户存在，无法删除");
                }
                if(DepartEnum.valueOf(department.getDepartType()) == DepartEnum.CLASS){
                    departmentDAO.delete(department);
                }else {
                    if(childDepart.size()>0){
                        Message message = delChildDepart(childDepart);
                        if(!message.isSuccess()){//删除子院信息失败时立即返回
                            return message;
                        }
                    }
                    departmentDAO.delete(department);
                }
            }
        return new Message(true,"删除成功");
    }

    /**
     * 查询院系树
     * @param type
     * @return
     * */
    @Override
    public IMessage quDepartTree(DepartEnum type){
        try{
            List<MenuTreeDTO> departDTOs = new ArrayList<>();
            List<Department> departList = departmentDAO.findAllAndOrderBTime();
            if(departList.size()>0){
                //去除根节点
                List<Department> childDepart = departList.stream().filter(department -> null != department.getParentId() && !"".equals(department.getParentId())).collect(Collectors.toList());
                if(type == DepartEnum.COLLEGE || type == DepartEnum.DEPARTMENT){//将院和系的信息过滤出来
                    childDepart = childDepart.stream().filter(department -> DepartEnum.valueOf(department.getDepartType()) == DepartEnum.COLLEGE
                            || DepartEnum.valueOf(department.getDepartType()) == DepartEnum.DEPARTMENT).collect(Collectors.toList());
                }
                for(Department department : departList){
                    if(!ParamUtils.allNotNull(department.getParentId())){//父菜单id为空为根节点
                        MenuTreeDTO departmentDTO = new MenuTreeDTO(department.getId(),department.getDepartName(),false,true);
                        departmentDTO.setChildren(dealDepartTree(department.getId(),childDepart));
                        departDTOs.add(departmentDTO);
                    }
                }
            }
            Message message = new Message(true,"查询成功");
            message.setData(departDTOs);
            return message;
        }catch (Exception e){
            e.printStackTrace();
            return new Message(false,"系统异常");
        }
    }

    /**
     * 处理院系树
     * @param departId
     * @param childDepart
     * @return
     * */
    public List<MenuTreeDTO> dealDepartTree(String departId,List<Department> childDepart){
        List<MenuTreeDTO> lists = new ArrayList<>();
        for(Department department : childDepart){
            if(department.getParentId().equals(departId)){
                MenuTreeDTO dto = new MenuTreeDTO(department.getId(),department.getDepartName(),false,true);
                dto.setChildren(dealDepartTree(department.getId(),childDepart.stream().filter(depart -> !depart.getId().equals(department.getId())).collect(Collectors.toList())));
                lists.add(dto);
            }
        }
        return lists;
    }

    /**
     * 根据id查询院系信息
     * @param id
     * @return
     * */
    @Override
    public IMessage quDepartById(String id){
        Department department = departmentDAO.findDepartById(id);
        if(null == department){
            return new Message(false,"该院/系的信息已不存在");
        }
        String parentName = null;
        if(ParamUtils.allNotNull(department.getParentId())){
            parentName =  departmentDAO.findDepartById(department.getParentId()).getDepartName();
        }
        DepartMentDTO departMentDTO = new DepartMentDTO(department.getId(),department.getParentId(),department.getDepartType(),
                department.getDepartName(),department.getDepartDesc(),department.getCrtUser(),department.getCrtDate(),parentName);
        Message message  = new Message(true,"查询成功");
        message.setData(departMentDTO);
        return message;
    }

}
