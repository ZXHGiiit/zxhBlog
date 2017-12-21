package com.zxh.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Created by admin on 2017/12/21.
 */

@Controller
public class AboutShowController {

    @GetMapping("/about")
    public String about() {
        return "/about";
    }
}
