package com.zxh.dao;

import com.zxh.model.ViewLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by admin on 2017/12/20.
 */
public interface ViewLogRepository extends JpaRepository<ViewLog, Long> {

    List<ViewLog> findByIp(String ip);
}
