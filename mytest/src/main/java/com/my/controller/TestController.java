package com.my.controller;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/my")
public class TestController {
	
	@RequestMapping("/test")
	public String test(){
		System.out.println("执行了my/test");
		return "my/test";
	}
	
	/**
	 * url路径文件下载
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping("/fileDownload")
	public void fileDownload(HttpServletRequest request, HttpServletResponse response) throws IOException{
		URL url = new URL("https://mp.weixin.qq.com/cgi-bin/showqrcode?ticket=gQFm8TwAAAAAAAAAAS5odHRwOi8vd2VpeGluLnFxLmNvbS9xLzAyQzBlb1FQMm1mcDExMDAwMGcwN3gAAgSxrkVaAwQAAAAA");
		URLConnection urlConnection = url.openConnection();
		String fileName = "myFileDownload.png";
		InputStream is = urlConnection.getInputStream();
		BufferedInputStream bins = new BufferedInputStream(is);
		OutputStream outs = response.getOutputStream();
		BufferedOutputStream bouts = new BufferedOutputStream(outs);
		response.setContentType("application/x-download");
		String encodedFileName = null;
		// 如果是IE,通过URLEncoder对filename进行UTF8编码。而其他的浏览器（firefox、chrome、safari、opera），则要通过字节转换成ISO8859-1。
		if (request.getHeader("User-Agent").toUpperCase().indexOf("MSIE") > 0) {
			encodedFileName = URLEncoder.encode(fileName, "UTF-8");
		} else {
			encodedFileName = new String(fileName.getBytes("UTF-8"), "ISO8859-1");
		}
		response.setHeader("Content-disposition", "attachment;filename=" + encodedFileName);
		int bytesRead = 0;
		byte[] buffer = new byte[1024 * 50];
		while ((bytesRead = bins.read(buffer, 0, 1024 * 50)) != -1) {
			bouts.write(buffer, 0, bytesRead);
		}
		bouts.flush();
		is.close();
		bins.close();
		outs.close();
		bouts.close();
		System.out.println("附件下载成功");
	}
}
