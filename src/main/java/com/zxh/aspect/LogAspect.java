package com.zxh.aspect;

import com.zxh.model.RequestLog;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by admin on 2017/12/14.
 */

@Aspect
@Component
public class LogAspect {
    private static final Logger logger = LoggerFactory.getLogger(LogAspect.class);
    @Autowired
    private HttpServletRequest request;

    /**
     * 定义切面
     */
    @Pointcut("execution(* com.zxh.controller.*.*(..))")
    public void log() {

    }


    @Before("log()")
    public void doBefore(JoinPoint point) {
        String url = request.getRequestURL().toString();
        String ip = request.getRemoteAddr();
        String classMethod = point.getSignature().getDeclaringTypeName() + "." + point.getSignature().getName();
        Object[] args = point.getArgs();
        RequestLog log = new RequestLog(url, ip, classMethod, args);
        logger.info("Request : {}", log.toString());
    }

    @After("log()")
    public void doAfter(JoinPoint point) {

    }

    @AfterReturning(returning = "result",pointcut = "log()")
    public void doAfterReturn(Object result) {
        logger.info("Result : {}", result);
    }



}
