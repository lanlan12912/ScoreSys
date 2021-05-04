package com.yelanlan.scoremanagersystem.Enum;

public enum ActRankEnum {
    NATIONAL_LEVEL("国家级",5,4,3.5,3,0.5),
    PROVINCE_LEVEL("省级",3.5,2.5,2,1,0.4),
    SCHOOL_LEVEL("校级",3,2,1.5,0.8,0.2),
    COLLEGE_LEVEL("院级",1,0.8,0.6,0.5,0.2)
    ;
    private final String name;//等级
    private final double partScore;//参与得分
    private final double priseScore1;//一等奖获奖得分
    private final double priseScore2;//二等奖获奖得分
    private final double priseScore3;//三等奖获奖得分
    private final double oPriseScore;//其他获奖得分


    ActRankEnum(String name, double priseScore1, double priseScore2, double priseScore3, double oPriseScore,double partScore) {
        this.name = name;
        this.partScore = partScore;
        this.priseScore1 = priseScore1;
        this.priseScore2 = priseScore2;
        this.priseScore3 = priseScore3;
        this.oPriseScore = oPriseScore;
    }

    public String getName() {
        return name;
    }

    public double getPartScore() {
        return partScore;
    }

    public double getPriseScore1() {
        return priseScore1;
    }

    public double getPriseScore2() {
        return priseScore2;
    }

    public double getPriseScore3() {
        return priseScore3;
    }

    public double getoPriseScore() {
        return oPriseScore;
    }

}
