package com.dong.seckill.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.dong.seckill.pojo.SeckillOrders;
import com.dong.seckill.pojo.User;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author dong
 * @since 2024-07-04
 */
public interface ISeckillOrdersService extends IService<SeckillOrders> {

    /**
     * 功能描述: 获取秒杀结果
     * @return OrderId: 成功; -1: 失败; 0: 排队中
     */
    Long getResult(User user, Long goodsId);

}
