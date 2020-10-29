package RFID;

/**
 * 工具类
 *
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

	public static byte hexToByte(String inHex){
		return (byte)Integer.parseInt(inHex,16);
	}
	public static byte[] bytesToHexByte(byte[] src, int start, int len) {
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
		return stringBuilder.toString().getBytes();
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


}
