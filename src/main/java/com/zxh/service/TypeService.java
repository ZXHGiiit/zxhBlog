package com.zxh.service;

import com.zxh.model.Type;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Created by admin on 2017/12/15.
 */
public interface TypeService {

    Type saveType(Type type);

    void deleteType(Long id);

    Type getType(Long id);

    Type getTypeByName(String name);

    Page<Type> listType(Pageable pageable);

    Type updateType(Long id, Type type);
}
