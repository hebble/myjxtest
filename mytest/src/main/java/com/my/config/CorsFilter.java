package com.my.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@Component
@Slf4j
/**
 * 跨域设置过滤器
 * 请注意只能在开发环境使用
 * Created by chenqiuzhen on 2018/1/17.
 */
public class CorsFilter implements Filter {


    /**
     * 是否开启跨域设置
     */
    @Value("${cors.enable:false}")
    private boolean enable = false;

    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        if(enable){
            HttpServletResponse response = (HttpServletResponse) res;
            response.setHeader("Access-Control-Allow-Origin", "*");
            response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
            response.setHeader("Access-Control-Max-Age", "3600");
            response.setHeader("Access-Control-Allow-Headers", "x-requested-with");
            log.info("*********************************过滤器被使用**************************");
        }
        chain.doFilter(req, res);
    }
    public void init(FilterConfig filterConfig) {}
    public void destroy() {}
}

