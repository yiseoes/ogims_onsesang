// src/com/semi/service/notice/impl/NoticeServiceImpl.java
package com.semi.service.notice.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.semi.common.Search;
import com.semi.domain.Notice;
import com.semi.service.notice.NoticeService;
import com.semi.service.notice.dao.NoticeDao;

public class NoticeServiceImpl implements NoticeService {

    private NoticeDao noticeDao = new NoticeDao();

    @Override
    public long addNotice(Notice notice) throws Exception {
        return noticeDao.insert(notice);
    }

    @Override
    public Notice getNotice(long noticeId) throws Exception {
        return noticeDao.findById(noticeId);
    }

    @Override
    public Map<String, Object> listNotice(Search search) throws Exception {

        int currentPage = search.getCurrentPage() > 0 ? search.getCurrentPage() : 1;
        int pageSize = search.getPageSize() > 0 ? search.getPageSize() : 10;
        int startRow = (currentPage - 1) * pageSize + 1;
        int endRow = currentPage * pageSize;
        String keyword = search.getSearchKeyword() == null ? "" : search.getSearchKeyword();
        
        List<Notice> list = noticeDao.findList(startRow, endRow, keyword);
        int totalCount = noticeDao.count(keyword);

        Map<String, Object> map = new HashMap<>();
        map.put("noticeList", list);
        map.put("totalCount", totalCount);
        return map;
    }
    
    @Override
    public int updateNotice(Notice notice) throws Exception {
        return noticeDao.update(notice);
    }

    @Override
    public int deleteNotice(long noticeId) throws Exception {
        return noticeDao.delete(noticeId);
    }
}