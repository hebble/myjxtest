package com.my.utils;

import org.apache.commons.codec.digest.DigestUtils;

public class MD5Util{

	public static String toMD5(String text){
		return DigestUtils.md5Hex(text);
	}

	public static void main(String[] args){
		System.out.println(toMD5("xyl83336840")); // 4297f44b13955235245b2497399d7a93
		System.out.println(toMD5("gzza123"));
	}
}
