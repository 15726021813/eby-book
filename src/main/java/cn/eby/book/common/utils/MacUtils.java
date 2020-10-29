package cn.eby.book.common.utils;


import java.net.InetAddress;
import java.net.NetworkInterface;


public class MacUtils {

    public static void main(String[] args) {
        String aaa = getLocalMac();
        System.out.println("本地MAC地址为： " + aaa);
    }

    public static String getLocalMac() {
        try {
            InetAddress ia = InetAddress.getLocalHost();
            // 获取网卡，获取地址
            byte[] mac = NetworkInterface.getByInetAddress(ia).getHardwareAddress();
            StringBuffer sb = new StringBuffer("");
            for (int i = 0; i < mac.length; i++) {
                if (i != 0) {
                    sb.append("-");
                }
                // 字节转换为整数
                int temp = mac[i] & 0xff;
                String str = Integer.toHexString(temp);
                if (str.length() == 1) {
                    sb.append("0" + str);
                } else {
                    sb.append(str);
                }
            }
            String localMAC = sb.toString().toUpperCase();
            return localMAC;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}