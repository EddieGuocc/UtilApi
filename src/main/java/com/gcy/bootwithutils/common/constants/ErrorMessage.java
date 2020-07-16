package com.gcy.bootwithutils.common.constants;

import io.swagger.annotations.ApiModelProperty;

public class ErrorMessage {
	
	@ApiModelProperty(value="状态码",name="err_code")
	private int err_code;
	
	@ApiModelProperty(value="错误信息",name="err_message")
	private String err_message;

	public int getErr_code() {
		return err_code;
	}

	public void setErr_code(int err_code) {
		this.err_code = err_code;
	}

	public String getErr_message() {
		return err_message;
	}

	public void setErr_message(String err_message) {
		this.err_message = err_message;
	}

}
