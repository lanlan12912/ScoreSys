package com.yelanlan.scoremanagersystem.RepositoryImpl;

import com.yelanlan.scoremanagersystem.RepositoryIface.IRole;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.sql.Timestamp;

@Entity
@Table(name = "role")
public class Role implements IRole, Serializable {
    private static final long serialVersionUID = 8189573936170138724L;
    @Id
    @Column(name = "role_id")
    private String roleId;
    @Column(name = "role_name")
    private String roleName;
    @Column(name = "crt_user")
    private String crtUser;
    @Column(name = "crt_date")
    private Timestamp crtDate;
    @Column(name = "role_des")
    private String roleDes;

    public Role() {
    }

    public Role(String roleId, String roleName, String crtUser, Timestamp crtDate, String roleDes) {
        this.roleId = roleId;
        this.roleName = roleName;
        this.crtUser = crtUser;
        this.crtDate = crtDate;
        this.roleDes = roleDes;
    }

    @Override
    public String getRoleId() {
        return roleId;
    }

    @Override
    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    @Override
    public String getRoleName() {
        return roleName;
    }

    @Override
    public void setRoleName(String roleName) {
        this.roleName = roleName;
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
    public String getRoleDes() {
        return roleDes;
    }

    @Override
    public void setRoleDes(String roleDes) {
        this.roleDes = roleDes;
    }
}
