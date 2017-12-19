package com.zxh.service;

import com.zxh.model.Comment;

import java.util.List;

/**
 * Created by admin on 2017/12/19.
 */
public interface CommentService {
    List<Comment> listCommentByBlogId(Long blogId);

    Comment saveComment(Comment comment);


}
