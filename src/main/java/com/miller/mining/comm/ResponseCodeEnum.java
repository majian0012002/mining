package com.miller.mining.comm;

public enum ResponseCodeEnum {
	
	SUCCESS("1001","请求成功"),
	VERIFY_INVALID("1002","校验失败"),
	JSON_FORMAR_INVALID("1003","json格式不正确"),
	DATA_INVALID("1004","数据错误"),
	BAD_REQUEST("1005","异常错误"),
	EXISTING_MINGING("2001","存在正在进行的挖矿"),
	NO_EXISTING_MINGING("2002","不存在正在进行的挖矿"),
	LOGIN_INVALID("3001","登陆失效");

	private String code;
	private String description;
	
	
	ResponseCodeEnum() {

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
