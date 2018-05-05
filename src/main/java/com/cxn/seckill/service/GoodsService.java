package com.cxn.seckill.service;

import com.cxn.seckill.vo.GoodsVo;

import java.util.List;

/**
 * @program: seckill
 * @description: ${description}
 * @author: cxn
 * @create: 2018-04-26 21:35
 * @Version v1.0
 */
public interface GoodsService {

    public List<GoodsVo> listGoodsVo();

    GoodsVo getGoodsVoByGoodsId(long goodsId);

    boolean reduceStock(GoodsVo goods);
}
