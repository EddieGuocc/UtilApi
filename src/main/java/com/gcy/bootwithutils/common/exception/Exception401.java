package com.gcy.bootwithutils.common.exception;

/**
 * @ClassName Exception401
 * @Description 权限控制异常类
 * @Author Eddie
 * @Date 2020/07/22 15:56
 */
public class Exception401 extends Exception{
    public Exception401() {
        super("访问无权限");
    }
}
