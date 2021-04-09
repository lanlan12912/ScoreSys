package com.yelanlan.scoremanagersystem.ServiceImpl;

import com.yelanlan.scoremanagersystem.DAO.RoleDao;
import com.yelanlan.scoremanagersystem.DAO.RoleResDAO;
import com.yelanlan.scoremanagersystem.Enum.MenuTypeEnum;
import com.yelanlan.scoremanagersystem.RepositoryIface.Common.IMessage;
import com.yelanlan.scoremanagersystem.RepositoryImpl.Common.Message;
import com.yelanlan.scoremanagersystem.RepositoryImpl.DTO.MenuTreeDTO;
import com.yelanlan.scoremanagersystem.RepositoryImpl.Role;
import com.yelanlan.scoremanagersystem.RepositoryImpl.RoleRes;
import com.yelanlan.scoremanagersystem.ServiceIface.IMenuService;
import com.yelanlan.scoremanagersystem.ServiceIface.IRoleService;
import com.yelanlan.scoremanagersystem.ServiceIface.IUserService;
import com.yelanlan.scoremanagersystem.Utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class RoleService implements IRoleService {
    @Autowired
    private RoleDao roleDao;
    @Autowired
    private IUserService userService;
    @Autowired
    private IMenuService menuService;
    @Autowired
    private RoleResDAO roleResDAO;
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
     * @param role
     * @return
     * */
    @Override
    public IMessage addRole(Role role){
        try{
            if(null != role.getRoleId() && !"".equals(role.getRoleId())){
                if(roleDao.updateRole(role.getRoleName(),role.getRoleDes(),role.getRoleId())>0){
                    return new Message(true,"修改成功");
                }else {
                    return new Message(false,"修改失败");
                }
            }
            String roleId = UUID.randomUUID().toString().replaceAll("-", "");
            String crtUser = userService.getCurrentUser().getUserNumber();
            Timestamp crtDate = DateUtils.getCurrentTime();
            role.setRoleId(roleId);
            role.setCrtDate(crtDate);
            role.setCrtUser(crtUser);
            roleDao.save(role);
            return new Message(true,"添加成功");
        }catch (Exception e){
            e.printStackTrace();
            return new Message(false,"系统异常");
        }
    }

    /**
     * 删除角色信息
     * @param roles
     * @return
     * */
    @Override
    public IMessage delRoles(List<String> roles){
        try{
            if(roles.size()>0){
                roleDao.deleteAllByRoleId(roles);
            }
            return new Message(true,"删除成功");
        }catch (Exception e){
            e.printStackTrace();
            return new Message(false,"系统异常");
        }
    }

    /**
     * 获取角色拥有权限的菜单
     * @param roleId
     * @return
     * */
    @Override
    public IMessage getAuthMenuRes(String roleId){
        try {
            //获取该角色被授权的资源
            List<RoleRes> roleResList = roleResDAO.findAllByRoleId(roleId);
            List<String> resIds = roleResList.stream().map(RoleRes::getResId).collect(Collectors.toList());
            //获取菜单树
            IMessage message = menuService.getMenuModule(MenuTypeEnum.PAGE);
            List<MenuTreeDTO> menuTrees = (List<MenuTreeDTO>) message.getData();
            //处理菜单是否给用户授权
            if(menuTrees.size()>0){
                for(MenuTreeDTO menu : menuTrees){
                    if (resIds.stream().anyMatch(s -> s.equals(menu.getId()))){
                        menu.setChecked(true);
                    }else{
                        menu.setChecked(false);
                    }
                    if(null != menu.getChildren() && menu.getChildren().size()>0){
                        menu.setChildren(dealResTree(menu.getChildren(),resIds));
                    }
                }
            }
            message.setData(menuTrees);
            return message;
        }catch (Exception e){
            e.printStackTrace();
            return new Message(false,"系统异常");
        }
    }

    /**
     * 处理资源树
     * @param menuList
     * @param resIds
     * @return
     * */
    public List<MenuTreeDTO> dealResTree(List<MenuTreeDTO> menuList,List<String> resIds){
        for(MenuTreeDTO menu: menuList){
            if(resIds.stream().anyMatch(s -> s.equals(menu.getId()))){
                menu.setChecked(true);
            }else {
                menu.setChecked(false);
            }
            if(null != menu.getChildren() && menu.getChildren().size()>0){
                dealResTree(menu.getChildren(),resIds);
            }
        }
        return menuList;
    }

    /**
     * 将角色与菜单资源绑定
     * @param roleId
     * @param resIds
     * @return
     * */
    @Override
    public IMessage authorization(String roleId,List<String> resIds){
        try {
            //先将角色已绑定的资源关系删除
            roleResDAO.deleteAllByRoleId(roleId);
            for (String resId : resIds){
                String relId = UUID.randomUUID().toString().replaceAll("-", "");
                RoleRes roleRes = new RoleRes(relId,roleId,resId);
                roleResDAO.save(roleRes);
            }
            return new Message(true,"成功授权");
        }catch (Exception e){
            e.printStackTrace();
            return new Message(false,"系统异常");
        }
    }
}
