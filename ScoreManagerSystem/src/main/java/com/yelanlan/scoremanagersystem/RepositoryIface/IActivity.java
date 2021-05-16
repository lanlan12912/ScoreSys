package com.yelanlan.scoremanagersystem.RepositoryIface;

import java.sql.Timestamp;
import java.util.Date;

public interface IActivity {
    public String getId();
    public void setId(String id);
    public String getActType();
    public void setActType(String actType);
    public String getActName();
    public void setActName(String actName);
    public String getActDesc();
    public void setActDesc(String actDesc);
    public String getActSite();
    public void setActSite(String actSite);
    public String getActHost();
    public void setActHost(String actHost);
    public String getActImgs();
    public void setActImgs(String actImgs);
    public String getActState();
    public void setActState(String actState);
    public String getActJudge();
    public void setActJudge(String actJudge);
    public String getJudegUser();
    public void setJudegUser(String judegUser);
    public Timestamp getJudgeDate();
    public void setJudgeDate(Timestamp judgeDate);
    public String getActRank();
    public void setActRank(String actRank);
    public Date getStartDate();
    public void setStartDate(Date startDate);
    public Date getEndDate();
    public void setEndDate(Date endDate);
    public int getDelFlag();
    public void setDelFlag(int delFlag);
    public String getCrtUser();
    public void setCrtUser(String crtUser);
    public Timestamp getCrtDate();
    public void setCrtDate(Timestamp crtDate);
    public String getModifyUser();
    public void setModifyUser(String modifyUser);
    public Timestamp getModifyDate();
    public void setModifyDate(Timestamp modifyDate);

}
