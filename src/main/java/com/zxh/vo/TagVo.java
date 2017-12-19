package com.zxh.vo;

/**
 * Created by admin on 2017/12/19.
 * 用于转换为json，将里面的属性List<Blog>去掉，防止无限循环（因为Blog里面含有List<Tag>）
 */
public class TagVo {
    private Long id;
    private String name;
    private int size;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }
}
