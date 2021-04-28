package com.yelanlan.scoremanagersystem.RepositoryIface;

import java.sql.Timestamp;

public interface IDepartment {
    public String getId();
    public void setId(String id);
    public String getParentId();
    public void setParentId(String parentId);
    public String getDepartType();
    public void setDepartType(String departType);
    public String getDepartName();
    public void setDepartName(String departName);
    public String getDepartDesc();
    public void setDepartDesc(String departDesc);
    public String getCrtUser();
    public void setCrtUser(String crtUser);
    public Timestamp getCrtDate();
    public void setCrtDate(Timestamp crtDate);
}
