package com.zxh.service.impl;

import com.zxh.dao.BlogRepository;
import com.zxh.exception.NotFoundException;
import com.zxh.model.Blog;
import com.zxh.model.Type;
import com.zxh.model.ViewLog;
import com.zxh.service.BlogService;
import com.zxh.service.RedisService;
import com.zxh.service.ViewLogService;
import com.zxh.util.JacksonUtils;
import com.zxh.util.MarkdownUtils;
import com.zxh.util.MyBeanUtils;
import com.zxh.vo.BlogQuery;
import com.zxh.vo.BlogVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.*;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * Created by admin on 2017/12/16.
 */

@Service
public class BlogServiceImpl implements BlogService {
    private static final Logger logger = LoggerFactory.getLogger(BlogServiceImpl.class);

    @Autowired
    private HttpServletRequest request;
    @Autowired
    private RedisService redisService;
    @Autowired
    private BlogRepository blogRepository;
    @Autowired
    private ViewLogService viewLogService;

    @Value("${redis.area}")
    private String AREA;
    @Value("${redis.recommendblogs.top.key}")
    private String RECOMMEND_BLOGS_TOP_KEY;
    @Value("${redis.blogs.top.key}")
    private String BLOGS_TOP_KEY;

    @Override
    public Blog getBlog(Long id) {
        return blogRepository.findByIdAndDeleteFlag(id, false);
    }

