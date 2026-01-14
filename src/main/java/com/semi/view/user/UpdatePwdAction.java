package com.semi.view.user;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.semi.framework.Action;          // TODO: if your Action interface lives elsewhere, fix import
import com.semi.service.user.UserService;
import com.semi.service.user.impl.UserServiceImpl;
import com.semi.domain.User;

public class UpdatePwdAction extends Action {

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
        request.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            return "forward:/user/loginView.jsp";
        }

        // Ensure they passed the confirm step
        Object verified = session.getAttribute("pwdVerified");
        if (!(verified instanceof Boolean) || !((Boolean) verified)) {
            request.setAttribute("errorMsg", "비밀번호 확인 후 다시 시도하세요.");
            return "forward:/user/confirmPwd.jsp";
        }

        String newPwd = request.getParameter("newPwd");
        String newPwd2 = request.getParameter("newPwd2");

        if (newPwd == null || newPwd.trim().isEmpty() || !newPwd.equals(newPwd2)) {
            request.setAttribute("errorMsg", "새 비밀번호가 일치하지 않습니다.");
            return "forward:/user/updatePwdView.jsp";
        }

        User sessionUser = (User) session.getAttribute("user");
        String userId = sessionUser.getUserId();

        UserService userService = new UserServiceImpl();

        // Load, update only the password, then persist
        User toUpdate = userService.getUser(userId);
        
        
        
        
        
        
        
        System.out.println("new received pwd: " + newPwd);
        
        
        userService.updatePassword(userId, userId, newPwd);
        userService.updateUser(toUpdate);     // assumes updateUser(User) exists

        
        
        
        
        
        
        
        // Keep session in sync
        sessionUser.setPassword(newPwd);
        session.setAttribute("user", sessionUser);

        // Clear the single-use verification flag
        session.removeAttribute("pwdVerified");

        // Redirect destination after success (logged-in main)
        // If your front controller treats "redirect:" specially, use that convention.
        return "forward:/index.jsp";
    }
}

