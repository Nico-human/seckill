package com.dong.seckill.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.dong.seckill.pojo.Orders;
import com.dong.seckill.pojo.User;
import com.dong.seckill.vo.GoodsVo;
import com.dong.seckill.vo.OrderDetailVo;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author dong
 * @since 2024-07-04
 */
public interface IOrdersService extends IService<Orders> {

    Orders seckill(User user, GoodsVo goods);

    OrderDetailVo detail(Long orderId);

    String createPath(User user, Long goodsId);

    boolean checkPath(User user, long goodsId, String path);

}
