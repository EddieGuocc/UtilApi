package com.gcy.bootwithutils.controller;

import com.gcy.bootwithutils.common.constants.ResultCode;
import com.gcy.bootwithutils.common.exception.Exception400;
import com.gcy.bootwithutils.component.common.SpringBeanLocator;
import com.gcy.bootwithutils.service.PaymentStrategy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequestMapping("/payment")
public class PaymentController {

    @GetMapping("/getPayUrl")
    public String initPayType(@RequestParam("type") String type) {
        PaymentStrategy strategy = SpringBeanLocator.getPaymentByType(type);
        return strategy.requestUrl();
    }

    @PostMapping("/getPayResult")
    public String processPayResult(@RequestParam("type") String type) throws InterruptedException {
        PaymentStrategy strategy = SpringBeanLocator.getPaymentByType(type);
        System.out.println(strategy.doPay());
        System.out.println("第三方支付结果获取中...");
        Thread.sleep(3000);
        return "success";
    }

    public static String getPaymentByType(String type) {
        switch (type) {
            case "1":
                return "wechatPaymentStrategy";
            case "2":
                return  "aliPaymentStrategy";
            default:
                throw new Exception400(ResultCode.PARAMS_INVALID);
        }

    }

}
