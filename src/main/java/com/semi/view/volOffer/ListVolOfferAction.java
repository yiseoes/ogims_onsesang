package com.semi.view.volOffer;

import java.util.Map;
import javax.servlet.ServletContext;
import javax.servlet.http.*;
import com.semi.common.Search;
import com.semi.framework.Action;
import com.semi.service.volOffer.VolOfferService;
import com.semi.service.volOffer.impl.VolOfferServiceImpl;

public class ListVolOfferAction extends Action {

    private final VolOfferService service = new VolOfferServiceImpl();

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {

        // === 페이징 설정(web.xml) ===
        ServletContext ctx = request.getServletContext();
        int pageUnit = Integer.parseInt(ctx.getInitParameter("pageUnit"));
        int pageSize = Integer.parseInt(ctx.getInitParameter("pageSize"));
        int currentPage = parseIntOrDefault(request.getParameter("page"), 1);

        // === 검색 파라미터 ===
        Search search = new Search();
        search.setCurrentPage(currentPage);
        search.setPageSize(pageSize);
        search.setSearchCondition(request.getParameter("searchCondition")); // title/author/authorId/region/date
        search.setSearchKeyword(request.getParameter("searchKeyword"));

        String category = request.getParameter("category"); // 라디오
        String status   = request.getParameter("status");   // 체크박스("모집중") or null

        // === 로그인 시 지역 고정 필터(regionLock) 추가 ===
        HttpSession session = request.getSession(false);
        Object loginUser = (session == null) ? null : session.getAttribute("user");
        String regionLock = null;
        if (loginUser != null) {
            regionLock = resolveUserRegion(loginUser, session);  // "서울시 구로구" 등
        }

        // === 서비스 호출 ===
        Map<String,Object> map = service.getVolOfferList(search, category, pageUnit, status, regionLock);

        // === 뷰 바인딩 ===
        request.setAttribute("list",       map.get("list"));
        request.setAttribute("page",       map.get("page"));
        request.setAttribute("totalCount", map.get("totalCount"));
        request.setAttribute("search",     map.get("search"));
        request.setAttribute("category",   category);
        request.setAttribute("status",     status);
        // 선택: 고정지역을 뷰에서 표시하고 싶다면
        // request.setAttribute("regionLock", regionLock);

        return "forward:/volOffer/listVolOffer.jsp";
    }

    private int parseIntOrDefault(String s, int def) {
        try { return (s==null || s.isEmpty()) ? def : Integer.parseInt(s); }
        catch (Exception e) { return def; }
    }

    /** 세션 user 객체에서 region을 안전하게 얻는다. */
    private String resolveUserRegion(Object user, HttpSession session) {
        if (user == null) return null;
        try {
            java.lang.reflect.Method m = user.getClass().getMethod("getRegion");
            Object v = m.invoke(user);
            if (v != null) return String.valueOf(v);
        } catch (Exception ignore) {}
        Object s = (session != null) ? session.getAttribute("region") : null;
        return (s != null) ? String.valueOf(s) : null;
    }
}
