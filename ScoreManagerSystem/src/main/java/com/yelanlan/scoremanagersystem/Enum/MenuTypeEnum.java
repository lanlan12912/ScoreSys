package com.yelanlan.scoremanagersystem.Enum;

public enum MenuTypeEnum {
    MODULE("模块"),
    PAGE("页面")
    ;
    private final String name;

    MenuTypeEnum(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
