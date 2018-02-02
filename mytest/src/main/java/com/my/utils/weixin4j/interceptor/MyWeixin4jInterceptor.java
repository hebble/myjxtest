package com.my.utils.weixin4j.interceptor;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.weixin4j.util.TokenUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
public class MyWeixin4jInterceptor implements HandlerInterceptor {

	@Override
	public void afterCompletion(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, Exception arg3)
			throws Exception {
	}

	@Override
	public void postHandle(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, ModelAndView arg3)
			throws Exception {
	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object object) throws Exception {
		// 获取请求方式
		String method = request.getMethod().toUpperCase();
		log.info("获得微信请求:{} 方式", method);
		log.info("微信请求URL:{}", request.getServletPath());
		if ("GET".equals(method)) {
			doGet(request,response);
		} else {
			//用户每次向公众号发送消息、或者产生自定义菜单点击事件时，响应URL将得到推送
			doPost(request, response);
		}
		return false;
	}

	private void doPost(HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * 执行get方式方法
	 * @param request
	 * @param response
	 * @throws IOException 
	 */
	private void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String signature = request.getParameter("signature");// 微信加密签名
		String timestamp = request.getParameter("timestamp");// 时间戳
		String nonce = request.getParameter("nonce"); // 随机数
		String echostr = request.getParameter("echostr"); //随机字符串
        ///api/vzhanqun
        String path = request.getServletPath();
        //weixin4jvzhanqun, TokenUtil.get()为获取配置文件weixin4j.token
        // token和网页上配置的一致
        String token = TokenUtil.get() + path.substring(path.lastIndexOf("/") + 1);
        //确认此次GET请求来自微信服务器，原样返回echostr参数内容，则接入生效，成为开发者成功，否则接入失败
        if (TokenUtil.checkSignature(token, signature, timestamp, nonce)) {
            response.getWriter().write(echostr);
        }
	}

}
