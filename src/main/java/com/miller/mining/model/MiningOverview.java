package com.miller.mining.model;

import java.math.BigDecimal;

public class MiningOverview extends BaseModel {
    private Integer id;

    private Integer userId;

    private BigDecimal totalAmount;

    private BigDecimal totalMile;

    private BigDecimal totalTime;

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

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public BigDecimal getTotalMile() {
        return totalMile;
    }

    public void setTotalMile(BigDecimal totalMile) {
        this.totalMile = totalMile;
    }

    public BigDecimal getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(BigDecimal totalTime) {
        this.totalTime = totalTime;
    }
}