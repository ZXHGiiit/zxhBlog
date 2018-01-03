package com.zxh.vo;

/**
 * Created by admin on 2018/1/3.
 * 用于转换为json,防止出现死循环
 */
public class BlogVo {
    private Long id;
    private String title;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
