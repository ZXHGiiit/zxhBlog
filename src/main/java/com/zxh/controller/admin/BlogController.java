package com.zxh.controller.admin;

import com.zxh.dao.BlogRespository;
import com.zxh.dao.TypeRespository;
import com.zxh.model.Blog;
import com.zxh.service.BlogService;
import com.zxh.service.TagService;
import com.zxh.service.TypeService;
import com.zxh.vo.BlogQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;


/**
 * Created by admin on 2017/12/16.
 */

@Controller
@RequestMapping("/admin")
public class BlogController {
    private static final Logger logger = LoggerFactory.getLogger(BlogController.class);

    @Autowired
    private TypeService typeService;
    @Autowired
    private BlogService blogService;
    @Autowired
    private TagService tagService;

    /**
     * 博客页面
     * @param pageable
     * @param model
     * @return
     */
    @GetMapping("/blogs")
    public String showBlogPage(@PageableDefault(size = 8, direction = Sort.Direction.DESC, sort = {"updateTime"})
                                           Pageable pageable, BlogQuery blogQuery, Model model) {
        //将Blog Type装载到Model中去，用来实现下拉框查询
        model.addAttribute("types", typeService.listType());
        model.addAttribute("page", blogService.listBlog(pageable, blogQuery));
        return "/admin/blogs";
    }


    /**
     * 新增博客页面
     * @param model
     * @return
     */
    @GetMapping("/blogs/input")
    public String showInputPage(Model model) {
        model.addAttribute("blog", new Blog());
        setTypeAndTag(model);
        return "/admin/blogs-input";
    }

    private void setTypeAndTag(Model model) {
        model.addAttribute("tags", tagService.listTag());
        model.addAttribute("types", typeService.listType());
    }

    /**
     * 修改博客页面
     * @param model
     * @return
     */
    @GetMapping("/blogs/{id}/input")
    public String showEditPage(@PathVariable Long id, Model model) {
        Blog blog = blogService.getBlog(id);
        logger.info("BlogController.showEditPage.info: {}", blog.toString());
        model.addAttribute("blog", blog);
        setTypeAndTag(model);
        return "/admin/blogs-input";
    }
}
