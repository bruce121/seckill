package com.cxn.seckill.config;

/**
 * @program: seckill
 * @description: ${description}
 * @author: cxn
 * @create: 2018-04-26 23:40
 * @Version v1.0
 */
public class SeckillUserKey extends BasePrefix {

    private static final int TOKEN_EXPIRE = 3600 * 24 * 2;

    private SeckillUserKey(String prefix) {
        super(prefix);
    }

    public SeckillUserKey(int expireSeconds, String prefix) {
        super(expireSeconds, prefix);
    }

    public static SeckillUserKey token = new SeckillUserKey(TOKEN_EXPIRE, "token");
    public static SeckillUserKey getById = new SeckillUserKey(0, "id");

}
