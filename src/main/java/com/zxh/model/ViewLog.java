package com.zxh.model;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by admin on 2017/12/20.
 * 用来记录查看博客记录
 */

@Entity
@Table(name = "view_log")
public class ViewLog {
    @Id
    @GeneratedValue
    private Long id;
    private String ip;
    private Long blogId;
    @Temporal(TemporalType.TIMESTAMP)
    private Date createTime;

    /**
     * 当实体类声明了其他带参构造方法时，需要显式声明不带参构造方法。
     * 所有持久化类必须要求有不带参的构造方法（也是JavaBean的规范）。Hibernate需要使用Java反射为你创建对象。
     */
    public ViewLog() {
    }

    public ViewLog(String ip, Long blogId, Date createTime) {
        this.ip = ip;
        this.blogId = blogId;
        this.createTime = createTime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Long getBlogId() {
        return blogId;
    }

    public void setBlogId(Long blogId) {
        this.blogId = blogId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
