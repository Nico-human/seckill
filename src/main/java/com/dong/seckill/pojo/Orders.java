package com.dong.seckill.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

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
@TableName("t_orders")
public class Orders implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 订单ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 商品ID
     */
    private Long goodsId;

    /**
     * 收货地址ID
     */
    private Long deliveryAddrId;

    /**
     * 冗余过来的商品名称
     */
    private String goodsName;

    /**
     * 商品数量
     */
    private Integer goodsCount;

    /**
     * 商品单价
     */
    private BigDecimal goodsPrice;

    /**
     * 下单渠道: 1pc, 2android, 3ios
     */
    private Integer orderChannel;

    /**
     * 订单状态: 0新建未支付, 1已支付, 2已发货, 3已收货, 4已退款, 5已完成
     */
    private Integer status;

    /**
     * 创建日期
     */
    private Date createDate;

    /**
     * 支付时间
     */
    private Date payDate;


}
