package cn.eby.book.fuction.hcicrf;

import cn.eby.book.common.exception.CustomException;
import cn.eby.book.entity.TbReader;
import cn.eby.book.fuction.util.Utils;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.ptr.LongByReference;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;

/**
 * @Auther: 徐长乐
 * @Date: 2020/09/19/10:20
 * @Description:
 */
public class HCICRFUtil {

    private static HCICRFDll epen = HCICRFDll.hcicrf;



    public static String GetErrMsg(short st) {
        byte[] msg = new byte[128];
        epen.getErrMsg(st, (short) 0, msg);
        try {
            return new String(msg,"GBK");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }



    public static int hc_net_init(String ip, int m_iPort) {
        return epen.hc_net_init(ip, m_iPort);
    }

    public static short hc_exit(int icdev) {
        return epen.hc_exit(icdev);
    }

    public static short dv_beep(int icdev, short time) {
        return epen.dv_beep(icdev, time);
    }

    /**
     * LCD显示
     *
     * @param icdev    连接读写器返回的设备句柄
     * @param line     要显示字符所在的行,取值0或1
     * @param offset   要显示字符所在的列,范围 0 ~ 14
     * @param disp_buf 要显示的字符数据，最多显示15字节
     * @return
     */
    public static short lcd_display(int icdev, byte line, byte offset, byte[] disp_buf) {
        return epen.lcd_display(icdev, line, offset, disp_buf);
    }

    public static short rf_card(int icdev, short mode, LongByReference Snr) {
        return epen.rf_card(icdev, mode, Snr);
    }

    public static short rf_halt(int icdev) {
        // TODO Auto-generated method stub
        return epen.rf_halt(icdev);
    }

    /**
     * 连接设备
     *
     * @param port 端口号,COM1 ~ COM20取值 0 ~ 19，USB设备取值100
     * @param buad 波特率， 9600 ~ 115200, usb设备默认9600
     * @return 返回设备句柄
     */
    public static int InitReader(short port, int buad) {
        int it = epen.hc_init(port, buad);
        if (it <= 0) {
          throw  new CustomException(GetErrMsg((short) it));
        }
        return it;
    }

    /**
     * 断开设备
     *
     * @param icdev
     */
    public static void ExitReader(int icdev) {
        short st = epen.hc_exit(icdev);
        if (st != 0) {
            throw  new CustomException(GetErrMsg(st));
        }
    }

    /**
     * M1卡操作
     *
     * @param
     * @param
     * @return
     */

    public static String GetCardNO(int icdev) throws UnsupportedEncodingException {
        // M1卡流程: 寻卡,验证密码,数据操作
        // 寻卡
//        序列号
        LongByReference snr = new LongByReference();
        short st = epen.rf_card(icdev, (short) 1, snr);
        if (st != 0) {
           throw  new CustomException("请把读者卡放在读卡区");
        }

        // 验证4块（1扇区）密码
        st = epen.rf_authentication_key(icdev, (short) 0, (short) 1,
                new byte[] { (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff });
        if (st != 0) {
            throw  new CustomException("卡片错误");
        }
        // 读4块数据
        byte[] data = new byte[32];
        st = epen.rf_read(icdev, (short) 1, data);
        if (st != 0) {
            throw  new CustomException("读取卡片数据错误");
        }
        epen.dv_beep(icdev, (short) 30);
        byte[] data1 = new byte[8];
        for (int i = 0; i < 4; i++) {
            data1[i] = data[i];
        }
        String s = Utils.bytesToHexString(data1,0,4);
        String newStr = s.replaceAll("^(0+)", "");
        return newStr;
    }


    public static void SetCardNO(int icdev,String CardNo) {
        // M1卡流程: 寻卡,验证密码,数据操作
//        序列号
        LongByReference snr = new LongByReference();
        short st = epen.rf_card(icdev, (short) 1, snr);
        if (st != 0) {
            throw  new CustomException(GetErrMsg(st));
        }

        // 验证4块（1扇区）密码
        st = epen.rf_authentication_key(icdev, (short) 0, (short) 4,
                new byte[] { (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff });
        if (st != 0) {
            throw  new CustomException("卡片错误");
        }
        // 写4块数据
        byte[] sb = new byte[32];
        for (int i = 0; i < CardNo.getBytes().length; i++) {
            sb[i]= CardNo.getBytes()[i];
        }
        st = epen.rf_write(icdev, (short) 4, sb);
		if (st != 0) {
		    throw new CustomException(GetErrMsg(st));
		}

        epen.dv_beep(icdev, (short) 30);

    }

    /**
     * 读取身份证信息
     *
     * @param
     * @param icdev
     * @return
     */
    public static TbReader IDcard(int icdev) throws UnsupportedEncodingException {
        // 读身份证
        epen.hc_ID_ClearInfo();
        short st = epen.hc_ID_Read(icdev);
        if (st != 0) {
            throw  new CustomException("请把身份证放到读卡器上");
        }
        TbReader tbReader = new TbReader();
        IntByReference len = new IntByReference();
        byte[] idData = new byte[256];
        // 获取姓名
        st = epen.hc_ID_GetName(idData, len);
        if (st != 0) {
            throw  new CustomException(GetErrMsg(st));
        }
        tbReader.setRdName(new String( idData,0,len.getValue(),"GBK"));
        // 获取性别
        Arrays.fill(idData, (byte) 0x00);
        st = epen.hc_ID_GetSex(idData, len);
        if (st != 0) {
            throw  new CustomException(GetErrMsg(st));
        }
        String sex = new String(idData, 0, len.getValue(),"GBK");

        tbReader.setRdSex((short) (sex.equals("男")? 0 : 1));
        // 获取身份证号码
        Arrays.fill(idData, (byte) 0x00);
        st = epen.hc_ID_GetNumber(idData, len);
        if (st != 0) {
            throw  new CustomException(GetErrMsg(st));
        }
        tbReader.setIdCard(new String(idData, 0, len.getValue(),"GBK"));
        // 获取出生日期
        Arrays.fill(idData, (byte) 0x00);
        st = epen.hc_ID_GetBirthday(idData, len);
        if (st != 0) {
            throw  new CustomException(GetErrMsg(st));
        }
        tbReader.setRdBirth(new String(idData, 0, len.getValue()));
        // 获取地址
        Arrays.fill(idData, (byte) 0x00);
        st = epen.hc_ID_GetAddress(idData, len);
        if (st != 0) {
            throw  new CustomException(GetErrMsg(st));
        }
        tbReader.setRdAdr(new String(idData, 0, len.getValue(),"GBK"));
        epen.dv_beep(icdev, (short) 30);
        return tbReader;
    }
    public static String IDcardNo(int icdev) throws UnsupportedEncodingException {
        // 读身份证
        epen.hc_ID_ClearInfo();
        short st = epen.hc_ID_Read(icdev);
        if (st != 0) {
            throw  new CustomException("请把身份证或读者证放到读卡器上");
        }
        TbReader tbReader = new TbReader();
        IntByReference len = new IntByReference();
        byte[] idData = new byte[256];
        // 获取身份证号码
        Arrays.fill(idData, (byte) 0x00);
        st = epen.hc_ID_GetNumber(idData, len);
        if (st != 0) {
            throw  new CustomException(GetErrMsg(st));
        }
        String cardNo = new String(idData, 0, len.getValue(), "GBK");
        epen.dv_beep(icdev, (short) 30);
        return cardNo;
    }
    public int Icode2Tag(int icdev) {
        //读写器默认协议类型为ISO14443 TypeA 协议, 切换为 ICODE2
        short st = epen.rf_select_protocal(icdev, (byte)2);
        if (st != 0) {
            System.out.println(GetErrMsg(st));
            return st;
        }
//
        byte[] cardInfo = new byte[256];
        st = epen.rf_inventory(icdev, (byte) 1, (byte) 0, (byte) 1, cardInfo); //不匹配 afi 查询标签

        if (st != 0) {
            System.out.println(GetErrMsg(st));
            return st;
        }
//
        byte[] cardUid = new byte[8];
        System.arraycopy(cardInfo, 2, cardUid, 0, 8); //可以查询多张标签, 这里只保存第一张标签的uid, 读写时需要用到
//
//		byte[] wdata = new byte[12];
//		for (byte i = 0; i < wdata.length; i++) {
//			wdata[i] = i;
//		}
//		//写数据, 需要uid匹配, 从 0 块开始写, 连续写3个块的数据（写 0， 1， 2 块的数据）, 每个块为4个字节, 所以写入的数据长度为 12
//		st = epen.rf_write_mulblock(icdev, (byte) 0, (byte) 1, cardUid, (byte) 0, (byte) 3, wdata);
//		if (st != 0) {
//			System.out.println(GetErrMsg(st));
//			return st;
//		}

        //读数据, 需要uid匹配, 从 0 块开始读, 连续读3个块的数据（将上面写入的数据读取出来）, 不返回安全状态
        byte[] rdata = new byte[15];
        st = epen.rf_read_mulblock(icdev, (byte) 0, (byte) 1, (byte) 0, cardUid, (byte) 0, (byte) 3, rdata);
//		if (st != 0) {
//			System.out.println(GetErrMsg(st));
//			return st;
//		}

//		System.out.println(Utils.bytesToHexString(rdata, 1, 12));

        return 0;
    }

}
