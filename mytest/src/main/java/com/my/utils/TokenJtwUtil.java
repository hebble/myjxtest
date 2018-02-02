package com.my.utils;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
/**
 * token 成功工具
 */
@Slf4j
public class TokenJtwUtil {
	/**
	 * 创建toke
	 * @param subject 需要加密的字符串
	 * @param ttlMillis  设置超时时间 单位为秒
	 * @return token
	 */
	public static String createToken(String subject) {
		long ttlMillis = UserCenterConstants.USER_TOKEN_TTL;
	  //加密类型
	    SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
	 
	    long nowMillis = System.currentTimeMillis();
	    Date now = new Date(nowMillis);
	 
	    //加密key
	    byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(UserCenterConstants.USER_TOKEN_KEY);
	    Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());
	 
	    JwtBuilder builder = Jwts.builder().setId("JWT")
	                                .setIssuedAt(now)
	                                .setSubject(subject)
	                                .signWith(signatureAlgorithm, signingKey);
	    if (ttlMillis >= 0) {
	    long expMillis = nowMillis + ttlMillis;
	        Date exp = new Date(expMillis);
	        builder.setExpiration(exp);
	    }
	    return builder.compact();
	}
	
	/**
	 * 创建带有时间限制的密钥
	 * @param subject 需要加密的字符串
	 * @param ttlMillis  设置超时时间 单位为秒
	 * @return token
	 */
	public static String createJtwsPwd(String subject, long ttlMillis) {
		//加密类型
	    SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
	 
	    long nowMillis = System.currentTimeMillis();
	    Date now = new Date(nowMillis);
	 
	    //加密key
	    byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(UserCenterConstants.USER_TOKEN_KEY);
	    Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());
	 
	    JwtBuilder builder = Jwts.builder().setId("JWT")
	                                .setIssuedAt(now)
	                                .setSubject(subject)
	                                .signWith(signatureAlgorithm, signingKey);
	    if (ttlMillis >= 0) {
	    long expMillis = nowMillis + ttlMillis;
	        Date exp = new Date(expMillis);
	        builder.setExpiration(exp);
	    }
	    return builder.compact();
	}
	
	/**
	 * 解密
	 * @param token
	 * return
	 * code  0000:解密成功,1001:已过期,1002:token信息不合法
	 * msg
	 * subject 解密后的内容
	 */
	public static Map<String,String> parseToken(String token) {
		Map<String,String> rs = new HashMap<>();
		try {
			Claims claims = Jwts.parser()
				       .setSigningKey(DatatypeConverter.parseBase64Binary(UserCenterConstants.USER_TOKEN_KEY))
				       .parseClaimsJws(token).getBody();
//		    System.out.println("ID:" + claims.getId());
		    System.out.println("subject: " + claims.getSubject());
		    rs.put("code","0000");
		    rs.put("msg","解密成功");
		    rs.put("subject",claims.getSubject());
		} catch (ExpiredJwtException e) {
			rs.put("code","1001");
		    rs.put("msg","token 已过期");
		    log.error("token:{},已过期",token);
		} catch (MalformedJwtException e2) {
			rs.put("code","1002");
		    rs.put("msg","token信息不合法");
		    log.error("token:{},信息不合法",token);
		}
		return rs;
	}
	
	public static void main(String[] args) {
//		String token = TokenJtwUtil.createToken("20481f3d-a51f-4b52-8546-40aa9e6b284d");
		String token = TokenJtwUtil.createToken("12345");
		System.out.println(token);
		TokenJtwUtil.parseToken(token);
		String string = "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiJKV1QiLCJpYXQiOjE1MTM3NjAwODMsInN1YiI6IjEyMzQ1IiwiZXhwIjoxNTE0MzY0ODgzfQ.LPBIqaNimLtvMz99xgtvmlMpgS7eynRz5IgsiJ3XSfY";
		System.out.println(string.length());
	}
}
