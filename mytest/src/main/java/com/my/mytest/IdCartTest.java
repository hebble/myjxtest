package com.my.mytest;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.junit.Test;

import com.alibaba.fastjson.JSONObject;
import com.my.utils.HttpUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class IdCartTest {
	
	/**
	 * 测试身份证识别
	 */
	@Test
	public void test() {
		String host = "https://dm-51.data.aliyun.com";
	    String path = "/rest/160601/ocr/ocr_idcard.json";
	    String method = "POST";
	    String appcode = "ae84931444e24ac9aa47d230e8c03dc1";
	    Map<String, String> headers = new HashMap<String, String>();
	    //最后在header中的格式(中间是英文空格)为Authorization:APPCODE 83359fd73fe94948385f570e3c139105
	    headers.put("Authorization", "APPCODE " + appcode);
	    //根据API的要求，定义相对应的Content-Type
	    headers.put("Content-Type", "application/json; charset=UTF-8");
	    Map<String, String> querys = new HashMap<String, String>();
	    String imagePath = "D:/myidcart.jpg";
	    String binaryToString = getImageBinaryToString(imagePath);
	    System.out.println("===========binaryToString="+binaryToString);
	    String bodys = "{\"image\":\""+ binaryToString +"\",\"configure\":\"{\\\"side\\\":\\\"face\\\"}\"}";


	    try {
	    	/**
	    	* 重要提示如下:
	    	* HttpUtils请从
	    	* https://github.com/aliyun/api-gateway-demo-sign-java/blob/master/src/main/java/com/aliyun/api/gateway/demo/util/HttpUtils.java
	    	* 下载
	    	*
	    	* 相应的依赖请参照
	    	* https://github.com/aliyun/api-gateway-demo-sign-java/blob/master/pom.xml
	    	*/
	    	HttpResponse response = HttpUtils.doPost(host, path, method, headers, querys, bodys);
	    	System.out.println("======================================");
	    	System.out.println(response.toString());
	    	//获取response的body
	    	String jsonString = EntityUtils.toString(response.getEntity());
	    	JSONObject jsonObject = JSONObject.parseObject(jsonString);
	    	log.info("获取到数据{}",jsonObject);
	    	System.out.println("======================================");
	    } catch (Exception e) {
	    	e.printStackTrace();
	    }
	}
	
	/**
	 * 测试驾驶证识别
	 */
	@Test
	public void test2() {
		String host = "https://dm-52.data.aliyun.com";
	    String path = "/rest/160601/ocr/ocr_driver_license.json";
	    String method = "POST";
	    String appcode = "ae84931444e24ac9aa47d230e8c03dc1";
	    Map<String, String> headers = new HashMap<String, String>();
	    //最后在header中的格式(中间是英文空格)为Authorization:APPCODE 83359fd73fe94948385f570e3c139105
	    headers.put("Authorization", "APPCODE " + appcode);
	    //根据API的要求，定义相对应的Content-Type
	    headers.put("Content-Type", "application/json; charset=UTF-8");
	    Map<String, String> querys = new HashMap<String, String>();
	    String imagePath = "D:/driver_license.jpg";
	    String binaryToString = getImageBinaryToString(imagePath);
	    String bodys = "{\"image\":\"" + binaryToString + "\",\"configure\":\"{\\\"side\\\":\\\"face\\\"}\"}";
	    
	    try {
	    	/**
	    	* 重要提示如下:
	    	* HttpUtils请从
	    	* https://github.com/aliyun/api-gateway-demo-sign-java/blob/master/src/main/java/com/aliyun/api/gateway/demo/util/HttpUtils.java
	    	* 下载
	    	*
	    	* 相应的依赖请参照
	    	* https://github.com/aliyun/api-gateway-demo-sign-java/blob/master/pom.xml
	    	*/
	    	HttpResponse response = HttpUtils.doPost(host, path, method, headers, querys, bodys);
	    	System.out.println("======================================");
	    	System.out.println(response.toString());
	    	//获取response的body
	    	String jsonString = EntityUtils.toString(response.getEntity());
	    	JSONObject jsonObject = JSONObject.parseObject(jsonString);
	    	log.info("获取到数据{}",jsonObject);
	    	System.out.println("======================================");
	    } catch (Exception e) {
	    	e.printStackTrace();
	    }

	}
	
	/** 
     * 获取图片的base64编码数据 
     * </p> 
     *  
     * @param imagePath 
     * @return 
     */  
    public static String getImageBinaryToString(String imagePath) {  
        try {  
            File file = new File(imagePath);  
            byte[] content = new byte[(int) file.length()];  
            FileInputStream finputstream = new FileInputStream(file);  
            finputstream.read(content);  
            finputstream.close();  
            return new String(Base64.encodeBase64(content));  
        } catch (IOException e) {  
            e.printStackTrace();  
            return null;  
        }  
    }  
}
