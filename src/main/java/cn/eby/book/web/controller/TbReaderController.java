package cn.eby.book.web.controller;

import cn.eby.book.common.Result;
import cn.eby.book.common.constant.AlipayConcig;
import cn.eby.book.common.core.page.TableDataInfo;
import cn.eby.book.common.exception.CustomException;
import cn.eby.book.entity.PayOrder;
import cn.eby.book.entity.TbReader;
import cn.eby.book.fuction.D1081.D1081Utils;
import cn.eby.book.fuction.D6520.AcardDao;
import cn.eby.book.fuction.hcicrf.HCICRFUtil;
import cn.eby.book.service.Impl.AliPayServerImpl;
import cn.eby.book.service.Impl.WxPayService;
import cn.eby.book.service.PayOrderService;
import cn.eby.book.service.TbBookborrowService;
import cn.eby.book.service.TbReaderService;
import cn.hutool.core.util.IdUtil;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.baomidou.mybatisplus.extension.api.R;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * @Auther: 徐长乐
 * @Date: 2020/09/12/16:06
 * @Description:
 */

@RestController
public class TbReaderController {

    private static byte[] RecvBuf = new byte[300];

    @Value("${D1081.com}")
    private int com;

    @Value("${D1081.port}")
    private int port;

    @Value("${pay.amount}")
    private String  amount;


    @Autowired
    private WxPayService wxPayService;

    @Autowired
    private PayOrderService payOrderService;

    @Autowired
    private TbReaderService readerService;

    @Autowired
    private AliPayServerImpl aliPayServer;

    @Autowired
    private TbBookborrowService tbBookborrowService;


    private static AlipayClient alipayClient = new DefaultAlipayClient(AlipayConcig.ALIPAY_GATEWAY_URL, AlipayConcig.APP_ID, AlipayConcig.RSA2_PRIVATE_KEY, AlipayConcig.FORMAT, AlipayConcig.CHARSET, AlipayConcig.ALIPAY_RSA2_PUBLIC_KEY, AlipayConcig.SIGN_TYPE);


    @GetMapping("reader/{page}/{size}")
    public TableDataInfo list(@PathVariable int page, @PathVariable int size) {

        return readerService.list(page, size);
    }
    @GetMapping("requirePay")
    public Result requirePay(){
        if (StringUtils.isNotBlank(amount)){
            BigDecimal decimal = new BigDecimal(amount);
            if (decimal.intValue()>0){
                return Result.success(true);
            }
            return Result.success(false);
        }
        return Result.error("支付押金不能小于零");
    }



    @GetMapping("InitCard")
    public Result insert() throws UnsupportedEncodingException {

        int i = D1081Utils.CommOpenWithBaud(com, port);
        if (i == 0){
            System.out.println("串口打开失败");
            return Result.error("串口打开失败");
        }
        int mac = D1081Utils.AutoTestMac();
        if(mac >= 0) {
            System.out.println("获取Mac地址成功，地址为：" + mac);
        }
        else {
            System.out.println("获取Mac地址失败");
        }
        try {
            D1081Utils.SensorQuery();

            //F   发卡到写卡
            RecvBuf[0] = 0x46;
            //C
            RecvBuf[1] = 0x43;
            //7
            RecvBuf[2] = 0x37;
            int sendFC6 = D1081Utils.SendCmd(RecvBuf, 3);
            if(sendFC6 != 0)
            {
                System.out.println("发卡到写卡位置失败失败");
                throw new CustomException("发卡到写卡位置失败失败,请联系管理员");
            }

        } finally {
            int close = D1081Utils.CommClose();
            if(close != 0) {
                System.out.println("串口关闭失败");
                return Result.error("串口关闭失败");
            }
        }
        int icdev = HCICRFUtil.InitReader((short) 100, 9600);
        TbReader tbReader = HCICRFUtil.IDcard(icdev);
        HCICRFUtil.ExitReader(icdev);
        if (readerService.findByIdCard(tbReader.getIdCard())!=null){
            return Result.error("该身份证已注册");
        }
        return Result.success(tbReader);
    }

