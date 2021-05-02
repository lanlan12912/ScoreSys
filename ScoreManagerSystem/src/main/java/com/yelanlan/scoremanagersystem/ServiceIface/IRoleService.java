package com.yelanlan.scoremanagersystem.ServiceIface;

import com.yelanlan.scoremanagersystem.RepositoryIface.Common.IMessage;
import com.yelanlan.scoremanagersystem.RepositoryImpl.Role;

import java.util.List;
import java.util.Map;

public interface IRoleService {
    /**
     * 查询所有的角色信息
     * @param start
     * @param limit
     * @return
     * */
    public IMessage quAllRoles(int start,int limit);

    /**
     * 添加角色信息
     * @param role
     * @return
     * */
    public IMessage addRole(Role role);

    /**
     * 删除角色信息
     * @param roles
     * @return
     * */
    public IMessage delRoles(List<String> roles);

    /**
     * 获取角色拥有权限的菜单
     * @param roleId
     * @return
     * */
    public IMessage getAuthMenuRes(String roleId);

    /**
     * 将角色与菜单资源绑定
     * @param roleId
     * @param resIds
     * @return
     * */
    public IMessage authorization(String roleId,List<String> resIds);

    /**
     * 不分页查询角色列表
     * @param
     * @return
     * */
    public IMessage getRoleListNoPage();

    /**
     *获取角色绑定的资源，不止菜单，还有可能是其他的
     * @param userNumber
     * @return
     * */
    public IMessage getAuthRes(String userNumber);

    /**
     * 给用户分配角色
     * @param map
     * @return
     * */
    public IMessage distrUserRoles(Map<String,Object> map);
}
