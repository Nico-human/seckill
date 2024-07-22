package com.dong.seckill.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.dong.seckill.pojo.Goods;
import com.dong.seckill.vo.GoodsVo;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author dong
 * @since 2024-07-04
 */
public interface IGoodsService extends IService<Goods> {

    List<GoodsVo> findGoodsVo();

    GoodsVo findGoodsVoByGoodsId(Long goodsId);
}
