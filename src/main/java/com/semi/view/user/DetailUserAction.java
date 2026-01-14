// src/com/semi/view/user/DetailUserAction.java
package com.semi.view.user;

import javax.servlet.http.*;
import com.semi.framework.Action;
import com.semi.service.user.UserService;
import com.semi.service.user.impl.UserServiceImpl;
import com.semi.domain.User;

public class DetailUserAction extends Action {
    private final UserService userService = new UserServiceImpl();

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        // 1) 파라미터(userId / userid) 허용
        String userId = trimToNull(req.getParameter("userId"));
        if (userId == null) userId = trimToNull(req.getParameter("userid"));

        // 2) 세션 사용자 (user / loginUser 허용)
        HttpSession session = req.getSession();
        User sessionUser = (User)(session.getAttribute("user") != null
                ? session.getAttribute("user")
                : session.getAttribute("loginUser"));

        // 3) 파라미터 없으면 내 정보
        if (userId == null) {
            if (sessionUser == null) {
                System.out.println("[DetailUserAction] no param, no session -> redirect login");
                return "redirect:/loginView.do";
            }
            userId = sessionUser.getUserId();
        }

        // 4) 조회
        User user = userService.getUser(userId);
        System.out.println("[DetailUserAction] param.userId=" + userId
                + ", sessionUser=" + (sessionUser != null ? sessionUser.getUserId() : "null")
                + ", loaded=" + (user != null ? user.getUserId() : "null"));

        // 5) 바인딩
        req.setAttribute("user", user);

        // 6) 레이아웃 직접 forward(규약 통일)
        req.setAttribute("pageTitle", "내정보 보기");
        req.setAttribute("contentPage", "/user/detailUserView.jsp");
        return "forward:/common/layout.jsp";
    }

    private static String trimToNull(String s) {
        if (s == null) return null;
        String t = s.trim();
        return t.isEmpty() ? null : t;
    }
}
