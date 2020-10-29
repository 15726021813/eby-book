package cn.eby.book.service.Impl;

import cn.hutool.core.util.IdUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.eby.book.mapper.PayOrderMapper;
import cn.eby.book.entity.PayOrder;
import cn.eby.book.service.PayOrderService;

/**
 * @Auther: 徐长乐
 * @Date: 2020/09/22/14:42
 * @Description: 
 */
@Service
public class PayOrderServiceImpl extends ServiceImpl<PayOrderMapper, PayOrder> implements PayOrderService{

    @Autowired
    private PayOrderMapper payOrderMapper;

    @Value("${pay.amount}")
    private BigDecimal amount;

    @Override
    public PayOrder createWXPayOrder() {

        PayOrder payOrder = new PayOrder();
        payOrder.setOutTradeNo(IdUtil.getSnowflake(1,1).nextId());
        payOrder.setCreateTime(new Date());
        payOrder.setPayAmount(amount);
        payOrder.setPayType((byte)1);
        payOrder.setPayStatus((byte)0);
        payOrderMapper.insert(payOrder);
        return payOrder;
    }

    public PayOrder createALiPayOrder() {
        PayOrder payOrder = new PayOrder();
        payOrder.setOutTradeNo(IdUtil.getSnowflake(1,1).nextId());
        payOrder.setCreateTime(new Date());
        payOrder.setPayAmount(amount);
        payOrder.setPayType((byte)2);
        payOrder.setPayStatus((byte)0);
        payOrderMapper.insert(payOrder);
        return payOrder;
    }
    @Override
    public PayOrder findById(Long id) {
        PayOrder payOrder = payOrderMapper.selectById(id);
        return payOrder;
    }
}
