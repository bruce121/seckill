package com.cxn.seckill.controller;

import com.cxn.seckill.config.UserKey;
import com.cxn.seckill.model.SeckillOrder;
import com.cxn.seckill.model.SeckillUser;
import com.cxn.seckill.model.User;
import com.cxn.seckill.result.CodeMsg;
import com.cxn.seckill.result.Result;
import com.cxn.seckill.service.RedisService;
import com.cxn.seckill.service.SeckillUserService;
import com.cxn.seckill.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @program: seckill
 * @description: ${description}
 * @author: cxn
 * @create: 2018-04-25 21:56
 * @Version v1.0
 */
@Controller
@RequestMapping("/user")
public class UserController {

    Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private SeckillUserService seckillUserService;

    @RequestMapping("/info")
    @ResponseBody
    public Result<String> info(SeckillUser user){
        SeckillUser userInfo = seckillUserService.getById(user.getId());
        logger.info("userInfo:" + userInfo);
        return Result.success("success");
    }


}
