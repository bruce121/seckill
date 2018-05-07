package com.cxn.seckill.interceptor;

import com.cxn.seckill.model.SeckillUser;

/**
 * @program: seckill
 * @description: ${description}
 * @author: cxn
 * @create: 2018-05-07 23:23
 * @Version v1.0
 */
public class UserContext {
    private static ThreadLocal<SeckillUser> userHolder = new ThreadLocal<>();
    public static void setUser(SeckillUser user){
        userHolder.set(user);
    }
    public static SeckillUser getUser(){
        return userHolder.get();
    }
}
