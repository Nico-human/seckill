//package com.dong.seckill.config;
//
//import org.springframework.amqp.core.*;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
///**
// * @Description: RabbitMQ配置类
// * @Author: Dong
// * @Date: 2024/7/18
// */
//@Configuration
//public class RabbitMQConfig {
//
//    private static final String QUEUE_01 = "queue_fanout01";
//    private static final String QUEUE_02 = "queue_fanout02";
//    private static final String EXCHANGE = "fanoutExchange";
//
//    @Bean
//    public Queue queue(){
//        return new Queue("queue", true);
//    }
//
//    @Bean
//    public Queue queue01(){
//        return new Queue(QUEUE_01);
//    }
//
//    @Bean
//    public Queue queue02(){
//        return new Queue(QUEUE_02);
//    }
//
//
//    /**
//     * Fanout模式: 将消息广播到所有与其绑定的队列，无论消息的路由键是什么
//     */
//    @Bean
//    public FanoutExchange fanoutExchange(){
//        return new FanoutExchange(EXCHANGE);
//    }
//
//    @Bean
//    public Binding binding01(){
//        return BindingBuilder.bind(queue01()).to(fanoutExchange());
//    }
//
//    @Bean
//    public Binding binding02(){
//        return BindingBuilder.bind(queue02()).to(fanoutExchange());
//    }
//
//    @Bean
//    public DirectExchange directExchange(){
//
//    }
//
//}
