package com.zxh.handler;

import com.zxh.Model.User;
import com.zxh.exception.MyException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by admin on 2017/12/14.
 */

@ControllerAdvice
public class ControllerAdviceTest {
    private static final Logger logger = LoggerFactory.getLogger(ControllerAdviceTest.class);

    @ModelAttribute
    public User newUser() {
        System.out.println("应用到所有的@RequestMapping 注解方法中，在其执行之前，将返回值放入model");
        return new User();
    }

    @InitBinder
    public void initBinder() {
        System.out.println("应用到所有的@RequestMapping注解方法中，在其执行之前，初始化数据绑定器");
    }


    /**
     * 作用于所有的@RequestMapping注解方法中
     * 在其抛出MyException异常时执行方法
     * @return
     */
    @ExceptionHandler(MyException.class)
    //@ResponseStatus(HttpStatus.NOT_FOUND)
    public ModelAndView processException(HttpServletRequest request, MyException e) {
          logger.error("=============出了异常=============");
          logger.error("errorURL:{}, exceptionMsg:{}", request.getRequestURL(),e);
          ModelAndView mv = new ModelAndView();
          mv.setViewName("/error/error");
          return mv;
    }


}
