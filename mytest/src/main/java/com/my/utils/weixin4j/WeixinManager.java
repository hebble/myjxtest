package com.my.utils.weixin4j;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.weixin4j.Configuration;
import org.weixin4j.Weixin;
import org.weixin4j.WeixinException;
import org.weixin4j.http.OAuthToken;

import com.my.utils.AppUtils;
import com.my.utils.weixin4j.util.Weixin4jConstant;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class WeixinManager {
	
	private static Weixin weixin = null;  
    private static String accessToken = null;  
    
	@Autowired
	private RedisTemplate<String, String> redisTemplate;
      
    public Weixin getWeixin() throws WeixinException {  
        //获取配置  
        String appId = Configuration.getOAuthAppId();  
        String secret = Configuration.getOAuthSecret();  
        if (AppUtils.isOneBlank(weixin,accessToken)) { 
        	log.info("-----------start-----------第一次运行，初始化Weixin对象-----------");
            //1.初始化Weixin对象  
            weixin = new Weixin();  
            //先从数据库查询，是否有未过期的access_token  
            OAuthToken oauthToken = getOAuthToken(appId);  
            //判断，如果为null，则说明已过期，需要重新登录获取  
            if (oauthToken == null) {  
            	log.info("从redis中未读取到Token或已过期!准备访问微信服务器，进行access_token获取!");
                //2.第一次初始化，需要进行登录  
                oauthToken = weixin.login(appId, secret);  
                //重置  
                accessToken = oauthToken.getAccess_token();  
                //3.保存Token  
                saveOAuthToken(appId, oauthToken.getAccess_token(), oauthToken.getExpires_in());  
            } else {  
                log.info("从redis中获取到accessToken,直接初始化");
                //2.初始化  
                weixin.init(oauthToken.getAccess_token(), appId, secret, oauthToken.getExpires_in());  
            }  
        } else {  
            log.info("已存在Weixin对象，准备验证Token是否过期！");
            //2.已经初始化，直接登录，如果未过期，默认不重新获取，如果重新获取则会改变accessToken，需要重新保存到数据库  
            OAuthToken oauthToken = weixin.login(appId, secret);  
            //如果不相等则说明重新获取过，需要保存到数据库  
            if (!oauthToken.getAccess_token().equals(accessToken)) {  
                log.info("Weixin对象Token已过期，已重新获取access_token,准备保存到数据库！");
                //重置  
                accessToken = oauthToken.getAccess_token();  
                //3.保存Token  
                saveOAuthToken(appId,oauthToken.getAccess_token(), oauthToken.getExpires_in());  
            } 
        }  
        return weixin;  
    }  
  
    /**
     * 获取Accesstoken  
     * @param appId
     * @return
     */
    private OAuthToken getOAuthToken(String appId) {  
        OAuthToken token = null;  
        // 从redis中获取
        if (appId == null) {
			return null;
		}
        String accessToken = redisTemplate.opsForValue().get(Weixin4jConstant.WEIXIN4J + "_" + appId);
        Long expire = redisTemplate.getExpire(Weixin4jConstant.WEIXIN4J + "_" + appId,TimeUnit.SECONDS);
        if (accessToken != null && expire != null) {
        	token = new OAuthToken();
			token.setAccess_token(accessToken);
			token.setExpires_in(expire.intValue());
			return token;
		}
        return null;  
    }  
  
    /**
     * 保存Accesstoken  
     * @param appId
     * @param accessToken
     * @param expiresIn
     */
    private void saveOAuthToken(String appId, String accessToken, int expiresIn) { 
    	if (AppUtils.isOneBlank(appId,accessToken,expiresIn)) {
			return;
		}
    	redisTemplate.opsForValue().set(Weixin4jConstant.WEIXIN4J+"_"+appId, accessToken, expiresIn, TimeUnit.SECONDS);
    }  

}
