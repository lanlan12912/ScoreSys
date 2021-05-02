package com.yelanlan.scoremanagersystem.RepositoryImpl.DTO;

import java.io.Serializable;

public class UserDTO implements Serializable {
    private static final long serialVersionUID = 249227639211074779L;
    private String userNumber;
    private String userName;
    private String userState;
    private String userRank;
    private String userRole;
    private String userTeleno;
    private String departmentName;

    public UserDTO() {
    }

    public UserDTO(String userNumber, String userName, String userState,String userRank, String userTeleno, String departmentName) {
        this.userNumber = userNumber;
        this.userName = userName;
        this.userState = userState;
        this.userRank = userRank;
        this.userTeleno = userTeleno;
        this.departmentName = departmentName;
    }

    public String getUserNumber() {
        return userNumber;
    }

    public void setUserNumber(String userNumber) {
        this.userNumber = userNumber;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserState() {
        return userState;
    }

    public void setUserState(String userState) {
        this.userState = userState;
    }

    public String getUserRank() {
        return userRank;
    }

    public void setUserRank(String userRank) {
        this.userRank = userRank;
    }

    public String getUserRole() {
        return userRole;
    }

    public void setUserRole(String userRole) {
        this.userRole = userRole;
    }

    public String getUserTeleno() {
        return userTeleno;
    }

    public void setUserTeleno(String userTeleno) {
        this.userTeleno = userTeleno;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }
}
