package com.cxn.seckill.service.impl;

import com.cxn.seckill.config.GoodsKey;
import com.cxn.seckill.model.OrderInfo;
import com.cxn.seckill.model.SeckillOrder;
import com.cxn.seckill.model.SeckillUser;
import com.cxn.seckill.service.GoodsService;
import com.cxn.seckill.service.OrderService;
import com.cxn.seckill.service.RedisService;
import com.cxn.seckill.service.SeckillService;
import com.cxn.seckill.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @program: seckill
 * @description: ${description}
 * @author: cxn
 * @create: 2018-04-29 17:55
 * @Version v1.0
 */
@Service
public class SeckillServiceImpl implements SeckillService {

    @Autowired
    private GoodsService goodsService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private RedisService redisService;

    @Transactional
    @Override
    public OrderInfo seckill(SeckillUser seckillUser, GoodsVo goods) {
        // 减库存 下订单 写入秒杀订单
        boolean success = goodsService.reduceStock(goods);
        if (success) {
            OrderInfo orderInfo = orderService.createOrder(seckillUser, goods);
            return orderInfo;
        } else {
            setGoodsOver(goods.getId());
            return null;
        }

    }

    @Override
    public long getSeckillResult(Long id, Long goodsId) {

        SeckillOrder order = orderService.getSeckillOrderByUserIdGoodsId(id, goodsId);
        if (order != null) {
            return order.getOrderId();
        } else {
            boolean isOver = getGoodsOver(goodsId);
            if (isOver) {
                return -1;
            } else {
                return 0;
            }
        }
    }

    private void setGoodsOver(Long goodsId) {
        redisService.set(GoodsKey.goodsIsOver, "" + goodsId, true);
    }

    private boolean getGoodsOver(Long goodsId) {
        return redisService.exists(GoodsKey.goodsIsOver,"" + goodsId);
    }
}
