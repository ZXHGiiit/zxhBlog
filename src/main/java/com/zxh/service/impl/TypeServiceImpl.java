package com.zxh.service.impl;

import com.zxh.dao.TypeRepository;
import com.zxh.exception.NotFoundException;
import com.zxh.model.Type;
import com.zxh.service.RedisService;
import com.zxh.service.TypeService;
import com.zxh.util.JacksonUtils;
import com.zxh.vo.TypeVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2017/12/15.
 */

@Service
public class TypeServiceImpl implements TypeService {
    private static final Logger logger = LoggerFactory.getLogger(TypeServiceImpl.class);
    @Autowired
    private RedisService redisService;
    @Autowired
    private TypeRepository typeRepository;
    @Value("${redis.area}")
    private String AREA;
    @Value("${redis.types.top.key}")
    private String TYPES_TOP_KEY;

    @Transactional
    @Override
    public Type saveType(Type type) {
        return typeRepository.save(type);
    }

    @Transactional
    @Override
    public void deleteType(Long id) {
        redisService.del(AREA, TYPES_TOP_KEY);
        typeRepository.delete(id);
    }

    @Override
    public Type getType(Long id) {
        return typeRepository.findOne(id);
    }

    @Override
    public Type getTypeByName(String name) {
        return typeRepository.findByName(name);
    }


    @Override
    public Page<Type> listType(Pageable pageable) {
        return typeRepository.findAll(pageable);
    }

    @Transactional
    @Override
    public Type updateType(Long id, Type type) {
        redisService.del(AREA, TYPES_TOP_KEY);
        Type t = typeRepository.findOne(id);
        if(t == null){
            logger.error("TypeServiceImpl.updateType. id is not exist. id = {}", id);
            throw new NotFoundException("该类型不存在");
        }
        BeanUtils.copyProperties(type,t);
        return typeRepository.save(t);
    }

    @Override
    public List<Type> listType() {
        return typeRepository.findAll();
    }

    @Override
    public List<TypeVo> listTypeTop(Integer size) {
        try {
            String typesJson = redisService.get(AREA, TYPES_TOP_KEY);
            if(typesJson != null) {
                List<TypeVo> typeVos = JacksonUtils.jsonToList(typesJson, TypeVo.class);
                //使结果不大于size
                try {
                    typeVos = typeVos.subList(0, size);
                } catch (Exception e) {
                    logger.warn("TypeServiceImpl.listTypeTop.subList.ERROR.IndexOutOfBoundsException");
                }
                return typeVos;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Sort sort = new Sort(Sort.Direction.DESC, "blogs.size");
        Pageable pageable = new PageRequest(0, size, sort);
        List<Type> types = typeRepository.findTop(pageable);
        List<TypeVo> typeVos= toVo(types);
        //将结果放入redis缓存
        redisService.set(AREA, TYPES_TOP_KEY, JacksonUtils.toJson(typeVos));
        return typeVos;
    }


    private List<TypeVo> toVo(List<Type> types) {
        List<TypeVo> typeVos = new ArrayList<>();
        if(types != null) {
            for (Type type : types) {
                TypeVo vo = new TypeVo();
                vo.setId(type.getId());
                vo.setName(type.getName());
                vo.setSize(type.getBlogs().size());
                typeVos.add(vo);
            }
        }
        return typeVos;
    }
}
