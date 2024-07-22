package com.dong.seckill.controller;


import com.dong.seckill.pojo.User;
import com.dong.seckill.service.IOrdersService;
import com.dong.seckill.vo.OrderDetailVo;
import com.dong.seckill.vo.RespBean;
import com.dong.seckill.vo.RespBeanEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author dong
 * @since 2024-07-04
 */
@Controller
@RequestMapping("/order")
public class OrdersController {

    @Autowired
    private IOrdersService ordersService;

    @RequestMapping("/detail")
    @ResponseBody
    public RespBean<OrderDetailVo> detail(User user, Long orderId) {

        if(user == null){
            return RespBean.error(RespBeanEnum.SESSION_ERROR);
        }

        OrderDetailVo detail = ordersService.detail(orderId);

        return RespBean.success(detail);

    }

}
