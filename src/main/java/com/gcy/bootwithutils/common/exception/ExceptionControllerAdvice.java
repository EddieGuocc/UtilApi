package com.gcy.bootwithutils.common.exception;

import com.gcy.bootwithutils.common.constants.ResultCode;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName ExceptionControllerAdvice
 * @Description Controller增强器 捕获异常封装处理错误消息
 * @Author Eddie
 * @Date 2020/07/22 16:13
 */

@ControllerAdvice
public class ExceptionControllerAdvice{
    @ResponseBody
    @ExceptionHandler(value = Exception.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String,Object> errorHandler(Exception ex) {
        ex.printStackTrace();
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("err_code", ResultCode.FAIL.getResultCode());
        map.put("err_message", ex.getMessage());
        return map;
    }


    @ResponseBody
    @ExceptionHandler(value = Exception401.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public Map<String,Object> ouathErrorHandler(Exception ex) {
        Map<String,Object> map = new HashMap<String,Object>(1);
        map.put("err_code", "401");
        map.put("err_message", ex.getMessage());
        return map;
    }
}
