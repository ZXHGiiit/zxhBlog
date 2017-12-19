package com.zxh.dao;

import com.zxh.model.Comment;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by admin on 2017/12/19.
 */
public interface CommentRepository extends JpaRepository<Comment, Long> {

    /**
     * 查询出一级评论
     * @param blogId
     * @param sort
     * @return
     */
    List<Comment> findByBlogIdAndParentCommentNull(Long blogId, Sort sort);
}
