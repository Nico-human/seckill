package com.dong.seckill.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Description: 秒杀信息
 * @Author: Dong
 * @Date: 2024/7/19
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SeckillMessage {

    private User user;
    private Long goodsId;
}
