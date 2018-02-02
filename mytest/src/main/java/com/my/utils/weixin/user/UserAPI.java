package com.my.utils.weixin.user;

import java.net.URLEncoder;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSON;
import com.my.utils.HttpUtil;
import com.my.utils.RequestUtil;

import lombok.extern.log4j.Log4j;

/**
 * 微信用户相关api
 * @author tz
 */
@Log4j
public class UserAPI{
	/**
	 * 用户列表
	 * @param access_token
	 * @param next_openid 第一个拉取的OPENID，不填默认从头开始拉取
	 * @return
	 */
	public static final UserList get(String access_token, String next_openid){
		UserList getResult = null;
		if(StringUtils.isNotEmpty(access_token)){
			String url = "https://api.weixin.qq.com/cgi-bin/user/get?access_token=" + access_token + "&next_openid=";
			String getJson = HttpUtil.invokeHttps(url);
			getResult = JSON.parseObject(getJson, UserList.class);
		}
		return getResult;
	}

	/**
	 * 获取微信用户信息
	 * @param access_token
	 * @param openId
	 * @param lang
	 * @return
	 */
	public static final UserInfo info(String accessToken, String openId){
		UserInfo userInfo = null;
		if(StringUtils.isNotBlank(accessToken)){
			String url = " https://api.weixin.qq.com/cgi-bin/user/info?access_token=" + accessToken + "&openid=" + openId;
			String json = HttpUtil.invokeHttps(url);
			userInfo = JSON.parseObject(json, UserInfo.class);
		}
		return userInfo;
	}

	/**
	 * app 登录url
	 * @param context
	 * @param referrer
	 * @return
	 */
	public final String getAppLoginUrl(String context, String referrer){
		return getAppLoginUrl(context, referrer, "");
	}

	public final String getAppLoginUrl(String context, String referrer, String callbackAction){
		return String.format("weixinAppLogin?context=%s&referrer=%s&callbackAction=%s", new Object[]{context, URLEncoder.encode(referrer), callbackAction});
	}
	
	/**
	 * 静默授权地址
	 * @param appID
	 * @param callbackAction
	 * @return
	 */
	public final String getAppSilentAuthUrl(String appID, String callbackAction){
		return getAppSilentAuthUrl(appID, callbackAction, null);
	}

	/**
	 * 授权地址
	 * @param appID
	 * @param callbackAction
	 * @param data
	 * @return
	 */
	private final String getAppSilentAuthUrl(String appID, String callbackAction, String data){
		StringBuffer stringBuffer = new StringBuffer("https://open.weixin.qq.com/connect/oauth2/authorize");
		stringBuffer.append("?appid=").append(appID);
		stringBuffer.append("&redirect_uri=").append(callbackAction);
		stringBuffer.append("&response_type=code");
		stringBuffer.append("&scope=snsapi_base");
		String state = (StringUtils.isNotEmpty(appID) ? appID : "") + ":" + (StringUtils.isNotEmpty(data) ? data : "");
		if(state.length() > 128){
			throw new RuntimeException("state参数过长：" + data + " state:" + state);
		}
		stringBuffer.append("&state=" + state);
		stringBuffer.append("#wechat_redirect");

		return stringBuffer.toString();
	}

	/**
	 * 静默授权成功
	 * @return
	 */
	public final SilentAuth getAppSilentAuthResult(){
		String string = RequestUtil.getRequest().getParameter("code");
		String string3 = RequestUtil.getRequest().getParameter("state");
		if(StringUtils.isNotEmpty(string)){
			String context = string3.substring(0, string3.indexOf(":"));
			String data = string3.substring(string3.indexOf(":") + 1);
			log.info("微信静默授权返回的 code: " + string + " context: " + context + " data:" + data);
			StringBuilder stringBuilder = new StringBuilder("https://api.weixin.qq.com/sns/oauth2/access_token?");
			stringBuilder.append("appid=").append(context);
			stringBuilder.append("&secret=").append("d748b9726a57f10234217ab6fb405841");
			stringBuilder.append("&code=").append(string);
			stringBuilder.append("&refresh_token=null");
			stringBuilder.append("&grant_type=authorization_code");

			String result = HttpUtil.invokeHttp(stringBuilder.toString());
			log.info(result);
			Map map = JSON.parseObject(result, Map.class);
			String openid = map != null ? (String)map.get("openid") : null;
			log.info("微信静默授权得到openid:" + openid);
			return new SilentAuth(openid, context, data);
		}
		return new SilentAuth();
	}
}
