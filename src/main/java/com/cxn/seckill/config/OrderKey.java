package com.cxn.seckill.config;

/**
 * @program: seckill
 * @description: ${description}
 * @author: cxn
 * @create: 2018-04-26 23:41
 * @Version v1.0
 */
public class OrderKey extends BasePrefix {
    private OrderKey(int expireSeconds, String prefix) {
        super(expireSeconds, prefix);
    }

    public static OrderKey getOrderSeckillOrderByUidGid = new OrderKey(0,"seckillUidGid");
}
