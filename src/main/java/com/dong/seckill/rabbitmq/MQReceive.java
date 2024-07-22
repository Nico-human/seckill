package com.dong.seckill.rabbitmq;

import com.dong.seckill.pojo.SeckillMessage;
import com.dong.seckill.pojo.SeckillOrders;
import com.dong.seckill.pojo.User;
import com.dong.seckill.service.IGoodsService;
import com.dong.seckill.service.IOrdersService;
import com.dong.seckill.utils.JsonUtil;
import com.dong.seckill.vo.GoodsVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

/**
 * @Description: 消息消费者
 * @Author: Dong
 * @Date: 2024/7/18
 */
@Service
@Slf4j
public class MQReceive {

//    @RabbitListener(queues = "queue")
//    public void receive(Object msg) {
//        log.info("接受消息: " + msg);
//    }
//
//    @RabbitListener(queues = "queue_fanout01")
//    public void receive01(Object msg) {
//        log.info("QUEUE01接受消息: " + msg);
//    }
//
//    @RabbitListener(queues = "queue_fanout02")
//    public void receive02(Object msg) {
//        log.info("QUEUE02接受消息: " + msg);
//    }
//
//    @RabbitListener(queues = "queue_direct01") // queue.apple
//    public void receive03(Object msg) {
//        log.info("QueueDirect01接受消息: " + msg);
//    }
//
//    @RabbitListener(queues = "queue_direct02") // queue.orange
//    public void receive04(Object msg) {
//        log.info("QueueDirect02接受消息: " + msg);
//    }
//
//    @RabbitListener(queues = "queue_topic01")
//    public void receive05(Object msg) {
//        log.info("queue_topic01接受消息: " + msg);
//    }
//
//    @RabbitListener(queues = "queue_topic02")
//    public void receive06(Object msg) {
//        log.info("queue_topic02接受消息: " + msg);
//    }
//
//    @RabbitListener(queues = "queue_header01")
//    public void receive07(Message msg){
//        log.info("QUEUE01接收Message对象: " + msg);
//        log.info("QUEUE01接收消息: " + new String(msg.getBody()));
//    }
//
//    @RabbitListener(queues = "queue_header02")
//    public void receive08(Message msg){
//        log.info("QUEUE02接收Message对象: " + msg);
//        log.info("QUEUE02接收消息: " + new String(msg.getBody()));
//    }

    @Autowired
    private IGoodsService goodsService;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private IOrdersService ordersService;

    @RabbitListener(queues = "seckillQueue")
    public void receive(String msg) {
        log.info("接收的消息: " + msg);
        SeckillMessage seckillMessage = JsonUtil.jsonStr2Object(msg, SeckillMessage.class);
        User user = seckillMessage.getUser();
        Long goodsId = seckillMessage.getGoodsId();
        GoodsVo goodsVo = goodsService.findGoodsVoByGoodsId(goodsId);

        if (goodsVo.getStockCount() < 1){
            return;
        }

        // 判断是否重复抢购
        SeckillOrders seckillOrders = (SeckillOrders) redisTemplate.opsForValue().get("order:" + user.getId() + ":" + goodsId);
        if (seckillOrders != null){
            return;
        }

        //下单操作
        ordersService.seckill(user, goodsVo);

    }

}
