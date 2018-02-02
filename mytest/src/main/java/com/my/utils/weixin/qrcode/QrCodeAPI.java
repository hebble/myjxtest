package com.my.utils.weixin.qrcode;

import org.apache.commons.lang3.StringUtils;

import com.my.utils.weixin.WX;
import com.my.utils.weixin.WeixinHelper;

/**
 * 微信二维码推广
 * @see http://mp.weixin.qq.com/wiki/18/28fc21e7ed87bec960651f0ce873ef8a.html
 */
public class QrCodeAPI extends WX{

	/**
	 * 创建二维码
	 * @param sceneId 场景ID，不同场景生成不同二维码
	 */
	public static final QrCodeCreate create(String appid, String secret, Long sceneId){
		String access_token = WeixinHelper.accessToken.get(appid, secret);
		return create(access_token, sceneId);
	}
	public static final QrCodeCreate create(String appid, String secret, String sceneStr){
		String access_token = WeixinHelper.accessToken.get(appid, secret);
		return create(access_token, sceneStr);
	}

	/**
	 * 创建二维码
	 * @param sceneId 场景ID，不同场景生成不同二维码
	 */
	public static final QrCodeCreate create(String access_token, Long sceneId){
		QrCodeCreate qrCodeCreate = null;
		try{
			if(StringUtils.isNotEmpty(access_token)){
				String url = "https://api.weixin.qq.com/cgi-bin/qrcode/create?access_token=" + access_token;
				//QR_LIMIT_SCENE 永久二维码，QR_SCENE 临时二维码（最久保存7天）
				String body = "{\"action_name\": \"QR_LIMIT_SCENE\", \"action_info\": {\"scene\": {\"scene_id\": " + sceneId + "}}}";
				qrCodeCreate = httpsUrlToObject(url, body, QrCodeCreate.class);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return qrCodeCreate;
	}
	public static final QrCodeCreate create(String access_token, String sceneStr){
		QrCodeCreate qrCodeCreate = null;
		try{
			if(StringUtils.isNotEmpty(access_token)){
				String url = "https://api.weixin.qq.com/cgi-bin/qrcode/create?access_token=" + access_token;
				//QR_LIMIT_SCENE 永久二维码，QR_SCENE 临时二维码（最久保存7天）
				String body = "{\"action_name\": \"QR_LIMIT_STR_SCENE\", \"action_info\": {\"scene\": {\"scene_str\": \"" + sceneStr + "\"}}}";
				qrCodeCreate = httpsUrlToObject(url, body, QrCodeCreate.class);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return qrCodeCreate;
	}

	public static final String qrCodeUrl(String ticket){
		return "https://mp.weixin.qq.com/cgi-bin/showqrcode?ticket=" + ticket;
	}
}
