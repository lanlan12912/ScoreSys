package com.yelanlan.scoremanagersystem.Enum;

public enum DutyScoreEnum {
    PRISDENT("会长",4,3.5,0),
    MINISTER("部长",3,2.5,0),
    UNDER_MINISTER("副部长",2.5,2,0),
    WORKS("干事",1,0.5,0),
    CLASS_MONITOR("班长",0,0,1),
    CLASS_SERVE("团支书",0,0,1),
    CLASS_MEMBER("班级委员",0,0,0.5)
    ;
    private final String name;
    private final double sScore; //校级加分
    private final double colScore;//院级加分
    private final double claScore;//班级加分

    DutyScoreEnum(String name, double sScore, double colScore, double claScore) {
        this.name = name;
        this.sScore = sScore;
        this.colScore = colScore;
        this.claScore = claScore;
    }

    public String getName() {
        return name;
    }

    public double getsScore() {
        return sScore;
    }

    public double getColScore() {
        return colScore;
    }

    public double getClaScore() {
        return claScore;
    }
}
