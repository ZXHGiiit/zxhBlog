package com.zxh.dao;

import com.zxh.model.Type;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by admin on 2017/12/15.
 */
public interface TypeRespository extends JpaRepository<Type, Long>{

    Type findByName(String name);


}
