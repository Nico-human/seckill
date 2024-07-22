package com.dong.seckill.controller;

import com.dong.seckill.service.IUserService;
import com.dong.seckill.vo.LoginVo;
import com.dong.seckill.vo.RespBean;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("login")
@Slf4j
public class LoginController {

    @Autowired
    private IUserService userService;

    /**
     * 功能描述: 跳转登录页面
     */
    @RequestMapping("toLogin")
    public String toLogin(){
        return "login";
    }

    /**
     * 登录功能
     */
    @RequestMapping("doLogin")
    @ResponseBody
    public RespBean<String> doLogin(@Validated LoginVo loginVo, HttpServletRequest req, HttpServletResponse resp){
        return userService.doLogin(loginVo, req, resp);
    }

}
