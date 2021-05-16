package com.yelanlan.scoremanagersystem.Enum;

public enum ActTypeEnum {
    ACT("活动类"),
    DUTY("职务类")
    ;
    private final String name;

    ActTypeEnum(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
