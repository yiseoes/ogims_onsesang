// src/com/semi/view/user/MyVolReqHistoryAction.java
package com.semi.view.user;

import javax.servlet.http.*;
import java.util.List;

import com.semi.framework.Action;
import com.semi.domain.User;
import com.semi.domain.VolRequest;
import com.semi.service.user.dao.UserDao;

public class MyReqHistoryAction extends Action {

    private final UserDao userDao = new UserDao();

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        // 1) 로그인 체크 (loginUser 우선, 없으면 user 키도 확인)
        HttpSession session = req.getSession(false);
        User login = null;
        if (session != null) {
            login = (User) session.getAttribute("loginUser");
            if (login == null) {
                login = (User) session.getAttribute("user");
            }
        }
        if (login == null) return "redirect:/loginView.do";

        // 2) 페이징 파라미터
        int page = 1, size = 10;
        try {
            String p = req.getParameter("page");
            String s = req.getParameter("size");
            if (p != null && !p.isEmpty()) page = Math.max(1, Integer.parseInt(p));
            if (s != null && !s.isEmpty()) size = Math.max(1, Integer.parseInt(s));
        } catch (NumberFormatException ignore) {}
        int offset = (page - 1) * size;

        // 3) 데이터 조회
        List<VolRequest> list = userDao.listMyVolRequests(login.getUserId(), offset, size);
        int total = userDao.countMyVolRequests(login.getUserId());
        int last  = (int) Math.ceil(total / (double) size);

        // 4) 뷰 바인딩
        req.setAttribute("list", list);
        req.setAttribute("page", page);
        req.setAttribute("pageSize", size);
        req.setAttribute("total", total);
        req.setAttribute("lastPage", last);
        req.setAttribute("pageTitle", "내 봉사 요청 이력");

        // 5) 레이아웃 사용: 본문 JSP 경로 전달 후 공통 레이아웃으로 포워드
        req.setAttribute("contentPage", "/user/myVolReqHistory.jsp"); // 실제 JSP 경로와 일치
        return "forward:/common/layout.jsp";
    }
}