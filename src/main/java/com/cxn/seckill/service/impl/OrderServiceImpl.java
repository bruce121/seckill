package com.cxn.seckill.service.impl;

import com.cxn.seckill.dao.OrderDao;
import com.cxn.seckill.model.OrderInfo;
import com.cxn.seckill.model.SeckillOrder;
import com.cxn.seckill.model.SeckillUser;
import com.cxn.seckill.service.OrderService;
import com.cxn.seckill.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.channels.SelectionKey;
import java.util.Date;

/**
 * @program: seckill
 * @description: ${description}
 * @author: cxn
 * @create: 2018-04-29 17:51
 * @Version v1.0
 */
@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderDao orderDao;

    @Override
    public SeckillOrder getSeckillOrderByUserIdGoodsId(Long userId, long goodsId) {
        return orderDao.getSeckillOrderByUserIdGoodsId(userId, goodsId);
    }

    @Transactional
    @Override
    public OrderInfo createOrder(SeckillUser user, GoodsVo goods) {
        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setCreateDate(new Date());
        orderInfo.setDeliveryAddrId(0L);
        orderInfo.setGoodsCount(1);
        orderInfo.setGoodsId(goods.getId());
        orderInfo.setGoodsName(goods.getGoodsName());
        orderInfo.setGoodsPrice(goods.getSeckillPrice());
        orderInfo.setOrderChannel(1);

        orderInfo.setStatus(0);
        orderInfo.setUserId(user.getId());

        long orderId = orderDao.insert(orderInfo);
        SeckillOrder seckillOrder = new SeckillOrder();
        seckillOrder.setGoodsId(goods.getId());
        seckillOrder.setOrderId(orderId);
        seckillOrder.setUserId(user.getId());
        long seckillOrderId = orderDao.insertSeckillOrder(seckillOrder);

        return orderInfo;
    }
}
