package com.dong.seckill.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dong.seckill.mapper.SeckillOrdersMapper;
import com.dong.seckill.pojo.SeckillOrders;
import com.dong.seckill.pojo.User;
import com.dong.seckill.service.ISeckillOrdersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author dong
 * @since 2024-07-04
 */
@Service
public class SeckillOrdersServiceImpl extends ServiceImpl<SeckillOrdersMapper, SeckillOrders> implements ISeckillOrdersService {

    @Autowired
    private SeckillOrdersMapper seckillOrdersMapper;

    @Autowired
    private RedisTemplate redisTemplate;

    // return: OrderId: 成功; -1: 失败; 0: 排队中
    @Override
    public Long getResult(User user, Long goodsId) {

        SeckillOrders seckillOrders = seckillOrdersMapper.selectOne(new QueryWrapper<SeckillOrders>().eq("user_id",
                user.getId()).eq("goods_id", goodsId));

        if(seckillOrders != null){
            return seckillOrders.getOrderId();
        }else if(redisTemplate.hasKey("isStockEmpty:" + goodsId)){
            return -1L;
        }else{
            return 0L;
        }
    }

}
