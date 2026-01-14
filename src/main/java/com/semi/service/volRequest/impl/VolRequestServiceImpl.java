// src/main/java/com/semi/service/volRequest/impl/VolRequestServiceImpl.java
package com.semi.service.volRequest.impl;

import java.util.HashMap;
import java.util.Map;

import com.semi.common.Search;
import com.semi.domain.VolRequest;
import com.semi.service.volRequest.VolRequestService;
import com.semi.service.volRequest.dao.VolRequestDao;

public class VolRequestServiceImpl implements VolRequestService {

    private final VolRequestDao volRequestDao = new VolRequestDao();

    @Override
    public Map<String, Object> getVolRequestList(Search search, String category, int pageUnit, String status, String regionLock) throws Exception {
        return volRequestDao.getVolRequestList(search, category, pageUnit, status, regionLock);
    }

    @Override
    public VolRequest getVolRequest(long volunteerId) throws Exception {
        return volRequestDao.getDetailVolRequest(volunteerId);
    }

    @Override
    public Map<String, Object> getVolRequestDetail(long volunteerId) throws Exception {
        // 상세 + 작성자 이름만 합쳐서 전달 (한 줄 결합)
        VolRequest item = volRequestDao.getDetailVolRequest(volunteerId);
        String authorName = volRequestDao.getAuthorNameByVolunteerId(volunteerId);
        Map<String, Object> map = new HashMap<>();
        map.put("item", item);
        map.put("authorName", authorName);
        return map;
    }

    @Override
    public long addVolRequest(VolRequest req) throws Exception {
        return volRequestDao.addVolRequest(req);
    }

    @Override
    public int updateVolRequest(VolRequest req) throws Exception {
        return volRequestDao.updateVolRequest(req);
    }

    @Override
    public int processVolRequest(long volunteerId) throws Exception {
        return volRequestDao.updateProcessVolRequest(volunteerId);
    }

    @Override
    public int deleteVolRequest(long volunteerId) throws Exception {
        return volRequestDao.deleteVolRequest(volunteerId);
    }
    
    @Override
    public String findImageByVolunteerId(long volunteerId) throws Exception {
        return volRequestDao.findImageByVolunteerId(volunteerId);
    }
}
