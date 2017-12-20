package com.zxh.controller;

import com.zxh.model.Tag;
import com.zxh.service.BlogService;
import com.zxh.service.RedisService;
import com.zxh.service.TagService;
import com.zxh.vo.TagVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

/**
 * Created by admin on 2017/12/20.
 */

@Controller
public class TagShowController {
    @Autowired
    private TagService tagService;
    @Autowired
    private BlogService blogService;
    @Autowired
    private RedisService redisService;
    /**
     * 标签页面
     * id=-1,未选择具体标签,就默认第一个
     * @param pageable
     * @param id
     * @param model
     * @return
     */
    @GetMapping("/tags/{id}")
    public String tags(@PageableDefault(sort = {"updateTime"}, size = 8, direction = Sort.Direction.DESC)
                               Pageable pageable, @PathVariable Long id, Model model) {
        //获取所有标签

        List<TagVo> tagVos = tagService.listTagTop(10000);


        if(id == -1) {
            id= tagVos.get(0).getId();
        }
        model.addAttribute("tags", tagVos);
        model.addAttribute("page", blogService.listBlogByTagId(id, pageable));
        model.addAttribute("activeTagId", id);
        return "tags";
    }

}
