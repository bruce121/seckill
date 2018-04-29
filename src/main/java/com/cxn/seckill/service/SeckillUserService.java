package com.cxn.seckill.service;

import com.cxn.seckill.model.SeckillUser;
import com.cxn.seckill.result.CodeMsg;
import com.cxn.seckill.vo.LoginVo;

import javax.servlet.http.HttpServletResponse;

/**
 * @program: seckill
 * @description: ${description}
 * @author: cxn
 * @create: 2018-04-28 15:24
 * @Version v1.0
 */
public interface SeckillUserService {

    public SeckillUser getById(long id);

    boolean login(HttpServletResponse response, LoginVo loginVo);

    SeckillUser getByToken(HttpServletResponse response, String token);

    public boolean updatePassword(String token, long id, String newPassword);

}
