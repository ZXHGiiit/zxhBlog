package com.zxh.dao;

import com.zxh.model.Type;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by admin on 2017/12/15.
 */
public interface TypeRespository extends JpaRepository<Type, Long>{

    Type findByName(String name);


    /**
     * 用Pageable实现排序，查询出前六个
     * @param pageable
     * @return
     */
    @Query("select t from Type t")
    List<Type> findTop(Pageable pageable);
}
