package com.semi.view.notice;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.semi.framework.Action;
import com.semi.domain.Notice;
import com.semi.domain.User;
import com.semi.service.notice.NoticeService;
import com.semi.service.notice.impl.NoticeServiceImpl;

/**
 * 공지 등록 처리 액션 (NPE 방지: resultPage는 절대 null 반환 금지)
 * - 작성자는 세션 로그인 사용자로 확정
 * - 실패 시 등록 화면으로 포워드(JSP 경로 문자열 반환)
 * - 성공 시 상세로 리다이렉트("redirect:/...") 문자열 반환
 */
public class AddNoticeAction extends Action {

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {

        try { request.setCharacterEncoding("UTF-8"); } catch (Exception ignore) {}

        // 1) 파라미터 수집/검증
        final String title   = nvl(request.getParameter("title"));
        final String content = nvl(request.getParameter("content"));

        System.out.println("[AddNoticeAction] PARAM title=" + title);
        System.out.println("[AddNoticeAction] PARAM content.length=" + content.length());

        if (title.isEmpty() || content.isEmpty()) {
            request.setAttribute("error", "제목/내용은 필수입니다.");
            return "forward:/notice/addNoticeView.jsp"; // null 금지
        }

        // 2) 로그인 세션 확인
        HttpSession session = request.getSession(false);
        User loginUser = (session == null) ? null : (User) session.getAttribute("loginUser");
        System.out.println("[AddNoticeAction] SESSION user=" + (loginUser == null ? "null" : loginUser.getUserId()));

        if (loginUser == null || nvl(loginUser.getUserId()).isEmpty()) {
            request.setAttribute("error", "로그인 후 작성 가능합니다.");
            return "forward:/notice/addNoticeView.jsp"; // null 금지
        }

        // 3) 도메인 구성
        Notice vo = new Notice();
        vo.setAuthorId(loginUser.getUserId());
        vo.setTitle(title);
        vo.setContent(content);

        // 4) INSERT -> PK
        NoticeService service = new NoticeServiceImpl();
        long newId;
        try {
            newId = service.addNotice(vo); // PK(long) 반환
            System.out.println("[AddNoticeAction] INSERT OK newId=" + newId);
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "[등록 실패] " + (e.getMessage() == null ? "원인 불명" : e.getMessage()));
            return "forward:/notice/addNoticeView.jsp"; // null 금지
        }

        // 5) 성공: 반드시 문자열 리턴(redirect:)
        return "redirect:/detailNotice.do?noticeId=" + newId;
    }

    private String nvl(String s) { return (s == null) ? "" : s.trim(); }
}
