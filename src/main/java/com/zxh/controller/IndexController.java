package com.zxh.controller;

import com.zxh.model.Blog;
import com.zxh.model.Tag;
import com.zxh.model.Type;
import com.zxh.service.BlogService;
import com.zxh.service.TagService;
import com.zxh.service.TypeService;
import com.zxh.vo.TagVo;
import com.zxh.vo.TypeVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
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
        List<TagVo> tagVos = tagService.listTagTop(10);//获取前10个标签
        List<TypeVo> types = typeService.listTypeTop(6);//获取前6个类型
        model.addAttribute("tags", tagVos);
        model.addAttribute("types", types);
        model.addAttribute("page", blogService.listBlog(pageable));
        model.addAttribute("recommendBlogs", blogService.listReCommendBlogTop(8));
        //TODO 添加redis缓存
        return "index";
    }



    @PostMapping("/search")
    public String search(@PageableDefault(size = 8, sort = {"updateTime"}, direction = Sort.Direction.DESC)
                                     Pageable pageable,
                         @RequestParam("query") String query, Model model) {
        model.addAttribute("page", blogService.listPage(query, pageable));
        model.addAttribute("query", query);
        return "search";
    }


    /**
     * 博客详情
     * @param id
     * @param model
     * @return
     */
    @GetMapping("/blog/{id}")
    public String blog(@PathVariable Long id, Model model) {
        model.addAttribute("blog", blogService.getAndConvert(id));
        return "/blog";
    }

}
