package com.semi.view.notice;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.semi.framework.Action;
import com.semi.domain.Notice;
import com.semi.service.notice.NoticeService;
import com.semi.service.notice.impl.NoticeServiceImpl;

/**
 * 공지 상세 조회 액션
 * - 정상: 상세 JSP로 forward
 * - 오류/누락: 목록 액션(.do)으로 redirect  ← 톤 통일(탑에서 .do 받는 구조)
 * - VolOffer 톤 호환: 세션에도 바인딩(필요 시 JSP에서 세션 참조 가능)
 */
public class DetailNoticeAction extends Action {

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {

        request.setCharacterEncoding("UTF-8");

        // 1) 파라미터 수집/검증
        final String idStr = request.getParameter("noticeId");
        System.out.println("[DetailNoticeAction] PARAM noticeId=" + idStr);

        if (idStr == null || idStr.trim().isEmpty()) {
            // return "redirect:/listNotice.do";
            // response.sendRedirect(request.getContextPath()+"/listNotice.do"); return null;
            return "redirect:/listNotice.do";
        }

        final long noticeId;
        try {
            noticeId = Long.parseLong(idStr.trim());
        } catch (NumberFormatException e) {
            return "redirect:/listNotice.do";
        }

        // 2) 조회
        NoticeService service = new NoticeServiceImpl();
        final Notice notice;
        try {
            notice = service.getNotice(noticeId);
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/listNotice.do";
        }

        if (notice == null) {
            return "redirect:/listNotice.do";
        }

        // 3) 바인딩
        HttpSession session = request.getSession();
        session.setAttribute("notice", notice); //  호환(세션)
        request.setAttribute("notice", notice); // JSP 즉시 접근(리퀘스트)

        // 4) 정상 forward - ActionServlet 로직 우회하고 직접 forward
        request.getRequestDispatcher("/notice/detailViewNotice.jsp").forward(request, response);
        return null;
    }
}
