// src/main/java/com/semi/service/volRequest/VolRequestService.java
package com.semi.service.volRequest;

import java.util.List;
import java.util.Map;

import com.semi.common.Page;
import com.semi.common.Search;
import com.semi.domain.VolRequest;
import com.semi.service.volRequest.dto.VolRequestListItem;

/**
 * 봉사요청 서비스 인터페이스
 * - 지역, 작성자ID 필수 / 카테고리 선택 / 상태 기본 '모집중'
 */
public interface VolRequestService {

    // 목록: totalCount, page, list 를 Map으로 반환
    Map<String, Object> getVolRequestList(Search search, String category, int pageUnit, String status, String regionLock) throws Exception;

    // 상세(도메인만)
    VolRequest getVolRequest(long volunteerId) throws Exception;

    // 상세(+작성자이름 포함 반환이 필요할 때)
    Map<String, Object> getVolRequestDetail(long volunteerId) throws Exception;

    // 등록(생성된 volunteerId 반환)
    long addVolRequest(VolRequest req) throws Exception;

    // 수정
    int updateVolRequest(VolRequest req) throws Exception;

    // 상태 처리(모집중 -> 모집완료)
    int processVolRequest(long volunteerId) throws Exception;

    // 삭제
    int deleteVolRequest(long volunteerId) throws Exception;
    
    /** 물리 파일 삭제/교체를 위한 image 단건 조회 */
    String findImageByVolunteerId(long volunteerId) throws Exception;
}
