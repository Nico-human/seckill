package com.dong.seckill.vo;

import com.dong.seckill.pojo.Orders;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Description: 订单详情返回对象
 * @Author: Dong
 * @Date: 2024/7/17
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDetailVo {

    private Orders orders;

    private GoodsVo goodsVo;
}
