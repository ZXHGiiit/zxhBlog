package com.zxh.service.impl;

import com.zxh.dao.ViewLogRepository;
import com.zxh.model.ViewLog;
import com.zxh.service.ViewLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by admin on 2017/12/20.
 */

@Service
public class ViewLogServiceImpl implements ViewLogService {
    @Autowired
    private ViewLogRepository viewLogRepository;

    @Override
    public List<ViewLog> listViewLogByIp(String ip) {
        return viewLogRepository.findByIp(ip);
    }

    @Override
    public List<Long> listBlogIdByIp(String ip) {
        List<ViewLog> viewLogs = viewLogRepository.findByIp(ip);
        return viewLogs.stream().map(i -> i.getBlogId()).collect(Collectors.toList());
    }

    @Override
    public ViewLog save(ViewLog viewLog) {
        return viewLogRepository.save(viewLog);
    }
}
