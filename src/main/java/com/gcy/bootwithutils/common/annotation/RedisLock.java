package com.gcy.bootwithutils.common.annotation;

import com.gcy.bootwithutils.common.constants.RedisLockTypeEnum;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface RedisLock {

    // 生成存入redis唯一key，请求参数的index
    int lockFailed() default 0;

    int tryCount() default 3;

    RedisLockTypeEnum typeEnum();

    long lockTime() default 30;
}
