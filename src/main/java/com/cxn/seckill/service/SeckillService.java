package com.cxn.seckill.service;

import com.cxn.seckill.model.OrderInfo;
import com.cxn.seckill.model.SeckillOrder;
import com.cxn.seckill.model.SeckillUser;
import com.cxn.seckill.vo.GoodsVo;

import java.awt.image.BufferedImage;

/**
 * @program: seckill
 * @description: ${description}
 * @author: cxn
 * @create: 2018-04-29 17:55
 * @Version v1.0
 */
public interface SeckillService {
    OrderInfo seckill(SeckillUser seckillUser, GoodsVo goods);

    long getSeckillResult(Long id, Long goodsId);

    String createSeckillPath(SeckillUser user, long goodsId);

    boolean checkPath(SeckillUser seckillUser, String path, long goodsId);

    BufferedImage createVerifyCode(SeckillUser user, long goodsId);

    boolean checkVerifyCode(SeckillUser user, long goodsId, int verifyCode);
}
