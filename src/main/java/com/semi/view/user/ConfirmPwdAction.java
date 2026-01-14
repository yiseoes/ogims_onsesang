package com.semi.view.user;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.semi.framework.Action;
import com.semi.service.user.UserService;
import com.semi.service.user.impl.UserServiceImpl;
import com.semi.domain.User;

import com.semi.common.util.PasswordUtilSHA256;


public class ConfirmPwdAction extends Action {

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
        request.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            // Not logged in → go to login controller
            return "redirect:/loginView.do";
        }

        String currentPwd = request.getParameter("currentPwd");
        if (currentPwd == null || currentPwd.trim().isEmpty()) {
            request.setAttribute("errorMsg", "현재 비밀번호를 입력하세요.");
            return "forward:/user/confirmPwd.jsp";
        }

        User sessionUser = (User) session.getAttribute("user");
        String userId = sessionUser.getUserId();

        UserService userService = new UserServiceImpl();
        User dbUser = userService.getUser(userId);
        
        String decryptedPwd = PasswordUtilSHA256.hash(dbUser.getPassword());
        
        boolean ok = PasswordUtilSHA256.matches(currentPwd, dbUser.getPassword());
        
        
        //debugging printout
        System.out.println(ok);
       
        
        
        if (!ok) {
            request.setAttribute("errorMsg", "비밀번호가 일치하지 않습니다.");
            return "forward:/user/confirmPwd.jsp";
        }

        // Mark as verified for the next step
        session.setAttribute("pwdVerified", Boolean.TRUE);

        // PRG: redirect to controller that decides which JSP to show
        return "forward:/WEB-INF/user/updatePwdView.jsp";
    }
}
