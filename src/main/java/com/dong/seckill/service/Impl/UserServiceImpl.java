package com.dong.seckill.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dong.seckill.exception.GlobalException;
import com.dong.seckill.mapper.UserMapper;
import com.dong.seckill.pojo.User;
import com.dong.seckill.service.IUserService;
import com.dong.seckill.utils.CookieUtil;
import com.dong.seckill.utils.MD5Util;
import com.dong.seckill.utils.UUIDUtil;
import com.dong.seckill.vo.LoginVo;
import com.dong.seckill.vo.RespBean;
import com.dong.seckill.vo.RespBeanEnum;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author dong
 * @since 2024-06-28
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public RespBean<String> doLogin(LoginVo loginVo, HttpServletRequest req, HttpServletResponse resp) {

        String mobile = loginVo.getMobile();
        String password = loginVo.getPassword();

        // 根据手机号获取用户
        User user = userMapper.selectById(mobile);
        if (user == null) {
            throw new GlobalException(RespBeanEnum.ID_ERROR);
        }

        // 校验密码
        if (!MD5Util.fromPassToDBPass(password, user.getSalt()).equals(user.getPassword())) {
            throw new GlobalException(RespBeanEnum.PASSWORD_ERROR);
        }

        String ticket = UUIDUtil.getUUID(); //生成SessionID // session存储在服务器中, cookie存储在客户端(浏览器)中

//        req.getSession().setAttribute(ticket, user); // 在session中存{key: ticket, value: user}
        redisTemplate.opsForValue().set("user:"+ticket, user);
        CookieUtil.setCookie(req, resp, "userTicket", ticket); //将SessionID存在cookie中{name: "userTicket", value: ticket}
        return RespBean.success(ticket);


    }

    @Override
    public User getUserByCookie(String userTicket, HttpServletRequest req, HttpServletResponse resp) {
        if (StringUtils.isEmpty(userTicket)){
            return null;
        }
        User user = (User) redisTemplate.opsForValue().get("user:"+userTicket);
        if (user != null){
            CookieUtil.setCookie(req, resp, "userTicket", userTicket);
        }
        return user;
    }

    @Override
    public RespBean<String> updatePassword(String userTicket, String password, HttpServletRequest req, HttpServletResponse resp) {
        User user = getUserByCookie(userTicket, req, resp);
        if (user == null){
            throw new GlobalException(RespBeanEnum.MOBILE_NOT_EXIST);
        }
        user.setPassword(MD5Util.inputPassToDBPass(password, user.getSalt()));
        int result = userMapper.updateById(user);
        if (1==result) {
            //删除Redis
            redisTemplate.delete("user:" + userTicket);
            return RespBean.success();
        }
        return RespBean.error(RespBeanEnum.PASSWORD_UPDATE_FAIL);
    }


}
