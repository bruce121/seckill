package com.cxn.seckill.vo;

import com.cxn.seckill.model.OrderInfo;

/**
 * @program: seckill
 * @description: ${description}
 * @author: cxn
 * @create: 2018-04-30 10:03
 * @Version v1.0
 */
public class OrderDetailVo {
    private GoodsVo goods;
    private OrderInfo order;

    public GoodsVo getGoods() {
        return goods;
    }

    public void setGoods(GoodsVo goods) {
        this.goods = goods;
    }

    public OrderInfo getOrder() {
        return order;
    }

    public void setOrder(OrderInfo order) {
        this.order = order;
    }

    @Override
    public String toString() {
        return "OrderDetailVo{" +
                "goods=" + goods +
                ", order=" + order +
                '}';
    }
}
