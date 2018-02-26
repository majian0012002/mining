package com.miller.mining.vo;

public class MiningInfoVo implements BaseMiningVo {

    private String username;
    private String userToken;
    private int mingType;
    private int mingState;
    private String startTime;
    private String endTime;
    private String runningMile;
    private String runningAmount;

    public MiningInfoVo() {
    }

	public MiningInfoVo(String username, String userToken, int mingType, int mingState, String startTime,
			String endTime, String runningMile, String runningAmount) {
		super();
		this.username = username;
		this.userToken = userToken;
		this.mingType = mingType;
		this.mingState = mingState;
		this.startTime = startTime;
		this.endTime = endTime;
		this.runningMile = runningMile;
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

	public String getRunningMile() {
		return runningMile;
	}

	public void setRunningMile(String runningMile) {
		this.runningMile = runningMile;
	}

	public String getRunningAmount() {
		return runningAmount;
	}

	public void setRunningAmount(String runningAmount) {
		this.runningAmount = runningAmount;
	}
}
