package cn.eby.book.service.Impl;

import cn.eby.book.common.Result;
import cn.eby.book.common.constant.SIP2Instruct;
import cn.eby.book.sip.Client;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @Auther: 徐长乐
 * @Date: 2020/10/07/8:34
 * @Description:
 */
@Service
public class Sip2ClientService {
    //终端用户名
    public static final String institution = "AOilascs|";

    public static void main(String[] args) {
        Client client = new Client();
//        String send = client.send(SIP2Instruct.RETURN + "N" + client.getNewDate() + client.getNewDate() + institution + "AB033490100001182|");
//        String send = client.send(SIP2Instruct.BORROW + "NN" + client.getNewDate() + client.getNewDate() + institution + "AA" + "033409000000117" + "|AB033490100001182|");
//        String send = client.send(SIP2Instruct.BORROW + "NN" + client.getNewDate() + client.getNewDate() + institution + "AA" + "033409000000117" + "|AB033490100001097|");
//        String send = client.send(SIP2Instruct.RENEW + "NN" + client.getNewDate() + client.getNewDate() + institution +"AA" + "033409000000117" + "|AB033490100001182|");
//        String send = client.send(SIP2Instruct.BOOK + client.getNewDate() + institution + "AB033490100001182|");
        String send = client.send(SIP2Instruct.READER+"000" + client.getNewDate() + institution + "AA|");
//        String[] bookList = send.substring(send.indexOf("AS") + 2, send.lastIndexOf("|AT")).split(",");

        System.out.println(send);
//        Sip2ClientService sip2ClientService = new Sip2ClientService();
//        Map<String, Object> stringObjectMap = sip2ClientService.bookBack("033490100001182");
//        Result result = sip2ClientService.bookRenew("033490100001182", "033409000000117");
//        System.out.println(sip2ClientService.idCardCheck("410325196003160517"));
//        System.out.println(result.toString());
    }

    /**
     * 借书
     * @param zi_id 图书ID
     * @param rd_id 读者编号
     * @return
     */
    public Map<String,Object> bookBorrow(String zi_id, String rd_id){
        Client client = new Client();
        String send = client.send(SIP2Instruct.BORROW + "NN" + client.getNewDate() + client.getNewDate() + institution + "AA" + rd_id + "|AB" + zi_id + "|");
        String code = send.substring(2, 3);
        String bkId = send.substring(send.indexOf("AB") +2, send.lastIndexOf("|AJ"));
        String bkName = send.substring(send.indexOf("AJ") +2, send.lastIndexOf("|AH"));
        String rndate = send.substring(send.indexOf("AH") +2, send.lastIndexOf("|BT"));
        String message = send.substring(send.indexOf("AF") +2, send.lastIndexOf("|AG"));
        client.closeSocket();
        Map<String,Object> res = new HashMap<>(5);
        res.put("code",code);
        res.put("bkId", bkId);
        res.put("bkName",  bkName);
        res.put("rndate", rndate );
        res.put("message",message);

        return res;
    }

    /**
     * 还书
     * @param zi_id  图书ID
     * @return
     */
    public Map<String,Object> bookBack(String zi_id) {
        Client client = new Client();
        String send = client.send(SIP2Instruct.RETURN + "N" + client.getNewDate() + client.getNewDate() + institution + "AB" + zi_id + "|");
        String code = send.substring(2, 3);
        String bkId = send.substring(send.indexOf("AB") +2, send.lastIndexOf("|AJ"));
        String bkName = send.substring(send.indexOf("AJ") +2, send.lastIndexOf("|CL"));
        String message = send.substring(send.indexOf("AF") +2, send.lastIndexOf("|AG"));
        client.closeSocket();
        Map<String,Object> res = new HashMap<>(4);
        res.put("code",code);
        res.put("bkId", bkId);
        res.put("bkName",  bkName);
        res.put("message",message);
        return  res;
    }

    /**
     * 图书续借
     * @param zi_id
     * @param rd_id
     * @return
     */
    public Result bookRenew(String  zi_id,String  rd_id) {
        Client client = new Client();
        String send = client.send(SIP2Instruct.RENEW + "NN" + client.getNewDate() + client.getNewDate() + institution +"AA" + rd_id + "|AB" + zi_id + "|");
        String code = send.substring(2, 3);
        String message = send.substring(send.indexOf("AF") +2, send.lastIndexOf("|AG"));

        if ("1".equals(code)){
            String rndate = send.substring(send.indexOf("AH") +2, send.lastIndexOf("|BT"));
            return Result.success("续借成功,归还日期"+rndate);
        }
        return Result.error(message);
    }

