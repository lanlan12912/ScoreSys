package com.yelanlan.scoremanagersystem.RepositoryIface;

import java.sql.Timestamp;

public interface IUser {
    public String getUserNumber();
    public void setUserNumber(String userNumber);
    public String getUserName();
    public void setUserName(String userName);
    public String getUserPwd();
    public void setUserPwd(String userPwd);
    public String getUserState();
    public void setUserState(String userState);
    public String getUserRole();
    public void setUserRole(String userRole);
    public String getUserTeleno();
    public void setUserTeleno(String userTeleno);
    public Timestamp getUserCrtdate();
    public void setUserCrtdate(Timestamp userCrtdate);
    public Integer getFailedLoginCount();
    public void setFailedLoginCount(Integer failedLoginCount);
    public Timestamp getFailedLoginTime();
    public void setFailedLoginTime(Timestamp failedLoginTime);
    public String getUserDesc();
    public void setUserDesc(String userDesc);
    public String getHeadAvatar();
    public void setHeadAvatar(String headAvatar);
    public String getCollegeId();
    public void setCollegeId(String collegeId);
    public String getDepartmentId();
    public void setDepartmentId(String departmentId);
}
