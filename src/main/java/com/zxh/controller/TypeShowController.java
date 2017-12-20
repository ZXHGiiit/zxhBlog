package com.zxh.controller;

import com.zxh.service.BlogService;
import com.zxh.service.RedisService;
import com.zxh.service.TypeService;
import com.zxh.vo.BlogQuery;
import com.zxh.vo.TypeVo;
import org.springframework.beans.factory.annotation.Autowired;
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
public class TypeShowController {
    @Autowired
    private RedisService redisService;
    @Autowired
    private TypeService typeService;
    @Autowired
    private BlogService blogService;


    @GetMapping("/types/{id}")
    public String types(@PageableDefault(size = 8, sort = {"updateTime"}, direction = Sort.Direction.DESC)
                                    Pageable pageable,@PathVariable Long id, Model model) {
        //TODO 添加Redis
        List<TypeVo> typeVos = typeService.listTypeTop(10000);
        if(id == -1) {
            id = typeVos.get(0).getId();
        }
        BlogQuery blogQuery = new BlogQuery();
        blogQuery.setTypeId(id);
        model.addAttribute("types", typeVos);
        model.addAttribute("page", blogService.listBlog(pageable, blogQuery));
        model.addAttribute("activeTypeId", id);
        return "types";
    }
}
