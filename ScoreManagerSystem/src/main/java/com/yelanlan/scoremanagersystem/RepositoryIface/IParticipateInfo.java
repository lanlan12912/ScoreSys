package com.yelanlan.scoremanagersystem.RepositoryIface;

public interface IParticipateInfo {
    public String getId();
    public void setId(String id);
    public String getActId();
    public void setActId(String actId);
    public String getUserNumber();
    public void setUserNumber(String userNumber);
    public String getPartInState();
    public void setPartInState(String partInState);
    public String getCertImg();
    public void setCertImg(String certImg);
    public String getCertState();
    public void setCertState(String certState);
    public String getActRank();
    public void setActRank(String actRank);
    public double getMeasureScore();
    public void setMeasureScore(double measureScore);
}
