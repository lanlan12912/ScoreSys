package com.yelanlan.scoremanagersystem.RepositoryImpl.DTO;

import java.io.Serializable;

public class CertInfoDTO implements Serializable {
    private static final long serialVersionUID = -2468048840315614909L;
    private String partId;
    private String userNumber;
    private String userName;
    private String departName;
    private String actName;
    private String actRank;
    private String partInState;
    private double score;
    private String certImg;
    private String certState;

    public CertInfoDTO() {
    }

    public CertInfoDTO(String partId, String userNumber, String actRank, String partInState, double score, String certImg) {
        this.partId = partId;
        this.userNumber = userNumber;
        this.actRank = actRank;
        this.partInState = partInState;
        this.score = score;
        this.certImg = certImg;
    }

    public String getPartId() {
        return partId;
    }

    public void setPartId(String partId) {
        this.partId = partId;
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

    public String getActName() {
        return actName;
    }

    public void setActName(String actName) {
        this.actName = actName;
    }

    public String getActRank() {
        return actRank;
    }

    public void setActRank(String actRank) {
        this.actRank = actRank;
    }

    public String getPartInState() {
        return partInState;
    }

    public void setPartInState(String partInState) {
        this.partInState = partInState;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public String getCertImg() {
        return certImg;
    }

    public void setCertImg(String certImg) {
        this.certImg = certImg;
    }

    public String getCertState() {
        return certState;
    }

    public void setCertState(String certState) {
        this.certState = certState;
    }
}
