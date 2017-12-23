package com.zxh.controller.admin;

import com.zxh.model.Tag;
import com.zxh.service.TagService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.naming.Binding;
import javax.validation.Valid;


/**
 * Created by admin on 2017/12/16.
 */
@Controller
@RequestMapping("/admin")
public class TagController {
    private static final Logger logger = LoggerFactory.getLogger(TagController.class);

    @Autowired
    private TagService tagService;

    /**
     * 标签页
     * @param pageable
     * @param model
     * @return
     */
    @GetMapping("/tags")
    public String tags(@PageableDefault(size = 3, sort = {"id"}, direction = Sort.Direction.DESC)
                               Pageable pageable, Model model) {
        Page<Tag> tags = tagService.listTag(pageable);
        logger.info("TagController.tags.info: {}", tags);
        model.addAttribute("page", tags);
        return "admin/tags";
    }


    /**
     * 输入页
     * @param model
     * @return
     */
    @GetMapping("/tags/input")
    public String showInputPage(Model model) {
        model.addAttribute("tag", new Tag());
        return "admin/tags-input";
    }

    /**
     * 修改页
     * @param model
     * @return
     */
    @GetMapping("/tags/{id}/input")
    public String showEditPage(@PathVariable Long id, Model model) {
        Tag tag = tagService.getTag(id);
        if(tag == null) {
            logger.error("TagController.showEditPage.ERROR.Tag is not exist. id: {}", id);
        }
        model.addAttribute("tag", tag);
        return "admin/tags-input";
    }

    /**
     * 新增Tag
     * @param tag
     * @param result
     * @param attributes
     * @return
     */
    @PostMapping("/tags")
    public String post(@Valid Tag tag, BindingResult result, RedirectAttributes attributes) {
        Tag tag1 = tagService.getTagByName(tag.getName());
        if(tag1 != null) {
            logger.error("TagController.post.ERROR.新增的Tag已存在. Tag: {}",tag1.toString());
            result.rejectValue("name", "nameError", "不能添加重复的类型");
        }
        if(result.hasErrors()) {
            logger.error("TagController.post.ERROR。tag校验不通过：{}", result.toString());
            return "admin/tags-input";
        }
        Tag tag2 = tagService.saveTag(tag);
        if(tag2 != null) {
            attributes.addFlashAttribute("message", "新增成功");
        } else {
            attributes.addFlashAttribute("message", "新增失败");
        }
        return "redirect:/admin/tags";
    }

    /**
     * 修改Tag
     * @param tag
     * @param result
     * @param id
     * @param attributes
     * @return
     */
    @PostMapping("/tags/{id}")
    public String editPost(@Valid Tag tag, BindingResult result,@PathVariable Long id, RedirectAttributes attributes) {
        Tag tag1 = tagService.getTagByName(tag.getName());
        if(tag1 != null) {
            logger.error("TagController.post.ERROR.新增的Tag已存在. Tag: {}",tag1.toString());
            result.rejectValue("name", "nameError", "不能添加重复的类型");
        }
        if(result.hasErrors()) {
            logger.error("TagController.post.ERROR。tag校验不通过：{}", result.toString());
            return "admin/tags-input";
        }
        Tag tag2 = tagService.updateTag(id, tag);
        if(tag2 == null) {
            attributes.addFlashAttribute("message", "修改成功");
        } else {
            attributes.addFlashAttribute("message", "修改失败");
        }
        return "/admin/tags-input";

    }
}
