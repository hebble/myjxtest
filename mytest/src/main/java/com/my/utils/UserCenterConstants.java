package com.my.utils;

/**
 * 
 * @ClassName:  UserConstants   
 * @Description: 用户常量类  
 * @author: Hebble 
 * @date:   2017年11月3日 上午9:34:01   
 *
 */
public class UserCenterConstants {
	
	/**
	 *  支付密码静态部分
	 */
	public static final String PAY_PASSWORD_STATIC_CODE = "9b076384-99e9-4013-823e-84493b634ddc";
	
	/**
	 *  登录密码静态部分
	 */
	public static final String LOGIN_PASSWORD_STATIC_CODE = "e67cd8f9-a24d-4fc0-8c2b-0c9250448e21";
	
	/**
	 * 用户token 密钥
	 */
	public static final String USER_TOKEN_KEY = "99e9-4013-XX-4fc0-8c2b";
	
	/**
	 * 失效时间设置为一个星期
	 * token 超时时间 24 * 60 * 60 * 1000 * 7  
	 */
	public static final long USER_TOKEN_TTL = 24 * 60 * 60 * 1000 * 7;
}
