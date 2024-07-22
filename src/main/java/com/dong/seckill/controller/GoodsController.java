package com.dong.seckill.controller;

import com.dong.seckill.pojo.Goods;
import com.dong.seckill.pojo.User;
import com.dong.seckill.service.IGoodsService;
import com.dong.seckill.service.IUserService;
import com.dong.seckill.vo.DetailVo;
import com.dong.seckill.vo.GoodsVo;
import com.dong.seckill.vo.RespBean;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.spring6.view.ThymeleafViewResolver;
import org.thymeleaf.spring6.web.webflux.ISpringWebFluxWebExchange;
import org.thymeleaf.web.IWebExchange;
import org.thymeleaf.web.servlet.IServletWebExchange;
import org.thymeleaf.web.servlet.JakartaServletWebApplication;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @Description: 商品
 * @Author: Dong
 * @Date: 2024/7/2
 */
@Controller
@RequestMapping("goods")
public class GoodsController {

    @Autowired
    private IUserService userService;

    @Autowired
    private IGoodsService goodsService;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private ThymeleafViewResolver thymeleafViewResolver;

    /**
     * 跳转商品列表页
     * 线程数: 50000 循环10次
     * Windows 优化前 QPS: 2050
     *          缓存后 QPS:
     * Linux 优化前 QPS: 3724
     *
     */
    @RequestMapping(value = "toList", produces = "text/html;charset=utf-8")
    @ResponseBody
    public String toList(Model model, User user, HttpServletRequest req, HttpServletResponse resp){

        //Redis中获取页面, 如果不为空, 直接返回页面
        ValueOperations valueOperations = redisTemplate.opsForValue();
        String html = (String) valueOperations.get("goodsList");
        if (!StringUtils.isEmpty(html)){
            return html;
        }

        model.addAttribute("user", user);
        model.addAttribute("goodsList", goodsService.findGoodsVo());
//        return "goodsList";
        //如果为空, 手动渲染, 存入Redis并返回
        IServletWebExchange webExchange = JakartaServletWebApplication.buildApplication(req.getServletContext()).buildExchange(req, resp);
        WebContext context = new WebContext(webExchange, req.getLocale(), model.asMap());
        html = thymeleafViewResolver.getTemplateEngine().process("goodsList", context);
        if (!StringUtils.isEmpty(html)){
            valueOperations.set("goodsList", html, 60, TimeUnit.SECONDS);
        }
        return html;
    }


//    @RequestMapping(value = "detail2/{goodsId}", produces = "text/html;charset=utf-8")
//    @ResponseBody
//    public String toDetail2(@PathVariable Long goodsId, Model model, User user, HttpServletRequest req, HttpServletResponse resp){
//        ValueOperations valueOperations = redisTemplate.opsForValue();
//        // Redis中获取页面, 如果不为空, 直接返回页面
//        String html = (String) valueOperations.get("goodsDetail:" + goodsId);
//        if(!StringUtils.isEmpty(html)){
//            return html;
//        }
//
//        GoodsVo goodsVo = goodsService.findGoodsVoByGoodsId(goodsId);
//        Date startTime = goodsVo.getStartDate();
//        Date endTime = goodsVo.getEndDate();
//        Date nowTime = new Date();
//        //秒杀状态
//        int secKillStatus = 0;
//        //秒杀倒计时
//        int remainSec = 0;
//
//        if (nowTime.before(startTime)) {
//            remainSec = (int) ((startTime.getTime() - nowTime.getTime()) / 1000);//秒杀倒计时
//        }else if (nowTime.after(endTime)) {
//            secKillStatus = 2; //秒杀已结束
//            remainSec = -1;
//        }else {
//            secKillStatus = 1; //秒杀进行中
//        }
//
//        model.addAttribute("remainSec", remainSec);
//        model.addAttribute("secKillStatus", secKillStatus);
//        model.addAttribute("user", user);
//        model.addAttribute("goods", goodsVo);
//
//        //如果为空, 手动渲染, 存入Redis并返回
//        IWebExchange webExchange = JakartaServletWebApplication.buildApplication(req.getServletContext()).buildExchange(req, resp);
//        WebContext context = new WebContext(webExchange, req.getLocale(), model.asMap());
//        html = thymeleafViewResolver.getTemplateEngine().process("goodsDetail", context);
//        if (!StringUtils.isEmpty(html)){
//            valueOperations.set("goodsDetail" + goodsId, html, 60, TimeUnit.SECONDS);
//        }
//        return html;
//    }

    @RequestMapping(value = "detail/{goodsId}")
    @ResponseBody
    public RespBean<DetailVo> toDetail(@PathVariable Long goodsId, User user){

        GoodsVo goodsVo = goodsService.findGoodsVoByGoodsId(goodsId);
        Date startTime = goodsVo.getStartDate();
        Date endTime = goodsVo.getEndDate();
        Date nowTime = new Date();
        //秒杀状态
        int secKillStatus = 0;
        //秒杀倒计时
        int remainSec = 0;

        if (nowTime.before(startTime)) {
            remainSec = (int) ((startTime.getTime() - nowTime.getTime()) / 1000);//秒杀倒计时
        }else if (nowTime.after(endTime)) {
            secKillStatus = 2; //秒杀已结束
            remainSec = -1;
        }else {
            secKillStatus = 1; //秒杀进行中
        }

        DetailVo detailVo = new DetailVo();
        detailVo.setUser(user);
        detailVo.setGoodsVo(goodsVo);
        detailVo.setSecKillStatus(secKillStatus);
        detailVo.setRemainSeconds(remainSec);
        return RespBean.success(detailVo);
    }
}
