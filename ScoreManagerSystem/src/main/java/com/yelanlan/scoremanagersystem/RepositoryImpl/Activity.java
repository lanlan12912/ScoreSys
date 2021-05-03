package com.yelanlan.scoremanagersystem.RepositoryImpl;

import com.yelanlan.scoremanagersystem.RepositoryIface.IActivity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.sql.Date;
import java.sql.Timestamp;

@Entity
@Table(name = "activity")
public class Activity implements IActivity, Serializable {
    private static final long serialVersionUID = 804650166820597593L;
    @Id
    @Column(name = "id")
    private String id;//活动id，主键
    @Column(name = "act_name")
    private String actName;//活动名称
    @Column(name = "act_desc")
    private String actDesc;//活动介绍
    @Column(name = "act_site")
    private String actSite;//活动地点
    @Column(name = "act_host")
    private String actHost;//活动主办方
    @Column(name = "act_imgs")
    private String actImgs;//活动图片
    @Column(name = "act_state")
    private String actState;//活动状态(未开始/进行中/已结束)
    @Column(name = "act_rank")
    private String actRank;//活动等级
    @Column(name = "start_date")
    private Date startDate;//开始时间
    @Column(name = "end_date")
    private Date endDate;//结束时间
    @Column(name = "crt_user")
    private String crtUser;//创建人
    @Column(name = "crt_date")
    private Timestamp crtDate;//创建时间
    @Column(name = "modify_user")
    private String modifyUser;//修改人
    @Column(name = "modify_date")
    private Timestamp modifyDate;//修改时间

    public Activity() {
    }

    public Activity(String id, String actName, String actDesc, String actSite, String actHost, String actImgs, String actState, String actRank, Date startDate, Date endDate, String crtUser, Timestamp crtDate, String modifyUser, Timestamp modifyDate) {
        this.id = id;
        this.actName = actName;
        this.actDesc = actDesc;
        this.actSite = actSite;
        this.actHost = actHost;
        this.actImgs = actImgs;
        this.actState = actState;
        this.actRank = actRank;
        this.startDate = startDate;
        this.endDate = endDate;
        this.crtUser = crtUser;
        this.crtDate = crtDate;
        this.modifyUser = modifyUser;
        this.modifyDate = modifyDate;
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
    public String getActName() {
        return actName;
    }

    @Override
    public void setActName(String actName) {
        this.actName = actName;
    }

    @Override
    public String getActDesc() {
        return actDesc;
    }

    @Override
    public void setActDesc(String actDesc) {
        this.actDesc = actDesc;
    }

    @Override
    public String getActSite() {
        return actSite;
    }

    @Override
    public void setActSite(String actSite) {
        this.actSite = actSite;
    }

    @Override
    public String getActHost() {
        return actHost;
    }

    @Override
    public void setActHost(String actHost) {
        this.actHost = actHost;
    }

    @Override
    public String getActImgs() {
        return actImgs;
    }

    @Override
    public void setActImgs(String actImgs) {
        this.actImgs = actImgs;
    }

    @Override
    public String getActState() {
        return actState;
    }

    @Override
    public void setActState(String actState) {
        this.actState = actState;
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
    public Date getStartDate() {
        return startDate;
    }

    @Override
    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    @Override
    public Date getEndDate() {
        return endDate;
    }

    @Override
    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    @Override
    public String getCrtUser() {
        return crtUser;
    }

    @Override
    public void setCrtUser(String crtUser) {
        this.crtUser = crtUser;
    }

    @Override
    public Timestamp getCrtDate() {
        return crtDate;
    }

    @Override
    public void setCrtDate(Timestamp crtDate) {
        this.crtDate = crtDate;
    }

    @Override
    public String getModifyUser() {
        return modifyUser;
    }

    @Override
    public void setModifyUser(String modifyUser) {
        this.modifyUser = modifyUser;
    }

    @Override
    public Timestamp getModifyDate() {
        return modifyDate;
    }

    @Override
    public void setModifyDate(Timestamp modifyDate) {
        this.modifyDate = modifyDate;
    }
}
