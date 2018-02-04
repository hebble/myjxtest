package com.my.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.weixin4j.Weixin;
import org.weixin4j.util.TokenUtil;

import lombok.extern.slf4j.Slf4j;

@RequestMapping("/api/wx")
@RestController
@Slf4j
public class WxController {

	@RequestMapping("/messageHandler")
	public String messageHandler(HttpServletRequest request) {
		if (request.getMethod().toLowerCase().equals("get")) {
			String signature = request.getParameter("signature");// 微信加密签名
			String timestamp = request.getParameter("timestamp");// 时间戳
			String nonce = request.getParameter("nonce"); // 随机数
			String echostr = request.getParameter("echostr"); //随机字符串
			///api/vzhanqun
			String path = request.getServletPath();
			//weixin4jvzhanqun, TokenUtil.get()为获取配置文件weixin4j.token
			// token和网页上配置的一致
			String token = TokenUtil.get();
			//确认此次GET请求来自微信服务器，原样返回echostr参数内容，则接入生效，成为开发者成功，否则接入失败
			if (TokenUtil.checkSignature(token, signature, timestamp, nonce)) {
				return echostr;
			}
			return null;
		} else {
			log.info("消息处理");
		}
		return null;
	}
	
	@RequestMapping("/qrScan")
	public String qrScan() {
		return null;
	}
}
