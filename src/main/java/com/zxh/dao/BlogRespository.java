package com.zxh.dao;

import com.zxh.model.Blog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Created by admin on 2017/12/16.
 * 继承JpaSpecificationExecutor，来实现复杂的多条件分页查询
 */
public interface BlogRespository extends JpaRepository<Blog, Long>, JpaSpecificationExecutor<Blog> {

}
