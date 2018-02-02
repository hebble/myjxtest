package com.my.mytest;

import java.math.BigDecimal;
import java.net.URLEncoder;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import org.junit.Test;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.my.utils.HttpUtil;
import com.my.utils.RequestUtil;

public class Demo1 {
	public static void main(String[] args) {
		NumberFormat currency = NumberFormat.getCurrencyInstance(); //建立货币格式化引用   
	    NumberFormat percent = NumberFormat.getPercentInstance();  //建立百分比格式化引用   
	    percent.setMaximumFractionDigits(3); //百分比小数点最多3位   
	      
	    BigDecimal loanAmount = new BigDecimal("15000.48"); //贷款金额  
	    BigDecimal interestRate = new BigDecimal("0.008"); //利率     
	    BigDecimal interest = loanAmount.multiply(interestRate); //相乘  
	   
	    System.out.println("贷款金额:\t" + currency.format(loanAmount));   
	    System.out.println("利率:\t" + percent.format(interestRate));   
	    System.out.println("利息:\t" + currency.format(interest));
	    JSONObject jsonObject = new JSONObject();
	}
	
	@Test
	public void test1() {
		double d = 29.0 * 0.01;  
		System.out.println(d);  
		System.out.println(d * 100);  
		System.out.println((int) (d * 100)); 
	}
	@Test
	public void test2() {
		float f = (float) (29.45*0.01);  
		System.out.println(f);  
		System.out.println(f * 100);  
		System.out.println((int) (f * 100));   
	}
	@Test
	public void test3() {
		BigDecimal x2 = new BigDecimal(500000001);  
		BigDecimal y2 = new BigDecimal(0.6);  
		BigDecimal z2 = x2.multiply(y2);  
		System.out.println(">>>>>>>>>> "+z2);   
	}
	@Test
	public void test4() {
		BigDecimal x2 = new BigDecimal("500000001");  
		BigDecimal y2 = new BigDecimal("0.6");  
		BigDecimal z2 = x2.multiply(y2);  
		System.out.println(">>>>>>>>>> "+z2);   
	}
	
	@Test
	public void test5() {
		String reg = "^\\d{3}$";
		String string = "898";
		Pattern pattern = Pattern.compile(reg);
		boolean matches = pattern.matcher(string).matches();
		System.out.println(matches);
	}
	
	@Test
	public void test6() {
		String url = RequestUtil.getRequest().getScheme() + "://" + RequestUtil.getRequest().getServerName() + ":" + RequestUtil.getRequest().getServerPort() + RequestUtil.getRequest().getContextPath() + "/";
		System.out.println("url="+url);
		System.out.println(RequestUtil.getRequest().getScheme());
		System.out.println(RequestUtil.getRequest().getServerName());
		System.out.println(RequestUtil.getRequest().getServerPort());
		System.out.println(RequestUtil.getRequest().getContextPath());
	}
	
	@Test
	public void test7() {
		String string = String.format("weixinAppLogin?context=%s&referrer=%s&callbackAction=%s", new Object[]{"appId", URLEncoder.encode("123"), "qwe"});
		System.out.println(string);
	}
	
	@Test
	public void test8() {
		String code = "453466";
		String content = "您的登录验证码是${code}十五分钟内有效，请不要告诉其他人喔";
		String replace = content.replace("${code}", code);
		System.out.println(replace);
	}
	
	@Test
	public void test9() {
		BigDecimal bigDecimal = new BigDecimal(0);
		System.out.println(bigDecimal.doubleValue()==0);
	}
	
	@Test
	public void test10() {
		String requestUrl = "https://www.baidu.com";
		String string = HttpUtil.invokeHttps(requestUrl);
		System.out.println(string);
	}
	
}
