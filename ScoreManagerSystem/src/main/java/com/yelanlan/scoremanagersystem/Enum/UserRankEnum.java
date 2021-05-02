package com.yelanlan.scoremanagersystem.Enum;

public enum UserRankEnum {
    STUDENT("学生"),
    TEACHER("老师"),
    ADMIN("系统管理员")
    ;
    private final String name;

    UserRankEnum(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
