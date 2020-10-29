package cn.eby.book.fuction.hcicrf;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.ptr.LongByReference;

/**
 * @Auther: 徐长乐
 * @Date: 2020/09/19/10:14
 * @Description:
 */
public interface HCICRFDll extends Library {


    HCICRFDll hcicrf = Native.load("dll\\hc_icrf32",HCICRFDll.class);

    int hc_init(short port, int buad);

    int hc_net_init(String ip, int m_iPort);

    short hc_exit(int icdev);

    short getErrMsg(short st, short lg, byte[] byMsg);

    short srd_ver(int icdev, short len, byte[] ver);

    short dv_beep(int icdev, short time);

    short rf_select_protocal(int icdev, byte protype);

    short lcd_display(int icdev, byte line, byte offset, byte[] disp_buf);

    //short rf_card(int icdev, short mode, byte[] Snr);
    short rf_card(int icdev, short mode, LongByReference Snr);

    short rf_authentication_key(int icdev, short mode, short block, byte[] key);

    short rf_read(int icdev, short addr, byte[] data);

    short rf_read_hex(int icdev, short addr, byte[] data);

    short rf_write(int icdev, short addr, byte[] data);

    short rf_write_hex(int icdev, short addr, byte[] data);

    short rf_changeb3(int icdev, short SecNr, byte[] KeyA, short _B0, short _B1, short _B2, short _B3, short _Bk,
                      byte[] _KeyB);

    short rf_pro_reset(int icdev, IntByReference len, byte[] _Data);

    short rf_pro_trn(int icdev, int slen, byte[] sbuff, IntByReference rlen, byte[] rbuff);

    short rf_pro_halt(int icdev);

    short hex_a(byte[] hex, byte[] a, int len);

    short a_hex(byte[] a, byte[] hex, int len);

    short hc_ID_Read(int icdev);

    short hc_ID_GetName(byte[] pName, IntByReference strLen);

    short hc_ID_GetSex(byte[] pSex, IntByReference strLen);

    short hc_ID_GetNation(byte[] pNation, IntByReference strLen);

    short hc_ID_GetBirthday(byte[] pBirthday, IntByReference strLen);

    short hc_ID_GetAddress(byte[] pAddress, IntByReference strLen);

    short hc_ID_GetNumber(byte[] pNumber, IntByReference strLen);

    short hc_ID_GetDepartment(byte[] pDepartment, IntByReference strLen);

    short hc_ID_GetExpiryDate(byte[] pExpiryDate, IntByReference strLen);

    short hc_ID_GetPhoto_Bmp(String path);

    short hc_ID_GetFingerprint(byte[] pFingerprint, IntByReference strLen);

    short hc_ID_ClearInfo();

    short hc_GetSAMID(int icdev, byte[] samId, IntByReference strLen);

    short chk_4442(int icdev);

    short get_status(int icdev, IntByReference statu);

    short srd_4442(int icdev, short offset, short len, byte[] data_buffer);

    short swr_4442(int icdev, short offset, short len, byte[] data_buffer);

    short prd_4442(int icdev, short len, byte[] data_buffer);

    short pwr_4442(int icdev, short offset, short len, byte[] data_buffer);

    short csc_4442(int icdev, short len, byte[] data_buffer);

    short wsc_4442(int icdev, short len, byte[] data_buffer);

    short rsc_4442(int icdev, short len, byte[] data_buffer);

    short rsct_4442(int icdev, IntByReference counter);
    /**
     * 接触CPU卡复位
     *
     * @param icdev
     * @param CardType 卡座编号. 0: 用户卡座; 1 ~ 4:SAM1 ~ SAM4
     * @param rlen
     * @param rbuffer
     * @return
     */
    short sam_slt_reset(int icdev, byte CardType, IntByReference rlen, byte[] rbuffer);

    /**
     * 接触CPU卡指令操作
     *
     * @param icdev
     * @param CardType 卡座编号. 0: 用户卡座; 1 ~ 4:SAM1 ~ SAM4
     * @param slen
     * @param sbuffer
     * @param rlen
     * @param rbuffer
     * @return
     */
    short sam_slt_protocol(int icdev, byte CardType, int slen, byte[] sbuffer, IntByReference rlen, byte[] rbuffer);

    /**
     * 设置接触CPU卡通信波特率
     *
     * @param icdev
     * @param CardType 卡座编号. 0: 用户卡座; 1 ~ 4:SAM1 ~ SAM4
     * @param BaudCode 通信波特率. 1: 9600; 2: 19200; 3: 38400
     * @return
     */
    short set_card_baud(int icdev, byte CardType, byte BaudCode);

    short rf_write_file(int icdev, short fileName, short dLen, byte[] data);

    short rf_read_file(int icdev, short fileName, short rlen, byte[] data);

    short rf_halt(int icdev);

    /******* ICODE 2 **************************************************************/
    short rf_config_mode(int icdev, byte cardtype, byte _Mode);

    short rf_inventory(int icdev, byte afi_flag, byte afi, byte slot_Flag, byte[] card_info);

    short rf_select_uid(int icdev, byte[] uid);

    short rf_reset_to_ready(int icdev, byte Select_Flag, byte Address_Flag, byte[] uid);

    short rf_stay_quiet(int icdev, byte[] uid);

    short rf_get_sysinfo(int icdev, byte Select_Flag, byte Address_Flag, byte[] uid, byte[] sysinfo);

    short rf_read_mulblock(int icdev, byte Select_Flag, byte Address_Flag, byte Option_Flag, byte[] uid,
                           byte startblock, byte blocknum, byte[] _Data);

    short rf_write_mulblock(int icdev, byte Select_Flag, byte Address_Flag, byte[] uid, byte startblock,
                            byte blocknum, byte[] _Data);

    short rf_lock_block(int icdev, byte Select_Flag, byte Address_Flag, byte[] uid, byte block);

    short rf_write_AFI(int icdev, byte Select_Flag, byte Address_Flag, byte[] uid, byte afi);

    short rf_write_DSFID(int icdev, byte Select_Flag, byte Address_Flag, byte[] uid, byte dsfid);

    short rf_lock_DSFID(int icdev, byte Select_Flag, byte Address_Flag, byte[] uid);

    short rf_get_mulblock_security(int icdev, byte Select_Flag, byte Address_Flag, byte[] uid, byte startblock,
                                   byte blocknum, byte[] security);

    short rf_lock_AFI(int icdev, byte Select_Flag, byte Address_Flag, byte[] uid);

    short rf_set_EAS(int icdev, byte mode);

    short rf_clear_EAS(int icdev, byte mode);

    short rf_lock_EAS(int icdev, byte mode);



}
