package com.cxn.seckill.controller;

import com.cxn.seckill.config.UserKey;
import com.cxn.seckill.model.OrderInfo;
import com.cxn.seckill.model.SeckillOrder;
import com.cxn.seckill.model.SeckillUser;
import com.cxn.seckill.model.User;
import com.cxn.seckill.result.CodeMsg;
import com.cxn.seckill.result.Result;
import com.cxn.seckill.service.*;
import com.cxn.seckill.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

/**
 * @program: seckill
 * @description: ${description}
 * @author: cxn
 * @create: 2018-04-25 21:56
 * @Version v1.0
 */
@Controller
@RequestMapping("/seckill")
public class SeckillController {

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private SeckillService seckillService;

    @RequestMapping(value = "/do_seckill", method = {RequestMethod.POST})
    @ResponseBody
    public Result<OrderInfo> doSeckill(Model model, SeckillUser seckillUser, @RequestParam("goodsId") long goodsId){

        if (seckillUser == null) {
            return Result.error(CodeMsg.SESSION_ERROR);
        }
        // 判断库存
        GoodsVo goods = goodsService.getGoodsVoByGoodsId(goodsId);
        Integer stockCount = goods.getStockCount();
        if (stockCount<=0) {
            return Result.error(CodeMsg.SECKILL_ERROR);
        }

        // 判断是否是重复秒杀
        SeckillOrder seckillOrder =  orderService.getSeckillOrderByUserIdGoodsId(seckillUser.getId(), goodsId);

        if (seckillOrder != null) {
            return Result.error(CodeMsg.REPEATE_SECKILL);
        }

        // 减库存，下订单，写入秒杀订单
        OrderInfo orderInfo = seckillService.seckill(seckillUser, goods);

        return Result.success(orderInfo);
    }


}
