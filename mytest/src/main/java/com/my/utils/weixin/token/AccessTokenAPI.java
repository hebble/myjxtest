package com.my.utils.weixin.token;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import com.my.component.cache.HessianRedisTemplate;
import com.my.utils.weixin.WX;

/**
 * 微信访问权限Token
 * @see http://mp.weixin.qq.com/wiki/11/0e4b294685f817b95cbed85ba5e82b8f.html
 */
@Component
public class AccessTokenAPI extends WX{
	private static final String WEIXIN = "weixin";

	public static final boolean useRedis = true;

//	private static HessianRedisTemplate hessianRedisTemplate = null;
	@Autowired
	private RedisTemplate<String, String> redisTemplate;

//	static{
//		hessianRedisTemplate = SpringUtil.getBean(HessianRedisTemplate.class, "hessianRedisTemplate");
//	}

	/**
	 * 获取访问公众号token
	 * @param context
	 * @return
	 */
//	public static final String get(String context){
//		WxAppAccount account = WXData.get(context);
//		return get(account.getAppid(), account.getSecret());
//	}

	/**
	 * 取公众号访问token
	 */
	public final String get(String appid, String secret){
		String access_token = null;
		try{
			if(StringUtils.isNotEmpty(appid) && StringUtils.isNotEmpty(secret)){
				if(useRedis){
					access_token = redisTemplate.opsForValue().get(WEIXIN + "_" + appid) == null ? null : redisTemplate.opsForValue().get(WEIXIN + "_" + appid).toString();
				}
				if(StringUtils.isEmpty(access_token)){
					String url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential" + "&appid=" + appid + "&secret=" + secret;
					Map map = httpUrlToMap(url);
					access_token = (String)map.get("access_token");
					if(StringUtils.isNotEmpty(access_token)){
						int expires_in = Integer.parseInt(map.get("expires_in").toString()); //凭证有效时间，单位：秒
						if(useRedis){
							redisTemplate.opsForValue().set(WEIXIN + "_" + appid, access_token, expires_in - 200, TimeUnit.SECONDS);
						}
					}
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return access_token;
	}
}
