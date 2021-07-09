package com.gcy.bootwithutils.service.impl;

import com.gcy.bootwithutils.service.PaymentStrategy;
import org.springframework.stereotype.Component;

/*
 * @description: 微信支付策略具体实现类
 * @author Eddie Guo
 * @date: 2021/7/9 09:11
 */
@Component
public class WechatPaymentStrategy implements PaymentStrategy {
    @Override
    public String doPay() {
        return "WechatPayment process";
    }

    @Override
    public String requestUrl() {
        return "WechatPayment request Url";
    }
}
