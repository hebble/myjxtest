package com.my.config;

import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.NoHandlerFoundException;

import com.my.exception.BusinessException;
import com.my.pojo.ResultData;

import lombok.extern.slf4j.Slf4j;

/**
 * 统一异常处理
 *
 * Created by chenqiuzhen on 2016/10/21.
 */
@Slf4j
@ControllerAdvice
public class GlobalDefaultExceptionHandler {
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ResultData exceptionHandler(Exception ex, HttpServletResponse response) throws  Exception{

        ResultData rs = new ResultData();
        rs.setMsg("系统繁忙，请稍后再试！");
        if (ex instanceof BusinessException) {
            BusinessException be = (BusinessException) ex;
            rs.setCode(be.getCode());
            rs.setMsg(be.getMessage());
            BusinessException.ExceptionTypeEnum exceptionTypeEnum = ((BusinessException) ex).getType();
            if(exceptionTypeEnum==BusinessException.ExceptionTypeEnum.WARN)
                log.warn("requestId="+ UUID.randomUUID(),ex);
            else
                log.error("requestId="+ UUID.randomUUID(),ex);
        }else if(ex instanceof NoHandlerFoundException){
            log.warn("requestId="+ UUID.randomUUID(),ex);
            return ResultData.getFailResult("您访问的页面不存在或您没有权限访问当前内容");
        }else if(ex instanceof ServletException){
            log.error("requestId="+ UUID.randomUUID(),ex);
            return ResultData.getFailResult(ex.getMessage());
        }else{
            log.error("requestId="+ UUID.randomUUID(),ex);
        }
        return rs;
     }
}
