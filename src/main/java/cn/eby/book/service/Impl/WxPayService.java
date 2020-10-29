package cn.eby.book.service.Impl;

import cn.eby.book.common.Result;
import cn.eby.book.common.utils.IpUtils;
import cn.eby.book.config.WXPayConfigImpl;
import cn.eby.book.entity.PayOrder;
import cn.eby.book.entity.TbReader;
import cn.eby.book.service.PayOrderService;
import cn.eby.book.service.TbReaderService;
import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;

import com.baomidou.mybatisplus.extension.api.R;
import com.github.wxpay.sdk.WXPay;
import com.github.wxpay.sdk.WXPayUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @Auther: 徐长乐
 * @Date: 2020/09/15/16:19
 * @Description:
 */
@Service
@Transactional
public class WxPayService {

    @Autowired
    private WXPayConfigImpl wxPayAppConfig;

    @Autowired
    private PayOrderService payOrderService;

    @Autowired
    private TbReaderService tbReaderService;

    public Result unifiedOrder(HttpServletRequest request, Long  orderNo, BigDecimal amount, String body) {
        Map<String, String> returnMap = new HashMap<>();
        Map<String, String> responseMap = new HashMap<>();
        Map<String, String> requestMap = new HashMap<>();
        try {
            WXPay wxpay = new WXPay(wxPayAppConfig);
            requestMap.put("body", body);                                     // 商品描述
            requestMap.put("out_trade_no", String.valueOf(orderNo));                          // 商户订单号
            requestMap.put("total_fee",String.valueOf(amount.multiply(BigDecimal.valueOf(100)).intValue()));   // 总金额
            requestMap.put("spbill_create_ip", IpUtils.getRemortIP(request)); // 终端IP
            requestMap.put("trade_type", "NATIVE");
            requestMap.put("notify_url", wxPayAppConfig.getPayNotifyUrl());   // 接收微信支付异步通知回调地址
            DateTime offset = DateUtil.offset(new Date(), DateField.SECOND, 61);
            String expireTime = offset.toString("yyyyMMddHHmmss");
            requestMap.put("time_expire",expireTime);
            Map<String, String> resultMap = wxpay.unifiedOrder(requestMap);
            //获取返回码
            String returnCode = resultMap.get("return_code");
            String returnMsg = resultMap.get("return_msg");
            //若返回码为SUCCESS，则会返回一个result_code,再对该result_code进行判断
            if ("SUCCESS".equals(returnCode)) {
                String resultCode = resultMap.get("result_code");
                String errCodeDes = resultMap.get("err_code_des");
                if ("SUCCESS".equals(resultCode)) {
                    responseMap = resultMap;
                }
            }
            if (responseMap == null || responseMap.isEmpty()) {
                System.out.println(resultMap);
                return Result.error(returnMsg);
            }
            // 3、签名生成算法
            returnMap.put("code_url",responseMap.get("code_url"));
            returnMap.put("out_trade_no", String.valueOf(orderNo));
            return Result.success(returnMap);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("微信支付统一下单失败");
        }
    }

    public String notify(String notifyStr) {
        String xmlBack = "<xml><return_code><![CDATA[FAIL]]></return_code><return_msg><![CDATA[报文为空]]></return_msg></xml> ";
        try {
            // 转换成map
            Map<String, String> resultMap = WXPayUtil.xmlToMap(notifyStr);
            WXPay wxpayApp = new WXPay(wxPayAppConfig);
            if (wxpayApp.isPayResultNotifySignatureValid(resultMap)) {
                String returnCode = resultMap.get("return_code");  //状态
                String outTradeNo = resultMap.get("out_trade_no");//商户订单号
                String transactionId = resultMap.get("transaction_id");
                if (returnCode.equals("SUCCESS")) {
                    if (StringUtils.isNotBlank(outTradeNo)) {
                        /**
                         * 注意！！！
                         * 请根据业务流程，修改数据库订单支付状态，和其他数据的相应状态
                         *
                         */
                        PayOrder order = payOrderService.findById(Long.valueOf(outTradeNo));
                        order.setPayStatus((byte)1);
                        payOrderService.updateById(order);
                        xmlBack = "<xml><return_code><![CDATA[SUCCESS]]></return_code><return_msg><![CDATA[OK]]></return_msg></xml>";
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return xmlBack;
    }

    public Result refund(Integer rdId,String outTradeNo , BigDecimal  payAmount){

        Map<String, String> responseMap = new HashMap<>();
        Map<String, String> requestMap = new HashMap<>();
        WXPay wxpay = new WXPay(wxPayAppConfig);
        requestMap.put("out_trade_no", outTradeNo);
        requestMap.put("out_refund_no", IdUtil.simpleUUID());
        requestMap.put("total_fee", String.valueOf(payAmount.multiply(BigDecimal.valueOf(100)).intValue()) );
        requestMap.put("refund_fee",String.valueOf(payAmount.multiply(BigDecimal.valueOf(100)).intValue()));//所需退款金额
        requestMap.put("refund_desc", "读者证退款");
        try {
            responseMap = wxpay.refund(requestMap);
        } catch (Exception e) {
            e.printStackTrace();
        }
        String return_code = responseMap.get("return_code");   //返回状态码
        String return_msg = responseMap.get("return_msg");     //返回信息
        if ("SUCCESS".equals(return_code)) {
            String result_code = responseMap.get("result_code");       //业务结果
            String err_code_des = responseMap.get("err_code_des");     //错误代码描述
            if ("SUCCESS".equals(result_code)) {
                //表示退款申请接受成功，结果通过退款查询接口查询
                //修改用户订单状态为退款申请中或已退款。退款异步通知根据需求，可选
                //
                PayOrder order = payOrderService.findById(Long.valueOf(outTradeNo));
                order.setPayStatus((byte)2);
                payOrderService.updateById(order);
                tbReaderService.updateState(rdId);
                return  Result.success("退款申请成功");
            } else {
                return Result.error(err_code_des);
            }
        } else {
            return Result.error(return_msg);
        }
    }



    public Result orderQuery(String orderNo){

        Map<String, String> responseMap = new HashMap<>();
        Map<String, String> requestMap = new HashMap<>();
        WXPay wxpay = new WXPay(wxPayAppConfig);
        requestMap.put("out_trade_no", orderNo);
        try {
            responseMap = wxpay.orderQuery(requestMap);
        } catch (Exception e) {
            e.printStackTrace();
        }
        String return_code = responseMap.get("return_code");   //返回状态码
        String return_msg = responseMap.get("return_msg");     //返回信息
        if ("SUCCESS".equals(return_code)) {
            String result_code = responseMap.get("result_code");       //业务结果
            String err_code_des = responseMap.get("err_code_des");     //错误代码描述
            if ("SUCCESS".equals(result_code)) {
                if (StringUtils.isEmpty(responseMap.get("trade_state"))){
                    return Result.success("NOTPAY");
                }
                String trade_state = responseMap.get("trade_state");
                if("SUCCESS".equals(trade_state)){
//                    支付成功
                    PayOrder order = payOrderService.findById(Long.valueOf(orderNo));
                    order.setPayStatus((byte)1);
                    payOrderService.updateById(order);
                    return Result.success("SUCCESS");

                }else{
                    return Result.success("NOTPAY");
                }
            } else {
                return Result.error(err_code_des);
            }
        } else {
            return Result.error(return_msg);
        }
    }
}
