package com.miller.mining.comm;

public enum MiningTypeEnum {
    ORDINARY_MODE(0,"普通模式"),
    SPORTS_MODE(1,"运动模式"),
    DATA_MODE(2,"数据模式"),
    CLOCK_MODE(3,"打卡模式");


    private int type;
    private String description;


    MiningTypeEnum() {

    }
    MiningTypeEnum(int type, String description) {
        this.type = type;
        this.description = description;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
