package com.dong.seckill.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dong.seckill.pojo.Goods;
import com.dong.seckill.vo.GoodsVo;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author dong
 * @since 2024-07-04
 */
public interface GoodsMapper extends BaseMapper<Goods> {

    List<GoodsVo> findGoodsVo();

    GoodsVo findGoodsVoByGoodsId(Long goodsId);
}
