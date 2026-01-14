// src/com/semi/view/user/AddUserViewAction.java
package com.semi.view.user;

import javax.servlet.http.*;
import com.semi.framework.Action;

public class AddUserViewAction extends Action {
    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        System.out.println("[USER][ACT] AddUserView");

        // 레이아웃에 넘길 값
        req.setAttribute("pageTitle", "회원가입");
        req.setAttribute("contentPage", "/user/addUserView.jsp"); // 본문 JSP 경로만!

        // ★ ActionServlet을 건드리지 않고 레이아웃 강제 적용
        return "forward:/common/layout.jsp";
    }
}
