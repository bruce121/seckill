package com.cxn.seckill.controller;

import com.cxn.seckill.config.UserKey;
import com.cxn.seckill.model.SeckillUser;
import com.cxn.seckill.model.User;
import com.cxn.seckill.rabbitmq.MQSender;
import com.cxn.seckill.result.CodeMsg;
import com.cxn.seckill.result.Result;
import com.cxn.seckill.service.RedisService;
import com.cxn.seckill.service.UserService;
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
@RequestMapping("/demo")
public class DemoController {

    @Autowired
    private UserService userService;

    @Autowired
    private RedisService redisService;

    @Autowired
    private MQSender mqSender;

    @RequestMapping("/hello")
    @ResponseBody
    public Result<String> hello(){
     return Result.success("hello seckill");
    }

    @RequestMapping("/helloError")
    @ResponseBody
    public Result<CodeMsg> helloError(){
        CodeMsg codeMsg = CodeMsg.SERVER_ERROR;
        return Result.error(codeMsg);
    }

    @RequestMapping("/helloThymleaf")
    public String helloThymleaf(Model model){
        model.addAttribute("name","seckill");
        return "hello";
    }

    @RequestMapping("/db/get/{id}")
    @ResponseBody
    public Result<User> dbGet(@PathVariable("id") Long id){
        User user = userService.getById(id);
        return Result.success(user);
    }

    @RequestMapping("/db/tx")
    @ResponseBody
    public Result<Boolean> dbTx(){
        userService.tx();
        return Result.success(true);
    }

    @RequestMapping("/redis/get")
    @ResponseBody
    public Result<User> redisGet(){
        User user = redisService.get(UserKey.getById, "key1", User.class);
        return Result.success(user);
    }

    @RequestMapping("/redis/set")
    @ResponseBody
    public Result<Boolean> redisSet(){

        User user = new User();
        user.setId(1L);
        user.setName("userName");

       boolean boo = redisService.set(UserKey.getById,"key1", user);
        return Result.success(boo);
    }

    @RequestMapping("/rabbitmq/send")
    @ResponseBody
    public Result<Boolean> amqp(){
        SeckillUser seckillUser = new SeckillUser();
        seckillUser.setId(8L);
        seckillUser.setPassword("password");
        seckillUser.setNickname("nickname");
        mqSender.send(seckillUser);
        return Result.success(true);
    }

}
