package com.cxn.seckill.model;

/**
 * @program: seckill
 * @description: ${description}
 * @author: cxn
 * @create: 2018-04-29 10:44
 * @Version v1.0
 */
public class SeckillOrder {

    private Long id;
    private Long userId;
    private Long orderId;
    private Long goodsId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Long getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Long goodsId) {
        this.goodsId = goodsId;
    }

    @Override
    public String toString() {
        return "SeckillOrder{" +
                "id=" + id +
                ", userId=" + userId +
                ", orderId=" + orderId +
                ", goodsId=" + goodsId +
                '}';
    }
}