    @Override
    public Page<Blog> listBlog(Pageable pageable, BlogQuery blog) {
        return blogRepository.findAll(new Specification<Blog>() {
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
                //predicates.add(cb.equal(root.<Boolean>get("deleteFlag"), false));
                cq.where(predicates.toArray(new Predicate[predicates.size()]));
                return null;
            }
        }, pageable);
    }

    @Override
    public Page<Blog> listBlog(Pageable pageable) {
        return blogRepository.findAll(pageable);
    }

    @Override
    public Page<Blog> listBlogByTagId(Long tagId, Pageable pageable) {
        return blogRepository.findAll(new Specification<Blog>() {
            @Override
            public Predicate toPredicate(Root<Blog> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
                Join join = root.join("tags");
                return cb.equal(join.get("id"),tagId);
            }
        },pageable);
    }

    @Transactional
    @Override
    public Blog saveBlog(Blog blog) {
        redisService.del(AREA, BLOGS_TOP_KEY);
        redisService.del(AREA, RECOMMEND_BLOGS_TOP_KEY);
        if(blog.getFlag() == null || "".equals(blog.getFlag())) {
            blog.setFlag("原创");
        }
        if(blog.getId() == null) {
            //可以在数据库设置默认值和触发器
            blog.setCreateTime(new Date());
            blog.setUpdateTime(new Date());
            blog.setViews(0);
        } else {
            blog.setUpdateTime(new Date());
        }

        return blogRepository.save(blog);
    }


    @Transactional
    @Override
    public Blog updateBlog(Long id, Blog blog) {
        redisService.del(AREA, BLOGS_TOP_KEY);
        redisService.del(AREA, RECOMMEND_BLOGS_TOP_KEY);
        Blog blog1 = blogRepository.findOne(id);
        if(blog1 == null) {
            logger.error("BlogServiceImpl.updateBlog.ERROR.不存在此id的blog, id: {}", id);
            throw new NotFoundException("该博客不存在");
        }
        //防止为空的属性覆盖了数据库。如view这个字段前端没有传过来，我们需要把它忽略
        BeanUtils.copyProperties(blog, blog1, MyBeanUtils.getNullPropertyName(blog));
        blog1.setUpdateTime(new Date());//可在数据库设置触发器
        return blogRepository.save(blog1);
    }

    @Transactional
    @Override
    public void deleteBlog(Long id) {
        redisService.del(AREA, BLOGS_TOP_KEY);
        redisService.del(AREA, RECOMMEND_BLOGS_TOP_KEY);
        Blog blog = blogRepository.findOne(id);
        if(blog == null) {
            logger.error("BlogServiceImpl.deleteBlog.ERROR.blog is not exist. id: {}", id);
        }
        blog.setDeleteFlag(true);
        blogRepository.save(blog);
    }

    @Override
    public List<Blog> listBlog() {
        return blogRepository.findAll();
    }

    @Override
    public List<BlogVo> listReCommendBlogTop(Integer size) {
        try {
            String blogVos = redisService.get(AREA, RECOMMEND_BLOGS_TOP_KEY);
            if(!StringUtils.isEmpty(blogVos)) {
                return JacksonUtils.jsonToList(blogVos, BlogVo.class);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Sort sort = new Sort(Sort.Direction.DESC, "updateTime");
        Pageable pageable = new PageRequest(0, size, sort);
        List<Blog> blogTop = blogRepository.findReCommendTop(pageable);
        List<BlogVo> blogVos = toVo(blogTop);
        redisService.set(AREA, RECOMMEND_BLOGS_TOP_KEY, blogVos);
        return blogVos;
    }

    @Override
    public List<BlogVo> listBlogTop(Integer size) {
        try {
            String blogVos = redisService.get(AREA, BLOGS_TOP_KEY);
            if(!StringUtils.isEmpty(blogVos)) {
                return JacksonUtils.jsonToList(blogVos, BlogVo.class);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Sort sort = new Sort(Sort.Direction.DESC, "updateTime");
        Pageable pageable = new PageRequest(0, size, sort);
        List<Blog> blogTop = blogRepository.findTop(pageable);
        List<BlogVo> blogVos = toVo(blogTop);
        redisService.set(AREA, BLOGS_TOP_KEY, blogVos);//不设置过期，有博客更新时，同时更新redis缓存
        return blogVos;
    }

    @Override
    public Page<Blog> listPage(String query, Pageable pageable) {
        query = "%" + query + "%";//实现模糊查询
        return blogRepository.findByQuery(query, pageable);
    }

    @Override
    public Blog getAndConvert(Long id) {
        Blog blog = blogRepository.findOne(id);
        if(blog == null) {
            throw new NotFoundException("该博客不存在");
        }

        //创建一个新的Blog对象，对该新对象进行Markdown格式转换并返回给前端，防止对数据库的数据修改
        Blog blog1 = new Blog();
        BeanUtils.copyProperties(blog, blog1);
        blog1.setContent(MarkdownUtils.markdownToHtmlExtensions(blog.getContent()));
        //改变view
        String ip = request.getRemoteAddr();
        if(ip == null) {
            blogRepository.updateViews(id);
            return blog1;
        }
        Boolean isView = redisService.get(ip, String.valueOf(id), Boolean.class);
        if(isView == null) {
            //将该IP的访问记录临时放入redis中
            redisService.set(ip, String.valueOf(id), true, 100);
            //从数据库中查询出该ip的访问记录
            List<Long> blogIds = viewLogService.listBlogIdByIp(ip);
            if(!blogIds.contains(id)) {
                //访问记录存入数据库
                viewLogService.save(new ViewLog(ip, id, new Date()));
                //blog.view+1
                blogRepository.updateViews(id);
            }
        }
        return blog1;
    }

    @Override
    public Long countBlog() {
        return blogRepository.count();
    }

    @Override
    public Map<String, List<Blog>> archiveBlog() {
        //获取年分
        List<String> years = blogRepository.findGroupYear();
        Map<String, List<Blog>> map = new HashMap<>();
        for(String year : years) {
            map.put(year, blogRepository.findByYear(year));
        }

        return map;
    }

    private List<BlogVo> toVo(List<Blog> blogs) {
        List<BlogVo> blogVos = new ArrayList<>();
        if(blogs != null) {
            for(Blog blog : blogs) {
                BlogVo vo = new BlogVo();
                vo.setId(blog.getId());
                vo.setTitle(blog.getTitle());
                blogVos.add(vo);
            }
        }
        return blogVos;
    }
}
