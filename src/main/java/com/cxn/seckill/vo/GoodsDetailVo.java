package com.cxn.seckill.vo;

import com.cxn.seckill.model.SeckillUser;

/**
 * @program: seckill
 * @description: ${description}
 * @author: cxn
 * @create: 2018-04-29 22:53
 * @Version v1.0
 */
public class GoodsDetailVo {

    private int seckillStatus = 0;
    private int remainSeconds = 0;
    private GoodsVo goods;
    private SeckillUser user;

    public int getSeckillStatus() {
        return seckillStatus;
    }

    public void setSeckillStatus(int seckillStatus) {
        this.seckillStatus = seckillStatus;
    }

    public int getRemainSeconds() {
        return remainSeconds;
    }

    public void setRemainSeconds(int remainSeconds) {
        this.remainSeconds = remainSeconds;
    }

    public GoodsVo getGoods() {
        return goods;
    }

    public void setGoods(GoodsVo goods) {
        this.goods = goods;
    }

    public SeckillUser getUser() {
        return user;
    }

    public void setUser(SeckillUser user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "GoodsDetailVo{" +
                "seckillStatus=" + seckillStatus +
                ", remainSeconds=" + remainSeconds +
                ", goods=" + goods +
                ", user=" + user +
                '}';
    }
}
