package cn.eby.book.fuction.D1081;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.win32.StdCallLibrary;
import org.springframework.core.io.ClassPathResource;
import java.io.File;

public interface D1081 extends StdCallLibrary {



    D1081 INSTANCE = Native.load("dll\\D1801_DLL.dll", D1081.class);

    /**
     *  =========== 动态库版本函数 ================
     */

    /**
     * 读取D1801动态库版本信息
     * @param ComHandle 串口句柄
     * @param strVersion 存动态库版本信息，读取成功会存储版本信息，如 “D1801DLL_V1.00”
     * @return 正确=0，错误=非0
     */
    int D1801_GetDllVersion(int ComHandle,byte[] strVersion);

    /**
     * 打开串口，默认的波特率“9600, n, 8, 1”
     * @param Port 要打开的串口，例如打开com1，则Port 存储1
     * @return 正确返回串口的句柄；错误=0
     */
    int D1801_CommOpen(int Port);

    /**
     * 以相应的波特率打开串口
     * @param Port 要打开的串口，例如打开com1，则Port 存储1
     * @param data 波特率选项，有效值如下：
     *             1200 2400 4800 9600 19200 38400
     * @return 正确返回串口的句柄；错误=0
     */
    int D1801_CommOpenWithBaud(int Port,int data);

    /**
     * 关闭当前打开的串口
     * @param ComHandle 要关闭的串口的句柄
     * @return 正确=0，错误=非0
     */
    int D1801_CommClose(int ComHandle);

    /**
     *  =========== 机器操作函数 ================
     */

    /**
     * 获取D1801机器的版本信息，对应协议中“GV”这条命令
     * @param ComHandle 已经打开的串口的句柄
     * @param MacAddr 机器的地址，有效取值（0到15）
     * @param strVersion 存机器的版本信息，读取成功会存储版本信息，
     *                   如 “TTCE_D1801_02_V2.**”
     * @return 正确=0，错误=非0
     *
     */
    int D1801_GetSysVersion(int ComHandle,byte MacAddr,byte[] strVersion);

    /**
     * 一般查询，D1801状态信息，返回3字节的状态信息，对应协议中“RF”这条指令
     * @param ComHandle 已经打开的串口的句柄
     * @param MacAddr   机器的地址，有效取值（0到15）
     * @param StateInfo 存储D1801状态信息，返回3个字节，详情请参D1801照通讯协议
     * @return  正确=0，错误=非0
     */
    int D1801_Query(int ComHandle,byte MacAddr,byte[] StateInfo);

    /**
     * 高级查询，D1801状态信息，返回4字节的状态信息，对应协议中“AP”这条指令
     * @param ComHandle 已经打开的串口的句柄
     * @param MacAddr   机器的地址，有效取值（0到15）
     *
     * @param StateInfo 存储D1801状态信息，返回4个字节，详情请参D1801照通讯协议
     * @return  正确=0，错误=非0
     */
    int D1801_SensorQuery(int ComHandle,byte MacAddr,byte[] StateInfo);

    /**
     * 发送D1801的操作命令
     * @param ComHandle 已经打开的串口的句柄
     * @param MacAddr   机器的地址，有效取值（0到15）
     * @param p_Cmd     存储命令字符串
     * @param CmdLen    命令字符串口的长度
     * @return 正确=0，错误=非0
     * （注：	此函数可以执行命令如下：
     * 	D1801_SendCmd(ComHandle, MacAddr, “DC”, 2)	发卡（到取卡位置）
     * 	D1801_SendCmd(ComHandle, MacAddr, “CP”, 2)	收卡
     * 	D1801_SendCmd(ComHandle, MacAddr, “RS”, 2)	复位
     * 	D1801_SendCmd(ComHandle, MacAddr, “BE”, 2)	允许蜂鸣（卡少，卡空，出错蜂鸣器会报警）
     * 	D1801_SendCmd(ComHandle, MacAddr, “BD”, 2)	停止蜂鸣器工作
     * 	D1801_SendCmd(ComHandle, MacAddr, “CS0”, 3)	设置机器通讯为波特率1200bps
     * 	D1801_SendCmd(ComHandle, MacAddr, “CS1”, 3)	设置机器通讯为波特率2400bps
     * 	D1801_SendCmd(ComHandle, MacAddr, “CS2”, 3)	设置机器通讯为波特率4800bps
     * 	D1801_SendCmd(ComHandle, MacAddr, “CS3”, 3)	设置机器通讯为波特率9600bps
     * 	D1801_SendCmd(ComHandle, MacAddr, “CS4”, 3)	设置机器通讯为波特率19200bps
     * 	D1801_SendCmd(ComHandle, MacAddr, “CS5”, 3)	设置机器通讯为波特率38400bps
     * 	D1801_SendCmd(ComHandle, MacAddr, “FC6”, 3)	发卡到传感器2
     * 	D1801_SendCmd(ComHandle, MacAddr, “FC7”, 3)	发卡到读卡位置
     * 	D1801_SendCmd(ComHandle, MacAddr, “FC0”, 3)	发卡到取卡位置
     * 	D1801_SendCmd(ComHandle, MacAddr, “FC4”, 3)	发卡到卡口外
     * D1801_SendCmd(ComHandle, MacAddr, “FC8”, 3)	前端进卡
     * 	建议使用标准的9600的波特率通讯，否则速率太低，可能影响命令发送和接受
     */
    int D1801_SendCmd(int ComHandle,byte MacAddr,byte[] p_Cmd,int CmdLen);

    /**
     * 获取发卡以及回收卡计数
     * @param ComHandle 已经打开的串口的句柄
     * @param MacAddr   机器的地址，有效取值（0到15）
     * @param szData    存储计数数据，前十个字节记录发卡数量最后一个字节记录回收卡数量
     * @return 正确=0，错误=非0
     */
    int D1801_GetCountSum(int ComHandle,byte MacAddr,byte[] szData);

    /**
     * 清除发卡计数
     * @param ComHandle 已经打开的串口的句柄
     * @param MacAddr   机器的地址，有效取值（0到15）
     * @return  正确=0，错误=非0
     */
    int D1801_ClearSendCount(int ComHandle,byte MacAddr);

    /**
     * 清除回收卡计数
     * @param ComHandle 已经打开的串口的句柄
     * @param MacAddr   机器的地址，有效取值（0到15）
     * @return  正确=0，错误=非0
     */
    int D1801_ClearRecycleCount(int ComHandle,byte MacAddr);

    /**
     *获取设备Mac地址
     * @param ComHandle
     * @param MacAddr
     * @return
     */
    int D1801_AutoTestMac(int ComHandle,byte MacAddr);
}
