package com.zxh.service.impl;

import com.zxh.dao.BlogRespository;
import com.zxh.exception.NotFoundException;
import com.zxh.model.Blog;
import com.zxh.model.Type;
import com.zxh.service.BlogService;
import com.zxh.util.MyBeanUtils;
import com.zxh.vo.BlogQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by admin on 2017/12/16.
 */

@Service
public class BlogServiceImpl implements BlogService {
    private static final Logger logger = LoggerFactory.getLogger(BlogServiceImpl.class);

    @Autowired
    private BlogRespository blogRespository;

    @Override
    public Blog getBlog(Long id) {
        return blogRespository.findByIdAndDeleteFlag(id, false);
    }

    @Override
    public Page<Blog> listBlog(Pageable pageable, BlogQuery blog) {
        return blogRespository.findAll(new Specification<Blog>() {
            @Override
            public Predicate toPredicate(Root<Blog> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList<>();
                if (!"".equals(blog.getTitle()) && blog.getTitle() != null) {
                    predicates.add(cb.like(root.<String>get("title"), "%"+blog.getTitle()+"%"));
                }
                if (blog.getTypeId() != null) {
                    predicates.add(cb.equal(root.<Type>get("type").get("id"), blog.getTypeId()));
                }
                if (blog.isRecommend()) {
                    predicates.add(cb.equal(root.<Boolean>get("recommend"), blog.isRecommend()));
                }
                predicates.add(cb.equal(root.<Boolean>get("deleteFlag"), false));
                cq.where(predicates.toArray(new Predicate[predicates.size()]));
                return null;
            }
        }, pageable);
    }

    @Override
    public Page<Blog> listBlog(Pageable pageable) {
        return blogRespository.findAll(pageable);
    }

    @Transactional
    @Override
    public Blog saveBlog(Blog blog) {
        if(blog.getId() == null) {
            //可以在数据库设置默认值和触发器
            blog.setCreateTime(new Date());
            blog.setUpdateTime(new Date());
            blog.setViews(0);
        } else {
            blog.setUpdateTime(new Date());
        }

        return blogRespository.save(blog);
    }


    @Transactional
    @Override
    public Blog updateBlog(Long id, Blog blog) {
        Blog blog1 = blogRespository.findOne(id);
        if(blog1 == null) {
            logger.error("BlogServiceImpl.updateBlog.ERROR.不存在此id的blog, id: {}", id);
            throw new NotFoundException("该博客不存在");
        }
        //防止为空的属性覆盖了数据库。如view这个字段前端没有传过来，我们需要把它忽略
        BeanUtils.copyProperties(blog, blog1, MyBeanUtils.getNullPropertyName(blog));
        blog1.setUpdateTime(new Date());//可在数据库设置触发器
        return blogRespository.save(blog1);
    }

    @Transactional
    @Override
    public void deleteBlog(Long id) {
        Blog blog = blogRespository.findOne(id);
        if(blog == null) {
            logger.error("BlogServiceImpl.deleteBlog.ERROR.blog is not exist. id: {}", id);
        }
        blog.setDeleteFlag(true);
        blogRespository.save(blog);
    }

    @Override
    public List<Blog> listBlog() {
        return blogRespository.findAll();
    }

    @Override
    public List<Blog> listReCommendBlogTop(Integer size) {
        Sort sort = new Sort(Sort.Direction.DESC, "updateTime");
        Pageable pageable = new PageRequest(0, size, sort);
        return blogRespository.findReCommendTop(pageable);
    }
}
