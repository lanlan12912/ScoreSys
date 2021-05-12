package com.yelanlan.scoremanagersystem.Enum;

public enum ActStateEnum {
    //actState
    NOTSTART("未开始"),
    ONGOING("进行中"),
    ENDED("已结束"),

    //actJudge的值;certState的值
    INJUDGE("审核中"),
    PASS("成功"),
    REFUSED("失败")
    ;
    private final String name;

    ActStateEnum(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
