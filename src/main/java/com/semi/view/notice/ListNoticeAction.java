package com.semi.view.notice;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.semi.framework.Action;       
import com.semi.common.Search;                    
import com.semi.domain.Notice;
import com.semi.service.notice.NoticeService;
import com.semi.service.notice.impl.NoticeServiceImpl;

public class ListNoticeAction extends Action {

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
        System.out.println("[ListNoticeAction] /listNotice.do");

        // ---- 파라미터 정규화 ----
        int currentPage = parseInt(nvl(request.getParameter("currentPage"),
                                       request.getParameter("page"),
                                       request.getParameter("cPage")), 1);
        int pageSize    = parseInt(nvl(request.getParameter("pageSize"),
                                       request.getParameter("size")), 10);
        String keyword  = nvl(request.getParameter("q"),
                              request.getParameter("keyword"),
                              request.getParameter("searchKeyword"));
        if (keyword != null) keyword = keyword.trim();

        // ---- Search 구성 ----
        Search search = new Search();
        search.setCurrentPage(currentPage);
        search.setPageSize(pageSize);
        search.setSearchKeyword(keyword);

        // ---- 서비스 호출 ----
        NoticeService noticeService = new NoticeServiceImpl();
        Map<String, Object> result = noticeService.listNotice(search);

        @SuppressWarnings("unchecked")
        List<Notice> noticeList = (List<Notice>) result.get("noticeList");
        int totalCount = (int) result.get("totalCount");

        // ---- 페이지 계산(블록 단위 옵션) ----
        int totalPage = (totalCount + pageSize - 1) / pageSize;
        int blockSize = 5; // 필요 시 JSP에서 안 쓰면 무시됨
        int startPage = ((currentPage - 1) / blockSize) * blockSize + 1;
        int endPage   = Math.min(startPage + blockSize - 1, totalPage);

        // ---- 뷰 전달값 ----
        request.setAttribute("noticeList", noticeList); // 서비스 키 그대로
        request.setAttribute("list", noticeList);       // JSP에서 list로도 접근 가능하게 별칭 제공
        request.setAttribute("totalCount", totalCount);
        request.setAttribute("currentPage", currentPage);
        request.setAttribute("pageSize", pageSize);
        request.setAttribute("totalPage", totalPage);
        request.setAttribute("startPage", startPage);
        request.setAttribute("endPage", endPage);
        request.setAttribute("searchKeyword", keyword == null ? "" : keyword);

        return "forward:/notice/listNoticeView.jsp";
    }

    // ===== 유틸 =====
    private String nvl(String... v) {
        if (v == null) return "";
        for (String s : v) if (s != null && !s.isEmpty()) return s;
        return "";
    }

    private int parseInt(String s, int def) {
        if (s == null) return def;
        try { return Integer.parseInt(s.trim()); } catch (Exception ignore) { return def; }
    }
}
