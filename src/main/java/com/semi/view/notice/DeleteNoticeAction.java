package com.semi.view.notice;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.semi.framework.Action;
import com.semi.domain.Notice;
import com.semi.service.notice.NoticeService;
import com.semi.service.notice.impl.NoticeServiceImpl;

public class DeleteNoticeAction extends Action {

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {

        request.setCharacterEncoding("UTF-8");

        // 1. 파라미터 수집 (코드를 명확하고 단순하게 변경!)
        long noticeId = -1L;
        String noticeIdStr = request.getParameter("noticeId");

        // [디버깅] 넘어온 파라미터 값이랑, 숫자로 바꾼 값이 뭔지 확인!
        System.out.println("[디버깅] noticeId 파라미터: " + noticeIdStr);

        try {
            // noticeIdStr가 null이 아니거나 비어있지 않을 때만 숫자로 변환
            if (noticeIdStr != null && !noticeIdStr.trim().isEmpty()) {
                noticeId = Long.parseLong(noticeIdStr.trim());
            }
        } catch (NumberFormatException e) {
            // 숫자로 변환 실패 시 로그를 남기고, 유효하지 않은 요청으로 처리
            System.err.println("[에러] noticeId가 올바른 숫자 형식이 아닙니다: " + noticeIdStr);
            return "redirect:/listNotice.do?r=invalid";
        }

        System.out.println("[디버깅] 최종 noticeId 값: " + noticeId);

        // noticeId가 유효하지 않으면 목록으로 돌려보냄
        if (noticeId <= 0L) {
            return "redirect:/listNotice.do?r=invalid";
        }

        // 2. 서비스 로직 호출 (이 부분은 기존 코드와 동일)
        NoticeService service = new NoticeServiceImpl();

        // 2-1. (선택사항) 삭제 전에 해당 공지가 있는지 한번 더 확인하면 더 안전함!
        Notice exist = service.getNotice(noticeId);
        if (exist == null) {
            return "redirect:/listNotice.do?r=notfound";
        }

        // 3. 삭제 실행
        int result = 0;
        try {
            result = service.deleteNotice(noticeId);
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/listNotice.do?r=error";
        }

        // 4. 결과에 따라 분기
        if (result > 0) {
            // 삭제 성공!
            return "redirect:/listNotice.do?r=ok";
        } else {
            // 삭제는 시도했지만 실패 (이미 삭제되었거나 등등)
            return "redirect:/listNotice.do?r=fail";
        }
    }
}