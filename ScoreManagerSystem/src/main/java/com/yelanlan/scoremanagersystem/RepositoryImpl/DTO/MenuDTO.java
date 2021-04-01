package com.yelanlan.scoremanagersystem.RepositoryImpl.DTO;

import java.io.Serializable;
import java.util.List;

public class MenuDTO implements Serializable {
    private static final long serialVersionUID = 398026227171800378L;
    private String menuId;//菜单id
    private String menuName;//菜单名称
    private String menuIcon;//菜单图标
    private String menuPath;//菜单路径
    private String type;//菜单类型
    private Integer leafFlag;//是否为叶子菜单，1是，0否
    private Integer order;//排序
    private List<MenuDTO> childPages;//子菜单

    public MenuDTO() {
    }

    public MenuDTO(String menuId, String menuName, String menuIcon, String menuPath, String type, Integer leafFlag, Integer order) {
        this.menuId = menuId;
        this.menuName = menuName;
        this.menuIcon = menuIcon;
        this.menuPath = menuPath;
        this.type = type;
        this.leafFlag = leafFlag;
        this.order = order;
    }

    public String getMenuId() {
        return menuId;
    }

    public void setMenuId(String menuId) {
        this.menuId = menuId;
    }

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    public String getMenuIcon() {
        return menuIcon;
    }

    public void setMenuIcon(String menuIcon) {
        this.menuIcon = menuIcon;
    }

    public String getMenuPath() {
        return menuPath;
    }

    public void setMenuPath(String menuPath) {
        this.menuPath = menuPath;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getLeafFlag() {
        return leafFlag;
    }

    public void setLeafFlag(Integer leafFlag) {
        this.leafFlag = leafFlag;
    }

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    public List<MenuDTO> getChildPages() {
        return childPages;
    }

    public void setChildPages(List<MenuDTO> childPages) {
        this.childPages = childPages;
    }
}
