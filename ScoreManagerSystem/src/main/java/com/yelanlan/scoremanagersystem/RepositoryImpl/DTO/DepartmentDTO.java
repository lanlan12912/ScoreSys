package com.yelanlan.scoremanagersystem.RepositoryImpl.DTO;

import com.yelanlan.scoremanagersystem.RepositoryImpl.Department;

import java.io.Serializable;
import java.util.List;

public class DepartmentDTO implements Serializable {
    private static final long serialVersionUID = -9163913563093201359L;
    private Department college;//院
    private List<Department> departments;//系

    public DepartmentDTO(){
    }

    public DepartmentDTO(Department college, List<Department> departments) {
        this.college = college;
        this.departments = departments;
    }

    public Department getCollege() {
        return college;
    }

    public void setCollege(Department college) {
        this.college = college;
    }

    public List<Department> getDepartments() {
        return departments;
    }

    public void setDepartments(List<Department> departments) {
        this.departments = departments;
    }
}
