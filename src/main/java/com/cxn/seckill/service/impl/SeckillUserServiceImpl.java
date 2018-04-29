package com.cxn.seckill.service.impl;

import com.cxn.seckill.config.SeckillUserKey;
import com.cxn.seckill.dao.SeckillUserDao;
import com.cxn.seckill.exception.GlobalException;
import com.cxn.seckill.model.SeckillUser;
import com.cxn.seckill.result.CodeMsg;
import com.cxn.seckill.service.RedisService;
import com.cxn.seckill.service.SeckillUserService;
import com.cxn.seckill.util.MD5Util;
import com.cxn.seckill.util.UUIDUtil;
import com.cxn.seckill.vo.LoginVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

/**
 * @program: seckill
 * @description: ${description}
 * @author: cxn
 * @create: 2018-04-28 15:25
 * @Version v1.0
 */
@Service
public class SeckillUserServiceImpl implements SeckillUserService {

    public static final String COOKIE_NAME_TOKEN = "token";

    @Autowired
    private SeckillUserDao seckillUserDao;

    @Autowired
    private RedisService redisService;

    @Override
    public SeckillUser getById(long id) {

        SeckillUser user = redisService.get(SeckillUserKey.getById, id + "", SeckillUser.class);
        if (user == null) {
            user = seckillUserDao.getById(id);
            redisService.set(SeckillUserKey.getById, id + "", user);
        }
        return user;
    }
    @Override
    public boolean updatePassword(String token, long id, String newPassword) {
        SeckillUser seckillUser = getById(id);
        if (seckillUser == null) {
            throw new GlobalException(CodeMsg.MOBILE_NOT_EXIST);
        }
        // 更新数据库
        SeckillUser toBeUpdate = new SeckillUser();
        toBeUpdate.setId(id);
        toBeUpdate.setPassword(MD5Util.formPassToDBPass(newPassword, seckillUser.getSalt()));
        seckillUserDao.update(toBeUpdate);

        // 处理缓存,删除用户对象缓存，更新用户token缓存
        redisService.delete(SeckillUserKey.getById, ""+id);
        seckillUser.setPassword(toBeUpdate.getPassword());
        redisService.set(SeckillUserKey.token, token, seckillUser);
        return true;
    }

    @Override
    public SeckillUser getByToken(HttpServletResponse response, String token) {
        if (StringUtils.isEmpty(token)) {
            return null;
        }

        SeckillUser user = redisService.get(SeckillUserKey.token, token, SeckillUser.class);

        if (user != null) {
            // 延长有效期
            addCookie(response, token, user);
        }
        return user;
    }

    @Override
    public boolean login(HttpServletResponse response, LoginVo loginVo) {
        if (loginVo == null) {
            throw new GlobalException(CodeMsg.SERVER_ERROR);
        }
        String mobile = loginVo.getMobile();
        String formPass = loginVo.getPassword();
        // 判断手机号是否存在
        SeckillUser seckillUser = getById(Long.parseLong(mobile));
        if (seckillUser == null) {
            throw new GlobalException(CodeMsg.MOBILE_NOT_EXIST);
        }
        // 验证密码
        String dbPass = seckillUser.getPassword();
        String dbSalt = seckillUser.getSalt();
        String calPass = MD5Util.formPassToDBPass(formPass, dbSalt);
        if (!dbPass.equals(calPass)) {
            throw new GlobalException(CodeMsg.PASSWORD_ERROR);
        }
        // 生成一个分布式session
        String token = UUIDUtil.uuid();
        addCookie(response, token, seckillUser);
        return true;
    }

    private void addCookie(HttpServletResponse response, String token, SeckillUser seckillUser) {

        redisService.set(SeckillUserKey.token, token, seckillUser);
        Cookie cookie = new Cookie(COOKIE_NAME_TOKEN, token);
        cookie.setMaxAge(SeckillUserKey.token.expireSeconds());
        cookie.setPath("/");
        response.addCookie(cookie);
    }


}
