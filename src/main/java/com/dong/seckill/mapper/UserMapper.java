package com.dong.seckill.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dong.seckill.pojo.User;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author dong
 * @since 2024-06-28
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

}
