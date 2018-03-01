package com.my.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.weixin4j.Configuration;
import org.weixin4j.OAuth2;
import org.weixin4j.Weixin;
import org.weixin4j.WeixinException;
import org.weixin4j.http.OAuth2Token;
import org.weixin4j.pay.SignUtil;
import org.weixin4j.pay.UnifiedOrder;
import org.weixin4j.pay.UnifiedOrderResult;
import org.weixin4j.util.TokenUtil;

import com.my.exception.BusinessException;
import com.my.utils.IdGenUtil;
import com.my.utils.IpUtil;
import com.my.utils.RandomUtil;
import com.my.utils.weixin4j.WeixinManager;

import lombok.extern.slf4j.Slf4j;

@RequestMapping("/api/wx")
@RestController
@Slf4j
public class WxController {
	
	@Autowired
	private WeixinManager weixinManager;

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
	public String qrScan(HttpServletRequest request, HttpServletResponse response) {
		String userAgent = request.getHeader("User-Agent");
		if (userAgent.toLowerCase().contains("micromessenger")) {
			log.info("用户使用了微信扫描");
			try {
				request.getRequestDispatcher("validateOAuth2").forward(request, response);
			} catch (Exception e) {
				log.error("请求转发失败,{}",e);
			}
		} else if (userAgent.toLowerCase().contains("alipayclient")) {
			log.info("用户使用了支付宝扫描");
		} else {
			return "请使用微信或支付宝扫描";
		}
		return null;
	}
	
	@RequestMapping("/validateOAuth2")
	public void validateOAuth2(HttpServletRequest request, HttpServletResponse response) {
		String code = request.getParameter("code");
		if (code == null) {
			log.info("-------code为空--------");
			// 获取code
			OAuth2 auth2 = new OAuth2();
//			String oAuth2CodeBaseUrl = auth2.getOAuth2CodeBaseUrl(Configuration.getOAuthAppId(), Configuration.getProperty("weixin4j.oauth.url")+"/api/wx/validateOAuth2");
			String oAuth2CodeBaseUrl = auth2.getOAuth2CodeUserInfoUrl(Configuration.getOAuthAppId(), Configuration.getProperty("weixin4j.oauth.url")+"/api/wx/validateOAuth2");
			log.info("oAuth2CodeBaseUrl="+oAuth2CodeBaseUrl);
			try {
				response.sendRedirect(oAuth2CodeBaseUrl);
			} catch (IOException e) {
				log.error("重定向获取code失败{}",e);
			}
		} else {
			 try {
				log.info("-------code不为空--------");
				//创建一个微信链接
				OAuth2 auth2 = new OAuth2();
				OAuth2Token oAuth2Token = auth2.login(Configuration.getOAuthAppId(), Configuration.getOAuthSecret(), code);
				System.out.println("获取到openid="+oAuth2Token.getOpenid());
				try {
					log.info("前往微信支付");
					response.sendRedirect("http://192.168.1.23:8384/api/wxPay/openid="+oAuth2Token.getOpenid());
				} catch (IOException e) {
					log.error("重定向失败{}",e);
				}
			} catch (WeixinException e) {
				log.error("获取用户信息出错{}",e);
			}
		}
	}
	
	@RequestMapping("/wxPay")
	public Map<String, String> wxPay(HttpServletRequest request, String openid) throws WeixinException {
		log.info("------------微信调用统一下单开始--------------");
		String appId = Configuration.getOAuthAppId();
		String nonceStr = RandomUtil.generateString(32);
		
		// 生成统一下单
		UnifiedOrder unifiedOrder = new UnifiedOrder();
		unifiedOrder.setAppid(appId); // 微信分配的公众账号ID（企业号corpid即为此appId）
		unifiedOrder.setMch_id(Configuration.getProperty("weixin4j.pay.partner.id")); // 微信支付分配的商户号
		unifiedOrder.setNonce_str(nonceStr); // 随机字符串，最大32位
		unifiedOrder.setBody("商品简单描述"); // 商品简单描述
		unifiedOrder.setOut_trade_no(IdGenUtil.genThirdRequestSeq(6)); // 商户系统内部的订单号,32个字符内、可包含字母
		unifiedOrder.setTotal_fee("120.34"); // 订单总金额，单位为分
		unifiedOrder.setSpbill_create_ip(IpUtil.getUserIp(request)); // APP和网页支付提交用户端ip
		unifiedOrder.setNotify_url(Configuration.getProperty("weixin4j.pay.notify_url")); // 异步接收微信支付结果通知的回调地址，通知url必须为外网可访问的url，不能携带参数
		unifiedOrder.setTrade_type("JSAPI"); //JSAPI 公众号支付	NATIVE 扫码支付	APP APP支付
		unifiedOrder.setOpenid(openid); // trade_type=JSAPI，此参数必传，用户在商户appid下的唯一标识
		String signStr = SignUtil.getSign(unifiedOrder.toMap(), Configuration.getProperty("weixin4j.pay.partner.key")); // 所有参数签名
		unifiedOrder.setSign(signStr);
		Weixin weixin = weixinManager.getWeixin();
		UnifiedOrderResult unifiedOrderResult = weixin.payUnifiedOrder(unifiedOrder);
		String prepay_id = unifiedOrderResult.getPrepay_id();
		log.info("获取到预支付交易会话标识prepay_id={}", prepay_id);
		if (unifiedOrderResult.isSuccess() && "SUCCESS".equals(unifiedOrderResult.getResult_code())) {
			// return_code 和result_code都为SUCCESS的时候有返回
			Map<String, String> finalpackage = new HashMap<String, String>();

			//返回拉起微信支付参数
			finalpackage.put("appId", appId);
			finalpackage.put("timeStamp", String.valueOf(System.currentTimeMillis() / 1000));
			finalpackage.put("nonceStr", nonceStr);
			finalpackage.put("package", "prepay_id=" + unifiedOrderResult.getPrepay_id()); //
			finalpackage.put("signType", "MD5");

			String finalsign = SignUtil.getSign(finalpackage, Configuration.getProperty("weixin4j.pay.partner.key"));
			finalpackage.put("paySign", finalsign);
			log.info("------------微信调用统一下单结束--------------");
			return finalpackage;
		} else {
			String result_code = unifiedOrderResult.getResult_code();
			String return_msg = unifiedOrderResult.getReturn_msg();
			String err_code = unifiedOrderResult.getErr_code();
			String err_code_des = unifiedOrderResult.getErr_code_des();
			log.warn("调用微信服务器统一下单出现错误,获取到微信服务器返回数据result_code={},return_msg={},err_code={},err_code_des={}",result_code,return_msg,err_code,err_code_des);
			log.info("------------微信调用统一下单结束--------------");
			throw new BusinessException("微信支付失败");
		}
		
	}
}
