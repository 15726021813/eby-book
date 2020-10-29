package cn.eby.book.fuction.D1081;


import cn.eby.book.common.exception.CustomException;
import cn.eby.book.fuction.util.Utils;

public class D1081Utils {

    private static D1081 d1081 = D1081.INSTANCE;

    private static int portId = 0;

    private static String strVersion = "";

    private static byte MacAddress = -1;

    private static byte[] RecvBuf = new byte[300];

    private static int dataB;


    /**
     * 以相应的波特率打开串口
     * @param port 串口
     * @param data 波特率 1200 2400 4800 9600 19200 38400
     * @return 正确返回串口的句柄；错误=0
     */
    public static int CommOpenWithBaud(int port,int data){

        portId = d1081.D1801_CommOpenWithBaud(port, data);
        dataB = data;
        return portId;
    }

    /**
     * 关闭当前打开的串口
     * 要关闭的串口的句柄
     * @return 正确=0，错误=非0
     */
    public static int CommClose(){

        int i = d1081.D1801_CommClose(portId);

        return i;
    }

    /**
     * 读取D1801动态库版本信息
     * @param
     * @param
     * @return
     */
    public static String GetDllVersion(){

        int i = d1081.D1801_GetDllVersion(portId, RecvBuf);
        strVersion = Utils.ByteToString(RecvBuf);
        if (i != 0){
            System.out.println("获取动态库版本失败");
            return null;
        }
        return strVersion;
    }


    /**
     * 获取设备Mac地址
     * @return
     */
    public static int AutoTestMac(){

        //此处遍历0-15的Mac地址
        for(int i = 0; i < 16; i++)
        {
            int getData = d1081.D1801_AutoTestMac(portId, (byte) i);
            if(getData == 0)
            {
                MacAddress = (byte) i;
                break;
            }
        }

        return MacAddress;
    }

    /**
     * 获取D1801机器的版本信息
     * @return 正确=0，错误=非0 返回机器版本信息
     */
    public static String GetSysVersio(){

        int i;
        for(i = 0; i < 300; i++){
            RecvBuf[i] = 0;
        }
        int i1 = d1081.D1801_GetSysVersion(portId,MacAddress, RecvBuf);
        String string = Utils.ByteToString(RecvBuf);
        if (i1 != 0){
            System.out.println("获取机器的版本信息失败");
            return null;
        }
        System.out.println("获取机器的版本信息成功，版本号为：" + string);

        return string;
    }

    /**
     * 获取设备传感器
     * @return 正确=0，错误=非0
     */
    public static boolean SensorQuery(){

        boolean type = true;

        int sensorQuery = d1081.D1801_SensorQuery(portId,MacAddress,RecvBuf);
        String StrMessage = "";
        String StrMessageError = "";

        if(sensorQuery != 0) {
            System.out.println("获取设备传感器状态失败--高级查询");
            return false;
        } else {

            switch(RecvBuf[0])
            {
                case 0x38:
                    StrMessageError += "命令状态：回收卡箱已满\r\n";
                    StrMessageError += "卡箱状态：卡箱非预空\r\n";
                //命令不能执行
                case 0x34:
                    StrMessageError += "命令状态：命令不能执行，请点击执行复位指令\r\n";
                //准备卡失败
                case 0x32:
                    StrMessageError += "命令状态：命令不能执行，请点击执行复位指令\r\n";
                //正在准备卡
                case 0x31:
                    StrMessage += "命令状态：正在准备卡\r\n";
                    break;
                //机器空闲
                case 0x30:
                    StrMessage += "命令状态：机器空闲\r\n";
                    break;
            }

            switch(RecvBuf[1])
            {
                //正在发卡
                case 0x38:
                    StrMessage += "命令状态：正在发卡\r\n";
                    break;
                //正在收卡
                case 0x34:
                    StrMessage += "命令状态：正在收卡\r\n";
                    break;
                //发卡出错
                case 0x32:
                    StrMessageError += "命令状态：发卡出错，请点击执行复位指令\r\n";
                //收卡出错
                case 0x31:
                    StrMessageError += "命令状态：收卡出错，请点击执行复位指令\r\n";
                //没有任何动作
                case 0x30:
                    StrMessage += "命令状态：没有任何动作\r\n";
                    break;
            }

            switch(RecvBuf[3])
            {
                case 0x39:
                    StrMessageError += "命令状态：回收卡箱已满\r\n";
                    StrMessageError += "卡箱状态：卡箱预空\r\n";
                case 0x38:
                    StrMessageError += "命令状态：回收卡箱已满\r\n";
                    StrMessageError += "卡箱状态：卡箱非预空\r\n";
                case 0x34:
                    StrMessage += "卡箱状态：重叠卡\r\n";
                    break;
                case 0x32:
                    StrMessageError += "卡箱状态：卡堵塞\r\n";
                    break;
                //正在准备卡
                case 0x31:
                    StrMessage += "卡箱状态：卡箱预空\r\n";
                    break;
                //机器空闲
                case 0x30:
                    StrMessage += "卡箱状态：卡箱非预空\r\n";
                    break;
            }

            switch(RecvBuf[3])
            {
                case 0x3E:
                    StrMessage += "卡片位置：只有一张卡在传感器2-3位置\r\n";
                    break;
                case 0x3B:
                    StrMessage += "卡片位置：只有一张卡在传感器1-2位置\r\n";
                    break;
                case 0x39:
                    StrMessage += "卡片位置：只有一张卡在传感器1位置\r\n";
                    break;
                case 0x38:
                    StrMessageError += "卡片位置：发卡箱已空\r\n";
                    StrMessageError += "卡箱状态：卡箱空\r\n";
                case 0x36:
                    StrMessage += "卡片位置：卡在传感器2-3位置\r\n";
                    break;
                case 0x34:
                    StrMessage += "卡片位置：卡在传感器3位置，预发卡位置\r\n";
                    break;
                case 0x33:
                    StrMessage += "卡片位置：卡在传感器1-2位置，读卡位置\r\n";
                    break;
                case 0x32:
                    StrMessage += "卡片位置：卡在传感器2位置\r\n";
                    break;
                case 0x31:
                    StrMessage += "卡片位置：卡在取卡位置\r\n";
                    break;

            }
            System.out.println(StrMessage);
            if (StrMessageError != ""){
                throw new CustomException(StrMessageError);
            }
        }
        System.out.println("获取设备传感器状态成功");
        return type;
    }

    /**
     * 发送D1801的操作命令
     * @param p_Cmd 存储命令
     * @param CmdLen 命令字符串口的长度
     * @return 正确=0，错误=非0
     */
    public static int SendCmd(byte[] p_Cmd,int CmdLen){



        return d1081.D1801_SendCmd(portId,MacAddress,p_Cmd,CmdLen);
    }

    /**
     * 发卡（到口卡位置）
     * @return
     */
    public static int sendCmdFC0(){

        //F
        RecvBuf[0] = 0x46;
        //C
        RecvBuf[1] = 0x43;
        //0
        RecvBuf[2] = 0x30;
        int sendCmd = d1081.D1801_SendCmd(portId,MacAddress,RecvBuf,3);
        if(sendCmd != 0)
        {
            throw new CustomException("发卡到卡口失败");
        }
        System.out.println("发卡到取卡位置成功");
        return 0;
    }











}
