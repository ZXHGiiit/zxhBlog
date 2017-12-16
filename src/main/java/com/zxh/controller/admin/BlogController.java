package com.zxh.controller.admin;

import com.zxh.model.Blog;
import com.zxh.model.User;
import com.zxh.service.BlogService;
import com.zxh.service.TagService;
import com.zxh.service.TypeService;
import com.zxh.util.JacksonUtils;
import com.zxh.vo.BlogQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.util.List;


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
     * 条件搜索
     * @param pageable
     * @param blogQuery
     * @param model
     * @return
     */
    @PostMapping("/blogs/search")
    public String search(@PageableDefault(size = 8, direction = Sort.Direction.DESC, sort = {"updateTime"})
                         Pageable pageable, BlogQuery blogQuery, Model model) {
        model.addAttribute("page", blogService.listBlog(pageable, blogQuery));
        //返回html局部片段，实现局部渲染
        return "/admin/blogs :: blogList";
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
        blog.init();
        logger.info("BlogController.showEditPage.info: {}", blog.toString());
        model.addAttribute("blog", blog);
        setTypeAndTag(model);
        return "/admin/blogs-input";
    }


    /**
     * 添加/修改博客
     * @param blog
     * @param attributes
     * @param session
     * @return
     */
    @PostMapping("/blogs")
    public String post(Blog blog, RedirectAttributes attributes, HttpSession session) {
        logger.info("BlogController.post.info: {}", blog);
        User user = (User) session.getAttribute("user");
        if(user == null) {
            attributes.addFlashAttribute("message", "操作失败，未知用户");
        }
        blog.setUser(user);
        //前端保存的是其id，需要将整个对象存入blog中
        blog.setType(typeService.getType(blog.getType().getId()));
        blog.setTags(tagService.listTag(blog.getTagIds()));

        Blog b;
        if(blog.getId() == null) {
            //新增博客
            b = blogService.saveBlog(blog);
        } else {
            //若id存在，表示修改已存在的博客
            b = blogService.updateBlog(blog.getId(), blog);
        }

        if(b == null) {
            logger.error("BlogController.post.ERROR");
            attributes.addFlashAttribute("message", "操作失败");
        } else {
            attributes.addFlashAttribute("message", "操作成功");
        }
        return "redirect:/admin/blogs";
    }


    @GetMapping("/blogs/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes attributes) {
        blogService.deleteBlog(id);
        attributes.addFlashAttribute("message", "操作成功");
        return "redirect:/admin/blogs";
    }





    @RequestMapping("/test")
    @ResponseBody
    public String test() {
        List<Blog> blogs = blogService.listBlog();
        logger.info("BlogController.test.info: {}",blogs.toString());
        return JacksonUtils.toJson(blogs);
    }
}
