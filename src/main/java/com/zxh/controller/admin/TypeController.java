package com.zxh.controller.admin;

import com.sun.javafx.event.RedirectedEvent;
import com.zxh.model.Type;
import com.zxh.service.TypeService;
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

import javax.validation.Valid;

/**
 * Created by admin on 2017/12/15.
 */

@Controller
@RequestMapping("/admin")
public class TypeController {
    private static final Logger logger = LoggerFactory.getLogger(TypeController.class);

    @Autowired
    private TypeService typeService;
    /**
     * 获取新增类型页面
     * @param model
     * @return
     */
    @GetMapping("/types/input")
    public String showInputPage(Model model) {
        //修改页需要tag作为参数，防止空指针异常，这里传入一个空的tag对象
        model.addAttribute("type", new Type());
        return "admin/types-input";
    }

    /**
     * 获取修改类型页面，（新增页面中预置待修改类型的值）
     * @param id
     * @param model
     * @return
     */
    @GetMapping("/types/{id}/input")
    public String showEditPage(@PathVariable Long id, Model model) {
        Type type = typeService.getType(id);
        if(type == null) {
            logger.error("TypeController.showEditPage.Type is not exist. id: {}", id);
        }
        model.addAttribute("type", typeService.getType(id));
        return "admin/types-input";
    }

    /**
     * 获取类型页面
     * @param pageable
     * @param model
     * @return
     */
    @GetMapping("/types")
    public String types(@PageableDefault(size = 3, direction = Sort.Direction.DESC, sort = {"id"})
                                    Pageable pageable, Model model) {
        Page<Type> types = typeService.listType(pageable);
        logger.info("TypeController.types.info: {}", types);
        model.addAttribute("page", types);
        return "admin/types";
    }

    /**
     * 删除类型
     */
    @GetMapping("/types/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes attributes) {
        typeService.deleteType(id);
        attributes.addFlashAttribute("message", "删除成功");
        return "redirect:/admin/types";

    }


    /**
     * 新增类型
     *  Valid注解 是校验属性的作用，其结果用BinddngResult接受
     * @param type
     * @param result
     * @param attributes
     * @return
     */
    @PostMapping("/types")
    public String post(@Valid Type type, BindingResult result, RedirectAttributes attributes) {
        Type type1 = typeService.getTypeByName(type.getName());
        if(type1 != null) {
            logger.error("TypeController.post.ERROR.不能添加相同的类型: {}", type.getName());
            result.rejectValue("name", "nameError", "不能添加重复的分类");
        }
        if(result.hasErrors()) {
            logger.error("TypeController.post.ERROR.type校验不通过: {}",result.toString());
            return "admin/types-input";
        }
        Type type2 = typeService.saveType(type);
        if(type2 == null) {
            attributes.addFlashAttribute("message", "新增失败");
        } else  {
            attributes.addFlashAttribute("message", "新增成功");
        }

        return "redirect:/admin/types";
    }

    /**
     * 修改类型
     * @param type
     * @param result
     * @param id
     * @param attributes
     * @return
     */
    @RequestMapping("/types/{id}")
    public String editPost(@Valid Type type, BindingResult result, @PathVariable Long id, RedirectAttributes attributes) {
        Type type1 = typeService.getTypeByName(type.getName());
        if(type1 != null) {
            logger.error("TypeController.editPost.ERROR.不能添加相同的类型: {}", type.getName());
            result.rejectValue("name", "nameError", "不能添加重复的分类");
        }
        if(result.hasErrors()) {
            logger.error("TypeController.post.ERROR.type校验不通过: {}",result.toString());
            return "admin/types-input";
        }
        Type type2 = typeService.updateType(id, type);
        if(type2 == null) {
            attributes.addFlashAttribute("message", "更新失败");
        } else  {
            attributes.addFlashAttribute("message", "更新成功");
        }
        return "redirect:/admin/types";
    }

}