    @GetMapping("readCard")
    public Result readCard() throws UnsupportedEncodingException {
        int icdev = HCICRFUtil.InitReader((short) 100, 9600);
        String tbReaderId = HCICRFUtil.GetCardNO(icdev);
        HCICRFUtil.ExitReader(icdev);
        return Result.success("读卡成功",tbReaderId);
    }

    @GetMapping("borrowCard")
    public Result borrowCard() throws UnsupportedEncodingException {
        boolean idcard = false;
        String tbReaderId = null;
        try {
        int icdev = HCICRFUtil.InitReader((short) 100, 9600);
        tbReaderId = HCICRFUtil.GetCardNO(icdev);
        HCICRFUtil.ExitReader(icdev);
        }catch (CustomException e){
            idcard = true;
        }
        if (idcard) {
            int icdev = HCICRFUtil.InitReader((short) 100, 9600);
            String iDcardNo = HCICRFUtil.IDcardNo(icdev);
            HCICRFUtil.ExitReader(icdev);
            tbReaderId = readerService.findIdByCardNo(iDcardNo);
        }

        return Result.success("读卡成功",tbReaderId);
    }
    @GetMapping("/refund")
    public Result refund(@RequestParam String tbReaderId) throws Exception {
        boolean isboroow = tbBookborrowService.bookBorrow(Integer.valueOf(tbReaderId));
        if (!isboroow) {
            return Result.error("该读者还有借阅图书未还");
        }
        TbReader reader = readerService.findById(Long.valueOf(tbReaderId));
        PayOrder order = payOrderService.findById(reader.getOutTradeNo());
        //wx  退款
        if(order.getPayType()==1) {
            return wxPayService.refund(Integer.valueOf(tbReaderId), String.valueOf(reader.getOutTradeNo()),order.getPayAmount());
        }
        //alipay 退款
        return aliPayServer.refund(Integer.valueOf(tbReaderId), String.valueOf(reader.getOutTradeNo()),order.getPayAmount().toString());
    }

    @PostMapping("insert")
    public Result addTel(@RequestBody TbReader tbReader){
        int i = D1081Utils.CommOpenWithBaud(com, port);
        if (i == 0){
            System.out.println("串口打开失败");
            return Result.error("串口打开失败");
        }

        Integer lastId = readerService.findLastId();

        tbReader.setRdId(lastId+1);
        tbReader.setRdRoleid(4);
        D1081Utils.AutoTestMac();

        String res = "办卡成功";
        try{
            //写入
            //找不到卡，或卡冲突报错
            AcardDao.API_MF_Request();
            //写入卡
            AcardDao.writeDataHex("1", "" + (lastId + 1));

            //吐卡
            D1081Utils.sendCmdFC0();

            readerService.save(tbReader);

        } catch (Exception e){
            //C
            RecvBuf[0] = 0x43;
            //P
            RecvBuf[1] = 0x50;
            //收卡--RS
            D1081Utils.SendCmd(RecvBuf, 2);

            return Result.error("发卡失败");

        }finally {
            int close = D1081Utils.CommClose();
            if(close != 0)
            {
                System.out.println("串口关闭失败");
                return Result.error("串口关闭失败");
            }
        }

        return Result.success(res);


    }



    @PostMapping("notify")
    public String notify(String auth_no,String out_order_no,String operation_id,String out_request_no,String operation_type,String amount,String status,String gmt_create,String gmt_trans,String payer_logon_id,String payer_user_id,String payee_logon_id,String payee_user_id,String total_freeze_amount,String total_unfreeze_amount,String total_pay_amount,String rest_amount,String credit_amount,String fund_amount,String total_freeze_credit_amount,String total_freeze_fund_amount,String total_unfreeze_credit_amount,String total_unfreeze_fund_amount,String  total_pay_credit_amount,String total_pay_fund_amount,String rest_credit_amount,String rest_fund_amount,String pre_auth_type){
        System.out.println("异步通知");
        return "success";
    }


}