package com.semi.view.notice;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.semi.framework.Action;
import com.semi.service.notice.NoticeService;
import com.semi.service.notice.impl.NoticeServiceImpl;
import com.semi.domain.Notice;

/**
 * 공지 수정 처리 액션
 * - POST /updateNotice.do
 * - 파라미터: noticeId, title, content
 * - 처리 후 상세 화면으로 리다이렉트
 */
public class UpdateNoticeAction extends Action {

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {

        request.setCharacterEncoding("UTF-8");

        final String noticeIdStr = nvl(request.getParameter("noticeId"));
        final String title       = nvl(request.getParameter("title"));
        final String content     = nvl(request.getParameter("content"));

        if (noticeIdStr.isEmpty() || title.isEmpty() || content.isEmpty()) {
            System.out.println("[UpdateNoticeAction] 필수 파라미터 누락");
            request.setAttribute("errorMessage", "필수 항목을 입력해 주세요.");
            request.setAttribute("noticeId", noticeIdStr);
            return "forward:/notice/updateNoticeView.jsp";
        }

        final long noticeId;
        try {
            noticeId = Long.parseLong(noticeIdStr);
        } catch (NumberFormatException e) {
            System.out.println("[UpdateNoticeAction] 잘못된 noticeId: " + noticeIdStr);
            request.setAttribute("errorMessage", "잘못된 공지 ID 입니다.");
            request.setAttribute("noticeId", noticeIdStr);
            return "forward:/notice/updateNoticeView.jsp";
        }

        NoticeService noticeService = new NoticeServiceImpl();

        // 원본 조회: getNotice(long) 사용
        Notice origin = noticeService.getNotice(noticeId);
        if (origin == null) {
            System.out.println("[UpdateNoticeAction] 공지 없음: noticeId=" + noticeId);
            request.setAttribute("errorMessage", "수정할 공지를 찾을 수 없습니다.");
            return "forward:/notice/listNoticeView.jsp";
        }

        // 변경 필드만 반영
        origin.setTitle(title);
        origin.setContent(content);

        // 업데이트 수행 (기존 시그니처 사용)
        noticeService.updateNotice(origin);

        // 리다이렉트 규칙은 프로젝트 관례에 따름
        return "redirect:/detailNotice.do?noticeId=" + noticeId;
    }

    // ===== 내부 유틸 =====
    private String nvl(String s) {
        return s == null ? "" : s.trim();
    }
}
