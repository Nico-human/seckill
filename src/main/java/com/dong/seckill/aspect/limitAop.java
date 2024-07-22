package com.dong.seckill.aspect;

import com.dong.seckill.exception.GlobalException;
import com.dong.seckill.validator.Limit;
import com.dong.seckill.vo.RespBeanEnum;
import com.google.common.collect.Maps;
import com.google.common.util.concurrent.RateLimiter;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * @Description: 接口限流aop
 * @Author: Dong
 * @Date: 2024/7/20
 */
@Slf4j
@Aspect
@Component
public class limitAop {

    /**
     * 不同的接口, 不同的流量控制
     */
    private final Map<String, RateLimiter> limitMap = Maps.newConcurrentMap();

    @Around("@annotation(com.dong.seckill.validator.Limit)")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        Limit limit = method.getAnnotation(Limit.class); //拿Limit注解
        if (limit != null) {
            String key = limit.key(); // key: 接口url
            RateLimiter rateLimiter = null;
            if(!limitMap.containsKey(key)) {
                //创建令牌桶
                rateLimiter = RateLimiter.create(limit.permitsPerSecond());
                limitMap.put(key, rateLimiter);
                log.info("新建了令牌桶={}, 容量={}", key, limit.permitsPerSecond());
            }
            rateLimiter = limitMap.get(key);
            boolean acquire = rateLimiter.tryAcquire(limit.timeout(), limit.timeUnit());
            if (!acquire) {
                log.info("令牌桶={}, 获取令牌失败", key);
//                this.responseFail(limit.msg());
                throw new GlobalException(RespBeanEnum.REPEAT_ERROR);
            }
        }
        return joinPoint.proceed();
    }

}
