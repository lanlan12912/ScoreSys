package com.yelanlan.scoremanagersystem.RepositoryImpl.DTO;

import java.io.Serializable;

public class ActPartDetailDTO implements Serializable {
    private static final long serialVersionUID = -6056141063995347049L;
    private String partId;
    private String actName;
    private String actRank;
    private String actDate;
    private String partInState;
    private String certImg;
    private double score;
    private String certState;

    public ActPartDetailDTO() {
    }

    public ActPartDetailDTO(String partId, String actRank, String partInState, String certImg, double score, String certState) {
        this.partId = partId;
        this.actRank = actRank;
        this.partInState = partInState;
        this.certImg = certImg;
        this.score = score;
        this.certState = certState;
    }

    public String getPartId() {
        return partId;
    }

    public void setPartId(String partId) {
        this.partId = partId;
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

    public String getActDate() {
        return actDate;
    }

    public void setActDate(String actDate) {
        this.actDate = actDate;
    }

    public String getPartInState() {
        return partInState;
    }

    public void setPartInState(String partInState) {
        this.partInState = partInState;
    }

    public String getCertImg() {
        return certImg;
    }

    public void setCertImg(String certImg) {
        this.certImg = certImg;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public String getCertState() {
        return certState;
    }

    public void setCertState(String certState) {
        this.certState = certState;
    }
}
