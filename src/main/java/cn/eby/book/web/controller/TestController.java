package cn.eby.book.web.controller;


import cn.eby.book.fuction.D1081.D1081Utils;
import cn.eby.book.fuction.D6520.AcardDao;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    private static byte[] RecvBuf = new byte[300];

    @Value("${D1081.com}")
    private int com;

    @Value("${D1081.port}")
    private int port;

    @GetMapping("/open")
    public String open(){

        int i = D1081Utils.CommOpenWithBaud(3, 9600);
        if (i == 0){
            System.out.println("串口打开失败");
            return "串口打开失败";
        }
        int mac = D1081Utils.AutoTestMac();
        if(mac >= 0) {
            System.out.println("获取Mac地址成功，地址为：" + mac);
        }
        else {
            System.out.println("获取Mac地址失败");
        }

        System.out.println("串口打开成功");

        String version = D1081Utils.GetDllVersion();

        if (version == null){
            System.out.println("获取动态库版本失败");
        }

        String versio = D1081Utils.GetSysVersio();

        if (versio == null){
            System.out.println("获取机器的版本信息失败");
        }

        return "串口打开成功";


    }

    /**
     * 设备状态
     * @return
     */
    @GetMapping("/getSen")
    public String getSensorQuery(){

        boolean sensorQuery = D1081Utils.SensorQuery();
        if (sensorQuery == true){
            System.out.println("获取设备传感器状态 ===>>>" + sensorQuery);
        }

        return "获取设备传感器成功";
    }

    /**
     * 发卡到传感器2
     */
    @GetMapping("/sendFC6")
    public String sendFC6(){

        //F
        RecvBuf[0] = 0x46;
        //C
        RecvBuf[1] = 0x43;
        //6
        RecvBuf[2] = 0x36;
        int sendCmd = D1081Utils.SendCmd(RecvBuf, 3);
        if (sendCmd != 0){
            System.out.println("发卡到传感器2 ======》》失败");
        }
        System.out.println("发卡到传感器2 ======》》 成功");

        return "发卡到传感器2 ======》》 成功";

    }

    /**
     * 发卡到读卡位置
     */
    @GetMapping("/sendFC7")
    public String sendFC7(){

        //F
        RecvBuf[0] = 0x46;
        //C
        RecvBuf[1] = 0x43;
        //7
        RecvBuf[2] = 0x37;

        int sendCmd2 = D1081Utils.SendCmd(RecvBuf, 3);

        if (sendCmd2 != 0){
            System.out.println("发卡到读卡位置 ======》》失败");
        }
        System.out.println("发卡到读卡位置 ======》》 成功");

        return "发卡到读卡位置 ======》》 成功";

    }

    @GetMapping("/getInt")
    public int comlist(){

        System.out.println(com);
        System.out.println(port);

        return com + port;
    }

    /**
     * 发卡到取卡位置
     */
    @GetMapping("/sendFC4")
    public String sendFC4(){

        //F
        RecvBuf[0] = 0x46;
        //C
        RecvBuf[1] = 0x43;
        //4
        RecvBuf[2] = 0x34;

        int sendCmd3 = D1081Utils.SendCmd(RecvBuf, 3);

        if (sendCmd3 != 0){
            System.out.println("发卡到取卡位置 ======》》失败");
        }
        System.out.println("发卡到取卡位置 ======》》 成功");

        return "发卡到取卡位置 ======》》 成功";

    }

    /**
     * 卡到卡口外
     */
    @GetMapping("/sendFC0")
    public String sendFC0(){

        //F
        RecvBuf[0] = 0x46;
        //C
        RecvBuf[1] = 0x43;
        //0
        RecvBuf[2] = 0x30;

        int sendCmd4 = D1081Utils.SendCmd(RecvBuf, 3);

        if (sendCmd4 != 0){
            System.out.println("卡到卡口外 ======》》失败");
        }
        System.out.println("卡到卡口外 ======》》 成功");

        return "卡到卡口外 ======》》 成功";

    }

    /**
     * 复位成功
     */
    @GetMapping("/sendRS")
    public String sendRS(){

        //R
        RecvBuf[0] = 0x52;
        //S
        RecvBuf[1] = 0x53;

        //复位--RS
        int RS = D1081Utils.SendCmd(RecvBuf, 2);
        if(RS != 0)
        {
            System.out.println("复位失败");
            return null;
        }
        System.out.println("复位成功");

        return "复位成功 ======》》 成功";

    }



    @GetMapping("/close")
    public String close() {

        int close = D1081Utils.CommClose();
        if(close != 0)
        {
            System.out.println("串口关闭失败");
            return null;
        }
        System.out.println("串口关闭成功");

        return "串口关闭成功=====》》》》成功";
    }

    @GetMapping("write")
    public String write(){
        AcardDao.API_MF_Request();
        //写入卡
        AcardDao.writeDataHex("1", "" + (2015551 + 1));
        return "success";
    }


}
