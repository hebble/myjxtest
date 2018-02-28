package com.my.controller;

import javax.sound.midi.MidiDevice.Info;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradeWapPayModel;
import com.alipay.api.request.AlipayTradeWapPayRequest;
import com.my.config.pay.ali.AlipayConfig;
import com.my.exception.BusinessException;
import com.my.utils.IdGenUtil;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/ali")
@Slf4j
public class AliController {
	
	@Autowired
	private AlipayConfig alipayConfig;

	@RequestMapping("/aliPay")
	public String aliPay() {
		log.info("--------------支付宝WAP支付下单开始---------------");
		
		 /**********************/
	    // SDK 公共请求类，包含公共请求参数，以及封装了签名与验签，开发者无需关注签名与验签     
	    //调用RSA签名方式
	    AlipayClient client = new DefaultAlipayClient(alipayConfig.getUrl(), alipayConfig.getApp_id(), alipayConfig.getRsa_private_key(), alipayConfig.FORMAT, alipayConfig.CHARSET, alipayConfig.getAlipay_public_key(),alipayConfig.SIGNTYPE);
	    AlipayTradeWapPayRequest alipay_request=new AlipayTradeWapPayRequest();
	    
	    // 封装请求支付信息
	    AlipayTradeWapPayModel model=new AlipayTradeWapPayModel();
	    model.setOutTradeNo(IdGenUtil.genThirdRequestSeq(6));
	    model.setSubject("商品的标题/交易标题/订单标题/订单关键字等");
	    model.setTotalAmount("120.23"); //订单总金额，单位为元，精确到小数点后两位
	    model.setBody("对一笔交易的具体描述信息"); // 可选
//	    model.setTimeoutExpress(""); //该笔订单允许的最晚付款时间，逾期将关闭交易(可选)
	    model.setProductCode("QUICK_WAP_PAY");
	    alipay_request.setBizModel(model);
	    // 设置异步通知地址
	    alipay_request.setNotifyUrl(alipayConfig.getNotify_url());
	    // 设置同步地址
	    alipay_request.setReturnUrl(alipayConfig.getReturn_url()); 
	    
	    // form表单生产
	    String form = "";
		try {
			// 调用SDK生成表单
			form = client.pageExecute(alipay_request).getBody();
			log.info("获取到支付宝返回的数据form={}",form);
		} catch (AlipayApiException e) {
			log.error("调用支付宝支付失败{}", e);
			throw new BusinessException("支付宝支付失败!");
		}
		//============================================
		// 处理下单逻辑
		//============================================
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("payUrl", form);
		jsonObject.put("payOrderNo", model.getOutTradeNo());
		log.info("--------------支付宝WAP支付下单结束---------------");
		return jsonObject.toJSONString();
	}
}
