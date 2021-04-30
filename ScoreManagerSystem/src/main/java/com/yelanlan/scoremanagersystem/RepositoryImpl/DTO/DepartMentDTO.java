package com.yelanlan.scoremanagersystem.RepositoryImpl.DTO;

import com.yelanlan.scoremanagersystem.RepositoryImpl.Department;

import java.io.Serializable;
import java.sql.Timestamp;

public class DepartMentDTO extends Department implements Serializable {
    private static final long serialVersionUID = 3792152558498572600L;
    private String parentName;

    public DepartMentDTO(String id, String parentId, String departType, String departName, String departDesc, String crtUser, Timestamp crtDate, String parentName) {
        super(id, parentId, departType, departName, departDesc, crtUser, crtDate);
        this.parentName = parentName;
    }

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }
}
