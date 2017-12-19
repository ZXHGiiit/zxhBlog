package com.zxh.controller;

import com.zxh.model.Comment;
import com.zxh.model.User;
import com.zxh.service.BlogService;
import com.zxh.service.CommentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpSession;

/**
 * Created by admin on 2017/12/19.
 */

@Controller
public class CommentController {
    private static final Logger logger = LoggerFactory.getLogger(CommentController.class);
    @Autowired
    private BlogService blogService;
    @Autowired
    private CommentService commentService;

    @Value("${comment.avatar}")
    private String avatar;

    /**
     * 获取博客的评论
     * @param blogId
     * @param model
     * @return
     */
    @GetMapping("/comments/{blogId}")
    public String comments(@PathVariable Long blogId, Model model) {
        model.addAttribute("comments", commentService.listCommentByBlogId(blogId));
        return "blog :: commentList"; //局部刷新
    }


    @PostMapping("comments")
    public String comments(Comment comment, HttpSession httpSession) {
        logger.info("CommentCOntroller.comments.info:[comment:{}]", comment.toString());
        Long blogId = comment.getBlog().getId();
        comment.setBlog(blogService.getBlog(blogId));
        User user = (User) httpSession.getAttribute("user");
        if(user != null) {
            comment.setAvatar(user.getAvatar());
            comment.setAdminComment(true);
        } else {
            comment.setAvatar(avatar);
        }
        commentService.saveComment(comment);
        return "redirect:/comments/" + comment.getBlog().getId();
    }

}
