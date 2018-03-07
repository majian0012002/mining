package com.miller.mining.vo;

public class UserMiningHistoryResponse {

	private String startTime;
	private String endTime;
	private String totalTime;
	private String totalMiles;
	private String totalMoney;
	
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
	public String getTotalTime() {
		return totalTime;
	}
	public void setTotalTime(String totalTime) {
		this.totalTime = totalTime;
	}
	public String getTotalMiles() {
		return totalMiles;
	}
	public void setTotalMiles(String totalMiles) {
		this.totalMiles = totalMiles;
	}
	public String getTotalMoney() {
		return totalMoney;
	}
	public void setTotalMoney(String totalMoney) {
		this.totalMoney = totalMoney;
	}
}
