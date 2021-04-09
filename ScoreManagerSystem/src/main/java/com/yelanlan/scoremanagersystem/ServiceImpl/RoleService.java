package com.yelanlan.scoremanagersystem.ServiceImpl;

import com.yelanlan.scoremanagersystem.DAO.RoleDao;
import com.yelanlan.scoremanagersystem.RepositoryIface.Common.IMessage;
import com.yelanlan.scoremanagersystem.RepositoryImpl.Common.Message;
import com.yelanlan.scoremanagersystem.RepositoryImpl.Role;
import com.yelanlan.scoremanagersystem.ServiceIface.IRoleService;
import com.yelanlan.scoremanagersystem.ServiceIface.IUserService;
import com.yelanlan.scoremanagersystem.Utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.UUID;

@Service
public class RoleService implements IRoleService {
    @Autowired
    private RoleDao roleDao;
    @Autowired
    private IUserService userService;
    /**
     * 查询所有的角色信息
     * @param start
     * @param limit
     * @return
     * */
    @Override
    public IMessage quAllRoles(int start,int limit){
        try {
            Page<Role> rolePage = roleDao.findAllTUserName(PageRequest.of(start-1,limit));
            Message message = new Message(true,"查询成功");
            message.setData(rolePage);
            return message;
        }catch (Exception e){
            e.printStackTrace();
            return new Message(false,"系统异常");
        }
    }

    /**
     * 添加角色信息
     * @param roleName
     * @param roleDes
     * @return
     * */
    @Override
    public IMessage addRole(String roleName,String roleDes){
        try{
            String roleId = UUID.randomUUID().toString().replaceAll("-", "");
            String crtUser = userService.getCurrentUser().getUserNumber();
            Timestamp crtDate = DateUtils.getCurrentTime();
            Role role = new Role(roleId,roleName,crtUser,crtDate,roleDes);
            roleDao.save(role);
            return new Message(true,"添加成功");
        }catch (Exception e){
            e.printStackTrace();
            return new Message(false,"系统异常");
        }
    }
}
