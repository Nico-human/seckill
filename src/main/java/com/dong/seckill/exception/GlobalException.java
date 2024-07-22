package com.dong.seckill.exception;

import com.dong.seckill.vo.RespBeanEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Description: 全局异常
 * @Author: Dong
 * @Date: 2024/7/1
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GlobalException extends RuntimeException {

    private RespBeanEnum respBeanEnum;

}
