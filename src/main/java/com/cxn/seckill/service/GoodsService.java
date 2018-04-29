package com.cxn.seckill.service;

import com.cxn.seckill.dao.GoodsDao;
import com.cxn.seckill.dao.UserDao;
import com.cxn.seckill.model.User;
import com.cxn.seckill.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    void reduceStock(GoodsVo goods);
}
