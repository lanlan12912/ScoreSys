package com.yelanlan.scoremanagersystem.Enum;

public enum FilePathEnum {
    ACTPATH("活动图片"),
    CERTPATH("材料图片"),
    HEADPATH("头像图片")
    ;
    private final String name;

    FilePathEnum(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
