package com.cxn.seckill.service.impl;

import com.cxn.seckill.dao.GoodsDao;
import com.cxn.seckill.model.SeckillGoods;
import com.cxn.seckill.service.GoodsService;
import com.cxn.seckill.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @program: seckill
 * @description: ${description}
 * @author: cxn
 * @create: 2018-04-29 17:01
 * @Version v1.0
 */
@Service
public class GoodsServiceImpl implements GoodsService {

    @Autowired
    private GoodsDao goodsDao;

    @Override
    public List<GoodsVo> listGoodsVo() {
        return goodsDao.listGoodsVo();
    }

    @Override
    public GoodsVo getGoodsVoByGoodsId(long goodsId) {
        return goodsDao.getGoodsVoByGoodsId(goodsId);
    }

    @Override
    public void reduceStock(GoodsVo goods) {

        SeckillGoods g = new SeckillGoods();
        g.setGoodsId(goods.getId());

        goodsDao.reduceStock(g);
    }
}
