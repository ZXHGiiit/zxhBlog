package com.zxh.controller.admin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


/**
 * Created by admin on 2017/12/16.
 */

@Controller
@RequestMapping("/admin")
public class BlogController {
    private static final Logger logger = LoggerFactory.getLogger(BlogController.class);

    /**
     * 博客页面
     * @param pageable
     * @param model
     * @return
     */
    @GetMapping("/blogs")
    public String showBlogPage(@PageableDefault(size = 3, direction = Sort.Direction.DESC, sort = {"id"})
                                           Pageable pageable, Model model) {

    }



}
