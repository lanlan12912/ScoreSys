package com.yelanlan.scoremanagersystem.RepositoryImpl;

import com.yelanlan.scoremanagersystem.RepositoryIface.IUser;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.sql.Timestamp;

@Entity
@Table(name="user")
public class User implements IUser,Serializable {
    private static final long serialVersionUID = -7339710774166090561L;
    @Id
    @Column(name = "user_number")
    private String userNumber;//用户账号
    @Column(name = "user_name")
    private String userName;//用户别名
    @Column(name = "user_pwd")
    private String userPwd;//用户密码
    @Column(name = "user_state")
    private String userState;//用户状态（停用，启用）
    @Column(name = "user_rank")
    private String userRank;//身份：学生/老师/系统管理员；
    @Column(name = "user_teleno")
    private String userTeleno;//固定电话号码
    @Column(name = "user_crtdate")
    private Timestamp userCrtdate;//创建时间
    @Column(name = "failed_login_count")
    private Integer failedLoginCount;//登录失败次数
    @Column(name = "failed_login_time")
    private Timestamp failedLoginTime;//登录失败时间
    @Column(name = "user_desc")
    private String userDesc;//描述
    @Column(name = "head_avatar")
    private String headAvatar;//用户头像
    @Column(name = "department_id")
    private String departmentId;//院系id（院，系，班）

    public User() {
    }

    public User(String userNumber, String userName, String userPwd, String userState,String userRank, String userTeleno, Timestamp userCrtdate, Integer failedLoginCount, Timestamp failedLoginTime, String userDesc, String headAvatar) {
        this.userNumber = userNumber;
        this.userName = userName;
        this.userPwd = userPwd;
        this.userState = userState;
        this.userRank = userRank;
        this.userTeleno = userTeleno;
        this.userCrtdate = userCrtdate;
        this.failedLoginCount = failedLoginCount;
        this.failedLoginTime = failedLoginTime;
        this.userDesc = userDesc;
        this.headAvatar = headAvatar;
    }

    public User(String userNumber, String userName, String userPwd, String userState,String userRank, String userTeleno, Timestamp userCrtdate, Integer failedLoginCount, Timestamp failedLoginTime, String userDesc, String headAvatar, String departmentId) {
        this.userNumber = userNumber;
        this.userName = userName;
        this.userPwd = userPwd;
        this.userState = userState;
        this.userRank = userRank;
        this.userTeleno = userTeleno;
        this.userCrtdate = userCrtdate;
        this.failedLoginCount = failedLoginCount;
        this.failedLoginTime = failedLoginTime;
        this.userDesc = userDesc;
        this.headAvatar = headAvatar;
        this.departmentId = departmentId;
    }

    @Override
    public String getUserNumber() {
        return userNumber;
    }

    @Override
    public void setUserNumber(String userNumber) {
        this.userNumber = userNumber;
    }

    @Override
    public String getUserName() {
        return userName;
    }

    @Override
    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Override
    public String getUserPwd() {
        return userPwd;
    }

    @Override
    public void setUserPwd(String userPwd) {
        this.userPwd = userPwd;
    }

    @Override
    public String getUserState() {
        return userState;
    }

    @Override
    public void setUserState(String userState) {
        this.userState = userState;
    }

    @Override
    public String getUserRank() {
        return userRank;
    }

    @Override
    public void setUserRank(String userRank) {
        this.userRank = userRank;
    }

    @Override
    public String getUserTeleno() {
        return userTeleno;
    }

    @Override
    public void setUserTeleno(String userTeleno) {
        this.userTeleno = userTeleno;
    }

    @Override
    public Timestamp getUserCrtdate() {
        return userCrtdate;
    }

    @Override
    public void setUserCrtdate(Timestamp userCrtdate) {
        this.userCrtdate = userCrtdate;
    }

    @Override
    public Integer getFailedLoginCount() {
        return failedLoginCount;
    }

    @Override
    public void setFailedLoginCount(Integer failedLoginCount) {
        this.failedLoginCount = failedLoginCount;
    }

    @Override
    public Timestamp getFailedLoginTime() {
        return failedLoginTime;
    }

    @Override
    public void setFailedLoginTime(Timestamp failedLoginTime) {
        this.failedLoginTime = failedLoginTime;
    }

    @Override
    public String getUserDesc() {
        return userDesc;
    }

    @Override
    public void setUserDesc(String userDesc) {
        this.userDesc = userDesc;
    }

    @Override
    public String getHeadAvatar() {
        return headAvatar;
    }

    @Override
    public void setHeadAvatar(String headAvatar) {
        this.headAvatar = headAvatar;
    }

    @Override
    public String getDepartmentId() {
        return departmentId;
    }

    @Override
    public void setDepartmentId(String departmentId) {
        this.departmentId = departmentId;
    }
}
