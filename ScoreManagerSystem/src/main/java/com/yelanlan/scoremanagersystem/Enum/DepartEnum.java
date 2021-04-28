package com.yelanlan.scoremanagersystem.Enum;

public enum DepartEnum {
    DEPARTMENT("系"),
    COLLEGE("院")
    ;
    private final String name;

    DepartEnum(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
