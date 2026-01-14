// src/com/semi/view/user/UpdateUserViewAction.java
package com.semi.view.user;

import javax.servlet.http.*;
import com.semi.framework.Action;

public class UpdateUserViewAction extends Action {
    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        req.setAttribute("pageTitle", "내정보 수정");
        req.setAttribute("contentPage", "/user/updateUserView.jsp");
        return "forward:/common/layout.jsp"; // 레이아웃 강제
    }
}
