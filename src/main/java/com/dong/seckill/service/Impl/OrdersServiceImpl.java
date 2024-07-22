package com.dong.seckill.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dong.seckill.exception.GlobalException;
import com.dong.seckill.mapper.OrdersMapper;
import com.dong.seckill.pojo.Orders;
import com.dong.seckill.pojo.SeckillGoods;
import com.dong.seckill.pojo.SeckillOrders;
import com.dong.seckill.pojo.User;
import com.dong.seckill.service.IGoodsService;
import com.dong.seckill.service.IOrdersService;
import com.dong.seckill.service.ISeckillGoodsService;
import com.dong.seckill.service.ISeckillOrdersService;
import com.dong.seckill.utils.MD5Util;
import com.dong.seckill.utils.UUIDUtil;
import com.dong.seckill.vo.GoodsVo;
import com.dong.seckill.vo.OrderDetailVo;
import com.dong.seckill.vo.RespBeanEnum;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author dong
 * @since 2024-07-04
 */
@Service
public class OrdersServiceImpl extends ServiceImpl<OrdersMapper, Orders> implements IOrdersService {

    @Autowired
    private ISeckillGoodsService seckillGoodsService;
    @Autowired
    private OrdersMapper ordersMapper;
    @Autowired
    private ISeckillOrdersService seckillOrdersService;
    @Autowired
    private IGoodsService goodsService;
    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    @Transactional
    public Orders seckill(User user, GoodsVo goods) {

        //秒杀商品表减库存
        SeckillGoods seckillGoods = seckillGoodsService.getOne(new QueryWrapper<SeckillGoods>().eq(
                "goods_id", goods.getId()));
        seckillGoods.setStockCount(seckillGoods.getStockCount()-1);
        boolean seckillResult = seckillGoodsService.update(new UpdateWrapper<SeckillGoods>().setSql("stock_count = " + "stock_count-1").eq(
                "goods_id", goods.getId()).gt("stock_count", 0));
        if (seckillGoods.getStockCount() < 1){
            redisTemplate.opsForValue().set("isStockEmpty:" + goods.getId(), 0);
            return null;
        }

        //生成订单
        Orders order = new Orders();
        order.setUserId(user.getId());
        order.setGoodsId(goods.getId());
        order.setDeliveryAddrId(0L);
        order.setGoodsName(goods.getGoodsName());
        order.setGoodsCount(1);
        order.setGoodsPrice(goods.getSeckillPrice());
        order.setOrderChannel(1);
        order.setStatus(0);
        order.setCreateDate(new Date());
        ordersMapper.insert(order);

        //生成秒杀订单
        SeckillOrders seckillOrders = new SeckillOrders();
        seckillOrders.setUserId(user.getId());
        seckillOrders.setOrderId(order.getId());
        seckillOrders.setGoodsId(goods.getId());
        seckillOrdersService.save(seckillOrders);
        redisTemplate.opsForValue().set("order:"+user.getId()+":"+goods.getId(), seckillOrders);
        return order;
    }

    @Override
    public OrderDetailVo detail(Long orderId) {
        if (orderId == null){
            throw new GlobalException(RespBeanEnum.ORDER_NOT_EXIST);
        }
        Orders orders = ordersMapper.selectById(orderId);
        GoodsVo goodsVo = goodsService.findGoodsVoByGoodsId(orders.getGoodsId());
        OrderDetailVo orderDetailVo = new OrderDetailVo();
        orderDetailVo.setOrders(orders);
        orderDetailVo.setGoodsVo(goodsVo);
        return orderDetailVo;
    }

    @Override
    public String createPath(User user, Long goodsId) {
        String str = MD5Util.md5(UUIDUtil.getUUID() + "123456");
        redisTemplate.opsForValue().set("seckillPath:"+user.getId()+":"+goodsId, str, 60, TimeUnit.SECONDS);
        return str;
    }

    @Override
    public boolean checkPath(User user, long goodsId, String path) {
        if (user == null || goodsId < 0 || StringUtils.isEmpty(path)) {
            return false;
        }
        String redisPath = (String) redisTemplate.opsForValue().get("seckillPath:" + user.getId() + ":" + goodsId);
        return path.equals(redisPath);
    }
}
