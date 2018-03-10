package com.miller.mining.vo;

public class MiningSingleVo {

	private String username;
	private String userToken;
	private int mingType;
	private String duringTime;
	private String currentMile;
	private String lastMile;
	private String miningId;
	
	public MiningSingleVo() {
		super();
	}

	public MiningSingleVo(String username, String userToken, int mingType, String duringTime, String currentMile, String lastMile, String miningId) {
		this.username = username;
		this.userToken = userToken;
		this.mingType = mingType;
		this.duringTime = duringTime;
		this.currentMile = currentMile;
		this.lastMile = lastMile;
		this.miningId = miningId;
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

	public String getDuringTime() {
		return duringTime;
	}

	public void setDuringTime(String duringTime) {
		this.duringTime = duringTime;
	}

	public String getCurrentMile() {
		return currentMile;
	}

	public void setCurrentMile(String currentMile) {
		this.currentMile = currentMile;
	}

	public String getLastMile() {
		return lastMile;
	}

	public void setLastMile(String lastMile) {
		this.lastMile = lastMile;
	}

	public String getMiningId() {
		return miningId;
	}

	public void setMiningId(String miningId) {
		this.miningId = miningId;
	}
}
