package com.cxn.seckill.controller;

import com.cxn.seckill.config.GoodsKey;
import com.cxn.seckill.config.SeckillUserKey;
import com.cxn.seckill.model.SeckillUser;
import com.cxn.seckill.result.Result;
import com.cxn.seckill.service.GoodsService;
import com.cxn.seckill.service.RedisService;
import com.cxn.seckill.service.SeckillUserService;
import com.cxn.seckill.service.impl.SeckillUserServiceImpl;
import com.cxn.seckill.vo.GoodsDetailVo;
import com.cxn.seckill.vo.GoodsVo;
import com.cxn.seckill.vo.LoginVo;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.thymeleaf.context.IContext;
import org.thymeleaf.spring4.context.SpringWebContext;
import org.thymeleaf.spring4.view.ThymeleafViewResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;

/**
 * @program: seckill
 * @description: ${description}
 * @author: cxn
 * @create: 2018-04-25 21:56
 * @Version v1.0
 */
@Controller
@RequestMapping("/goods")
public class GoodsController {

    private static Logger log = LoggerFactory.getLogger(GoodsController.class);

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

    @RequestMapping(value = "/to_list", produces = "text/html")
    @ResponseBody
    public String toList(HttpServletRequest request, HttpServletResponse response, Model model, SeckillUser user) {
        model.addAttribute("user", user);
        // 取缓存
        String html = redisService.get(GoodsKey.getGoodsList, "", String.class);
        if (!StringUtils.isEmpty(html)) {
            return html;
        }
        // 查询商品列表
        List<GoodsVo> goodsList = goodsService.listGoodsVo();
        model.addAttribute("goodsList", goodsList);
        // 手动渲染
        SpringWebContext context = new SpringWebContext(request,response,request.getServletContext(), request.getLocale(), model.asMap(), applicationContext);
        html = thymeleafViewResolver.getTemplateEngine().process("goods_list",context);
        if (!StringUtils.isEmpty(html)) {
           redisService.set(GoodsKey.getGoodsList,"",html);
        }
        log.info("用户：" + user + "，跳转到商品列表页。。。");
        return html;
    }

    @RequestMapping(value = "/to_detail/{goodsId}")
    @ResponseBody
    public Result<GoodsDetailVo> toDetail(HttpServletRequest request, HttpServletResponse response, SeckillUser user, @PathVariable("goodsId") long goodsId) {
        log.info("尝试从redis中查询url缓存...");

        // 查询商品列表
        GoodsVo goods = goodsService.getGoodsVoByGoodsId(goodsId);

        // 秒杀状态
        int seckillStatus = 0;
        int remainSeconds = 0;

        long startDate = goods.getStartDate().getTime();
        long endDate = goods.getEndDate().getTime();
        long now = System.currentTimeMillis();

        if (now < startDate) {
            // 秒杀还没开始，倒计时
            seckillStatus = 0;
            remainSeconds = (int) ((startDate - now) / 1000);

        } else if (now > endDate) {
            // 秒杀已结束
            seckillStatus = 2;
            remainSeconds = -1;
        } else {
            // 秒杀进行中
            seckillStatus = 1;
        }

        log.info("用户：" + user + "，跳转到商品详情页。。。");
        GoodsDetailVo goodsDetailVo = new GoodsDetailVo();
        goodsDetailVo.setGoodsVo(goods);
        goodsDetailVo.setRemainSeconds(remainSeconds);
        goodsDetailVo.setSeckillStatus(seckillStatus);
        goodsDetailVo.setSeckillUser(user);

        return Result.success(goodsDetailVo);
    }


    @RequestMapping(value = "/to_detail2/{goodsId}", produces = "text/html")
    @ResponseBody
    public String toDetail2(HttpServletRequest request, HttpServletResponse response, Model model, SeckillUser user, @PathVariable("goodsId") long goodsId) {
        model.addAttribute("user", user);
        log.info("尝试从redis中查询url缓存...");
        // 取缓存
        String html = redisService.get(GoodsKey.getGoodsDetail, goodsId+"", String.class);
        if (!StringUtils.isEmpty(html)) {
            return html;
        }

        // 查询商品列表
        GoodsVo goods = goodsService.getGoodsVoByGoodsId(goodsId);
        model.addAttribute("goods", goods);

        // 秒杀状态
        int seckillStatus = 0;
        int remainSeconds = 0;

        long startDate = goods.getStartDate().getTime();
        long endDate = goods.getEndDate().getTime();
        long now = System.currentTimeMillis();

        if (now < startDate) {
            // 秒杀还没开始，倒计时
            seckillStatus = 0;
            remainSeconds = (int) ((startDate - now) / 1000);

        } else if (now > endDate) {
            // 秒杀已结束
            seckillStatus = 2;
            remainSeconds = -1;
        } else {
            // 秒杀进行中
            seckillStatus = 1;
        }
        model.addAttribute("seckillStatus", seckillStatus);
        model.addAttribute("remainSeconds", remainSeconds);

        // 手动渲染
        log.info("开始手动渲染...");
        SpringWebContext context = new SpringWebContext(request,response,request.getServletContext(), request.getLocale(), model.asMap(), applicationContext);
        html = thymeleafViewResolver.getTemplateEngine().process("goods_detail",context);
        if (!StringUtils.isEmpty(html)) {
            redisService.set(GoodsKey.getGoodsDetail,goodsId+"",html);
        }

        log.info("用户：" + user + "，跳转到商品详情页。。。");
        return html;
    }
}
