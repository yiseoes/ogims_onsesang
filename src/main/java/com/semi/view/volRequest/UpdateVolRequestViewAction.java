package com.semi.view.volRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.semi.domain.VolRequest;
import com.semi.framework.Action;
import com.semi.service.volRequest.VolRequestService;
import com.semi.service.volRequest.impl.VolRequestServiceImpl;

public class UpdateVolRequestViewAction extends Action {

    private final VolRequestService service = new VolRequestServiceImpl();

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {

        long volunteerId = Long.parseLong(request.getParameter("volunteerId"));
        VolRequest item = service.getVolRequest(volunteerId);

        // (권한 체크가 필요하다면 여기에서 세션 userId와 item.getAuthorId() 비교)

        request.setAttribute("item", item);

        // 컨텍스트 유지
        request.setAttribute("region", request.getParameter("region"));
        request.setAttribute("category", request.getParameter("category"));
        request.setAttribute("page", request.getParameter("page"));
        request.setAttribute("pageSize", request.getParameter("pageSize"));

        return "forward:/volRequest/updateVolRequest.jsp";
    }
}
