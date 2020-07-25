package com.gcy.bootwithutils.common.exception;

import com.gcy.bootwithutils.common.constants.ResultCode;
import lombok.Getter;
import lombok.Setter;

/**
 * @ClassName Exception400
 * @Description 封装异常类
 * @Author Eddie
 * @Date 2020/07/22 15:40
 */

@Getter
@Setter
public class Exception400 extends RuntimeException{

    private int err_code;

    private String err_message;

    public Exception400(ResultCode resultCode){
        this.err_code = resultCode.getResultCode();
        this.err_message = resultCode.getMessage();
    }

    public Exception400(int err_code, String err_message){
        this.err_code = err_code;
        this.err_message = err_message;
    }
}
