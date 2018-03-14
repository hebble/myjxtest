/**
 * 
 */
package com.my.utils.oss;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.Bucket;
import com.aliyun.oss.model.OSSObject;
import com.aliyun.oss.model.ObjectMetadata;
import com.aliyun.oss.model.PutObjectResult;
import com.my.utils.FileUtil;
import com.my.utils.MD5Util;

import lombok.extern.slf4j.Slf4j;

/**
 * @author taozui
 */
@Slf4j
public class OSSUnit{
	//log  

	//阿里云API的内或外网域名  
	private static String ENDPOINT = "file.joyxuan.com";
	//阿里云API的密钥Access Key ID  
	private static String ACCESS_KEY_ID = "LTAIYOMIqs6gEoZy";
	//阿里云API的密钥Access Key Secret  
	private static String ACCESS_KEY_SECRET = "x0ZHlwS5IcMy5mZfWkmrQa4STpdFTa";
	//阿里云API的bucketName  
	private static String bucketName = "joyxuan"; //需要存储的bucketName  
	
	
	/**
	 * @return
	 */
	public static Object getOSSServer(){
		return ENDPOINT+File.separator;
	}
	/**
	 * 获取阿里云OSS客户端对象
	 */
	public static final OSSClient getOSSClient(){
		return new OSSClient(ENDPOINT, ACCESS_KEY_ID, ACCESS_KEY_SECRET);
	}

	/**
	 * 新建Bucket --Bucket权限:私有
	 * @param bucketName bucket名称
	 * @return true 新建Bucket成功
	 */
	public static final boolean createBucket(OSSClient client, String bucketName){
		Bucket bucket = client.createBucket(bucketName);
		return bucketName.equals(bucket.getName());
	}

	/**
	 * 删除Bucket
	 * @param bucketName bucket名称
	 */
	public static final void deleteBucket(OSSClient client, String bucketName){
		client.deleteBucket(bucketName);
		log.info("删除" + bucketName + "Bucket成功");
	}

	/**
	 * 向阿里云的OSS存储中存储文件
	 * @param file
	 * @return
	 */
	public static final String uploadObject2OSS(File file){
		return uploadObject2OSS(getOSSClient(), file, bucketName, 5);
	}

	/**
	 * 向阿里云的OSS存储中存储文件
	 * @param extension
	 * @param file
	 * @return
	 */
	public static final String uploadObject2OSS(byte[] files, String extension){
		String folder = System.getProperty("java.io.tmpdir");
		File file = FileUtil.toFile(files, folder, UUID.randomUUID().toString().toLowerCase()+ "." + extension);
		return uploadObject2OSS(getOSSClient(), file, bucketName, 5);
	}

	/**
	 * 向阿里云的OSS存储中存储文件 --file也可以用InputStream替代
	 * @param client OSS客户端
	 * @param file 上传文件
	 * @param bucketName bucket名称
	 * @param diskName 上传文件的目录 --bucket下文件的路径
	 * @return String 唯一MD5数字签名
	 */
	public static final String uploadObject2OSS(OSSClient client, File file, String bucketName, int count){
		String resultStr = null;
		long tick = System.currentTimeMillis();
		String key = MD5Util.toMD5(System.currentTimeMillis()+"");
		if(count > 0){
			int index = 0;
			while(StringUtils.isEmpty(resultStr) && index < count){
				try{
					InputStream is = new FileInputStream(file);
					String fileName = file.getName();
					Long fileSize = file.length();
					//创建上传Object的Metadata  
					ObjectMetadata metadata = new ObjectMetadata();
					metadata.setContentLength(is.available());
					metadata.setCacheControl("no-cache");
					metadata.setHeader("Pragma", "no-cache");
					metadata.setContentEncoding("utf-8");
					metadata.setContentType(getContentType(fileName));
					metadata.setContentDisposition("filename/filesize=" + key + "/" + fileSize + "Byte.");
					//上传文件   
					PutObjectResult putResult = client.putObject(bucketName, key, is, metadata);
					//解析结果  
					resultStr = putResult.getETag();
				}catch(Exception e){
					log.error("上传文件异常,index:{},message:{}", index, e.getMessage());
					return null;
				}
				if(StringUtils.isEmpty(resultStr)){
					log.info("oss 上传失败第" + (index + 1) + "次");
				}
				if(index != 0){
					try{
						TimeUnit.MILLISECONDS.sleep(200 * index);
					}catch(Exception e){
						log.error("上传文件异常 index:{}", index);
						return null;
					}
				}
				index++;
			}
		}
		long size = file.length() / 1024;
		if(size == 0){
			size = 1;
		}
		FileUtil.deleteFile(file);//上传完成删除临时目录
		log.info("上传文件：" + file.getName() + " 成功，file key："+key + "，文件大小：" + size + " KB，耗时：" + (System.currentTimeMillis() - tick) + " ms");
		return key;
	}

