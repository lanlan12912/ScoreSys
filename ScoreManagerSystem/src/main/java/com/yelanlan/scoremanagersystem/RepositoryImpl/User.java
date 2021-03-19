package com.yelanlan.scoremanagersystem.RepositoryImpl;

import javax.persistence.*;
import java.io.Serializable;
@Entity
@Table(name="user")
@NamedQueries({
        @NamedQuery(name = "UserDAO.findByUserID",query = "from User u where u.userId = ?0")
})
public class User implements Serializable {
    private static final long serialVersionUID = -7339710774166090561L;
    @Id
    @Column(name = "user_id")
    private String userId;//主键ID
    @Column(name = "user_number")
    private String userNumber;//用户账号
    @Column(name = "user_name")
    private String userName;//用户别名
    @Column(name = "user_pwd")
    private String userPwd;//用户密码
    @Column(name = "user_state")
    private String userState;//用户状态（0：停用，1：启用）
    @Column(name = "user_isadmin")
    private String userIsadmin;//0：学生；1：老师；2、教师；3：辅导员；4、系统管理员；
    @Column(name = "user_teleno")
    private String userTeleno;//固定电话号码
    @Column(name = "user_crtdate")
    private String userCrtdate;//创建时间
    @Column(name = "failed_login_count")
    private String failedLoginCount;//登录失败次数
    @Column(name = "failed_login_time")
    private String failedLoginTime;//登录失败时间
    @Column(name = "user_desc")
    private String userDesc;//描述

    public User() {
    }

    public User(String userId, String userNumber, String userName, String userPwd, String userState, String userIsadmin, String userTeleno, String userCrtdate, String failedLoginCount, String failedLoginTime, String userDesc) {
        this.userId = userId;
        this.userNumber = userNumber;
        this.userName = userName;
        this.userPwd = userPwd;
        this.userState = userState;
        this.userIsadmin = userIsadmin;
        this.userTeleno = userTeleno;
        this.userCrtdate = userCrtdate;
        this.failedLoginCount = failedLoginCount;
        this.failedLoginTime = failedLoginTime;
        this.userDesc = userDesc;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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

    public String getUserPwd() {
        return userPwd;
    }

    public void setUserPwd(String userPwd) {
        this.userPwd = userPwd;
    }

    public String getUserState() {
        return userState;
    }

    public void setUserState(String userState) {
        this.userState = userState;
    }

    public String getUserIsadmin() {
        return userIsadmin;
    }

    public void setUserIsadmin(String userIsadmin) {
        this.userIsadmin = userIsadmin;
    }

    public String getUserTeleno() {
        return userTeleno;
    }

    public void setUserTeleno(String userTeleno) {
        this.userTeleno = userTeleno;
    }

    public String getUserCrtdate() {
        return userCrtdate;
    }

    public void setUserCrtdate(String userCrtdate) {
        this.userCrtdate = userCrtdate;
    }

    public String getFailedLoginCount() {
        return failedLoginCount;
    }

    public void setFailedLoginCount(String failedLoginCount) {
        this.failedLoginCount = failedLoginCount;
    }

    public String getFailedLoginTime() {
        return failedLoginTime;
    }

    public void setFailedLoginTime(String failedLoginTime) {
        this.failedLoginTime = failedLoginTime;
    }

    public String getUserDesc() {
        return userDesc;
    }

    public void setUserDesc(String userDesc) {
        this.userDesc = userDesc;
    }
}
