package com.my.mytest;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.dom4j.Document;
import org.dom4j.io.SAXReader;
import org.junit.Test;

import com.alibaba.fastjson.JSON;
import com.my.pojo.UserAccountTypeEnum;
import com.my.utils.XmlUtils;

public class Demo2 {
	
	@Test
	public void test1() throws Exception {
		SAXReader reader = new SAXReader();
		String file = this.getClass().getClassLoader().getResource("mybatis-config.xml").getFile();
		Document document = reader.read(new File(file));
		Map<String, Object> map = XmlUtils.Dom2Map(document);
		System.out.println(map);
	}
	
	@Test
	public void test2() {
		Map<String, String> paramsMap = new HashMap<>();
		paramsMap.put("code", "123");
		paramsMap.put("nickname", "张三");
		paramsMap.put("integral","200");
		StringBuilder stringBuilder = new StringBuilder("{");
		for (Entry<String, String> entry : paramsMap.entrySet()) {
			String key = entry.getKey();
			String value = entry.getValue();
			stringBuilder.append("\""+key+"\"");
			stringBuilder.append(":");
			stringBuilder.append("\""+value+"\"");
			stringBuilder.append(",");
		}
		String string = stringBuilder.substring(0, stringBuilder.length()-1);
		string += "}";
		System.out.println(string);
	}
	
	@Test
	public void test3() {
		Map<String, String> paramsMap = new HashMap<>();
		paramsMap.put("code", "123");
//		paramsMap.put("nickname", "张三");
//		paramsMap.put("integral","200");
		String string = "您的好友${nickname}申请向您转赠积分${integral}，确认验证码${code}，如确认请告知好友";
		for (Entry<String, String> entry : paramsMap.entrySet()) {
			String key = entry.getKey();
			String value = entry.getValue();
			string = string.replace("${"+key+"}", value);
		}
		System.out.println(string);
	}
	
	@Test
	public void test4() {
		Map<String, String> paramsMap = new HashMap<>();
		paramsMap.put("code", "123");
		paramsMap.put("nickname", "张三");
		paramsMap.put("integral","200");
		System.out.println(paramsMap);
	}
	
	@Test
	public void test5() {
		for (int i = 1; i < 7 ; i++) {
			System.out.println(UserAccountTypeEnum.getDesc(i));
		}
	}
	
	@Test
	public void test6() {
		List itemImgs = new ArrayList<>();
		itemImgs.add("1.jpg");
		itemImgs.add("2.jpg");
		itemImgs.add("3.jpg");
		System.out.println(JSON.toJSONString(itemImgs));
	}
	
	@Test
	public void test7() {
		BigDecimal bigDecimal = new BigDecimal("0.1");
		System.out.println(bigDecimal);
		System.out.println(bigDecimal.setScale(2, BigDecimal.ROUND_HALF_UP));
	}
	
}
