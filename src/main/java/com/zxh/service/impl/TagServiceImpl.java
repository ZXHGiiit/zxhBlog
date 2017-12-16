package com.zxh.service.impl;

import com.zxh.dao.TagRespository;
import com.zxh.exception.NotFoundException;
import com.zxh.model.Tag;
import com.zxh.service.TagService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2017/12/16.
 */

@Service
public class TagServiceImpl implements TagService {
    private static final Logger logger = LoggerFactory.getLogger(TagServiceImpl.class);

    @Autowired
    private TagRespository tagRespository;

    @Transactional
    @Override
    public Tag saveTag(Tag tag) {
        return tagRespository.save(tag);
    }

    @Transactional
    @Override
    public void deleteTag(Long id) {
        tagRespository.delete(id);
    }

    @Transactional
    @Override
    public Tag updateTag(Long id, Tag tag) {
        Tag tag1 = tagRespository.findOne(id);
        if(tag1 == null) {
            logger.error("TagServiceImpl.updateTag.ERROR 不存在该标签. id:{}", id);
            throw new NotFoundException("不存在该标签");
        }
        BeanUtils.copyProperties(tag, tag1);
        return tagRespository.save(tag1);
    }

    @Override
    public Tag getTag(Long id) {
        return tagRespository.findOne(id);
    }

    @Override
    public Tag getTagByName(String name) {
        return tagRespository.findByName(name);
    }

    @Override
    public Page<Tag> listTag(Pageable pageable) {
        return tagRespository.findAll(pageable);
    }

    @Override
    public List<Tag> listTag() {
        return tagRespository.findAll();
    }

    @Override
    public List<Tag> listTag(String ids) {
        return tagRespository.findAll(convertToList(ids));
    }



    private List<Long> convertToList(String ids) {
        List<Long> list = new ArrayList<>();
        if(!"".equals(ids) && ids != null) {
            String[] idArr = ids.split(",");
            for(String id : idArr) {
                list.add(Long.parseLong(id));
            }
        }

        return list;
    }

}
