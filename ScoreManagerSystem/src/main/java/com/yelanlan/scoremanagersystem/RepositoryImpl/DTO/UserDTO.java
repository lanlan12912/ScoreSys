package com.yelanlan.scoremanagersystem.RepositoryImpl.DTO;

import java.io.Serializable;

public class UserDTO implements Serializable {
    private static final long serialVersionUID = 249227639211074779L;
    private String userNumber;
    private String userName;
    private String userState;
    private String stateName;
    private String userRank;
    private String rankName;
    private String userRole;
    private String userTeleno;
    private String departmentId;
    private String departmentName;
    private String userDesc;
    public UserDTO() {
    }

    public UserDTO(String userNumber, String userName, String userState,String stateName, String userRank, String rankName,String userTeleno,String  departmentId, String departmentName,String userDesc) {
        this.userNumber = userNumber;
        this.userName = userName;
        this.userState = userState;
        this.stateName =stateName;
        this.userRank = userRank;
        this.rankName = rankName;
        this.userTeleno = userTeleno;
        this.departmentId = departmentId;
        this.departmentName = departmentName;
        this.userDesc = userDesc;
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

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    public String getUserRank() {
        return userRank;
    }

    public void setUserRank(String userRank) {
        this.userRank = userRank;
    }

    public String getRankName() {
        return rankName;
    }

    public void setRankName(String rankName) {
        this.rankName = rankName;
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

    public String getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(String departmentId) {
        this.departmentId = departmentId;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getUserDesc() {
        return userDesc;
    }

    public void setUserDesc(String userDesc) {
        this.userDesc = userDesc;
    }
}
