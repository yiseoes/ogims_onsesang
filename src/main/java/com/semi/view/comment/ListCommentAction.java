package com.semi.view.comment;

import com.semi.service.comment.CommentService;
import com.semi.service.comment.impl.CommentServiceImpl;
// 필요 시 공통 유틸 사용 가능: RequestFilter로 인코딩 처리되더라도 명시 유지
// import com.semi.common.HttpUtil;   // 프로젝트 HttpUtil 시그니처 확정시 교체

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.semi.framework.Action; // 너희 프로젝트의 Action 슈퍼타입 가정
import java.util.Map;

/**
 * 댓글 목록 조회 액션
 * - 상세보기 JSP에서 <jsp:include> 또는 <c:import>로 동기 포함
 * - 동일 URL을 AJAX로 호출해 부분 갱신으로도 활용 가능(확장)
 */
public class ListCommentAction extends Action {

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
        // ====== 인코딩 명시 ======
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        // ====== 파라미터 파싱 ======
        // HttpUtil을 쓰는 경우 아래 코드를 교체하여 사용해도 됨
        long volunteerId = parseLongOrDefault(request.getParameter("volunteerId"), -1L);
        int page         = parseIntOrDefault(request.getParameter("page"), 1);
        int pageSize     = parseIntOrDefault(request.getParameter("pageSize"), getDefaultPageSize(request));

        // ====== 서비스 호출 ======
        CommentService svc = new CommentServiceImpl();
        Map<String, Object> map = svc.getComments(volunteerId, page, pageSize);

        // ====== 뷰에 전달 ======
        request.setAttribute("commentMap", map);
        request.setAttribute("volunteerId", volunteerId);

        // ====== 프래그먼트 JSP로 forward ======
        return "forward:/comment/commentListFragment.jsp";
    }

    // ====== 유틸: 정수 파싱 ======
    private int parseIntOrDefault(String s, int def) {
        try { return (s == null || s.trim().isEmpty()) ? def : Integer.parseInt(s.trim()); }
        catch (Exception e) { return def; }
    }

    // ====== 유틸: long 파싱 ======
    private long parseLongOrDefault(String s, long def) {
        try { return (s == null || s.trim().isEmpty()) ? def : Long.parseLong(s.trim()); }
        catch (Exception e) { return def; }
    }

    // ====== 기본 pageSize : web.xml > context-param에서 우선 읽기 ======
    private int getDefaultPageSize(javax.servlet.http.HttpServletRequest request) {
        String ps = request.getServletContext().getInitParameter("comment.pageSize");
        if (ps == null || !ps.matches("\\d+")) {
            ps = request.getServletContext().getInitParameter("pageSize"); // 공통 기본값
        }
        return (ps != null && ps.matches("\\d+")) ? Integer.parseInt(ps) : 10;
    }
}
