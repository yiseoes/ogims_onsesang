package com.semi.view.volRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.semi.framework.Action;

public class AddVolRequestViewAction extends Action {

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
        HttpSession session = request.getSession(false);
        Object loginUser = (session == null) ? null : session.getAttribute("user");

        if (loginUser == null) {
            // 로그인 필요 플래그를 쿼리스트링으로 전달 (로그인뷰에서 alert 처리)
            return "redirect:/loginView.do?msg=loginRequired";
        }

        // 로그인 ok → 페이지로 포워드
        return "forward:/volRequest/addVolRequest.jsp";
    }
}
