package com.cxn.seckill.config;

import com.cxn.seckill.interceptor.MybatisInterceptor;
import com.cxn.seckill.interceptor.PerformanceInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Description:
 *
 * @CreateDate: 2018/3/28 11:27
 **/
@Configuration
public class MybatisPluginConfig {

    @Value("${mybatis.performance.switch:1}")
    private String performanceSwitch;
    @Value("${mybatis.performance.maxTime:1000}")
    private long maxTime;

    /**
     * 开关打开
     */
    private final static String ON = "0";

    /**
     * 注入Mybatis拦截器，拦截DELETE UPDATE全表更新操作
     *
     * @return
     */
    @Bean
    MybatisInterceptor mybatisInterceptor() {
        MybatisInterceptor mybatisInterceptor = new MybatisInterceptor();
        return mybatisInterceptor;
    }

    /**
     * 注入Mybatis性能分析器，分析sql脚本执行效率
     *
     * @return
     */
    @Bean
    PerformanceInterceptor performanceInterceptor() {
        PerformanceInterceptor performanceInterceptor = new PerformanceInterceptor();
        //设置sql执行最大时常，例：设置500ms，如果执行超过500ms，抛出异常
        performanceInterceptor.setMaxTime(maxTime);
        //设置mybatis性能分析器开关
        performanceInterceptor.setPerformanceSwitch(ON.equals(performanceSwitch) ? true : false);
        return performanceInterceptor;
    }
}
