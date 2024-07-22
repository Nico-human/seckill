package com.dong.seckill.rabbitmq;

import com.dong.seckill.pojo.SeckillMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Description: 消息发送者
 * @Author: Dong
 * @Date: 2024/7/18
 */
@Service
@Slf4j
public class MQSender {

    @Autowired
    private RabbitTemplate rabbitTemplate;

//    public void send(Object msg){
//        log.info("发送消息: " + msg);
//        rabbitTemplate.convertAndSend("queue_fanout01", msg);
//    }
//
//    public void sendFanout(Object msg){
//        log.info("发送消息: " + msg);
//        rabbitTemplate.convertAndSend("fanoutExchange", "", msg);
//    }
//
//    public void sendDirect01(Object msg){
//        log.info("发送apple消息: " + msg);
//        rabbitTemplate.convertAndSend("directExchange", "queue.apple", msg);
//    }
//
//    public void sendDirect02(Object msg){
//        log.info("发送orange消息: " + msg);
//        rabbitTemplate.convertAndSend("directExchange", "", msg);
//    }
//
//    public void sendTopic03(Object msg){
//        log.info("发送Topic消息(被两个queue接收): " + msg);
//        rabbitTemplate.convertAndSend("topicExchange", "hah.queue.hah", msg);
//    }
//
//    public void sendTopic04(Object msg){
//        log.info("发送Topic消息(被queue_topic01接受): " + msg);
//        rabbitTemplate.convertAndSend("topicExchange", "ha.hah.queue.hah", msg);
//    }
//
//    public void sendHeaders05(String msg){
//        log.info("发送信息(被两个queue接收): " + msg);
//        MessageProperties messageProperties = new MessageProperties();
//        messageProperties.setHeader("color", "red");
//        messageProperties.setHeader("speed", "fast");
//        Message message = new Message(msg.getBytes(), messageProperties);
//        rabbitTemplate.convertAndSend("headersExchange", "", message);
//    }
//
//    public void sendHeaders06(String msg){
//        log.info("发送信息(被queue_header01接收): " + msg);
//        MessageProperties messageProperties = new MessageProperties();
//        messageProperties.setHeader("color", "red");
//        messageProperties.setHeader("speed", "haha");
//        Message message = new Message(msg.getBytes(), messageProperties);
//        rabbitTemplate.convertAndSend("headersExchange", "", message);
//    }

    public void sendSeckillMessage(String msg){
        log.info("发送消息: " + msg);
        rabbitTemplate.convertAndSend("seckillExchange", "seckill.hhh", msg);
    }





}
