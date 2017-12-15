package com.zxh.service;

import com.zxh.model.User;

/**
 * Created by admin on 2017/12/15.
 */
public interface UserService {

    User findByUAP(String account, String password);
}
