package com.yelanlan.scoremanagersystem.RepositoryImpl;

import com.yelanlan.scoremanagersystem.RepositoryIface.IRoleRes;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
/**角色与菜单，角色与用户的绑定关系表*/
@Entity
@Table(name = "role_res")
public class RoleRes implements IRoleRes, Serializable {
    private static final long serialVersionUID = -4431116627918991566L;
    @Id
    @Column(name = "rel_id")
    private String relId;
    @Column(name = "role_id")
    private String roleId;
    @Column(name = "res_id")
    private String resId;

    public RoleRes() {
    }

    public RoleRes(String relId, String roleId, String resId) {
        this.relId = relId;
        this.roleId = roleId;
        this.resId = resId;
    }

    @Override
    public String getRelId() {
        return relId;
    }

    @Override
    public void setRelId(String relId) {
        this.relId = relId;
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
    public String getResId() {
        return resId;
    }

    @Override
    public void setResId(String resId) {
        this.resId = resId;
    }
}
