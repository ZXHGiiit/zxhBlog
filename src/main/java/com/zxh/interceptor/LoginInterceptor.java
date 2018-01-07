package com.zxh.interceptor;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by admin on 2017/12/15.
 * 登录拦截器，需要配置，见WebConfig
 */
public class LoginInterceptor extends HandlerInterceptorAdapter {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if(request.getSession().getAttribute("user") == null) {
            response.sendRedirect("/admin");
            String url = request.getRequestURL().toString();
            return false;//返回false，表示不往下执行，
        }
        return true;
    }
}
