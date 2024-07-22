package com.dong.seckill.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.dong.seckill.pojo.User;
import com.dong.seckill.vo.LoginVo;
import com.dong.seckill.vo.RespBean;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author dong
 * @since 2024-06-28
 */
public interface IUserService extends IService<User> {

    RespBean<String> doLogin(LoginVo loginVo, HttpServletRequest req, HttpServletResponse resp);

    User getUserByCookie(String userTicket, HttpServletRequest req, HttpServletResponse resp);

    RespBean<String> updatePassword(String userTicket, String password, HttpServletRequest req, HttpServletResponse resp);
}
