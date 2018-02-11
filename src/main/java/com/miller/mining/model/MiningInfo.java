package com.miller.mining.model;

import java.math.BigDecimal;
import java.util.Date;

public class MiningInfo extends BaseModel {
    private Integer id;

    private Integer userId;

    private Date startTime;

    private Date endTime;

    private Integer type;

    private Integer state;

    private BigDecimal runningTime;

    private BigDecimal runningMile;

    private BigDecimal miningAmount;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public BigDecimal getRunningTime() {
        return runningTime;
    }

    public void setRunningTime(BigDecimal runningTime) {
        this.runningTime = runningTime;
    }

    public BigDecimal getRunningMile() {
        return runningMile;
    }

    public void setRunningMile(BigDecimal runningMile) {
        this.runningMile = runningMile;
    }

    public BigDecimal getMiningAmount() {
        return miningAmount;
    }

    public void setMiningAmount(BigDecimal miningAmount) {
        this.miningAmount = miningAmount;
    }
}