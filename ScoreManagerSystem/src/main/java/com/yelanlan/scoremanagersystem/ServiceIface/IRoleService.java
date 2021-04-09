package com.yelanlan.scoremanagersystem.ServiceIface;

import com.yelanlan.scoremanagersystem.RepositoryIface.Common.IMessage;
import com.yelanlan.scoremanagersystem.RepositoryImpl.Role;

import java.util.List;

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
}
