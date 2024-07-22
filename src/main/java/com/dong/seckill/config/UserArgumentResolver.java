package com.dong.seckill.config;

import com.dong.seckill.pojo.User;
import com.dong.seckill.service.IUserService;
import com.dong.seckill.utils.CookieUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

/**
 * @Description:
 * @Author: Dong
 * @Date: 2024/7/3
 */
@Component
public class UserArgumentResolver implements HandlerMethodArgumentResolver {

    @Autowired
    private IUserService userService;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        Class<?> clazz = parameter.getParameterType();
        return clazz == User.class;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        HttpServletRequest req = webRequest.getNativeRequest(HttpServletRequest.class);
        HttpServletResponse resp = webRequest.getNativeResponse(HttpServletResponse.class);

        //获取sessionID
        String ticket = CookieUtil.getCookieValue(req, "userTicket");
        if (StringUtils.isEmpty(ticket)) {
            return null;
        }

        //根据sessionID获取User
        return userService.getUserByCookie(ticket, req, resp);
    }
}
