package com.zxh.service.impl;

import com.zxh.dao.CommentRepository;
import com.zxh.model.Comment;
import com.zxh.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * Created by admin on 2017/12/19.
 */

@Service
public class CommentServiceImpl implements CommentService {
    @Autowired
    private CommentRepository commentRepository;

    @Override
    public List<Comment> listCommentByBlogId(Long blogId) {
        Sort sort = new Sort("createTime");
        List<Comment> comments = commentRepository.findByBlogIdAndParentCommentNull(blogId, sort);
        return comments;
    }

    @Override
    public Comment saveComment(Comment comment) {
        //先保存其父级评论
        Long parentId = comment.getParentComment().getId();
        if(parentId != -1) {
            comment.setParentComment(commentRepository.findOne(parentId));
        } else {
            comment.setParentComment(null);
        }
        comment.setCreateTime(new Date());

        return commentRepository.save(comment);
    }
}
