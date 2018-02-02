package com.my.utils;

import java.util.Locale;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.web.servlet.LocaleResolver;

/**
 * spring工具类
 */
@Component
public  class SpringUtil implements ApplicationContextAware {

    /**
     * applicationContext
     */
    public static ApplicationContext  applicationContext ;


    public static Object getBean(String name) {
        Assert.hasText(name);
        return applicationContext.getBean(name);
    }

    public static <T> T getBean(Class<T> type) {
        return applicationContext.getBean(type);
    }
   
	public static <T> T getBean(Class<T> type, Object... args) {
		return applicationContext.getBean(type, args);
	}

    public static <T> T getBean(String name, Class<T> type) {
        Assert.hasText(name);
        Assert.notNull(type);
        return applicationContext.getBean(name, type);
    }

  
    public static String getMessage(String code, Object... args) {
        LocaleResolver localeResolver = getBean("localeResolver",
                LocaleResolver.class);
        Locale locale = localeResolver.resolveLocale(null);
        return applicationContext.getMessage(code, args, locale);
    }


    public static boolean isClassExist(String classPath) {
        try {
            Class aClass = Class
                    .forName(classPath);
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

    public static String getCacheKey(String key){
        String nameSpace = applicationContext.getEnvironment().getProperty("autolaod.cache.nameSpace");
        if(StringUtils.isBlank(nameSpace)){
            nameSpace = getSpringApplicationName();
        }
        return nameSpace+":"+key;
    }

   
    public static String getSpringApplicationName(){
        return applicationContext.getEnvironment().getProperty("spring.application.name");
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        SpringUtil.applicationContext = applicationContext;
    }

}