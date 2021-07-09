package com.gcy.bootwithutils.service.impl;

import com.gcy.bootwithutils.service.PaymentStrategy;
import org.springframework.stereotype.Component;

/*
 * @description: 支付宝支付策略具体实现类
 * @author Eddie Guo
 * @date: 2021/7/9 09:11
 */
@Component
public class AliPaymentStrategy implements PaymentStrategy {
    @Override
    public String doPay() {
        return "AliPayment process";
    }

    @Override
    public String requestUrl() {
        return "AliPayment request Url";
    }
}
