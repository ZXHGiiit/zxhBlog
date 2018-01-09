package com.zxh.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Created by admin on 2018/1/9.
 */

@Controller
public class NoBugController {

    @GetMapping("/nobug")
    public String index() {
        return "nobug";
    }

}
