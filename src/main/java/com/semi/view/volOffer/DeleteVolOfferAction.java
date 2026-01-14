package com.semi.view.volOffer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.semi.framework.Action;
import com.semi.service.volOffer.VolOfferService;
import com.semi.service.volOffer.impl.VolOfferServiceImpl;

/**
 * VolOffer 삭제 액션
 * - Service Layer를 호출하여 비즈니스 로직 위임
 * - 작성자와 세션 사용자가 일치하는지 2차 서버 검증 수행
 * - volunteer 테이블에서 flag='o' 인 행만 삭제
 * * 디버깅 출력은 한글로 합니다.
 */
public class DeleteVolOfferAction extends Action {

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
        
        // ===== 1) 파라미터 수신 =====
        String idParam = request.getParameter("volunteerId");
        if (idParam == null || idParam.trim().isEmpty()) {
            idParam = request.getParameter("postId");
        }
        System.out.println("[DeleteVolOfferAction] 요청 수신 :: volunteerId=" + idParam);

        if (idParam == null || idParam.trim().isEmpty()) {
            System.out.println("[DeleteVolOfferAction][경고] 식별자 파라미터 없음");
            return "redirect:/listVolOffer.do?error=bad_request";
        }

        long volunteerId;
        try {
            volunteerId = Long.parseLong(idParam);
        } catch (NumberFormatException e) {
            System.out.println("[DeleteVolOfferAction][경고] 숫자 변환 실패 :: " + idParam);
            return "redirect:/listVolOffer.do?error=bad_request";
        }

        // ===== 2) 세션 사용자 추출 =====
        HttpSession session = request.getSession(false);
        String sessionUserId = null;
        if (session != null && session.getAttribute("user") != null) {
        	com.semi.domain.User user = (com.semi.domain.User) session.getAttribute("user");
        	sessionUserId = user.getUserId();
        }
        
        if (sessionUserId == null || sessionUserId.trim().isEmpty()) {
            System.out.println("[DeleteVolOfferAction][경고] 로그인 정보 없음");
            return "redirect:/loginView.do?error=session_expired"; // 로그인 페이지로 이동
        }
        System.out.println("[DeleteVolOfferAction] 세션 사용자 :: " + sessionUserId);

        // ===== 3) Service 호출하여 삭제 위임 =====
        VolOfferService volOfferService = new VolOfferServiceImpl();
        try {
            volOfferService.deleteVolOffer(volunteerId, sessionUserId);
            
            // ===== 4) 성공 처리 =====
            System.out.println("[DeleteVolOfferAction] 삭제 처리 성공");
            return "redirect:/listVolOffer.do?r=delete_ok";
            
        } catch (IllegalArgumentException e) {
            // 삭제할 대상을 찾지 못한 경우
            System.out.println("[DeleteVolOfferAction][실패] " + e.getMessage());
            return "redirect:/listVolOffer.do?error=not_found";
        } catch (SecurityException e) {
            // 작성자와 로그인 사용자가 다른 경우 (권한 없음)
            System.out.println("[DeleteVolOfferAction][거부] " + e.getMessage());
            return "redirect:/detailVolOffer.do?postId=" + volunteerId + "&error=forbidden";
        } catch (IllegalStateException e) {
            // 그 외 삭제 실패
            System.out.println("[DeleteVolOfferAction][실패] " + e.getMessage());
            return "redirect:/listVolOffer.do?error=delete_fail";
        } catch (Exception e) {
            // 기타 예외
            System.out.println("[DeleteVolOfferAction][예외] " + e.getClass().getSimpleName() + " :: " + e.getMessage());
            e.printStackTrace();
            return "redirect:/listVolOffer.do?error=server_error";
        }
    }
}