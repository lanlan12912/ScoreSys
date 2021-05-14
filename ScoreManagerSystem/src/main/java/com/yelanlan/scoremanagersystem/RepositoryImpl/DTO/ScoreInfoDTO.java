package com.yelanlan.scoremanagersystem.RepositoryImpl.DTO;

import java.io.Serializable;

public class ScoreInfoDTO implements Serializable {
    private static final long serialVersionUID = -5411233576352714238L;
    private String userNumber;
    private String userName;
    private String departName;
    private double totalScore;
    private String ranking;
    private int signEdAct;
    private int partEdAct;
    private int pirseAct;

    public ScoreInfoDTO() {
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

    public String getDepartName() {
        return departName;
    }

    public void setDepartName(String departName) {
        this.departName = departName;
    }

    public double getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(double totalScore) {
        this.totalScore = totalScore;
    }

    public String getRanking() {
        return ranking;
    }

    public void setRanking(String ranking) {
        this.ranking = ranking;
    }

    public int getSignEdAct() {
        return signEdAct;
    }

    public void setSignEdAct(int signEdAct) {
        this.signEdAct = signEdAct;
    }

    public int getPartEdAct() {
        return partEdAct;
    }

    public void setPartEdAct(int partEdAct) {
        this.partEdAct = partEdAct;
    }

    public int getPirseAct() {
        return pirseAct;
    }

    public void setPirseAct(int pirseAct) {
        this.pirseAct = pirseAct;
    }
}
