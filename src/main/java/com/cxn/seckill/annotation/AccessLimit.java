package com.cxn.seckill.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @program: seckill
 * @description: ${description}
 * @author: cxn
 * @create: 2018-05-07 23:11
 * @Version v1.0
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface AccessLimit {
    // 多少秒
    int seconds();
    // 最大访问次数
    int maxCount();
    // 是否需要登录
    boolean needLogin() default true;
}