    /**
     * 根据ID获取图书信息
     * @param id
     * @return
     */
    public  Map<String,Object> getBookByZi_id(String id){
        Client client = new Client();
        String send = client.send(SIP2Instruct.BOOK + client.getNewDate() + institution + "AB" + id + "|");
        client.closeSocket();
        //图书ID
        String bkId = send.substring(send.indexOf("AB") +2, send.lastIndexOf("|AJ"));
        //图书名称
        String bkName = send.substring(send.indexOf("AJ") +2, send.lastIndexOf("|BH"));
        //作者
        String auth = send.substring(send.indexOf("CF") +2, send.lastIndexOf("|AH"));
        //应还日期
        String rndate = send.substring(send.indexOf("AH") +2, send.lastIndexOf("|CJ"));
        //借书日期
        String bodate = send.substring(send.indexOf("CM") +2, send.lastIndexOf("|AB"));
        //价格
        String bkPric = send.substring(send.indexOf("BV") +2, send.lastIndexOf("|CK"));
        //出版社
        String prsNme =  send.substring(send.indexOf("AG") +2, send.lastIndexOf("|CA"));
        //状态
        String status =  send.substring(send.indexOf("CH") +2, send.lastIndexOf("|AF"));
        //所在馆藏地
        String currentLocation = send.substring(send.indexOf("AP") +2, send.lastIndexOf("|CH"));
        //所属馆藏地
        String permanentLocation = send.substring(send.indexOf("AQ") +2, send.lastIndexOf("|AP"));
        Map<String,Object> res = new HashMap<>(10);
        res.put("bkId",bkId);
        res.put("bkName",bkName);
        res.put("auth",auth);
        res.put("rndate",rndate);
        res.put("bodate",bodate);
        res.put("bkPric",bkPric);
        res.put("prsNme",prsNme);
        res.put("status",status);
        res.put("currentLocation",currentLocation);
        res.put("permanentLocation",permanentLocation);

        return res;
    }

    /**
     * 根据ID 获取读者信息
     * @param readerId
     * @return
     */
    public Map<String,Object> ReaderInfo(String readerId){
        Client client = new Client();
        String send = client.send(SIP2Instruct.READER+"000" + client.getNewDate() + institution + "AA" + readerId + "|");
        client.closeSocket();
        //读者ID
        String id = send.substring(send.indexOf("AO") +2, send.lastIndexOf("|AE"));
        //读者名称
        String name = send.substring(send.indexOf("AE") +2, send.lastIndexOf("|BZ"));
        //欠费总额
        String feeAmount = send.substring(send.indexOf("BV") +2, send.lastIndexOf("|YJ"));
        //手机号
        String tel = send.substring(send.indexOf("BF") +2, send.lastIndexOf("|XO"));
        //身份证号
        String idCard = send.substring(send.indexOf("XO") +2, send.lastIndexOf("|BD"));
        //到期时间
        String endDate = send.substring(send.indexOf("XD") +2, send.lastIndexOf("|XE"));
        //开始时间
        String begDate = send.substring(send.indexOf("XE") +2, send.lastIndexOf("|XM"));
        //押金
        String YJ = send.substring(send.indexOf("YJ") +2, send.lastIndexOf("|YZ"));
        //性别
        String sex = send.substring(send.indexOf("XM") +2, send.lastIndexOf("|AF"));
        //借阅图书列表
        String[] bookList = send.substring(send.indexOf("AS") + 2, send.lastIndexOf("|AT")).split(",");
        Map<String,Object> res = new HashMap<>(10);
        res.put("id",id);
        res.put("name",name);
        res.put("feeAmount",feeAmount);
        res.put("tel",tel);
        res.put("idCard",idCard);
        res.put("endDate",endDate);
        res.put("begDate",begDate);
        res.put("YJ",YJ);
        res.put("sex",sex.equals("M")? "男":"女");
        res.put("bookList",bookList);
        return res;
    }

    /**
     *办证
     * @return
     */
    public Map<String,Object> createCard(){
        String readerId ="";
        String password ="";
        String name ="";
        String idCard ="";
        String libraryId ="";
        String rdr="";
        String sex ="";    //M / F
        String deposit="";
        Client client = new Client();
        String send = client.send(SIP2Instruct.CARD + client.getNewDate() + institution + "AA" + readerId + "|"+"AD"+password+"|AE"+name+"|XO"+idCard+"|AM"+libraryId+"|XM"+sex+"|XT"+rdr+"|BV"+deposit+"XK01");
        client.closeSocket();
        String code = send.substring(2, 3);
        String id = send.substring(send.indexOf("AA") +2, send.lastIndexOf("|OK"));
        String message = send.substring(send.indexOf("AF") +2, send.lastIndexOf("|AY"));
        Map<String,Object> res = new HashMap<>(3);
        res.put("code",code);
        res.put("readerId",readerId);
        res.put("message",message);
        return res;
    }

    /**
     * 读者证查重
     * @param readId
     * @return true 重复  flase 不重复
     */
    public boolean readerIdCheck(String readId){
        Client client = new Client();
        String send = client.send(SIP2Instruct.CHECK + client.getNewDate() + institution + "AO" + institution +"AA"+readId+ "|XK0");
        client.closeSocket();
        int code = Integer.parseInt(send.substring(2, 3));
        return code == 1 ;
    }

    /**
     * 身份证查重
     * @param iCard
     * @return true 重复  false  不重复
     */
    public boolean idCardCheck(String iCard){
        Client client = new Client();
        String send = client.send(SIP2Instruct.CHECK + client.getNewDate() + institution + "AO" + institution +"XO"+iCard+ "|XK1");
        client.closeSocket();
        int code = Integer.parseInt(send.substring(2, 3));
        return code == 1;
    }

}
