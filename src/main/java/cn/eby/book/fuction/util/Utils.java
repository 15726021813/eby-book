package cn.eby.book.fuction.util;

import java.io.UnsupportedEncodingException;

/**
 * @Auther: 徐长乐
 * @Date: 2020/09/19/14:14
 * @Description:
 */
public class Utils {

    /**
     * 将字节数组转换为16进制字符串
     *
     * @param src
     *            要转换的数据
     * @param start
     *            起始地址
     * @param len
     *            转换的长度
     * @return 转换后的16进制字符串
     */
    public static String bytesToHexString(byte[] src, int start, int len) {
        StringBuilder stringBuilder = new StringBuilder("");
        if (src == null || len <= 0) {
            return null;
        }
        for (int i = start; i < start + len; i++) {
            int v = src[i] & 0xFF;
            String hv = Integer.toHexString(v).toUpperCase();
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }


    private static final char[] HEX_CHARS = {'0','1','2','3','4','5','6','7','8','9','a','b','c','d','e','f'};

    /*
     * byte[]数组转十六进制
     */
    public static String bytes2hexStr(byte[] bytes) {
        int len = bytes.length;
        if (len==0) {
            return null;
        }
        char[] cbuf = new char[len*2];
        for (int i=0; i<len; i++) {
            int x = i*2;
            cbuf[x]     = HEX_CHARS[(bytes[i] >>> 4) & 0xf];
            cbuf[x+1]    = HEX_CHARS[bytes[i] & 0xf];
        }
        return new String(cbuf);
    }


    public static String bytetoString(byte[] bytes) throws UnsupportedEncodingException {
        int  begain = 0;
        int  tail = 0;
        for (int i = 0; i < bytes.length; i++) {
            if (bytes[i]!=0){
                begain = i;
                break;
            }
        }
        for ( int i = begain; i < bytes.length; i++) {
            if (bytes[i]==0){
                tail = i;
                break;
            }
        }

        return new String(bytes, begain, tail-begain, "UTF-8");
    }

    /**
     * 将16进制字符串转换为字节数组
     *
     * @param hexString
     *            要转换的16进制字符串，长度必须为偶数
     * @return byte[] 转换后的数据
     */
    public static byte[] hexStringToBytes(String hexString) {
        if (hexString == null || hexString.equals("")) {
            return null;
        }
        hexString = hexString.toUpperCase();
        int length = hexString.length() / 2;
        char[] hexChars = hexString.toCharArray();
        byte[] d = new byte[length];
        for (int i = 0; i < length; i++) {
            int pos = i * 2;
            d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
        }
        return d;
    }


    public static String ByteToString(byte[] by)
    {
        String str="";
        char ch='\0';
        for(int i=0;by[i]!='\0';i++)
        {
            ch=(char)by[i];
            str+=ch;
        }
        return str;
    }

    public static String bytesToHexString(byte[] src){
        StringBuilder stringBuilder = new StringBuilder("");
        if (src == null || src.length <= 0) {
            return null;
        }
        for (int i = 0; i < src.length; i++) {
            int v = src[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }

    public static String str2HexStr(String str) {
        char[] chars = "0123456789ABCDEF".toCharArray();
        StringBuilder sb = new StringBuilder("");
        byte[] bs = str.getBytes();
        int bit;
        for (int i = 0; i < bs.length; i++) {
            bit = (bs[i] & 0x0f0) >> 4;
            sb.append(chars[bit]);
            bit = bs[i] & 0x0f;
            sb.append(chars[bit]);
            // sb.append(' ');
        }
        return sb.toString().trim();
    }

    /**
     * Convert char to byte
     *
     * @param c
     *            char
     * @return byte
     */
    private static byte charToByte(char c) {
        return (byte) "0123456789ABCDEF".indexOf(c);
    }

    public static byte[] intToByteArray(int it, int byteLen) {
        byte[] result = new byte[byteLen];
        for (int i = 0; i < byteLen; i++) {
            result[byteLen - i - 1] = (byte) ((it >> 8 * i) & 0xff);
        }
        return result;
    }

    public static byte hexToByte(String inHex){
        return (byte)Integer.parseInt(inHex,16);
    }

    public static boolean EqualsArray(byte[] data1, int data1Start, byte[] data2, int data2Start, int len) {
        if (data1 == null || data2 == null) {
            return false;
        }
        if (data1.length < data1Start + len || data2.length < data2Start + len) {
            return false;
        }
        for (int bt = 0; bt < len; bt++) {
            if (data1[data1Start + bt] != data2[data2Start + bt]) {
                return false;
            }
        }
        return true;
    }
    public static String hexStr2Str(String hexStr) {
        String str = "0123456789ABCDEF";
        char[] hexs = hexStr.toCharArray();
        byte[] bytes = new byte[hexStr.length() / 2];
        int n;
        for (int i = 0; i < bytes.length; i++) {
            n = str.indexOf(hexs[2 * i]) * 16;
            n += str.indexOf(hexs[2 * i + 1]);
            bytes[i] = (byte) (n & 0xff);
        }
        return new String(bytes);
    }
}
