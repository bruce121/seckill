package com.cxn.seckill.config;

/**
 * @program: seckill
 * @description: ${description}
 * @author: cxn
 * @create: 2018-04-26 23:41
 * @Version v1.0
 */
public class GoodsKey extends BasePrefix {
    private GoodsKey(int expireSeconds, String prefix) {
        super(expireSeconds, prefix);
    }
    public static GoodsKey getGoodsList = new GoodsKey(5,"gl");
    public static GoodsKey getGoodsDetail = new GoodsKey(5,"gd");

}
