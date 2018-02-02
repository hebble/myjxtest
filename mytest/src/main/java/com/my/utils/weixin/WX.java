package com.my.utils.weixin;

import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import com.aliyun.openservices.shade.com.alibaba.rocketmq.shade.com.alibaba.fastjson.JSON;
import com.my.utils.HttpUtil;

public class WX{
	public static Map httpUrlToMap(String url){
		try{
			String json = HttpUtil.invokeHttp(url);
			if(StringUtils.isNotEmpty(json)){
				return JSON.parseObject(json, Map.class);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}

	public static <T> T httpUrlToObject(String url, Class<T> clazz){
		try{
			String result = HttpUtil.invokeHttp(url);
			if(isValidResult(result)){
				return JSON.parseObject(result, clazz);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}

	public static <T> T httpsUrlToObject(String url, Class<T> clazz){
		return httpsUrlToObject(url, null, clazz);
	}

	public static <T> T httpsUrlToObject(String url, String body, Class<T> clazz){
		try{
			String result = HttpUtil.invokeHttps(url, body);
			if(isValidResult(result)){
				return JSON.parseObject(result, clazz);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}

	public static boolean httpsUrlToBoolean(String url){
		return httpsUrlToBoolean(url, null);
	}

	public static boolean httpsUrlToBoolean(String url, String body){
		try{
			String result = HttpUtil.invokeHttps(url, body);
			return isValidResult(result);
		}catch(Exception e){
			e.printStackTrace();
		}
		return false;
	}

	public static boolean isValidResult(String result){
		boolean ok = false;
		if(StringUtils.isNotEmpty(result)){
			if(result.contains("\"errcode\"")){
				Map map = JSON.parseObject(result);
				int errcode = (int)map.get("errcode");
				ok = (errcode == 0);
			}else{
				ok = true;
			}
		}
		return ok;
	}

}
