package com.zxh.service;

import com.zxh.model.ViewLog;

import java.util.List;

/**
 * Created by admin on 2017/12/20.
 */
public interface ViewLogService {

    List<ViewLog> listViewLogByIp(String ip);

    List<Long>  listBlogIdByIp(String ip);

    ViewLog save(ViewLog viewLog);
}
