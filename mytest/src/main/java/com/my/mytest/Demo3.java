package com.my.mytest;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.Resource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.alibaba.fastjson.JSON;
import com.my.mytest.model.BeanInject;
import com.my.pojo.ResultData;

@RunWith(SpringJUnit4ClassRunner.class)
@Import({BeanInject.class})
public class Demo3 {
	
	@Value("注入普通字符串")// 注入普通字符串  
    private String normal;  

    @Value("#{systemProperties['os.name']}")
    private String systemPropertiesName; // 注入操作系统属性

    @Value("#{ T(java.lang.Math).random() * 100.0 }")
//    @Value("#{ 2 * 100.0 }")
    private double randomNumber; //注入表达式结果

    @Value("#{beanInject.another}")
    private String fromAnotherBean; // 注入其他Bean属性：注入beanInject对象的属性another，类具体定义见下面, 
    								//spring整合junit @Import({BeanInject.class}),将beanInject类放入测试中的spring容器

    @Value("classpath:a.txt")
    private Resource resourceFile; // 注入文件资源

    @Value("http://www.baidu.com")
    private Resource testUrl; // 注入URL资源
	
    @Value("${book.name}")// 注入配置文件【注意是$符号】  
    private String bookName; 
    
    @Value("${name:kkk}")
    private String name;
    
    /**
     * 测试@value的用法
     * @throws IOException 
     */
    @Test
	public void test1() throws IOException {
		System.out.println("normal="+normal);
		System.out.println("systemPropertiesName="+systemPropertiesName);
		System.out.println("randomNumber="+randomNumber);
		System.out.println("fromAnotherBean="+fromAnotherBean);
		System.out.println("resourceFile="+resourceFile);
		System.out.println("testUrl="+testUrl);
		File file = resourceFile.getFile();
		URL url = testUrl.getURL();
		URLConnection connection = url.openConnection();
		InputStream inputStream = connection.getInputStream();
		String readFileToString = FileUtils.readFileToString(file,"utf-8");
		System.out.println("readFileToString="+readFileToString);
		System.out.println("=======================================================================");
		List<String> readLines = IOUtils.readLines(inputStream, "utf-8");
		System.out.println("size="+readLines.size());
		for (String string : readLines) {
			System.out.println(string);
		}
		System.out.println("======================================================================");
		System.out.println("name="+name);
	}
    
    @Test
    public void test2() {
    	String str1="120.00";
    	BigDecimal bd=new BigDecimal(str1);
    	System.out.println(bd);
    }
}

