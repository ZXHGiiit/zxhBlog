package com.zxh.dao;

import com.zxh.model.Blog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by admin on 2017/12/16.
 * 继承JpaSpecificationExecutor，来实现复杂的多条件分页查询
 */
public interface BlogRespository extends JpaRepository<Blog, Long>, JpaSpecificationExecutor<Blog> {
    Blog findByIdAndDeleteFlag(Long id, boolean deleteFlag);

    @Query("select t from Blog t where t.recommend = true")
    List<Blog> findReCommendTop(Pageable pageable);

    @Query("select t from Blog t where t.deleteFlag = false")
    List<Blog> findTop(Pageable pageable);
}
