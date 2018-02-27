package com.my.utils;

import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;


/**
 * IP工具类
 */
public class IpUtil {
	private static List<InetAddress> localAddressList;
	
	public static String getLocalIP(){
		return getLocalIP(false);
	}
	public static String getLocalIP(boolean isInter){
		if(localAddressList == null){
			localAddressList = getLocalAddresses();
		}
		String localIP="";
		for(InetAddress ia:localAddressList){
			String ip = ia.getHostAddress();
			if(ia instanceof Inet6Address || ip.startsWith("127")) {
				continue;
			}
			if(StringUtils.isBlank(localIP)){
				localIP = ip;
			}
			if(isInter && ip.startsWith("19.")){
				return ip;
			} 
			if(!isInter && !ip.startsWith("19.")){
				return ip;
			}
		}
		return localIP;
	}	
	public static List<InetAddress> getLocalAddresses(){
		if(localAddressList == null){
			localAddressList = new ArrayList<InetAddress>();
			try {
				Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
				while (interfaces != null && interfaces.hasMoreElements()) {
					NetworkInterface interfaceN = interfaces.nextElement();
					Enumeration<InetAddress> ienum = interfaceN.getInetAddresses();
					while (ienum.hasMoreElements()) {
						InetAddress ia = ienum.nextElement();
						localAddressList.add(ia);
					}
				}
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
		return localAddressList;
	}
	

	/**
	 * 请求客户端用户的真实 IP 地址,直接获取之前一个的IP则用request.getRemoteAddr().....
	 * @param request
	 * @return
	 */
	public static String getUserIp(HttpServletRequest request){
		String ip = request.getHeader("X-Forwarded-For");
		if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("X-Real-IP");
		}
		if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Cdn-Src-Ip");
		}
		if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		if(ip.indexOf(",") > -1){
			ip = ip.substring(0,ip.indexOf(","));
		}
		return ip.equals("0:0:0:0:0:0:0:1")?getLocalIP():ip;
	}


	/**
	 * 把IP按点号分4段，每段一整型就一个字节来表示，通过左移位来实现。
	 * 第一段放到最高的8位，需要左移24位，依此类推即可
	 *
	 * @param ipStr ip地址
	 * @return 整形
	 */
	public static Integer ip2Num(String ipStr) {
		if (ipStr == null || "".equals(ipStr)) {
			return -1;
		}

		if (ipStr.contains(":")) {
			//ipv6的地址，不解析，返回127.0.0.1
			ipStr = "127.0.0.1";
		}

		String[] ips = ipStr.split("\\.");

		return (Integer.parseInt(ips[0]) << 24) + (Integer.parseInt(ips[1]) << 16) + (Integer.parseInt(ips[2]) << 8) + Integer.parseInt(ips[3]);
	}

	/**
	 * 把整数分为4个字节，通过右移位得到IP地址中4个点分段的值
	 *
	 * @param ipNum ip int value
	 * @return ip str
	 */
	public static String num2Ip(int ipNum) {
		return ((ipNum >> 24) & 0xFF) + "." + ((ipNum >> 16) & 0xFF) + "." + ((ipNum >> 8) & 0xFF) + "." + (ipNum & 0xFF);
	}
}
