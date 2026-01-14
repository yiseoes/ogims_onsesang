// src/com/semi/view/user/LogoutAction.java
package com.semi.view.user;

import javax.servlet.http.*;
import com.semi.framework.Action;

public class LogoutAction extends Action {
    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) {
        System.out.println("[USER][ACT] Logout");
        HttpSession session = req.getSession(false);
        if (session != null) session.invalidate();
        return "redirect:/index.jsp";
    }
}
