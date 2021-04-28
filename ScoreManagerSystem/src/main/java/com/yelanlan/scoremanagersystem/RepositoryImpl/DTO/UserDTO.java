package com.yelanlan.scoremanagersystem.RepositoryImpl.DTO;

import java.io.Serializable;

public class UserDTO implements Serializable {
    private static final long serialVersionUID = 249227639211074779L;
    private String userNumber;
    private String userName;
    private String userState;
    private String userRole;
    private String userTeleno;
    private String collegeName;
    private String departName;

    public UserDTO() {
    }

    public UserDTO(String userNumber, String userName, String userState, String userRole, String userTeleno, String collegeName, String departName) {
        this.userNumber = userNumber;
        this.userName = userName;
        this.userState = userState;
        this.userRole = userRole;
        this.userTeleno = userTeleno;
        this.collegeName = collegeName;
        this.departName = departName;
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

    public String getCollegeName() {
        return collegeName;
    }

    public void setCollegeName(String collegeName) {
        this.collegeName = collegeName;
    }

    public String getDepartName() {
        return departName;
    }

    public void setDepartName(String departName) {
        this.departName = departName;
    }
}
