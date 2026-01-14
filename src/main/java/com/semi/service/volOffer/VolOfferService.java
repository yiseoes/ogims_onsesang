package com.semi.service.volOffer;

import java.util.Map;

import com.semi.common.Search;
import com.semi.domain.VolOffer;

/*
 * 회원관리를 추상화 캡슐화한 VolOfferService Interface
 */
public interface VolOfferService {
	
    // 목록: totalCount, page, list 를 Map으로 반환
    Map<String, Object> getVolOfferList(Search search, String category, int pageUnit, String status, String regionLock) throws Exception;
    
	public void addVolOffer(VolOffer volOffer) throws Exception;
	
	//public VolOffer loginVolOffer(VolOffer volOffer) throws Exception;
	
	public VolOffer getVolOffer(Long postId) throws Exception; //findVolOffer
	
	public Map<String, Object> getVolOfferList(Search search, String region) throws Exception;
	
	public void updateVolOffer(VolOffer volOffer) throws Exception;
	
	//public boolean checkDuplication(String volOfferId) throws Exception;
	
	void deleteVolOffer(long id, String requesterId) throws Exception; //삭제(이서)
	
	// 상태 처리(모집중 -> 모집완료)
    int processVolRequest(long volunteerId) throws Exception;
	
}