	/**
	 * 根据key获取OSS服务器上的文件输入流
	 * @param client OSS客户端
	 * @param bucketName bucket名称
	 * @param diskName 文件路径
	 * @param key Bucket下的文件的路径名+文件名
	 */
	public static final InputStream getOSS2InputStream(OSSClient client, String bucketName, String diskName, String key){
		OSSObject ossObj = client.getObject(bucketName, diskName + key);
		return ossObj.getObjectContent();
	}

	/**
	 * 根据key删除OSS服务器上的文件
	 * @param key文件名
	 */
	public static void deleteFile(String key){
		getOSSClient().deleteObject(bucketName, key);
		log.info("删除" + bucketName + "下的文件" + key + "成功");
	}

	/**
	 * 根据key删除OSS服务器上的文件
	 * @param client OSS客户端
	 * @param bucketName bucket名称
	 * @param diskName 文件路径
	 * @param key Bucket下的文件的路径名+文件名
	 */
	public static void deleteFile(OSSClient client, String bucketName, String key){
		client.deleteObject(bucketName, key);
		log.info("删除" + bucketName + "下的文件" + key + "成功");
	}

	public static final String getUrl(String key){
		// 设置URL过期时间为10年  3600l* 1000*24*365*10
		Date expiration = new Date(new Date().getTime() + 3600l * 1000 * 24 * 365 * 10);
		// 生成URL
		URL url = getOSSClient().generatePresignedUrl(bucketName, key, expiration);
		if(url != null){
			return url.toString();
		}
		return null;
	}

	/**
	 * 通过文件名判断并获取OSS服务文件上传时文件的contentType
	 * @param fileName 文件名
	 * @return 文件的contentType
	 */
	public static final String getContentType(String fileName){
		String fileExtension = FileUtil.getExtension(fileName);
		if("bmp".equalsIgnoreCase(fileExtension))
			return "image/bmp";
		if("gif".equalsIgnoreCase(fileExtension))
			return "image/gif";
		if("jpeg".equalsIgnoreCase(fileExtension) || "jpg".equalsIgnoreCase(fileExtension) || "png".equalsIgnoreCase(fileExtension))
			return "image/jpeg";
		if("html".equalsIgnoreCase(fileExtension))
			return "text/html";
		if("txt".equalsIgnoreCase(fileExtension))
			return "text/plain";
		if("vsd".equalsIgnoreCase(fileExtension))
			return "application/vnd.visio";
		if("ppt".equalsIgnoreCase(fileExtension) || "pptx".equalsIgnoreCase(fileExtension))
			return "application/vnd.ms-powerpoint";
		if("doc".equalsIgnoreCase(fileExtension) || "docx".equalsIgnoreCase(fileExtension))
			return "application/msword";
		if("xml".equalsIgnoreCase(fileExtension))
			return "text/xml";
		return "text/html";
	}

}
