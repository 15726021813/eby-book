package cn.eby.book.fuction.D6520;


import cn.eby.book.common.exception.CustomException;
import cn.eby.book.fuction.util.Utils;
import org.springframework.beans.factory.annotation.Value;

import java.util.HashMap;
import java.util.Map;

/**
 * @author 朱胜伟
 */
public class AcardDao {

    private static int res ;
    @Value("${writeCar.com}")
    private static int com = 2;
    @Value("${writeCar.port}")
    private static int bote = 9600;
    @Value("${writeCar.address}")
    private static int address = 0;
    /*
对返回值进行解释
 */
    private static Map<Integer, String> returnValueMap = new HashMap<>();
    private static Map<Integer, String> statusValueMap = new HashMap<>();
    static byte[] pass = new byte[]{(byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff};
    public AcardDao(){}
    public AcardDao(int com,int bote,int address){
        this.com = com;
        this.bote = bote;
        this.address = address;
    }
    //连接接口
    static {
        try{
            res = JNADLLCall.jnadllCall.API_OpenComm(com, bote);
        }catch (Exception e){
            System.out.println("链接失败：：：：：：：：：");
            System.out.println(e);
        }
        if (res == 0) {
            System.out.println("链接设备失败：" + res);

        }
        returnValueMap.put(0x0, "表示命令执行成功");
        returnValueMap.put(0x2, "表示接收数据长度不匹配");
        returnValueMap.put(0x3, "表示串口发送失败");
        returnValueMap.put(0x4, "表示串口未接到任何数据");
        returnValueMap.put(0x5, "表示设备地址不匹配");
        returnValueMap.put(0x7, "表示校验和不正确");
        returnValueMap.put(0xA, "表示输入参数有误，请参见不具体的函数说明");

        statusValueMap.put(0x00, "表示命令执行成功");
        statusValueMap.put(0x01, "表示命令操作失败（具体说明参见函数）");
        statusValueMap.put(0x80, "表示参数设置成功");
        statusValueMap.put(0x81, "表示参数设置失败");
        statusValueMap.put(0x82, "表示通讯超时");
        statusValueMap.put(0x83, "表示卡不存在");
        statusValueMap.put(0x84, "表示接收卡数据出错");
        statusValueMap.put(0x85, "表示输入参数或者输入命令格式错误");
        statusValueMap.put(0x87, "表示未知的错误");
        statusValueMap.put(0x89, "The parameter of the command or the Format of the command Erro");
        statusValueMap.put(0x8A, "在块初始化中出现错误");
        statusValueMap.put(0x8B, "在防冲突过程中得到错误的序列号");
        statusValueMap.put(0x8C, "密码认证没通过");
        statusValueMap.put(0x8f, "表示输入的指令代码不存在");
        statusValueMap.put(0x90, "表示卡不支持这个命令");
        statusValueMap.put(0x91, "表示命令格式有错误");
        statusValueMap.put(0x92, "表示在命令的FLAG参数中，不支持OPTION 模式");
        statusValueMap.put(0x93, "表示要操作的BLOCK不存在");
        statusValueMap.put(0x94, "表示要操作的对象已经别锁定，不能进行修改");
        statusValueMap.put(0x95, "表示锁定操作不成功");
        statusValueMap.put(0x96, "表示写操作不成功");
        statusValueMap.put(0x89, "在15693卡中flag不正确 ");

    }

    public static void main(String[] args) {
            System.out.println(writeDataHex("1","78988485"));
//        System.out.println(readDataHex("1"));
//        byte[] t = "Z".getBytes();
//        System.err.println(Utils.bytesToHexString(t));
//        writeData("4", "123456zsw");
//        System.out.println(readDataHex("4"));


    }

    /**
     * 安全认证，读卡前准备工作，包含寻卡，与防冲突，如果
     */
    public static String API_MF_Request(){
        StringBuffer sb = new StringBuffer();
        byte[] buffer= new byte[10];
        int re = JNADLLCall.jnadllCall.API_MF_Request(res,address, Utils.hexToByte("00"),buffer);

        if (re != 0){
            throw new CustomException("未找到卡，或者卡异常");
        }

        byte[] flag = new byte[1];
        byte[] uid = new byte[4];
        re = JNADLLCall.jnadllCall.API_MF_Anticoll(res,0,flag,uid);
        if(re != 0){
            throw new CustomException("卡防冲突失败:"+returnValueMap.get(re));
        }
        if(flag[0] == 1){
            throw new CustomException("卡防冲突:"+"检测到多张卡");
        }

        //返回卡的uid 卡芯片号
        for (byte b : uid) {
            sb.append(b);
        }
        return  sb.toString();
    }
    //读数据,返回的是一个十六位字符串
    //start 要读取的扇区
    public static String readDataHex(String start) {
        byte[] pa = pass;
        byte[] buffer = new byte[16];
        //读取模式   开始读取的扇区   读取扇区的长度
        int re = JNADLLCall.jnadllCall.API_MF_Read(res, address, Utils.hexToByte("01"), Byte.parseByte(start), Utils.hexToByte("1"), pa, buffer);
//        System.out.println(Utils.bytesToHexString(pa,0,4));
        if (re != 0) {
            throw new CustomException("读数据异常：" + returnValueMap.get(re));
        }
        return Utils.bytesToHexString(buffer, 0, 16);
    }

    //start 要读取的扇区    write 写入的数据
    public static String writeDataHex(String start, String write) {
        //对write进行封装，使格式符合规范
        write = fullString(write);
        byte[] pa = pass;

        byte[] buffer = Utils.hexStringToBytes(write);
        //读取模式   开始读取的扇区   读取扇区的长度

        int r = JNADLLCall.jnadllCall.API_MF_Write(res, address, Utils.hexToByte("01"), Byte.parseByte(start), Utils.hexToByte("1"), pa, buffer);

        if (r != 0) {
            throw new CustomException("写数据异常：" + statusValueMap.get(r));
        }
        return statusValueMap.get(r);
    }

    private static String fullString(String write) {
        //write的处理，write长度长度为16，如果低于16位 左侧补0，高于16位报错
      if (write.length() < 8) {
            int strLen = write.length();
            if (strLen < 8) {
                while (strLen < 8) {
                    StringBuffer sb = new StringBuffer();
                    sb.append("0").append(write);// 左补0
//                    sb.append(write).append("0");//右补0
                    write = sb.toString();
                    strLen = write.length();
                }
            }
        }
        if (write.length() > 32) {
            throw new CustomException("写入的位数超过32字节，无法正常写入");
        } else if (write.length() < 32) {
            int strLen = write.length();
            if (strLen < 32) {
                while (strLen < 32) {
                    StringBuffer sb = new StringBuffer();
//                    sb.append("0").append(write);// 左补0
                     sb.append(write).append("0");//右补0
                    write = sb.toString();
                    strLen = write.length();
                }
            }
        }
        return write;
    }

//    //以十进制写
//    public static String writeData(String start, String write) {
//        return writeDataHex(start, Utils.bytesToHexString(write.getBytes()));
//
//    }

}
