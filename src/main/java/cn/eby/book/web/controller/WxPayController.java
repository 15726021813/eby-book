package cn.eby.book.web.controller;


import cn.eby.book.common.Result;
import cn.eby.book.entity.PayOrder;
import cn.eby.book.entity.TbReader;
import cn.eby.book.fuction.hcicrf.HCICRFUtil;
import cn.eby.book.service.Impl.WxPayService;
import cn.eby.book.service.PayOrderService;
import cn.eby.book.service.TbReaderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * @Auther: 徐长乐
 * @Date: 2020/09/15/17:01
 * @Description:
 */
@RestController
@RequestMapping("/wxPay")
public class WxPayController {

    @Autowired
    private WxPayService wxPayService;

    @Autowired
    private PayOrderService payOrderService;

    @Autowired
    private TbReaderService tbReaderService;


    @GetMapping("/unifiedOrder")
    public Result unifiedOrder(HttpServletRequest request) {
        try {
            // 1、创建订单
            PayOrder wxPayOrder = payOrderService.createWXPayOrder();
            // 2、开始微信支付统一下单
            Result result = wxPayService.unifiedOrder(request,wxPayOrder.getOutTradeNo(), wxPayOrder.getPayAmount(),"办证押金");
            return result;//系统通用的返回结果集，见文章末尾
        } catch (Exception e) {
            return  Result.error("运行异常");
        }
    }
    @RequestMapping(value = "/notify")
    public String payNotify(HttpServletRequest request) {
        InputStream is = null;
        String xmlBack = "<xml><return_code><![CDATA[FAIL]]></return_code><return_msg><![CDATA[报文为空]]></return_msg></xml> ";
        try {
            is = request.getInputStream();
            // 将InputStream转换成String
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            xmlBack = wxPayService.notify(sb.toString());
        } catch (Exception e) {
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return xmlBack;
    }



    @GetMapping("/orderQuery")
    public Result refund( @RequestParam String orderNo){
        return wxPayService.orderQuery(orderNo);
    }
}
