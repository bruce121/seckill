package com.cxn.seckill.service.impl;

import com.cxn.seckill.model.Goods;
import com.cxn.seckill.model.OrderInfo;
import com.cxn.seckill.model.SeckillOrder;
import com.cxn.seckill.model.SeckillUser;
import com.cxn.seckill.service.GoodsService;
import com.cxn.seckill.service.OrderService;
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
    @Transactional
    @Override
    public OrderInfo seckill(SeckillUser seckillUser, GoodsVo goods) {
        // 减库存 下订单 写入秒杀订单
        goodsService.reduceStock(goods);

        OrderInfo orderInfo =  orderService.createOrder(seckillUser, goods);
       return orderInfo;


    }
}
