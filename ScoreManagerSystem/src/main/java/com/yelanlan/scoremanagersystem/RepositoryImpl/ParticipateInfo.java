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
    @Column(name = "user_number")
    private String userNumber;//参与人id
    @Column(name = "art_in_state")
    private String artInState;//参与（已报名/已参与/已获奖）
    @Column(name = "part_in_img")
    private String partInImg;//参与图片(参与证明)
    @Column(name = "cert_state")
    private String certState;//材料审核状态
    @Column(name = "award_name")
    private String awardName;//奖项名称
    @Column(name = "award_rank")
    private String awardRank;//奖项级别（院级/校级/省级/国家级）
    @Column(name = "certificate_img")
    private String certificateImg;//证书图片（获奖证明）
    @Column(name = "measure_score")
    private double measureScore;//测评分数

    public ParticipateInfo() {
    }

    public ParticipateInfo(String id, String actId, String userNumber, String artInState, String partInImg, String awardName, String awardRank, String certificateImg, double measureScore) {
        this.id = id;
        this.actId = actId;
        this.userNumber = userNumber;
        this.artInState = artInState;
        this.partInImg = partInImg;
        this.awardName = awardName;
        this.awardRank = awardRank;
        this.certificateImg = certificateImg;
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
    public String getArtInState() {
        return artInState;
    }

    @Override
    public void setArtInState(String artInState) {
        this.artInState = artInState;
    }

    @Override
    public String getPartInImg() {
        return partInImg;
    }

    @Override
    public void setPartInImg(String partInImg) {
        this.partInImg = partInImg;
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
    public String getAwardName() {
        return awardName;
    }

    @Override
    public void setAwardName(String awardName) {
        this.awardName = awardName;
    }

    @Override
    public String getAwardRank() {
        return awardRank;
    }

    @Override
    public void setAwardRank(String awardRank) {
        this.awardRank = awardRank;
    }

    @Override
    public String getCertificateImg() {
        return certificateImg;
    }

    @Override
    public void setCertificateImg(String certificateImg) {
        this.certificateImg = certificateImg;
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
