package com.miller.mining.vo;

public class MiningSingleVo {

	private String username;
	private String userToken;
	private int mingType;
	private String duringTime;
	private int currentMile;
	private int lastMile;
	private int miningId;
	
	public MiningSingleVo() {
		super();
	}
	public MiningSingleVo(String username, String userToken, int mingType, String duringTime, int currentMile,
			int lastMile, int miningId) {
		super();
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

	public int getCurrentMile() {
		return currentMile;
	}

	public void setCurrentMile(int currentMile) {
		this.currentMile = currentMile;
	}

	public int getLastMile() {
		return lastMile;
	}

	public void setLastMile(int lastMile) {
		this.lastMile = lastMile;
	}

	public int getMiningId() {
		return miningId;
	}

	public void setMiningId(int miningId) {
		this.miningId = miningId;
	}
}
