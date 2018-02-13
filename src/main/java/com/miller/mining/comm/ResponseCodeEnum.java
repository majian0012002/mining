package com.miller.mining.comm;

public enum ResponseCodeEnum {
	
	SUCCESS("1001","请求成功"),
	VERIFY_INVALID("1002","校验失败"),
	JSON_FORMAR_INVALID("1003",""),
	DATA_INVALID("1004",""),
	BAD_REQUEST("1005","");

	private String code;
	private String description;
	
	
	ResponseCodeEnum(String name, int ordinal) {

	}
	ResponseCodeEnum(String code, String description) {
		this.code = code;
		this.description = description;
	}


	public String getCode() {
		return code;
	}


	public void setCode(String code) {
		this.code = code;
	}


	public String getDescription() {
		return description;
	}


	public void setDescription(String description) {
		this.description = description;
	}
}
