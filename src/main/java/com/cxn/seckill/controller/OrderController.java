package com.cxn.seckill.controller;

import com.cxn.seckill.config.GoodsKey;
import com.cxn.seckill.model.OrderInfo;
import com.cxn.seckill.model.SeckillUser;
import com.cxn.seckill.result.CodeMsg;
import com.cxn.seckill.result.Result;
import com.cxn.seckill.service.GoodsService;
import com.cxn.seckill.service.OrderService;
import com.cxn.seckill.service.RedisService;
import com.cxn.seckill.service.SeckillUserService;
import com.cxn.seckill.vo.GoodsDetailVo;
import com.cxn.seckill.vo.GoodsVo;
import com.cxn.seckill.vo.OrderDetailVo;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.thymeleaf.spring4.context.SpringWebContext;
import org.thymeleaf.spring4.view.ThymeleafViewResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @program: seckill
 * @description: ${description}
 * @author: cxn
 * @create: 2018-04-25 21:56
 * @Version v1.0
 */
@Controller
@RequestMapping("/order")
public class OrderController {

    private static Logger log = LoggerFactory.getLogger(OrderController.class);

    @Autowired
    private RedisService redisService;
    @Autowired
    private SeckillUserService seckillUserService;
    @Autowired
    private GoodsService goodsService;
    @Autowired
    private ThymeleafViewResolver thymeleafViewResolver;
    @Autowired
    private ApplicationContext applicationContext;
    @Autowired
    private OrderService orderService;

    @RequestMapping(value = "/detail")
    @ResponseBody
    public Result<OrderDetailVo> toDetail(SeckillUser user, @RequestParam("orderId") long orderId) {
        OrderDetailVo orderDetailVo = new OrderDetailVo();
        if (user == null) {
            return Result.error(CodeMsg.SESSION_ERROR);
        }
        OrderInfo orderInfo = orderService.getOrderById(orderId);
        if (orderInfo == null) {
            return Result.error(CodeMsg.ORDER_NOT_EXIST);
        }

        Long goodsId = orderInfo.getGoodsId();
        GoodsVo goods = goodsService.getGoodsVoByGoodsId(goodsId);
        orderDetailVo.setGoods(goods);
        orderDetailVo.setOrder(orderInfo);

        return Result.success(orderDetailVo);
    }


}
