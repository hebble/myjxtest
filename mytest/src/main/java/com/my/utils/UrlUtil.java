package com.my.utils;

import org.apache.commons.lang3.StringUtils;

public class UrlUtil{
	/**
	 * 修正路径问题
	 */
	public static final String fixUrl(String url){
		if(url != null){
			while(url.contains("\\")){
				url = url.replaceAll("\\\\", "/");
			}
			url = org.apache.commons.lang3.StringUtils.replace(url, "\\", "/");
			if(url.startsWith("http://")){
				if(url.indexOf("//") != url.lastIndexOf("//")){
					url = url.substring(7);
					if(StringUtils.isNotEmpty(url)){
						boolean b = false;
						if(url.charAt(0) == '/'){
							b = true;
						}
						while(url.contains("//")){
							url = url.replaceAll("//", "/");
						}
						if(b){
							url = url.substring(1);
						}
					}
					url = "http://" + url;
				}
			}else{
				while(url.contains("//")){
					url = url.replaceAll("//", "/");
				}
			}
		}
		return url != null ? url : "";
	}

	public static void main(String[] args){
		System.out.println(fixUrl("http://img.uni0//20/.com/\\\\\\\\media\\homework\\A0187\\大切诺基(进口)\\1{400}.JPG"));
		System.out.println(fixUrl("http://////asdfasd12asdv5"));
		System.out.println(fixUrl("http://192.168.1.21/ak-base-wego/upload\\WegoUser\\201511\\2mSwloI4.png"));
	}
}
