package cn.eby.book.service;

import cn.eby.book.entity.PayOrder;
import com.baomidou.mybatisplus.extension.service.IService;
    
/**
 * @Auther: 徐长乐
 * @Date: 2020/09/22/14:42
 * @Description: 
 */
public interface PayOrderService extends IService<PayOrder>{



    PayOrder createWXPayOrder();


    PayOrder createALiPayOrder();

    PayOrder findById(Long id);


}
