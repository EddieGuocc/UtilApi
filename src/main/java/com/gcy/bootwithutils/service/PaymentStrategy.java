package com.gcy.bootwithutils.service;

/*
 * @description: 策略模式接口 只定义多个策略的动作 具体实现并不care
 * @author Eddie Guo
 * @date: 2021/7/9 09:10
 */
public interface PaymentStrategy {
    String doPay();
    String requestUrl();
}
