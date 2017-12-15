package com.zxh.controller;

import com.zxh.exception.MyException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * Created by admin on 2017/12/14.
 */

@Controller
@RequestMapping("/demo")
public class DemoController {

    @RequestMapping("/hello")
    @ResponseBody
    public String hello() {
        return "hello SpringBoot";
    }

    @RequestMapping("/error")
    public String error() {
        throw new MyException("出错了");
    }

    @GetMapping("/get")
    @ResponseBody
    public String getTest(@RequestParam String arg) {
        return "hello " + arg;
    }

    @PostMapping("/post")
    @ResponseBody
    public String postTest(@RequestParam String arg) {
        return "hello " + arg;
    }
}
