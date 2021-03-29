package com.yelanlan.scoremanagersystem.Enum;

public enum UserRoleEnum {
    STUDENT("学生"),
    TEACHER("老师"),
    COUNSELOR("辅导员"),
    ADMIN("系统管理员")
    ;
    private final String name;

    UserRoleEnum(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
