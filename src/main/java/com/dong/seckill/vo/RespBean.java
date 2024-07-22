
package com.dong.seckill.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 公共返回对象
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RespBean<T> {

    private long code;
    private String message;
    private T obj;

    //成功返回结果
    public static <T> RespBean<T> success() {
        return new RespBean<>(RespBeanEnum.SUCCESS.getCode(), RespBeanEnum.SUCCESS.getMessage(), null);
    }

    public static <T> RespBean<T> success(T obj) {
        return new RespBean<>(RespBeanEnum.SUCCESS.getCode(), RespBeanEnum.SUCCESS.getMessage(), obj);
    }

    //失败返回结果, 成功的状态码基本都是200, 失败却各有不同
    public static <T> RespBean<T> error(RespBeanEnum respBeanEnum){
        return new RespBean<>(respBeanEnum.getCode(), respBeanEnum.getMessage(), null);
    }

    public static <T> RespBean<T> error(RespBeanEnum respBeanEnum, T obj){
        return new RespBean<>(respBeanEnum.getCode(), respBeanEnum.getMessage(), obj);
    }



}
