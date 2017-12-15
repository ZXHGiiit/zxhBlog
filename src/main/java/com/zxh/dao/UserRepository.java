package com.zxh.dao;

import com.zxh.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by admin on 2017/12/15.
 */
public interface UserRepository extends JpaRepository<User, Long> {

    User findByAccountAndPassword(String acount, String password);

}
