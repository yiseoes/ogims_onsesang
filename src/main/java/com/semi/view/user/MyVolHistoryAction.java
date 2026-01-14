// src/com/semi/view/user/MyVolHistoryAction.java
package com.semi.view.user;

import javax.servlet.http.*;
import java.util.List;

import com.semi.framework.Action;
import com.semi.domain.User;
import com.semi.domain.VolOffer;
import com.semi.service.user.dao.UserDao;

public class MyVolHistoryAction extends Action {

    private final UserDao userDao = new UserDao();

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        // 1) 로그인 체크
        HttpSession session = req.getSession(false);
        User login = (session == null) ? null : (User) session.getAttribute("loginUser");
        if (login == null) return "redirect:/loginView.do";

        // 2) 페이징
        int page = 1, size = 10;
        try {
            String p = req.getParameter("page"), s = req.getParameter("size");
            if (p != null && !p.isEmpty()) page = Math.max(1, Integer.parseInt(p));
            if (s != null && !s.isEmpty()) size = Math.max(1, Integer.parseInt(s));
        } catch (NumberFormatException ignore) {}
        int offset = (page - 1) * size;

        // 3) 데이터 조회
        List<VolOffer> list = userDao.listMyVolOffers(login.getUserId(), offset, size);
        int total = userDao.countMyVolOffers(login.getUserId());
        int last  = (int)Math.ceil(total / (double)size);

        // 4) 뷰 데이터 세팅
        req.setAttribute("list", list);
        req.setAttribute("page", page);
        req.setAttribute("pageSize", size);
        req.setAttribute("total", total);
        req.setAttribute("lastPage", last);
        req.setAttribute("pageTitle", "내 봉사 오퍼 이력");

        // 5) 레이아웃에게 본문 경로 전달 후 레이아웃으로 forward
        req.setAttribute("contentPage", "/user/myVolHistoryView.jsp"); // 실제 파일 경로와 일치해야 함
        return "forward:/common/layout.jsp"; // 레이아웃 실경로
    }
}
