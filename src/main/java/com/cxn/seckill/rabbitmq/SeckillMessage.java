package com.cxn.seckill.rabbitmq;

import com.cxn.seckill.model.SeckillUser;

/**
 * @program: seckill
 * @description: ${description}
 * @author: cxn
 * @create: 2018-05-05 21:32
 * @Version v1.0
 */
public class SeckillMessage {
    private SeckillUser user;
    private long goodsId;

    public SeckillUser getUser() {
        return user;
    }

    public void setUser(SeckillUser user) {
        this.user = user;
    }

    public long getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(long goodsId) {
        this.goodsId = goodsId;
    }
}
