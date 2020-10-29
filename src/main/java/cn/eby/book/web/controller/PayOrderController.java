package cn.eby.book.web.controller;

import cn.eby.book.common.Result;
import cn.eby.book.entity.PayOrder;
import cn.eby.book.service.PayOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Auther: 徐长乐
 * @Date: 2020/09/23/8:19
 * @Description:
 */
@RestController
public class PayOrderController {

    @Autowired
    private PayOrderService payOrderService;



    @GetMapping("{orderNo}")
    public Result PayOrder(@PathVariable Long orderNo){
        PayOrder order = payOrderService.findById(orderNo);
        return Result.success(order);
    }
}
