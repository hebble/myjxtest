package com.my.utils.weixin4j.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.my.utils.weixin4j.interceptor.MyWeixin4jInterceptor;

/**
 * 添加拦截器
 * @ClassName:  InterceptorConfiguration   
 * @Description:TODO  
 * @author: Hebble 
 * @date:   2018年1月27日 下午4:24:10   
 *
 */
 /*
  * 从Spring3.0，@Configuration用于定义配置类，可替换xml配置文件，被注解的类内部包含有一个或多个被@Bean注解的方法，
  * 这些方法将会被AnnotationConfigApplicationContext或AnnotationConfigWebApplicationContext类进行扫描，并用于
  * 构建bean定义，初始化Spring容器
  */
@Configuration
public class InterceptorConfiguration extends WebMvcConfigurerAdapter {

	@Autowired
	private MyWeixin4jInterceptor myWeixin4jInterceptor;
	
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
    	// 多个拦截器组成一个拦截器链
    	// addPathPatterns 用于添加拦截规则
    	// excludePathPatterns 用户排除拦截
        registry.addInterceptor(myWeixin4jInterceptor).addPathPatterns("/**");
        super.addInterceptors(registry);
    }
}
