package com.ws.ssms.business.util;

public enum ResponseEnum {

	ERROR(0),
	SUCCESS(1),
	REPEAT(2),   //重复
	BADPARAM(3), //参数错误
	UNAUTH(401), //没有权限
	NOTLOGON(403), //未登录
	NOTFOUND(404); //未定义
	
	private int code;
	
	private ResponseEnum(int code){
		this.code = code;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}
}
