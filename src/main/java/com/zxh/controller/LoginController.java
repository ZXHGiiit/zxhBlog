package com.zxh.controller;

import com.zxh.model.User;
import com.zxh.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;

/**
 * Created by admin on 2017/12/15.
 */

@Controller
@RequestMapping("/admin")
public class LoginController {
    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);
    @Autowired
    private UserService userService;


    @GetMapping
    public String loginPage() {
        return "admin/login";
    }


    @PostMapping("login")
    public String login(@RequestParam String username,
                        @RequestParam String password,
                        HttpSession session,
                        RedirectAttributes attributes) {
        User user = userService.findByUAP(username, password);
        if(user != null) {
            logger.info("UserController.login.userInfo{}", user.toString());
            session.setAttribute("user", user);
            return "admin/index";
        } else {
            attributes.addFlashAttribute("message", "用户名或密码错误");
            //如果不使用重定向的话，就会出现静态资源引用出错
            return "redirect:/admin";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.removeAttribute("user");
        return "redirect:/admin";
    }

    @GetMapping("/test")
    @ResponseBody
    public String test(@RequestParam String account,
                       @RequestParam String password) {
        User user = userService.findByUAP(account, password);
        logger.info("UserController.test.User: {}", user.toString());
        return user.toString();
    }
}
