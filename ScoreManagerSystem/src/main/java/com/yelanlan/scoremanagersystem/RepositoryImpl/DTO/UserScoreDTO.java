package com.yelanlan.scoremanagersystem.RepositoryImpl.DTO;

import java.io.Serializable;

public class UserScoreDTO implements Serializable {
    private static final long serialVersionUID = 4139993027454075455L;
    private String userNumber;
    private String userName;
    private String headAvatar;
    private String departName;
    private double score;

    public UserScoreDTO() {
    }

    public UserScoreDTO(String userNumber, String userName, String headAvatar, String departName, double score) {
        this.userNumber = userNumber;
        this.userName = userName;
        this.headAvatar = headAvatar;
        this.departName = departName;
        this.score = score;
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

    public String getHeadAvatar() {
        return headAvatar;
    }

    public void setHeadAvatar(String headAvatar) {
        this.headAvatar = headAvatar;
    }

    public String getDepartName() {
        return departName;
    }

    public void setDepartName(String departName) {
        this.departName = departName;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }
}
