package com.yelanlan.scoremanagersystem.ServiceIface;

import com.yelanlan.scoremanagersystem.RepositoryIface.Common.IMessage;

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
     * @param roleName
     * @param roleDes
     * @return
     * */
    public IMessage addRole(String roleName,String roleDes);
}
