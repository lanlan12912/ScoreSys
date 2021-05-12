package com.yelanlan.scoremanagersystem.Enum;

public enum  PartInEnum {
    SIGNED("已报名"),
    PARTINED("参与"),
    FPRISE("一等奖"),
    SPRISE("二等奖"),
    TPRISE("三等奖"),
    OPRISE("其他获奖"),
    ;
    private final String name;

    PartInEnum(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
