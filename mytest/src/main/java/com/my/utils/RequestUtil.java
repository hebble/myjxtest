package com.my.utils;

import java.io.PrintWriter;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.context.request.ServletWebRequest;


/**
 * @author tz
 */
public class RequestUtil{
	public static boolean isAjaxRequest(){
		boolean isAjax = false;
		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
		if(StringUtils.equals(request.getHeader("X-Requested-With"), "XMLHttpRequest")){
			isAjax = true;
		}
		return isAjax;
	}

	public static String getParameter(String queryString, String name){
		if((StringUtils.isEmpty(queryString)) || (StringUtils.isEmpty(name)) || (queryString.indexOf(name) == -1)){
			return "";
		}
		String parameter = null;
		if(queryString.startsWith("?" + name + "=")){
			parameter = queryString.substring(name.length() + 2);
			if(parameter.indexOf("&") != -1)
				parameter = parameter.substring(0, parameter.indexOf("&"));
		}else if(queryString.startsWith(name + "=")){
			parameter = queryString.substring(name.length() + 1);
			if(parameter.indexOf("&") != -1)
				parameter = parameter.substring(0, parameter.indexOf("&"));
		}else{
			int index = queryString.indexOf("&" + name + "=");
			if(index != -1){
				parameter = queryString.substring(index + name.length() + 2);
				/*
				 * if (parameter.indexOf("&") != -1) { parameter = parameter.substring(0, parameter.indexOf("&")); }
				 */
			}
		}
		return parameter;
	}

	public static String getRemoteIp(){
		String header = getRequest().getHeader("X-Real-IP");
		if(StringUtils.isEmpty(header)){
			header = getRequest().getHeader("x-forwarded-for");
			if((StringUtils.isEmpty(header)) || ("unknown".equalsIgnoreCase(header))){
				header = getRequest().getHeader("Proxy-Client-IP");
			}
			if((StringUtils.isEmpty(header)) || ("unknown".equalsIgnoreCase(header))){
				header = getRequest().getHeader("WL-Proxy-Client-IP");
			}
			if((StringUtils.isEmpty(header)) || ("unknown".equalsIgnoreCase(header))){
				header = getRequest().getRemoteAddr();
			}
		}
		return null;
	}

	public static HttpServletResponse getResponse(){
		HttpServletResponse response = ((ServletWebRequest)RequestContextHolder.getRequestAttributes()).getResponse();
		return response;
	}

	public static HttpServletRequest getRequest(){
		return ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
	}

	public static <E> E getRequestAttribute(String key, Class<E> clazz){
		return (E)((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest().getAttribute(key);
	}

	public static HttpSession getSession(){
		HttpSession session = getRequest().getSession(false);
		return session;
	}

	public static ServletContext getApplication(){
		return getSession().getServletContext();
	}

	public static void outputAsString(Object obj){
		HttpServletResponse response = null;
		outputAsString(response, obj);
	}

	public static void outputAsString(HttpServletResponse response, Object obj){
		try{
			response.setCharacterEncoding("UTF-8");
			response.setContentType("text/html;charset=utf-8");
			response.setHeader("progma", "no-cache");
			response.setHeader("Cache-Control", "no-cache");
			response.setDateHeader("Expires", 0L);
			PrintWriter writer = response.getWriter();
			writer.write(obj.toString());
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	public static void outputAsJson(Object obj){
		HttpServletResponse response = ((ServletWebRequest)RequestContextHolder.getRequestAttributes()).getResponse();
		outputAsJson(response, obj);
	}

	public static void outputAsJson(HttpServletResponse response, Object obj){
		if(obj != null)
			try{
				response.setCharacterEncoding("UTF-8");
				response.setContentType("application/json;charset=utf-8");
				response.setHeader("progma", "no-cache");
				response.setHeader("Cache-Control", "no-cache");
				response.setDateHeader("Expires", 0L);
				PrintWriter writer = response.getWriter();
				//TODO
				//writer.write(JsonUtils.object2Json(obj));
			}catch(Exception e){
				e.printStackTrace();
			}
	}

}
