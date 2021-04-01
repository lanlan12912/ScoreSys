package com.yelanlan.scoremanagersystem.RepositoryIface;

import java.sql.Timestamp;

public interface IRole {
    public String getRoleId();
    public void setRoleId(String roleId);
    public String getRoleName();
    public void setRoleName(String roleName);
    public String getCrtUser();
    public void setCrtUser(String crtUser);
    public Timestamp getCrtDate();
    public void setCrtDate(Timestamp crtDate);
    public String getRoleDes();
    public void setRoleDes(String roleDes);
}
