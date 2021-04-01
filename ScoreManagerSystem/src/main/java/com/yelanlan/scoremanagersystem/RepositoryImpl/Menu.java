package com.yelanlan.scoremanagersystem.RepositoryImpl;

import com.yelanlan.scoremanagersystem.RepositoryIface.IMenu;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.sql.Timestamp;

@Entity
@Table(name="menu")
public class Menu implements IMenu, Serializable {
    private static final long serialVersionUID = -8721494702648240401L;
    @Id
    @Column(name = "menu_id")
    private String menuId;//菜单id
    @Column(name = "menu_name")
    private String menuName;//菜单名称
    @Column(name = "menu_icon")
    private String menuIcon;//菜单图标
    @Column(name = "menu_path")
    private String menuPath;//菜单路径
    @Column(name = "type")
    private String type;//菜单类型
    @Column(name = "parent_id")
    private String parentId;//父菜单id
    @Column(name = "leaf_flag")
    private Integer leafFlag;//是否为叶子菜单，1是，0否
    @Column(name = "order")
    private Integer order;//排序
    @Column(name = "crt_user")
    private String crtUser;//创建人
    @Column(name = "crt_date")
    private Timestamp crtDate;//创建时间
    @Column(name = "modify_user")
    private String modifyUser;//修改人
    @Column(name = "modify_date")
    private Timestamp modifyDate;//修改时间

    public Menu() {
    }

    public Menu(String menuId, String menuName, String menuIcon, String menuPath, String type, Integer order, String crtUser, Timestamp crtDate, String modifyUser, Timestamp modifyDate) {
        this.menuId = menuId;
        this.menuName = menuName;
        this.menuIcon = menuIcon;
        this.menuPath = menuPath;
        this.type = type;
        this.order = order;
        this.crtUser = crtUser;
        this.crtDate = crtDate;
        this.modifyUser = modifyUser;
        this.modifyDate = modifyDate;
    }

    public Menu(String menuId, String menuName, String menuIcon, String menuPath, String type, String parentId, Integer leafFlag, Integer order, String crtUser, Timestamp crtDate, String modifyUser, Timestamp modifyDate) {
        this.menuId = menuId;
        this.menuName = menuName;
        this.menuIcon = menuIcon;
        this.menuPath = menuPath;
        this.type = type;
        this.parentId = parentId;
        this.leafFlag = leafFlag;
        this.order = order;
        this.crtUser = crtUser;
        this.crtDate = crtDate;
        this.modifyUser = modifyUser;
        this.modifyDate = modifyDate;
    }

    @Override
    public String getMenuId() {
        return menuId;
    }

    @Override
    public void setMenuId(String menuId) {
        this.menuId = menuId;
    }

    @Override
    public String getMenuName() {
        return menuName;
    }

    @Override
    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    @Override
    public String getMenuIcon() {
        return menuIcon;
    }

    @Override
    public void setMenuIcon(String menuIcon) {
        this.menuIcon = menuIcon;
    }

    @Override
    public String getMenuPath() {
        return menuPath;
    }

    @Override
    public void setMenuPath(String menuPath) {
        this.menuPath = menuPath;
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String getParentId() {
        return parentId;
    }

    @Override
    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    @Override
    public Integer getLeafFlag() {
        return leafFlag;
    }

    @Override
    public void setLeafFlag(Integer leafFlag) {
        this.leafFlag = leafFlag;
    }

    @Override
    public Integer getOrder() {
        return order;
    }

    @Override
    public void setOrder(Integer order) {
        this.order = order;
    }

    @Override
    public String getCrtUser() {
        return crtUser;
    }

    @Override
    public void setCrtUser(String crtUser) {
        this.crtUser = crtUser;
    }

    @Override
    public Timestamp getCrtDate() {
        return crtDate;
    }

    @Override
    public void setCrtDate(Timestamp crtDate) {
        this.crtDate = crtDate;
    }

    @Override
    public String getModifyUser() {
        return modifyUser;
    }

    @Override
    public void setModifyUser(String modifyUser) {
        this.modifyUser = modifyUser;
    }

    @Override
    public Timestamp getModifyDate() {
        return modifyDate;
    }

    @Override
    public void setModifyDate(Timestamp modifyDate) {
        this.modifyDate = modifyDate;
    }
}
