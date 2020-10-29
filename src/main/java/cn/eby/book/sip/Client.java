package cn.eby.book.sip;


import cn.eby.book.common.constant.SIP2Instruct;
import cn.hutool.core.date.DateUtil;

import java.io.*;
import java.net.Socket;

/**
 * 
 * 客户端socket
 * 
 * @author Lawrence_Yi
 *
 */

public class Client {

	private Socket socket;
	private PrintWriter pw;
	private BufferedReader br;
	private String url;
	private int port;
	/**
	 * 初始化时进行socket连接
	 */
	public Client() {
		this.url = "36.99.35.218";
		this.port = 60001;
		try {
			socket = new Socket(url, port);
			pw = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), "gbk"));
			br = new BufferedReader(new InputStreamReader(socket.getInputStream(), "gbk"));
			String send = this.send("9300CNilascs|CO123|CP2|");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 监听消息
	 */
	public String receive() {
		String info = "";
		try
		{
			info = br.readLine();
		} catch (IOException e)
		{
			System.out.println("读取出错");
			reconnect();
		}
		return info;
	}

	/**
	 * 发送指令
	 *
	 * @param command
	 */
	public String  send(String command) {
		pw.write(command + "\r");
		pw.flush();
		return this.receive();
	}

    public String getNewDate(){
		return DateUtil.format(DateUtil.date(), "yyyyMMdd    HHmmss");
	}


	/**
	 * 关闭socket连接
	 */
	public void closeSocket() {
		this.send(SIP2Instruct.CLOSE+getNewDate());
		try {
			if (pw != null)
				pw.close();
			if (br != null)
				br.close();
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	
	
	
	
	
	
	/**
	 * 重新连接socket
	 */
	public void reconnect() {
		closeSocket();
		try {
			System.out.println("reconnecting...");
			socket = new Socket(url, port);
			pw = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), "gbk"));
			br = new BufferedReader(new InputStreamReader(socket.getInputStream(), "gbk"));
			System.out.println("connectted!");
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	

}
