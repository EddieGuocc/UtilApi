package com.gcy.bootwithutils.common.constants;

public enum ResultCode {

	SUCCESS(10000, "XXXXXX"),
	FAIL(-1, "操作失败 "),
	UNCONNECTION(-8,"连接超时"),
	UPDATE(-9,"下载最新版本"),
	MISS_QEQUEST_BODY(-2,"传参方式不正确"),
	OUT_OF_COUNT(-3, "访问太频繁"),
	PARAMS_INVALID(4, "参数无法识别")
	;

	private int resultCode;
	private String message;

	private ResultCode(int resultCode, String message) {
		this.resultCode = resultCode;
		this.message = message;
	}

	public int getResultCode() {
		return resultCode;
	}

	public void setResultCode(int resultCode) {
		this.resultCode = resultCode;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@Override
	public String toString() {
		return this.resultCode + ":" + this.message;
	}

}
