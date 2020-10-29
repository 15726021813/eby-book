package cn.eby.book.web.controller;

import cn.eby.book.common.Result;
import cn.eby.book.config.AliPayConfig;
import cn.eby.book.entity.PayOrder;
import cn.eby.book.service.AliPayServer;
import cn.eby.book.service.Impl.AliPayServerImpl;
import cn.eby.book.service.PayOrderService;
import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradeFastpayRefundQueryRequest;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.alipay.api.request.AlipayTradeQueryRequest;
import com.alipay.api.request.AlipayTradeRefundRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
/**
 * @author 朱胜伟
 */
@RestController
@RequestMapping("/alipay")
public class AliPayController  {
    @Autowired
    private AliPayServerImpl aliPayServer;

    @Autowired
    private PayOrderService payOrderService;

    /**
     * 创建支付宝订单
     * @return
     */
    @RequestMapping("/createOrder")
    public Result createOrder(){
        try {
            PayOrder wxPayOrder = payOrderService.createALiPayOrder();
            return aliPayServer.createOrder(String.valueOf(wxPayOrder.getOutTradeNo()),String.valueOf(wxPayOrder.getPayAmount()),"办证押金","办证押金");
        } catch (Exception e) {
            return Result.error("方法运行报错");
        }
    }

    /**
     * 通过订单号查询状态
     * @param orderNo 商户订单号，商户网站订单系统中唯一订单号
     * @throws Exception
     * @return
     *         "code": 订单的状态   40004 交易不存在(用户没扫码前），10000 交易创建（代表用户扫码，通过trade_status判断交易的状态）
     *         "out_trade_no"： 订单号
     *         "trade_status"： 当前订单的状态  WAIT_BUYER_PAY  用户扫码，等待支付。 TRADE_SUCCESS 支付成功
     */
    @RequestMapping("findOrder")
    public Result findOrder(@RequestParam String orderNo)   {
            return aliPayServer.findOrder(orderNo);
    }

    /**
     * 订单退款
     * @param trade_no 商户订单号，商户网站订单系统中唯一订单号
     * @param refund_amount 需要退款的金额，该金额不能大于订单金额，必填
     * @param refund_reason 退款的原因说明
     * @param out_request_no 标识一次退款请求，同一笔交易多次退款需要保证唯一，如需部分退款，则此参数必传
     * @return
     *          code  //状态码 10000执行成功 40004 执行失败（如果退款金额大于订单金额）
     *          refund_fee  退款金额
     *          gmt_refund_pay 退款时间
     *          fund_change 是否退款(如两次退款的 out_request_no 一至，则支付宝认为重复退款
                trade_no  退款单号
     * @throws Exception
     */

    /**
     * 退款查询
     * @param out_trade_no  订单单号
     * @param out_request_no 申请退款号（退款时返回）
     * @return
     *                       "code" 返回码  10001 代表退款成功（不一定成功）    40004代表订单单号不存在
     *          "msg"  返回消息
     */
    public Map<String,String> findRefund(String out_trade_no,String out_request_no) {
        try {
            return aliPayServer.findRefund(out_trade_no, out_request_no);
        } catch (Exception e) {
            return null;
        }
    }
}
