package com.zxh.service.impl;

import com.zxh.dao.TypeRespository;
import com.zxh.exception.NotFoundException;
import com.zxh.model.Type;
import com.zxh.service.TypeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by admin on 2017/12/15.
 */

@Service
public class TypeServiceImpl implements TypeService {
    private static final Logger logger = LoggerFactory.getLogger(TypeServiceImpl.class);

    @Autowired
    private TypeRespository typeRespository;

    @Transactional
    @Override
    public Type saveType(Type type) {
        return typeRespository.save(type);
    }

    @Transactional
    @Override
    public void deleteType(Long id) {
        typeRespository.delete(id);
    }

    @Override
    public Type getType(Long id) {
        return typeRespository.findOne(id);
    }

    @Override
    public Type getTypeByName(String name) {
        return typeRespository.findByName(name);
    }


    @Override
    public Page<Type> listType(Pageable pageable) {
        return typeRespository.findAll(pageable);
    }

    @Transactional
    @Override
    public Type updateType(Long id, Type type) {
        Type t = typeRespository.findOne(id);
        if(t == null){
            logger.error("TypeServiceImpl.updateType. id is not exist. id = {}", id);
            throw new NotFoundException("该类型不存在");
        }
        BeanUtils.copyProperties(type,t);
        return typeRespository.save(t);
    }

    @Override
    public List<Type> listType() {
        return typeRespository.findAll();
    }

    @Override
    public List<Type> listTypeTop(Integer size) {
        Sort sort = new Sort(Sort.Direction.DESC, "blogs.size");
        Pageable pageable = new PageRequest(0, size, sort);
        return typeRespository.findTop(pageable);
    }
}
