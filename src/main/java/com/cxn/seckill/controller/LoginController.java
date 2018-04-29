package com.cxn.seckill.controller;

import com.cxn.seckill.result.CodeMsg;
import com.cxn.seckill.result.Result;
import com.cxn.seckill.service.RedisService;
import com.cxn.seckill.service.SeckillUserService;
import com.cxn.seckill.service.UserService;
import com.cxn.seckill.util.ValidateUtil;
import com.cxn.seckill.vo.LoginVo;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * @program: seckill
 * @description: ${description}
 * @author: cxn
 * @create: 2018-04-25 21:56
 * @Version v1.0
 */
@Controller
@RequestMapping("/login")
public class LoginController {

    private static Logger log = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    private SeckillUserService seckillUserService;

    @Autowired
    private RedisService redisService;

    @RequestMapping("/to_login")
    public String toLogin(){
     return "login";
    }

    @RequestMapping("/do_login")
    @ResponseBody
    public Result<Boolean> doLogin(HttpServletResponse response, @Valid LoginVo loginVo){

        log.info("用户登录信息：", loginVo.toString());
        // 登录
        seckillUserService.login(response, loginVo);
        return Result.success(true);

    }

}
