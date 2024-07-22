package com.dong.seckill.controller;


import com.dong.seckill.pojo.User;
import com.dong.seckill.rabbitmq.MQSender;
import com.dong.seckill.vo.RespBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author dong
 * @since 2024-06-28
 */
@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private MQSender mqSender;

    @RequestMapping("info")
    @ResponseBody
    public RespBean<User> info(User user) {
        return RespBean.success(user);
    }

//    // 测试发送RabbitMQ消息
//    @RequestMapping("mq")
//    @ResponseBody
//    public void mq() {
//        mqSender.send("hello");
//    }
//
//    @RequestMapping("mq/fanout")
//    @ResponseBody
//    public void mqFanout() {
//        mqSender.sendFanout("hello");
//    }
//
//    @RequestMapping("mq/direct01")
//    @ResponseBody
//    public void mqDirect01() {
//        mqSender.sendDirect01("hello, apple");
//    }
//
//    @RequestMapping("mq/direct02")
//    @ResponseBody
//    public void mqDirect02() {
//        mqSender.sendDirect02("hello, orange");
//    }
//
//    @RequestMapping("mq/topic01")
//    @ResponseBody
//    public void mqTopic01() {
//        mqSender.sendTopic03("hello, topic01");
//    }
//
//    @RequestMapping("mq/topic02")
//    @ResponseBody
//    public void mqTopic02() {
//        mqSender.sendTopic04("hello, topic02");
//    }
//
//    @RequestMapping("mq/headers01")
//    @ResponseBody
//    public void mqHeaders01() {
//        mqSender.sendHeaders05("hello, Headers01");
//    }
//
//    @RequestMapping("mq/headers02")
//    @ResponseBody
//    public void mqHeaders02() {
//        mqSender.sendHeaders06("hello, Headers02");
//    }



}
