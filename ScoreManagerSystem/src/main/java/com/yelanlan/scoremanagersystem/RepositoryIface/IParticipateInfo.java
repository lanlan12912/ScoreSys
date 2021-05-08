package com.yelanlan.scoremanagersystem.RepositoryIface;

public interface IParticipateInfo {
    public String getId();
    public void setId(String id);
    public String getActId();
    public void setActId(String actId);
    public String getUserNumber();
    public void setUserNumber(String userNumber);
    public String getArtInState();
    public void setArtInState(String artInState);
    public String getPartInImg();
    public void setPartInImg(String partInImg);
    public String getCertState();
    public void setCertState(String certState);
    public String getAwardName();
    public void setAwardName(String awardName);
    public String getAwardRank();
    public void setAwardRank(String awardRank);
    public String getCertificateImg();
    public void setCertificateImg(String certificateImg);
    public double getMeasureScore();
    public void setMeasureScore(double measureScore);
}
