package com.semi.view.notice;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.semi.framework.Action;
import com.semi.service.notice.NoticeService;
import com.semi.service.notice.impl.NoticeServiceImpl;
import com.semi.domain.Notice;

/**
 * 공지 수정 화면 진입 액션
 * - 파라미터 noticeId로 공지 단건 조회
 * - request.setAttribute("notice", vo) 바인딩 후 수정화면으로 포워드
 */
public class UpdateNoticeViewAction extends Action {

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {

        request.setCharacterEncoding("UTF-8");

        final String noticeIdStr = nvl(request.getParameter("noticeId"));
        if (noticeIdStr.isEmpty()) {
            System.out.println("[UpdateNoticeViewAction] 누락: noticeId");
            request.setAttribute("errorMessage", "수정할 공지 ID가 없습니다.");
            return "forward:/notice/listNoticeView.jsp";
        }

        final long noticeId;
        try {
            noticeId = Long.parseLong(noticeIdStr);
        } catch (NumberFormatException e) {
            System.out.println("[UpdateNoticeViewAction] 잘못된 noticeId: " + noticeIdStr);
            request.setAttribute("errorMessage", "잘못된 공지 ID 입니다.");
            return "forward:/notice/listNoticeView.jsp";
        }

        // 서비스 메서드명: getNotice(long) 사용
        NoticeService noticeService = new NoticeServiceImpl();
        Notice notice = noticeService.getNotice(noticeId);

        if (notice == null) {
            System.out.println("[UpdateNoticeViewAction] 공지 없음: noticeId=" + noticeId);
            request.setAttribute("errorMessage", "수정할 공지를 찾을 수 없습니다.");
            return "forward:/notice/listNoticeView.jsp";
        }

        request.setAttribute("notice", notice);
        return "forward:/notice/updateNoticeView.jsp";
    }

    // ===== 내부 유틸 =====
    private String nvl(String s) {
        return s == null ? "" : s.trim();
    }
}
