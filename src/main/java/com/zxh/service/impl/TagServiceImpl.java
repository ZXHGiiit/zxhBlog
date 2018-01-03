package com.zxh.service.impl;

import com.zxh.dao.TagRepository;
import com.zxh.exception.NotFoundException;
import com.zxh.model.Tag;
import com.zxh.service.RedisService;
import com.zxh.service.TagService;
import com.zxh.util.JacksonUtils;
import com.zxh.vo.TagVo;
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
    private RedisService redisService;
    @Autowired
    private TagRepository tagRepository;
    @Value("${redis.area}")
    private String AREA;
    @Value("${redis.tags.top.key}")
    private String TAGS_TOP_KEY;

    @Transactional
    @Override
    public Tag saveTag(Tag tag) {
        redisService.del(AREA, TAGS_TOP_KEY);//删除缓存
        return tagRepository.save(tag);
    }

    @Transactional
    @Override
    public void deleteTag(Long id) {
        redisService.del(AREA, TAGS_TOP_KEY);//删除缓存
        tagRepository.delete(id);
    }

    @Transactional
    @Override
    public Tag updateTag(Long id, Tag tag) {
        redisService.del(AREA, TAGS_TOP_KEY);//删除缓存
        Tag tag1 = tagRepository.findOne(id);
        if(tag1 == null) {
            logger.error("TagServiceImpl.updateTag.ERROR 不存在该标签. id:{}", id);
            throw new NotFoundException("不存在该标签");
        }
        BeanUtils.copyProperties(tag, tag1);
        return tagRepository.save(tag1);
    }

    @Override
    public Tag getTag(Long id) {
        return tagRepository.findOne(id);
    }

    @Override
    public Tag getTagByName(String name) {
        return tagRepository.findByName(name);
    }

    @Override
    public Page<Tag> listTag(Pageable pageable) {
        return tagRepository.findAll(pageable);
    }

    @Override
    public List<Tag> listTag() {
        return tagRepository.findAll();
    }

    @Override
    public List<Tag> listTag(String ids) {
        return tagRepository.findAll(convertToList(ids));
    }


    @Override
    public List<TagVo> listTagTop(Integer size) {
        try {
            String tagsJson = redisService.get(AREA, TAGS_TOP_KEY);
            //logger.info("TagServiceImpl.listTagTop.tagsJson:{}", tagsJson);
            if(!StringUtils.isEmpty(tagsJson)) {
                //logger.info("TagServiceImpl.listTagTop.redis has the key-value:{}", tagsJson);
                List<TagVo> tagVos = JacksonUtils.jsonToList(tagsJson, TagVo.class);
                //当只查询前6个tag，redis可能存储了所有的tag，需要剪切
                try {
                    tagVos = tagVos.subList(0, size);
                } catch (Exception e) {
                    logger.warn("TagServiceImpl.listTagTop.subList.IndexOutOfBoundsException");
                }

                return tagVos;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        Sort sort = new Sort(Sort.Direction.DESC, "blogs.size");//以blogs.size作为排序依据
        Pageable pageable= new PageRequest(0, size, sort);
        List<Tag> tags = tagRepository.findTop(pageable);
        List<TagVo> tagVos = toVo(tags);
        //将结果放入redis缓存
        redisService.set(AREA, TAGS_TOP_KEY, JacksonUtils.toJson(tagVos));
        return tagVos;
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

    private List<TagVo> toVo(List<Tag> tags) {
        List<TagVo> tagVos = new ArrayList<>();
        if(tags != null) {
            for(Tag tag : tags) {
                TagVo vo = new TagVo();
                vo.setId(tag.getId());
                vo.setName(tag.getName());
                vo.setSize(tag.getBlogs().size());
                tagVos.add(vo);
            }
        }
        return tagVos;
    }


    public static void main(String[] args) {
        List<String> list = new ArrayList<>();
        list.add("a");
        list.add("b");
        list.add("c");
        try {
            list = list.subList(0,2);
        } catch (Exception e) {
            System.out.println("出错了");
        }
        System.out.println(list);
    }

}
