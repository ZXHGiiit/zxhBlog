package com.zxh.service.impl;

import com.zxh.dao.CommentRepository;
import com.zxh.model.Comment;
import com.zxh.service.CommentService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
        return eachComment(comments);
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


    /**
     * 遍历一级评论，拷贝到副本中，避免对数据库修改
     * @param comments
     * @return
     */
    private List<Comment> eachComment(List<Comment> comments) {
        List<Comment> commentsBak = new ArrayList<>();
        if(comments != null) {
            for(Comment comment : comments) {
                Comment temp = new Comment();
                BeanUtils.copyProperties(comment, temp);
                commentsBak.add(temp);
            }
        }
        //合并评论各层子集到第一级子集中
        combineChildren(commentsBak);
        return commentsBak;
    }

    /**
     * 合并评论各层子集到第一级子集中
     * @param comments
     */
    private void combineChildren(List<Comment> comments) {
        if(comments != null) {
            for(Comment comment : comments) {
                List<Comment> replys = comment.getReplyComments();
                for(Comment reply : replys) {
                    recursively(reply);
                }
                comment.setReplyComments(tempReplys);
                tempReplys = new ArrayList<>();//清除缓存区，以便于用来存放第二个一级评论的所有子集
            }
        }
    }


    /**
     * 存放迭代出的所有子集集合
     */
    private List<Comment> tempReplys = new ArrayList<>();

    /**
     * 递归迭代一个Comment的所有子集(包括本身)
     * @param comment
     */
    private void recursively(Comment comment) {
        tempReplys.add(comment);
        if (comment.getReplyComments().size() > 0) {
            List<Comment> replys = comment.getReplyComments();
            for(Comment reply : replys) {
                tempReplys.add(reply);
                if (reply.getReplyComments().size() > 0) {
                    recursively(reply);
                }
            }
        }
    }
}
