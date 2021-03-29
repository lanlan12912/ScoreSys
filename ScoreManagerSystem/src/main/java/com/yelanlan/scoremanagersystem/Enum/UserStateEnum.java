package com.yelanlan.scoremanagersystem.Enum;

public enum UserStateEnum {
    STOP("停用"),
    START("启用")
    ;
    private final String name;

    UserStateEnum(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
