// src/com/semi/view/user/DeleteAccAction.java
package com.semi.view.user;

import javax.servlet.http.*;

import com.semi.domain.User;
import com.semi.framework.Action;
import com.semi.service.user.UserService;
import com.semi.service.user.impl.UserServiceImpl;

public class DeleteAccAction extends Action {
    private final UserService userService = new UserServiceImpl();

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        HttpSession session = req.getSession(false);
        User login = (session == null) ? null : (User) session.getAttribute("loginUser");
        if (login == null) return "redirect:/loginView.do";

        System.out.println("[USER][ACT] Deactivate " + login.getUserId());
        userService.deactivateUser(login.getUserId());

        // 요청 범위로 메시지 (forward라서 살아있음)
        req.setAttribute("toast", "탈퇴가 완료되었습니다.");

        // 세션 정리(로그아웃)
        session.invalidate();

        // 홈 본문을 레이아웃에 꽂아서 index.jsp 타게 함
        req.setAttribute("contentPage", "/home/home.jsp");
        return "forward:/index.jsp";   // forward → URL은 /deleteAcc.do지만 화면은 홈 그대로
    }
}
