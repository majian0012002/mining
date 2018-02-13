package com.miller.mining.comm;

public enum MinigTypeEnum {

	NORMAL(0,"普通模式"),
	SPORTS(1,"运动模式");
	
	private int typeCode;
	private String typeDesc;
	
	MinigTypeEnum(String name, int ordinal) {
	}
	MinigTypeEnum(int typeCode, String typeDesc) {
		this.typeCode = typeCode;
		this.typeDesc = typeDesc;
	}
	public int getTypeCode() {
		return typeCode;
	}
	public void setTypeCode(int typeCode) {
		this.typeCode = typeCode;
	}
	public String getTypeDesc() {
		return typeDesc;
	}
	public void setTypeDesc(String typeDesc) {
		this.typeDesc = typeDesc;
	}
}
