package com.zxh.service;

import com.zxh.model.Tag;
import com.zxh.vo.TagVo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Created by admin on 2017/12/16.
 */
public interface TagService {

    Tag saveTag(Tag tag);

    void deleteTag(Long id);

    Tag updateTag(Long id, Tag tag);

    Tag getTag(Long id);

    Tag getTagByName(String name);

    Page<Tag> listTag(Pageable pageable);

    List<Tag> listTag();

    /**
     * 通过ids来批量查询出Tag。每个Blog都可以对应多个Tag，用于查询出每个Blog的所有Tag
     * @param ids
     * @return
     */
    List<Tag> listTag(String ids);

    /**
     * 使用redis
     * @param size
     * @return
     */
    List<TagVo> listTagTop(Integer size);
}
