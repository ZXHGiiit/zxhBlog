package com.zxh.dao;

import com.zxh.model.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by admin on 2017/12/16.
 */
public interface TagRespository extends JpaRepository<Tag, Long>{

    Tag findByName(String name);


}
