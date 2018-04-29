package com.cxn.seckill.config;

/**
 * @program: seckill
 * @description: ${description}
 * @author: cxn
 * @create: 2018-04-26 23:34
 * @Version v1.0
 */
public interface KeyPrefix {
    /**
     * 过期时间
     * @return
     */
    public int expireSeconds();

    /**
     * 获得前缀
     * @return
     */
    public String getPrefix();
}
