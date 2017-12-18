package com.zxh.service;

import com.zxh.model.Blog;
import com.zxh.vo.BlogQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Created by admin on 2017/12/16.
 */
public interface BlogService {
    Blog getBlog(Long id);

    Page<Blog> listBlog(Pageable pageable, BlogQuery blogQuery);

    Page<Blog> listBlog(Pageable pageable);

    Blog saveBlog(Blog blog);

    Blog updateBlog(Long id, Blog blog);

    void deleteBlog(Long id);

    List<Blog> listBlog();

    /**
     * 最新推荐的博客
     * @param size
     * @return
     */
    List<Blog> listReCommendBlogTop(Integer size);

    /**
     * 最新的博客
     * @param size
     * @return
     */
    List<Blog> listBlogTop(Integer size);

    /**
     * 全局查询，带分页
     * @param query
     * @param pageable
     * @return
     */
    Page<Blog> listPage(String query, Pageable pageable);
}
