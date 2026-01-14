// src/com/semi/service/notice/NoticeService.java
package com.semi.service.notice;

import java.util.Map;
import com.semi.common.Search;
import com.semi.domain.Notice;

public interface NoticeService {
    // DAO의 insert가 long을 반환하므로, 계약서도 long으로 수정
    public long addNotice(Notice notice) throws Exception;
    
    // DAO의 findById가 long을 받으므로, 계약서도 long으로 수정
    public Notice getNotice(long noticeId) throws Exception;
    
    public Map<String,Object> listNotice(Search search) throws Exception;
    
    public int updateNotice(Notice notice) throws Exception;
    
    public int deleteNotice(long noticeId) throws Exception;
    

}
