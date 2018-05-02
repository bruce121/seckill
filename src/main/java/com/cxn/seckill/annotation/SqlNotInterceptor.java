package com.cxn.seckill.annotation;

import java.lang.annotation.*;

/**
 * 有该注解的话，不走自定义mybatis语句拦截器
 *
 **/
@Documented
@Retention(RetentionPolicy.RUNTIME) // 注解不仅被保存到class文件中，jvm加载class文件之后，仍然存在；
@Target({ElementType.METHOD, ElementType.TYPE}) // 该注解修饰类中的方法
@Inherited
public @interface NoSqlInterceptor {
}
