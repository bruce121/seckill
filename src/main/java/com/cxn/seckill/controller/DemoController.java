package com.cxn.seckill.controller;

import com.cxn.seckill.result.CodeMsg;
import com.cxn.seckill.result.Result;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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

}
