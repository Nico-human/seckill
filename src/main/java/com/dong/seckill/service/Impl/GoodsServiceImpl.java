package com.dong.seckill.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dong.seckill.mapper.GoodsMapper;
import com.dong.seckill.pojo.Goods;
import com.dong.seckill.service.IGoodsService;
import com.dong.seckill.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
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
public class GoodsServiceImpl extends ServiceImpl<GoodsMapper, Goods> implements IGoodsService {

    @Autowired
    private GoodsMapper goodsMapper;

    @Override
    public List<GoodsVo> findGoodsVo() {
        return goodsMapper.findGoodsVo();
    }

    @Override
    public GoodsVo findGoodsVoByGoodsId(Long goodsId) {
        return goodsMapper.findGoodsVoByGoodsId(goodsId);
    }

}
