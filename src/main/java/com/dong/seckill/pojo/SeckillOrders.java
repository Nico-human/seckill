package com.dong.seckill.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author dong
 * @since 2024-07-04
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("t_seckill_orders")
public class SeckillOrders implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 秒杀订单(主键)ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 订单ID
     */
    private Long orderId;

    /**
     * 商品ID
     */
    private Long goodsId;


}
