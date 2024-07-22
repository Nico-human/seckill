package com.dong.seckill.exception;

import com.dong.seckill.vo.RespBean;
import com.dong.seckill.vo.RespBeanEnum;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @Description: 全局异常处理类
 * @Author: Dong
 * @Date: 2024/7/1
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({GlobalException.class, BindException.class})
    public RespBean<String> exceptionHandler(Exception e) {
        if (e instanceof GlobalException ex){
            RespBeanEnum respBeanEnum = ex.getRespBeanEnum();
            return RespBean.error(respBeanEnum);
        }else if(e instanceof BindException ex){
            RespBean<String> respBean = RespBean.error(RespBeanEnum.BIND_ERROR);
            respBean.setMessage("参数校验异常: "+ ex.getBindingResult().getAllErrors().get(0).getDefaultMessage());
            return respBean;
        }
        else{
            return null;
        }
//        return RespBean.error(RespBeanEnum.ERROR);
    }


}
