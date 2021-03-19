package com.yelanlan.scoremanagersystem.Repository_Iface;

public interface IUser {
    public String getUserId();
    public void setUserId(String userId);
    public String getUserNumber();
    public void setUserNumber(String userNumber);
    public String getUserName();
    public void setUserName(String userName);
    public String getUserPwd();
    public void setUserPwd(String userPwd);
    public String getUserState();
    public void setUserState(String userState);
    public String getUserIsadmin();
    public void setUserIsadmin(String userIsadmin);
    public String getUserTeleno();
    public void setUserTeleno(String userTeleno);
    public String getUserCrtdate();
    public void setUserCrtdate();
    public String getFailedLoginCount();
    public void setFailedLoginCount(String failedLoginCount);
    public String getFailedLoginTime();
    public void setFailedLoginTime(String failedLoginTime);
    public String getUserDesc();
    public void setUserDesc(String userDesc);

}
