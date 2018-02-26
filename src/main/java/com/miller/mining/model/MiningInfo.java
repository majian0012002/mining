package com.miller.mining.model;

import java.math.BigDecimal;
import java.util.Date;

public class MiningInfo extends BaseModel {
    /**
	 * 
	 */
	private static final long serialVersionUID = 175024108290214439L;

	private Integer id;

    private Integer userId;

    private String startTime;

    private String endTime;

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

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
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