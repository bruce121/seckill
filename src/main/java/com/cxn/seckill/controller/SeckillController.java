package com.cxn.seckill.controller;

import com.cxn.seckill.config.GoodsKey;
import com.cxn.seckill.config.SeckillKey;
import com.cxn.seckill.model.SeckillOrder;
import com.cxn.seckill.model.SeckillUser;
import com.cxn.seckill.rabbitmq.MQSender;
import com.cxn.seckill.rabbitmq.SeckillMessage;
import com.cxn.seckill.result.CodeMsg;
import com.cxn.seckill.result.Result;
import com.cxn.seckill.service.*;
import com.cxn.seckill.util.MD5Util;
import com.cxn.seckill.util.UUIDUtil;
import com.cxn.seckill.vo.GoodsVo;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @program: seckill
 * @description: ${description}
 * @author: cxn
 * @create: 2018-04-25 21:56
 * @Version v1.0
 */
@Controller
@RequestMapping("/seckill")
public class SeckillController implements InitializingBean {

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private SeckillService seckillService;

    @Autowired
    private RedisService redisService;

    private Map<Long, Boolean> localOverMap = new ConcurrentHashMap<>();

    /**
     * 系统初始化，当项目启动的时候加载秒杀商品的库存到redis中
     *
     * @throws Exception
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        // 预先将秒杀库存信息加载到redis中
        List<GoodsVo> goodsList = goodsService.listGoodsVo();
        if (goodsList == null) {
            return;
        }
        for (GoodsVo goodsVo : goodsList) {
            redisService.set(GoodsKey.getSeckillGoodsStock, "" + goodsVo.getId(), goodsVo.getStockCount());
            localOverMap.put(goodsVo.getId(), false);
        }
    }

    @Autowired
    private MQSender mqSender;

    @RequestMapping(value = "/{path}/do_seckill", method = {RequestMethod.POST})
    @ResponseBody
    public Result<Long> doSeckill(Model model, @PathVariable("path") String path, SeckillUser seckillUser, @RequestParam("goodsId") long goodsId) {

        // 检查path
        boolean check = seckillService.checkPath(seckillUser, path, goodsId);

        if (!check) {

            return Result.error(CodeMsg.REQUEST_ILLEGAL);
        }

        if (seckillUser == null) {
            return Result.error(CodeMsg.SESSION_ERROR);
        }

        // 使用内存标记减少redis访问
        Boolean isOver = localOverMap.get(goodsId);
        if (isOver) {
            return Result.error(CodeMsg.SECKILL_OVER);
        }

        // 预减库存
        Long stock = redisService.decr(GoodsKey.getSeckillGoodsStock, "" + goodsId);

        if (stock < 0) {
            localOverMap.put(goodsId, true);
            return Result.error(CodeMsg.SECKILL_OVER);
        }

        // 判断重复秒杀
        SeckillOrder order = orderService.getSeckillOrderByUserIdGoodsId(seckillUser.getId(), goodsId);
        if (order != null) {
            return Result.error(CodeMsg.REPEATE_SECKILL);
        }

        // 异步下单
        SeckillMessage seckillMessage = new SeckillMessage();
        seckillMessage.setUser(seckillUser);
        seckillMessage.setGoodsId(goodsId);
        mqSender.sendSeckillMessage(seckillMessage);

        // 返回下单成功，秒杀排队中
        return Result.success(0L);
        /**
         // 判断库存
         GoodsVo goods = goodsService.getGoodsVoByGoodsId(goodsId);
         Integer stockCount = goods.getStockCount();
         if (stockCount <= 0) {
         return Result.error(CodeMsg.SECKILL_OVER);
         }

         // 判断是否是重复秒杀
         SeckillOrder seckillOrder = orderService.getSeckillOrderByUserIdGoodsId(seckillUser.getId(), goodsId);

         if (seckillOrder != null) {
         return Result.error(CodeMsg.REPEATE_SECKILL);
         }

         // 减库存，下订单，写入秒杀订单
         OrderInfo orderInfo = seckillService.seckill(seckillUser, goods);

         return Result.success(orderInfo);
         */
    }

    /**
     * 成功 ：返回订单id -> orderId
     * 失败 ：返回-1
     * 排队中：返回0
     *
     * @param user
     * @param goodsId
     * @return
     */
    @RequestMapping(value = "/result", method = {RequestMethod.GET})
    @ResponseBody
    public Result<Long> seckillResult(SeckillUser user, @RequestParam("goodsId") Long goodsId) {
        if (user == null) {
            return Result.error(CodeMsg.SESSION_ERROR);
        }
        // 判断用户是否秒杀成功
        long result = seckillService.getSeckillResult(user.getId(), goodsId);
        return Result.success(result);
    }

    @RequestMapping(value = "/path", method = {RequestMethod.GET})
    @ResponseBody
    public Result<String> seckillPath(SeckillUser user, @RequestParam("goodsId") long goodsId, @RequestParam("verifyCode") int verifyCode) {
        if (user == null) {
            return Result.error(CodeMsg.SESSION_ERROR);
        }
        boolean check = seckillService.checkVerifyCode(user, goodsId, verifyCode);
        if (!check) {
            return Result.error(CodeMsg.REQUEST_ILLEGAL);
        }
        String path = seckillService.createSeckillPath(user, goodsId);
        return Result.success(path);
    }

    @RequestMapping(value = "/verifyCode", method = {RequestMethod.GET})
    @ResponseBody
    public Result<String> getSeckillVerifyCode(HttpServletResponse response, SeckillUser user, @RequestParam("goodsId") long goodsId) {
        if (user == null) {
            return Result.error(CodeMsg.SESSION_ERROR);
        }
        try {
            BufferedImage image = seckillService.createVerifyCode(user, goodsId);
            OutputStream out = response.getOutputStream();
            ImageIO.write(image, "JPEG", out);
            out.flush();
            out.close();
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error(CodeMsg.SECKILL_ERROR);
        }
    }

}
