package com.gcy.bootwithutils.component.common;

import com.gcy.bootwithutils.common.constants.ResultCode;
import com.gcy.bootwithutils.common.exception.Exception400;
import com.gcy.bootwithutils.service.PaymentStrategy;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class SpringBeanLocator implements ApplicationContextAware {

    private static ConfigurableApplicationContext context;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        context = (ConfigurableApplicationContext) applicationContext;
    }

    public static PaymentStrategy getPaymentByType(String type) {
        switch (type) {
            case "1":
                return (PaymentStrategy) context.getBean("wechatPaymentStrategy");
            case "2":
                return (PaymentStrategy) context.getBean("aliPaymentStrategy");
            default:
                throw new Exception400(ResultCode.PARAMS_INVALID);
        }

    }

}
