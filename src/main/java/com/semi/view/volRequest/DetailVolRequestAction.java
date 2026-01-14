package com.semi.view.volRequest;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.semi.domain.VolRequest;
import com.semi.framework.Action;
import com.semi.service.volRequest.VolRequestService;
import com.semi.service.volRequest.impl.VolRequestServiceImpl;

public class DetailVolRequestAction extends Action {

    private final VolRequestService service = new VolRequestServiceImpl();

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {

    	HttpSession session = request.getSession(false);
    	Object loginUser = (session == null) ? null : session.getAttribute("user");
    	if (loginUser == null) return "redirect:/loginView.do?msg=loginRequired";

        long volunteerId = Long.parseLong(request.getParameter("volunteerId"));        
        Map<String, Object> detail = service.getVolRequestDetail(volunteerId);

        request.setAttribute("volunteer", detail.get("item"));
        request.setAttribute("authorName", detail.get("authorName"));
        request.setAttribute("volunteerId", volunteerId);
     // ✅ LocalDateTime -> Timestamp (java.util.Date 계열이라 fmt:formatDate가 먹힘)
        
        LocalDateTime st = ((VolRequest)detail.get("item")).getStartTime();
        LocalDateTime et = ((VolRequest)detail.get("item")).getEndTime();
        request.setAttribute("startTime", (st != null) ? Timestamp.valueOf(st) : null);
        request.setAttribute("endTime",   (et != null) ? Timestamp.valueOf(et) : null);

        // 목록 컨텍스트 유지용
        request.setAttribute("region", request.getParameter("region"));
        request.setAttribute("category", request.getParameter("category"));
        request.setAttribute("page", request.getParameter("page"));
        request.setAttribute("pageSize", request.getParameter("pageSize"));
        request.setAttribute("searchCondition", request.getParameter("searchCondition"));
        request.setAttribute("searchKeyword",   request.getParameter("searchKeyword"));
        request.setAttribute("status",          request.getParameter("status"));

        return "forward:/volRequest/detailVolRequest.jsp";
    }
}
