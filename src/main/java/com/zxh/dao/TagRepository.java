package com.zxh.dao;

import com.zxh.model.Tag;
import org.hibernate.validator.constraints.URL;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by admin on 2017/12/16.
 */
public interface TagRepository extends JpaRepository<Tag, Long>{

    Tag findByName(String name);

    @Query("select t from Tag t")
    List<Tag> findTop(Pageable pageable);
}
