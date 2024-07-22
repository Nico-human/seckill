package com.dong.seckill.validator;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @Description: 自定义限流注解
 * @Author: Dong
 * @Date: 2024/7/20
 */
@Target({METHOD})
@Retention(RUNTIME)
@Documented
public @interface Limit {

    /**
     * 不同的接口, 不同的流量控制
     * @return 资源的key, 唯一
     */
    String key() default "";

    /**
     * @return 最多的访问限制次数
     */
    double permitsPerSecond();

    /**
     * @return 获取令牌的最大等待时间
     */
    long timeout();

    /**
     * @return 获取令牌最大等待时间的单位, 默认: 毫秒
     */
    TimeUnit timeUnit() default TimeUnit.MILLISECONDS;

    /**
     * @return 无法获取令牌时的提示语
     */
    String msg() default "系统繁忙, 请稍后再试.";

}
