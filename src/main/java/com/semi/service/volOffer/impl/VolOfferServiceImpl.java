package com.semi.service.volOffer.impl;

import java.util.Map;

import com.semi.common.Search;
import com.semi.domain.VolOffer;
import com.semi.service.volOffer.VolOfferService;
import com.semi.service.volOffer.dao.VolOfferDao;


public class VolOfferServiceImpl implements VolOfferService{
	
	///Field
	private VolOfferDao volOfferDao;
	
	///Constructor
	public VolOfferServiceImpl() {
		volOfferDao=new VolOfferDao();
	}

	///Method
	public void addVolOffer(VolOffer volOffer) throws Exception {
	volOfferDao.insertVolOffer(volOffer);
}

	public VolOffer getVolOffer(Long postId) throws Exception {
		return volOfferDao.findVolOffer(postId);
	}

    @Override
    public Map<String, Object> getVolOfferList(Search search, String category, int pageUnit, String status, String regionLock) throws Exception {
        return volOfferDao.getVolOfferList(search, category, pageUnit, status, regionLock);
    }
    
	public Map<String,Object> getVolOfferList(Search search, String region) throws Exception {
		return volOfferDao.getVolOfferList(search, region);
	}

	public void updateVolOffer(VolOffer volOffer) throws Exception {
		volOfferDao.updateVolOffer(volOffer);
	}
	
	@Override
    public int processVolRequest(long volunteerId) throws Exception {
        return volOfferDao.updateProcessVolOffer(volunteerId);
    }

//	public boolean checkDuplication(String volOfferId) throws Exception {
//		boolean result=true;
//		VolOffer volOffer=volOfferDao.findVolOffer(volOfferId);
//		if(volOffer != null) {
//			result=false;
//		}
//		return result;
//	}
	
	/**
     * 봉사제공 삭제(작성자 검증 포함)
     * - 인터페이스 시그니처: deleteVolOffer(long, String)
     * - 성공 시 조용히 종료, 실패/권한없음/없음은 예외 던짐
     */
	@Override
    public void deleteVolOffer(long volunteerId, String requesterId) throws Exception {
        System.out.println("[VolOfferServiceImpl] deleteVolOffer :: id=" + volunteerId + ", requester=" + requesterId);

        if (requesterId == null || requesterId.trim().isEmpty()) {
            System.out.println("[VolOfferServiceImpl][경고] 요청자 ID 없음");
            throw new IllegalStateException("로그인이 필요합니다.");
        }

        // 1) 작성자 조회
        String authorId = volOfferDao.findAuthorIdById(volunteerId);
        if (authorId == null) {
            System.out.println("[VolOfferServiceImpl] 대상 없음 :: id=" + volunteerId);
            throw new IllegalArgumentException("대상이 없습니다.");
        }

        // 2) 권한 체크(작성자만)
        if (!requesterId.equals(authorId)) {
            System.out.println("[VolOfferServiceImpl] 권한 없음 :: author=" + authorId + ", requester=" + requesterId);
            throw new SecurityException("작성자만 삭제할 수 있습니다.");
        }

        // 3) 삭제 실행 (제공글만: flag='O')
        int affected = volOfferDao.deleteVolOffer(volunteerId);
        System.out.println("[VolOfferServiceImpl] 삭제 결과 :: affected=" + affected);

        if (affected <= 0) {
            throw new IllegalStateException("삭제에 실패했습니다.");
        }
    }
	
}