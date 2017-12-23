package com.zxh.config;

import com.zxh.interceptor.LoginInterceptor;
import com.zxh.interceptor.PageFooterInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Page;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * Created by admin on 2017/12/15.
 */

@Configuration
public class WebConfig extends WebMvcConfigurerAdapter{

    @Bean
    public PageFooterInterceptor pageFooterInterceptor() {
        return new PageFooterInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        registry.addInterceptor(new LoginInterceptor())
                .addPathPatterns("/admin/**")
                .excludePathPatterns("/admin")
                .excludePathPatterns("/admin/login");

        registry.addInterceptor(pageFooterInterceptor())
                .addPathPatterns("/**");
     }

}
