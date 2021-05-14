package com.yelanlan.scoremanagersystem.RepositoryImpl.DTO;

import java.io.Serializable;

public class EnumDTO implements Serializable {
    private static final long serialVersionUID = 6974436694670317964L;
    private String code;
    private String name;

    public EnumDTO(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
