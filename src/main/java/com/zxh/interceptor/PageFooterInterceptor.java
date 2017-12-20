package com.zxh.interceptor;

import com.zxh.model.Blog;
import com.zxh.service.BlogService;
import com.zxh.service.RedisService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Created by admin on 2017/12/20.
 * 页面底部拦截器，页面底部是动态的，但是每个方法都加上这个属性的显示，很麻烦，
 * 放在拦截器中处理，存入session，不让session过期就好
 */
@Component
public class PageFooterInterceptor implements HandlerInterceptor {
    private static final Logger logger = LoggerFactory.getLogger(PageFooterInterceptor.class);
    @Autowired
    private BlogService blogService;
    @Autowired
    private RedisService redisService;


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse httpServletResponse, Object o) throws Exception {
        if(request.getSession().getAttribute("blogsTop") == null) {
            logger.info("PageFooterInterceptor.preHandle.session 已经过期，重新设置。。。");
            BeanFactory factory = WebApplicationContextUtils.getRequiredWebApplicationContext(request.getServletContext());
            if(blogService == null) {
                logger.warn("PageController.preHandle.blogService 无法通过@Autowired注入");
                blogService = (BlogService) factory.getBean("blogService");
            }
            List<Blog> blogs = blogService.listBlogTop(3);
            request.getSession().setAttribute("blogsTop", blogs);
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}
