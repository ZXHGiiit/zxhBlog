package com.zxh.controller;

import com.zxh.model.Tag;
import com.zxh.model.Type;
import com.zxh.service.BlogService;
import com.zxh.service.TagService;
import com.zxh.service.TypeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

/**
 * Created by admin on 2017/12/17.
 */

@Controller
public class IndexController {
    private static Logger logger = LoggerFactory.getLogger(IndexController.class);

    @Autowired
    private TagService tagService;
    @Autowired
    private TypeService typeService;
    @Autowired
    private BlogService blogService;

    @GetMapping("/")
    public String index(@PageableDefault(size = 8, sort = {"updateTime"}, direction = Sort.Direction.DESC)
                                    Pageable pageable, Model model) {
        List<Tag> tags = tagService.listTagTop(10);//获取前10个标签
        List<Type> types = typeService.listTypeTop(6);//获取前10个标签
        //logger.info("IndexController.index.info: [Tag:{}, Type:{}]", tags.toString(), types.toString());
        model.addAttribute("tags", tags);
        model.addAttribute("types", types);
        model.addAttribute("blogs", blogService.listBlog(pageable));
        return "index";
    }

}
