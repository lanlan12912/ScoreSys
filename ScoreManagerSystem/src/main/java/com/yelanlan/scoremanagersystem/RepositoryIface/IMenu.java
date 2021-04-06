package com.yelanlan.scoremanagersystem.RepositoryIface;

import java.sql.Timestamp;

public interface IMenu {
    public String getMenuId();
    public void setMenuId(String menuId);
    public String getMenuName();
    public void setMenuName(String menuName);
    public String getMenuIcon();
    public void setMenuIcon(String menuIcon);
    public String getMenuPath();
    public void setMenuPath(String menuPath);
    public String getType();
    public void setType(String type);
    public String getParentId();
    public void setParentId(String parentId);
    public String getParentName();
    public void setParentName(String parentName);
    public Integer getLeafFlag();
    public void setLeafFlag(Integer leafFlag);
    public Integer getOrders();
    public void setOrders(Integer orders);
    public String getCrtUser();
    public void setCrtUser(String crtUser);
    public Timestamp getCrtDate();
    public void setCrtDate(Timestamp crtDate);
    public String getModifyUser();
    public void setModifyUser(String modifyUser);
    public Timestamp getModifyDate();
    public void setModifyDate(Timestamp modifyDate);

}
