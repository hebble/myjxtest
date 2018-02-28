package com.my.controller;

import java.io.StringReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.weixin4j.Configuration;
import org.weixin4j.pay.PayNotifyResult;
import org.weixin4j.pay.PayUtil;
import org.weixin4j.util.XStreamFactory;

import com.alipay.api.internal.util.AlipaySignature;
import com.my.config.pay.ali.AlipayConfig;
import com.my.exception.BusinessException;
import com.my.utils.weixin4j.util.XMLBeanUtils;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequestMapping("/notify")
public class PayNotifyController {
	
	@Autowired
	private AlipayConfig alipayConfig;
	
	/**
	 * 微信支付回调
	 * @param request
	 * @param response
	 */
	@RequestMapping("/pay/wxPayNotify")
	public void wxPayNotify(HttpServletRequest request, HttpServletResponse response) {
		log.info("------------接收微信支付回调通知开始-----------");
		Map<String, String> returnMap = new HashMap<>();
		try {
			//商户密钥
			String paternerKey = Configuration.getProperty("weixin4j.pay.partner.key");
			ServletInputStream inputStream = request.getInputStream();
			// 将刘装换为字符串
			String xmlMsg = XStreamFactory.inputStream2String(inputStream);
			JAXBContext context = JAXBContext.newInstance(PayNotifyResult.class);
			Unmarshaller unmarshaller = context.createUnmarshaller();
			//结果
			PayNotifyResult payNotifyResult = (PayNotifyResult) unmarshaller.unmarshal(new StringReader(xmlMsg));
			String return_code = payNotifyResult.getReturn_code();
			String return_msg = payNotifyResult.getReturn_msg();
			log.info("接受到微信服务器支付回调参数return_code:{}, return_msg:{}", return_code, return_msg);
			
			// 判断签名和结果
			if ("SUCCESS".equals(return_code)) {
				//验证签名
				boolean verifySign = PayUtil.verifySign(xmlMsg, paternerKey);
				if (verifySign) {
					String out_trade_no = payNotifyResult.getOut_trade_no();
					log.info("获取到商户订单号为:{}", out_trade_no);
					//------------------------------
			        //即时到账处理业务开始
			        //------------------------------
			        //根据id查询支付订单信息
			        //商户内部处理订单交易状态业务逻辑 start
			        //商户内部代码
			        //商户内部处理订单交易状态业务逻辑 end
			        //注意交易单不要重复处理
			        //注意判断返回金额
			        //------------------------------
			        //即时到账处理业务完毕
			        //------------------------------
			        //给财付通系统发送成功信息，财付通系统收到此结果后不再进行后续通知
					log.info("收到微信支付回调 成功");
					returnMap.put("return_code", "SUCCESS");
					returnMap.put("return_msg", "OK");
			        response.getWriter().write(XMLBeanUtils.map2XmlString(returnMap));
				} else {
					log.warn("收到微信支付回调 签名失败");
					returnMap.put("return_code", "FAIL");
					returnMap.put("return_msg", "签名失败");
			        response.getWriter().write(XMLBeanUtils.map2XmlString(returnMap));
				}
			} else {
				// 支付失败
				log.warn("收到微信服务器支付回调失败  支付失败!");
				returnMap.put("return_code", "SUCCESS");
				returnMap.put("return_msg", "OK");
			    response.getWriter().write(XMLBeanUtils.map2XmlString(returnMap));
			}
		} catch (Exception e) {
			log.error("处理微信服务器支付回调发生系统异常,{}", e);
			throw new BusinessException("微信支付失败");
		}
        
		log.info("------------接收微信支付回调通知结束-----------");
	}
	
	/**
	 * 支付宝支付回调
	 * @param request
	 * @throws Exception 
	 */
	@RequestMapping("/pay/aliPayNotify")
	public String aliPayNotify(HttpServletRequest request, HttpServletResponse out) {
		log.info("--------------------接受支付宝支付回调开始--------------------");
		//获取支付宝POST过来反馈信息
		Map<String,String> params = new HashMap<String,String>();
		Map requestParams = request.getParameterMap();
		for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext();) {
			String name = (String) iter.next();
			String[] values = (String[]) requestParams.get(name);
			String valueStr = "";
			for (int i = 0; i < values.length; i++) {
				valueStr = (i == values.length - 1) ? valueStr + values[i]
						: valueStr + values[i] + ",";
			}
			//乱码解决，这段代码在出现乱码时使用。如果mysign和sign不相等也可以使用这段代码转化
			//valueStr = new String(valueStr.getBytes("ISO-8859-1"), "gbk");
			params.put(name, valueStr);
		}
		log.info("获取到支付回调参数为params={}", params);
		//获取支付宝的通知返回参数，可参考技术文档中页面跳转同步通知参数列表(以下仅供参考)//
		//商户订单号

		try {
			String out_trade_no = new String(request.getParameter("out_trade_no").getBytes("ISO-8859-1"),"UTF-8");
			//支付宝交易号

			String trade_no = new String(request.getParameter("trade_no").getBytes("ISO-8859-1"),"UTF-8");

			//交易状态
			String trade_status = new String(request.getParameter("trade_status").getBytes("ISO-8859-1"),"UTF-8");

			//获取支付宝的通知返回参数，可参考技术文档中页面跳转同步通知参数列表(以上仅供参考)//
			//计算得出通知验证结果
			//boolean AlipaySignature.rsaCheckV1(Map<String, String> params, String publicKey, String charset, String sign_type)
			boolean verify_result = AlipaySignature.rsaCheckV1(params, alipayConfig.getAlipay_public_key(), AlipayConfig.CHARSET, "RSA2");
			
			if(verify_result){//验证成功
				//////////////////////////////////////////////////////////////////////////////////////////
				//请在这里加上商户的业务逻辑程序代码

				//——请根据您的业务逻辑来编写程序（以下代码仅作参考）——
				
				if(trade_status.equals("TRADE_FINISHED")){
					//判断该笔订单是否在商户网站中已经做过处理
						//如果没有做过处理，根据订单号（out_trade_no）在商户网站的订单系统中查到该笔订单的详细，并执行商户的业务程序
						//请务必判断请求时的total_fee、seller_id与通知时获取的total_fee、seller_id为一致的
						//如果有做过处理，不执行商户的业务程序
						
					//注意：
					//如果签约的是可退款协议，退款日期超过可退款期限后（如三个月可退款），支付宝系统发送该交易状态通知
					//如果没有签约可退款协议，那么付款完成后，支付宝系统发送该交易状态通知。
				} else if (trade_status.equals("TRADE_SUCCESS")){
					//判断该笔订单是否在商户网站中已经做过处理
						//如果没有做过处理，根据订单号（out_trade_no）在商户网站的订单系统中查到该笔订单的详细，并执行商户的业务程序
						//请务必判断请求时的total_fee、seller_id与通知时获取的total_fee、seller_id为一致的
						//如果有做过处理，不执行商户的业务程序
						
					//注意：
					//如果签约的是可退款协议，那么付款完成后，支付宝系统发送该交易状态通知。
				}

				//——请根据您的业务逻辑来编写程序（以上代码仅作参考）——
//			out.clear();
//			out.println("success");	//请不要修改或删除
				log.info("--------------------接受支付宝支付回调结束--------------------");
				return "success";

				//////////////////////////////////////////////////////////////////////////////////////////
			}else{//验证失败
//			out.println("fail");
				return "fail";
			}
		} catch (Exception e) {
			log.error("微信支付回调发生系统异常{}", e);
			throw new BusinessException("支付宝支付失败!");
		}
	}
}
