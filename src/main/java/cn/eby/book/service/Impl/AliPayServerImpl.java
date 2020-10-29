package cn.eby.book.service.Impl;

import cn.eby.book.common.Result;
import cn.eby.book.common.exception.CustomException;
import cn.eby.book.config.AliPayConfig;
import cn.eby.book.entity.PayOrder;
import cn.eby.book.service.AliPayServer;
import cn.eby.book.service.PayOrderService;
import cn.eby.book.service.TbReaderService;
import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.*;
import com.alipay.api.response.AlipayTradePrecreateResponse;
import com.alipay.api.response.AlipayTradeQueryResponse;
import com.alipay.api.response.AlipayTradeRefundResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
/**
 * @author 朱胜伟
 */
@Service
public class AliPayServerImpl implements AliPayServer {
    @Autowired
    AliPayConfig aliPayConfig;
    AlipayClient alipayClient;

    @Autowired
    private PayOrderService payOrderService;

    @Autowired
    private TbReaderService tbReaderService;

    public AliPayServerImpl(){
    }

    /**
     * 创建支付宝订单
     * @param out_trade_no 商户网站订单系统中唯一订单号，必填
     * @param total_amount  付款金额，必填
     * @param subject 订单名称，必填
     * @param body 商品描述，可空
     * @return
     */
    public Result createOrder(String out_trade_no, String total_amount, String subject, String body) throws Exception{
        if(alipayClient == null){
            //获得初始化的AlipayClient
            alipayClient = new DefaultAlipayClient(aliPayConfig.getGatewayUrl(), aliPayConfig.getAppId(),aliPayConfig.getPrivateKey(), "json", aliPayConfig.getCharset(), aliPayConfig.getPublicKey(), aliPayConfig.getSignType());
        }
        //设置请求参数
        AlipayTradePrecreateRequest request   =   new   AlipayTradePrecreateRequest (); //创建API对应的request类
        request . setBizContent ( "{"   +
                "    \"out_trade_no\":\""+out_trade_no+"\","   + //商户订单号
                "    \"total_amount\":\""+total_amount+"\","   +
                "    \"subject\":\"办证押金\","   +
                "    \"store_id\":\"NJ_001\","   +
                "    \"timeout_express\":\"1m\"}" ); //订单允许的最晚付款时间
        AlipayTradePrecreateResponse response   =   alipayClient.execute( request );

        String result = response.getQrCode();
        Map<String,String> returnMap = new HashMap<>();
        returnMap.put("code_url",response.getQrCode());
        returnMap.put("out_trade_no", response.getOutTradeNo());
        return Result.success(returnMap);
    }

    /**
     * 通过订单号查询状态
     * @param out_trade_no 商户订单号，商户网站订单系统中唯一订单号
     * @throws Exception
     * @return
     *         "code": 订单的状态   40004 交易不存在(用户没扫码前），10000 交易创建（代表用户扫码，通过trade_status判断交易的状态）
     *         "out_trade_no"： 订单号
     *         "trade_status"： 当前订单的状态  WAIT_BUYER_PAY  用户扫码，等待支付。 TRADE_SUCCESS 支付成功
     */
    public Result findOrder(String out_trade_no) {
        if(alipayClient == null){
            //获得初始化的AlipayClient
            alipayClient = new DefaultAlipayClient(aliPayConfig.getGatewayUrl(), aliPayConfig.getAppId(),aliPayConfig.getPrivateKey(), "json", aliPayConfig.getCharset(), aliPayConfig.getPublicKey(), aliPayConfig.getSignType());
        }
        //设置请求参数
        AlipayTradeQueryRequest   request   =   new   AlipayTradeQueryRequest (); //创建API对应的request类
        request . setBizContent ( "{"   +
                "    \"out_trade_no\":\""+out_trade_no+"\"}");  //设置业务参数
        AlipayTradeQueryResponse response   = null; //通过alipayClient调用API，获得对应的response类
        try {
            response = alipayClient . execute ( request );
        } catch (AlipayApiException e) {
            throw new CustomException("系统错误");
        }
        if ("TRADE_SUCCESS".equals(response.getTradeStatus())) {
            PayOrder order = payOrderService.findById(Long.valueOf(out_trade_no));
            order.setPayStatus((byte)1);
            payOrderService.updateById(order);

            return Result.success("SUCCESS");
        }
        return Result.error("订单未支付");
    }

    /**
     * 订单退款
     * @param refund_amount 需要退款的金额，该金额不能大于订单金额，必填
     * @return
     *          code  //状态码 10000执行成功 40004 执行失败（如果退款金额大于订单金额）
     *          refund_fee  退款金额
     *          gmt_refund_pay 退款时间
     *          fund_change 是否退款(如两次退款的 out_request_no 一至，则支付宝认为重复退款
                trade_no  退款单号
     * @throws Exception
     */
    public Result refund(Integer rdId, String out_trade_no,String refund_amount) throws Exception{
        if(alipayClient == null){
            //获得初始化的AlipayClient
            alipayClient = new DefaultAlipayClient(aliPayConfig.getGatewayUrl(), aliPayConfig.getAppId(),aliPayConfig.getPrivateKey(), "json", aliPayConfig.getCharset(), aliPayConfig.getPublicKey(), aliPayConfig.getSignType());
        }
        //设置请求参数
        AlipayTradeRefundRequest request = new AlipayTradeRefundRequest();//创建API对应的request类
        request.setBizContent("{" +
                "\"out_trade_no\":\""+out_trade_no+"\"," +
                "\"refund_amount\":\""+refund_amount+"\"}"); //设置业务参数
        AlipayTradeRefundResponse response = alipayClient.execute(request);//通过alipayClient调用API，获得对应的response类
        String fundChange = response.getFundChange();
        if ("Y".equals(fundChange)){
            PayOrder order = payOrderService.findById(Long.valueOf(out_trade_no));
            order.setPayStatus((byte)2);
            payOrderService.updateById(order);
            tbReaderService.updateState(rdId);
            return  Result.success("退款申请成功");
        }
        return Result.error("退款申请失败");
    }

    /**
     * 退款查询
     * @param out_trade_no  订单单号
     * @param out_request_no 申请退款号（退款时返回）
     * @return
     *                       "code" 返回码  10001 代表退款成功（不一定成功）    40004代表订单单号不存在
     *          "msg"  返回消息
     */
    public Map<String,String> findRefund(String out_trade_no,String out_request_no) throws Exception{
//设置请求参数
        if(alipayClient == null){
            //获得初始化的AlipayClient
            alipayClient = new DefaultAlipayClient(aliPayConfig.getGatewayUrl(), aliPayConfig.getAppId(),aliPayConfig.getPrivateKey(), "json", aliPayConfig.getCharset(), aliPayConfig.getPublicKey(), aliPayConfig.getSignType());
        }
        AlipayTradeFastpayRefundQueryRequest alipayRequest = new AlipayTradeFastpayRefundQueryRequest();
        alipayRequest.setBizContent("{\"out_trade_no\":\""+ out_trade_no +"\","
                +"\"out_request_no\":\""+ out_request_no +"\"}");

        //请求
        String result = alipayClient.execute(alipayRequest).getBody();
        JSONObject res = JSONObject.parseObject(JSONObject.parseObject(result).getString("alipay_trade_fastpay_refund_query_response"));
        Map<String,String> resm = new HashMap<>();
        resm.put("code",res.getString("code"));
        resm.put("msg",res.getString("msg"));
        return resm;
    }
}
