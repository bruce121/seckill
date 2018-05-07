package com.cxn.seckill.config;

import com.cxn.seckill.interceptor.UserContext;
import com.cxn.seckill.model.SeckillUser;
import com.cxn.seckill.service.SeckillUserService;
import com.cxn.seckill.service.impl.SeckillUserServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @program: seckill
 * @description: ${description}
 * @author: cxn
 * @create: 2018-04-28 23:59
 * @Version v1.0
 */
@Service
public class UserArgumentResolver implements HandlerMethodArgumentResolver {

    @Autowired
    private SeckillUserService seckillUserService;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        Class<?> clazz = parameter.getParameterType();
        return clazz == SeckillUser.class;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        // 已经转移到注解限流代码中
        //HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
        //HttpServletResponse response = webRequest.getNativeResponse(HttpServletResponse.class);
        //String paramToken = request.getParameter(SeckillUserServiceImpl.COOKIE_NAME_TOKEN);
        //String cookieToken = getCookieValue(request, SeckillUserServiceImpl.COOKIE_NAME_TOKEN);
        //
        //if (StringUtils.isEmpty(cookieToken) && StringUtils.isEmpty(paramToken)) {
        //    return null;
        //}
        //String token = StringUtils.isEmpty(paramToken) ? cookieToken : paramToken;
        //return seckillUserService.getByToken(response, token);

        return UserContext.getUser();
    }
    // 已经转移到注解限流中了
    //private String getCookieValue(HttpServletRequest request, String cookieName){
    //    Cookie[] cookies = request.getCookies();
    //    if (cookies == null || cookies.length <= 0) {
    //        return null;
    //    }
    //    for (Cookie cookie : cookies) {
    //        if (cookieName.equals(cookie.getName())) {
    //            return cookie.getValue();
    //        }
    //    }
    //    return null;
    //}

}
