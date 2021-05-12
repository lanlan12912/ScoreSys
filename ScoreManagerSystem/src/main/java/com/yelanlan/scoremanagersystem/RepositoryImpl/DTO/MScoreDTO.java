package com.yelanlan.scoremanagersystem.RepositoryImpl.DTO;

import java.io.Serializable;

public class MScoreDTO implements Serializable {
    private static final long serialVersionUID = -1761661366281730857L;
    private String rank;//等级
    private String code;//奖项代码
    private String name;//奖项
    private double value;//得分

    public MScoreDTO() {
    }

    public MScoreDTO(String rank, String code, String name, double value) {
        this.rank = rank;
        this.code = code;
        this.name = name;
        this.value = value;
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
