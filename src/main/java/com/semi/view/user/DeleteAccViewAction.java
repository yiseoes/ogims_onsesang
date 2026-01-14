// src/com/semi/view/user/DeleteAccViewAction.java
package com.semi.view.user;

import javax.servlet.http.*;
import com.semi.framework.Action;

public class DeleteAccViewAction extends Action {
    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) {
        if (req.getSession().getAttribute("loginUser") == null) {
            return "redirect:/loginView.do";
        }
        req.setAttribute("contentPage", "/user/deleteAccView.jsp");
        return "forward:/common/layout.jsp";  // 또는 프로젝트에서 쓰는 레이아웃 JSP 경로

    }
}
