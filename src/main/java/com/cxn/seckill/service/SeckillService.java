package com.cxn.seckill.service;

import com.cxn.seckill.model.OrderInfo;
import com.cxn.seckill.model.SeckillOrder;
import com.cxn.seckill.model.SeckillUser;
import com.cxn.seckill.vo.GoodsVo; /**
 * @program: seckill
 * @description: ${description}
 * @author: cxn
 * @create: 2018-04-29 17:55
 * @Version v1.0
 */
public interface SeckillService {
    OrderInfo seckill(SeckillUser seckillUser, GoodsVo goods);
}
