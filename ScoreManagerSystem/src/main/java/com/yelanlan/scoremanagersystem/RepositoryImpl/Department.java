package com.yelanlan.scoremanagersystem.RepositoryImpl;

import com.yelanlan.scoremanagersystem.RepositoryIface.IDepartment;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.sql.Timestamp;

@Entity
@Table(name = "department")
public class Department implements Serializable, IDepartment {
    private static final long serialVersionUID = 5045568824690482637L;
    @Id
    @Column(name = "id")
    private String id;
    @Column(name = "parent_id")
    private String parentId;
    @Column(name = "depart_type")
    private String departType;
    @Column(name = "depart_name")
    private String departName;
    @Column(name = "depart_desc")
    private String departDesc;
    @Column(name = "crt_user")
    private String crtUser;
    @Column(name = "crt_date")
    private Timestamp crtDate;

    public Department() {
    }

    public Department(String id, String parentId, String departType, String departName, String departDesc, String crtUser, Timestamp crtDate) {
        this.id = id;
        this.parentId = parentId;
        this.departType = departType;
        this.departName = departName;
        this.departDesc = departDesc;
        this.crtUser = crtUser;
        this.crtDate = crtDate;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
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
    public String getDepartType() {
        return departType;
    }

    @Override
    public void setDepartType(String departType) {
        this.departType = departType;
    }

    @Override
    public String getDepartName() {
        return departName;
    }

    @Override
    public void setDepartName(String departName) {
        this.departName = departName;
    }

    @Override
    public String getDepartDesc() {
        return departDesc;
    }

    @Override
    public void setDepartDesc(String departDesc) {
        this.departDesc = departDesc;
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
}
