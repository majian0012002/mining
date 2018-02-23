package com.miller.mining.vo;

public class MiningInfoVo implements BaseMiningVo {

    private String username;
    private String userToken;
    private int mingType;
    private int mingState;
    private String startTimeStr;
    private String endTimeStr;
    private String runningMiles;
    private String runningAmount;

    public MiningInfoVo() {
    }

    public MiningInfoVo(String username, String userToken, int mingType, int mingState, String startTimeStr, String endTimeStr, String runningMiles, String runningAmount) {
        this.username = username;
        this.userToken = userToken;
        this.mingType = mingType;
        this.mingState = mingState;
        this.startTimeStr = startTimeStr;
        this.endTimeStr = endTimeStr;
        this.runningMiles = runningMiles;
        this.runningAmount = runningAmount;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserToken() {
        return userToken;
    }

    public void setUserToken(String userToken) {
        this.userToken = userToken;
    }

    public int getMingType() {
        return mingType;
    }

    public void setMingType(int mingType) {
        this.mingType = mingType;
    }

    public int getMingState() {
        return mingState;
    }

    public void setMingState(int mingState) {
        this.mingState = mingState;
    }

    public String getStartTimeStr() {
        return startTimeStr;
    }

    public void setStartTimeStr(String startTimeStr) {
        this.startTimeStr = startTimeStr;
    }

    public String getEndTimeStr() {
        return endTimeStr;
    }

    public void setEndTimeStr(String endTimeStr) {
        this.endTimeStr = endTimeStr;
    }

    public String getRunningMiles() {
        return runningMiles;
    }

    public void setRunningMiles(String runningMiles) {
        this.runningMiles = runningMiles;
    }

    public String getRunningAmount() {
        return runningAmount;
    }

    public void setRunningAmount(String runningAmount) {
        this.runningAmount = runningAmount;
    }
}
