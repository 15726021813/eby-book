package cn.eby.book.fuction.D6520;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.win32.StdCallLibrary;
/**
 * @author 朱胜伟
 */
public interface JNADLLCall extends Library {
    JNADLLCall jnadllCall  = (JNADLLCall) Native.loadLibrary("dll\\ALL.dll",JNADLLCall.class);

    /**
     * 打开一个串口
     * @param nCom com口
     * @param Port 波特率 9600
     * @return
     */
    int API_OpenComm(int nCom,int Port);

    /**
     * 关闭一个串口
     * @param commHand API_OpenComm的返回值
     * @return
     */
    boolean API_CloseComm(int commHand);

    /**
     * 设置读写器地址
     * @param commHandle  串口返回的句柄
     * @param DeviceAddress 设备地址老地址 默认 0  0-255
     * @param newAddr  设置的新地址
     */
    byte API_SetDeviceAddress(int commHandle,int DeviceAddress,byte newAddr);

    /**
     * 设置读写器波特率
     * @param commHandle  串口返回的句柄
     * @param DeviceAddress 设备地址老地址 默认 0  0-255
     * @param newBaud 设置新的波特率
     * @return
     */
    int API_SetBaudrate(int commHandle,int DeviceAddress,int newBaud);



    /**
     * 读取用户信息（就是16进制数）
     * @param commHandle
     * @param DeviceAddress
     * @param num_blk
     * @param num_length
     * @param buffer
     * @return
     */
    byte API_ReadUserInfo(int commHandle,int DeviceAddress,int num_blk,int num_length,byte[] buffer);

    int API_WriteUserInfo(int commHandle,int DeviceAddress,int num_blk,int num_length,byte[] user_info);
    /**
     * 获取版本信息
     * @param commHandle  串口返回的句柄
     * @param DeviceAddress 设备地址老地址 默认 0  0-255
     * @param version 这是一个形参转实参
     * @return
     */
    int API_GetVersionNum(int commHandle,int DeviceAddress,byte[] version);

    int API_MF_Request(int commHandle,int DeviceAddress,byte num_blk,byte[] buffer);
    int API_MF_Anticoll(int commHandle,int DeviceAddress,byte[] flag,byte[] uid);

    int API_MF_Read(int commHandle,int DeviceAddress,byte mode,byte blk_add,byte num_blk,byte[] snr,byte[] buffer);

    /**
     * 读取卡的序列号
     * @param commHandle
     * @param DeviceAddress
     * @param mode
     * @param cmd
     * @param flag
     * @param buffer
     * @return
     */
    int API_MF_GET_SNR(int commHandle,int DeviceAddress,byte mode,byte cmd,byte[] flag,byte[] buffer);
    int API_MF_Write(int commHandle,int DeviceAddress,byte mode,byte blk_add,byte num_blk,byte[] key,byte[] buffer);
}
