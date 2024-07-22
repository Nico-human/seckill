package com.dong.seckill.controller;

import com.dong.seckill.pojo.SeckillMessage;
import com.dong.seckill.pojo.SeckillOrders;
import com.dong.seckill.pojo.User;
import com.dong.seckill.rabbitmq.MQSender;
import com.dong.seckill.service.IGoodsService;
import com.dong.seckill.service.IOrdersService;
import com.dong.seckill.service.ISeckillOrdersService;
import com.dong.seckill.utils.JsonUtil;
import com.dong.seckill.validator.Limit;
import com.dong.seckill.vo.GoodsVo;
import com.dong.seckill.vo.RespBean;
import com.dong.seckill.vo.RespBeanEnum;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description: 秒杀
 * @Author: Dong
 * @Date: 2024/7/5
 */
@Controller
@RequestMapping("/secKill")
public class SeckillController implements InitializingBean {

    private Map<Long, Boolean> EmptyStockMap = new HashMap<>();
    @Autowired
    private IGoodsService goodsService;
    @Autowired
    private ISeckillOrdersService seckillOrdersService;
    @Autowired
    private IOrdersService ordersService;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private MQSender mqSender;
    @Autowired
    private RedisScript<Long> redisScript;


//    /**
//     * 功能描述: 秒杀
//     * windows优化前QPS: 974
//     *        缓存后QPS: 2427
//     * Linux优化前QPS: 1205
//     */
//    @RequestMapping("/doSecKill2")
//    public String doSecKill2(Model model, User user, long goodsId) {
//        if (user == null) return "login";
//
//        model.addAttribute("user", user);
//        GoodsVo goods = goodsService.findGoodsVoByGoodsId(goodsId);
//
//        //判断库存
//        if (goods.getStockCount() < 1){
//            model.addAttribute("errmsg", RespBeanEnum.EMPTY_STOCK.getMessage());
//            return "secKillFail";
//        }
//
//        //判断是否重复抢购
//        SeckillOrders seckillOrders = seckillOrdersService.getOne(new QueryWrapper<SeckillOrders>().
//                eq("user_id", user.getId()).eq("goods_id", goodsId));
//        if (seckillOrders != null){
//            model.addAttribute("errmsg", RespBeanEnum.REPEAT_ERROR.getMessage());
//            return "secKillFail";
//        }
//
//        //进行秒杀
//        Orders order = ordersService.seckill(user, goods);
//        model.addAttribute("order", order);
//        model.addAttribute("goods", goods);
//        return "orderDetail";
//    }

    @PostMapping("/{path}/doSecKill")
    @ResponseBody
    @Limit(key = "seckill", permitsPerSecond = 1, timeout = 500, msg = "系统繁忙, 请稍后再试!")
    public RespBean<Integer> doSecKill(@PathVariable String path, User user, long goodsId) {

        if (user == null) {
            return RespBean.error(RespBeanEnum.SESSION_ERROR);
        }

        ValueOperations valueOperations = redisTemplate.opsForValue();
        if(!ordersService.checkPath(user, goodsId, path)){
            return RespBean.error(RespBeanEnum.REQUEST_ILLEGAL);
        }

        // 判断是否重复抢购
        SeckillOrders seckillOrders = (SeckillOrders) valueOperations.get("order:" + user.getId() + ":" + goodsId);
        if (seckillOrders != null){
            return RespBean.error(RespBeanEnum.REPURCHASE_ERROR);
        }

        //内存标记, 减少Redis的访问
        if (EmptyStockMap.get(goodsId)){
            return RespBean.error(RespBeanEnum.EMPTY_STOCK);
        }

        //预减库存
//        Long stock = valueOperations.decrement("seckillGoods:" + goodsId);
        //TODO: 同一个非法用户多次请求的场景, 会出现卖不完 (112行和114行存在问题)
        Long stock = (Long) redisTemplate.execute(redisScript, Collections.singletonList("seckillGoods:" + goodsId), Collections.EMPTY_LIST);
        if (stock < 0){
            EmptyStockMap.put(goodsId, true);
            return RespBean.error(RespBeanEnum.EMPTY_STOCK);
        }

        //进行秒杀
        SeckillMessage seckillMessage = new SeckillMessage(user, goodsId);
        mqSender.sendSeckillMessage(JsonUtil.object2JsonStr(seckillMessage));
        return RespBean.success(0);

        /*
        GoodsVo goods = goodsService.findGoodsVoByGoodsId(goodsId);

        //判断库存
        if (goods.getStockCount() < 1){
            return RespBean.error(RespBeanEnum.EMPTY_STOCK);
        }

        //判断是否重复抢购
//        SeckillOrders seckillOrders = seckillOrdersService.getOne(new QueryWrapper<SeckillOrders>().
//                eq("user_id", user.getId()).eq("goods_id", goodsId));
        SeckillOrders seckillOrders = (SeckillOrders) redisTemplate.opsForValue().get("order:"+user.getId()+":"+goodsId);
        if (seckillOrders != null){
            return RespBean.error(RespBeanEnum.REPEAT_ERROR);
        }

        //进行秒杀
        Orders order = ordersService.seckill(user, goods);
        return RespBean.success(order);
         */

    }

    /**
     * 功能描述: 获取秒杀结果
     * @return OrderId: 成功; -1: 失败; 0: 排队中
     */
    @GetMapping("getResult")
    @ResponseBody
    public RespBean<Long> getResult(User user, Long goodsId) {
        if (user == null) {
            return RespBean.error(RespBeanEnum.SESSION_ERROR);
        }
        Long orderId = seckillOrdersService.getResult(user, goodsId);
        return RespBean.success(orderId);
    }


    /**
     * 获取秒杀地址
     */
    @GetMapping("path")
    @ResponseBody
    public RespBean<String> getPath(User user, Long goodsId) {

        if (user == null) {
            return RespBean.error(RespBeanEnum.SESSION_ERROR);
        }

        String path = ordersService.createPath(user, goodsId);
        return RespBean.success(path);
    }


    @Override
    public void afterPropertiesSet() throws Exception {
        List<GoodsVo> goods = goodsService.findGoodsVo();
        if(CollectionUtils.isEmpty(goods)){
            return;
        }

        goods.forEach(goodsVo -> {
            redisTemplate.opsForValue().set("seckillGoods:" + goodsVo.getId(),
                    goodsVo.getStockCount());
            EmptyStockMap.put(goodsVo.getId(), false);
        });
    }
}
