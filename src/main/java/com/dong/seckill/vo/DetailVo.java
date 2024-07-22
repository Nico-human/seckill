package com.dong.seckill.vo;

import com.dong.seckill.pojo.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Description: 详情返回对象
 * @Author: Dong
 * @Date: 2024/7/18
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DetailVo {

    private User user;

    private GoodsVo goodsVo;

    private int secKillStatus;

    private int remainSeconds;


}
