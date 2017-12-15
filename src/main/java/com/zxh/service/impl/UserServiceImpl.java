package com.zxh.service.impl;

import com.zxh.dao.UserRepository;
import com.zxh.model.User;
import com.zxh.service.UserService;
import com.zxh.util.MD5Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by admin on 2017/12/15.
 */

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public User findByUAP(String account, String password) {
        return userRepository.findByAccountAndPassword(account, MD5Utils.code(password));
    }
}
