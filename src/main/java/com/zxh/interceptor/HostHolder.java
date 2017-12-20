package com.zxh.interceptor;

import com.zxh.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by admin on 2017/12/20.
 * 存储用户的一些信息
 */
public class HostHolder {
    private static final Logger LOG = LoggerFactory.getLogger(HostHolder.class);

    @Autowired
    private HttpServletRequest request;


    private static final String userKey = "user_key_heheda";
    private static final String userIdKey = "user_id_key_heheda";
    private static final String userIpKey = "user_ip_key_heheda";


    public User getUser() {
        //LOG.info("HostHolder.request = " + request.hashCode());
        return (User) request.getSession().getAttribute(userKey);
    }

    public long getUserId() {
        return (Long) request.getSession().getAttribute(userIdKey);
    }

    public String getUserIp() {
        return (String) request.getSession().getAttribute(userIpKey);
    }


    public void setUser(User user) {
        //LOG.info("HostHolder.request = " + request.hashCode());
        request.getSession().setAttribute(userKey, user);
    }

    public void setUserId(long userId) {
        request.getSession().setAttribute(userIdKey, userId);
    }

    public void setUserIp(String userIp) {
        request.getSession().setAttribute(userIpKey, userIp);
    }




}
