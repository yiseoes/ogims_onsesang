package com.semi.view.volOffer;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.semi.domain.User;
import com.semi.domain.VolOffer;
import com.semi.framework.Action;
import com.semi.service.volOffer.VolOfferService;
import com.semi.service.volOffer.impl.VolOfferServiceImpl;


public class DetailVolOfferAction extends Action{

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {

		// 세션에서 userId를 가져오는 코드
		HttpSession session = request.getSession();
    	Object loginUser = (session == null) ? null : session.getAttribute("user");
    	if (loginUser == null) return "redirect:/loginView.do?msg=loginRequired";
		
		User user = (User) session.getAttribute("user");
	    String userId = null;
	    if (user != null) {
	        userId = user.getUserId();
	        System.out.println("로그인된 사용자 ID (userId): " + userId); // userId 로그 출력
	    } else {
	        System.out.println("로그인된 사용자 ID (userId): 없음-로그인되지 않은 상태 입니다.");
	    }
        System.out.println("1111111111111111111111111111111111111    volunteerId   " ); // postIdStr 로그 출력
		String postIdStr = request.getParameter("volunteerId");// 파라미터에서 postId를 가져오는 코드
        System.out.println("로그인된 사용자 postIdStr (postIdStr): " + postIdStr); // postIdStr 로그 출력
		Long postId = Long.parseLong(postIdStr);// postId 값을 Long 타입으로 변환

        System.out.println("로그인된 사용자 postId (postId): " + postId); // postIdStr 로그 출력
		VolOfferService volOfferService=new VolOfferServiceImpl();
		VolOffer volOffer=volOfferService.getVolOffer(postId);

		// volOffer가 null인 경우 처리
		if (volOffer == null) {
			request.setAttribute("errorMsg", "해당 게시글을 찾을 수 없습니다.");
			return "redirect:/listVolOffer.do";
		}

	    // 세션에 VolOffer 저장
	    request.getSession().setAttribute("volOffer", volOffer);

	    LocalDateTime st = volOffer.getStartTime();
        LocalDateTime et = volOffer.getEndTime();
        request.setAttribute("startTime", (st != null) ? Timestamp.valueOf(st) : null);
        request.setAttribute("endTime",   (et != null) ? Timestamp.valueOf(et) : null);
	   
	    // 로그인된 사용자 정보가 있으면 세션에 저장
	    if (userId != null) {
	        session.setAttribute("userId", userId);
	    }
        request.setAttribute("searchCondition", request.getParameter("searchCondition"));
        request.setAttribute("searchKeyword",   request.getParameter("searchKeyword"));
        request.setAttribute("status",          request.getParameter("status"));
	    
	    //request.setAttribute("volOffer", volOffer);
		
		return "forward:/volOffer/detailVolOffer.jsp";
	}
}