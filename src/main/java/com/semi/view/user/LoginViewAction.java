// src/com/semi/view/user/LoginViewAction.java
package com.semi.view.user;

import javax.servlet.http.*;
import com.semi.framework.Action;

public class LoginViewAction extends Action {
    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) {
        // If there's a ?msg=... in the query string, keep it as request attribute
        String msg = req.getParameter("msg");
        if (msg != null) {
            req.setAttribute("msg", msg);
            System.out.println("the req.msg has been set to (continued below)");
            System.out.println(req.getAttribute("msg"));
        }

        return "forward:/user/loginView.jsp";
    }
}
