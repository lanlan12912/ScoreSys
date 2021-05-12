package com.yelanlan.scoremanagersystem.RepositoryImpl;

import com.yelanlan.scoremanagersystem.RepositoryIface.IParticipateInfo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "participate_info")
public class ParticipateInfo implements IParticipateInfo, Serializable {
    private static final long serialVersionUID = -1578419136076632313L;
    @Id
    @Column(name = "id")
    private String id;//主键id
    @Column(name = "act_id")
    private String actId;//活动id
    @Column(name = "act_rank")
    private String actRank;//奖项级别（院级/校级/省级/国家级）
    @Column(name = "user_number")
    private String userNumber;//参与人id
    @Column(name = "part_in_state")
    private String partInState;//参与（已报名/已参与/一获奖/二获奖/三获奖/其他获奖））
    @Column(name = "cert_img")
    private String certImg;//图片(参与证明/获奖证明)
    @Column(name = "cert_state")
    private String certState;//材料审核状态
    @Column(name = "measure_score")
    private double measureScore;//测评分数

    public ParticipateInfo() {
    }



    public ParticipateInfo(String id, String actId, String userNumber, String partInState, String certImg, String actRank, double measureScore) {
        this.id = id;
        this.actId = actId;
        this.userNumber = userNumber;
        this.partInState = partInState;
        this.certImg = certImg;
        this.actRank = actRank;
        this.measureScore = measureScore;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String getActId() {
        return actId;
    }

    @Override
    public void setActId(String actId) {
        this.actId = actId;
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
    public String getPartInState() {
        return partInState;
    }

    @Override
    public void setPartInState(String partInState) {
        this.partInState = partInState;
    }

    @Override
    public String getCertImg() {
        return certImg;
    }

    @Override
    public void setCertImg(String certImg) {
        this.certImg = certImg;
    }

    @Override
    public String getCertState() {
        return certState;
    }

    @Override
    public void setCertState(String certState) {
        this.certState = certState;
    }

    @Override
    public String getActRank() {
        return actRank;
    }

    @Override
    public void setActRank(String actRank) {
        this.actRank = actRank;
    }

    @Override
    public double getMeasureScore() {
        return measureScore;
    }

    @Override
    public void setMeasureScore(double measureScore) {
        this.measureScore = measureScore;
    }
}
