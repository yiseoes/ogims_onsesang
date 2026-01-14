// src/com/semi/view/user/UpdateUserAction.java
package com.semi.view.user;

import javax.servlet.http.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import com.semi.framework.Action;
import com.semi.domain.User;
import com.semi.service.user.UserService;
import com.semi.service.user.impl.UserServiceImpl;

public class UpdateUserAction extends Action {
    private final UserService userService = new UserServiceImpl();

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        req.setCharacterEncoding("UTF-8");

        User login = (User) req.getSession().getAttribute("loginUser");
        if (login == null) {
            return "redirect:/loginView.do";
        }

        // 입력 파라미터
        String region   = req.getParameter("region");
        String name     = req.getParameter("name");
        String birthStr = req.getParameter("birthdate");   // "yyyy-MM-dd" or ""
        String phone    = req.getParameter("phone");
        String gender   = req.getParameter("gender");
        String category = req.getParameter("category");

        // LocalDate 파싱 (비어있으면 null 허용)
        LocalDate birthDate = null;
        if (birthStr != null && !birthStr.isBlank()) {
            try {
                birthDate = LocalDate.parse(birthStr, DateTimeFormatter.ISO_LOCAL_DATE);
            } catch (DateTimeParseException e) {
                // ❌ 파싱 실패 → 에러 메시지 + 사용자가 입력한 값 유지 + 레이아웃으로 포워드
                req.setAttribute("error", "생년월일 형식이 올바르지 않습니다. (예: 1990-01-31)");
                req.setAttribute("pageTitle", "내정보 수정");
                req.setAttribute("contentPage", "/user/updateUserView.jsp");
                // 사용자가 방금 입력한 값이 폼에 그대로 남도록 param.* 를 JSP에서 우선 사용
                return "forward:/common/layout.jsp";
            }
        }

        // 업데이트 대상 생성
        User u = new User();
        u.setUserId(login.getUserId()); // 수정 타깃
        u.setRegion(region);
        u.setName(name);
        u.setBirthDate(birthDate);      // LocalDate (null 허용)
        u.setPhone(phone);
        u.setGender(gender);
        u.setCategory(category);

        System.out.println("[USER][ACT] Update " + u.getUserId());
        userService.updateUser(u);

        // 세션 최신화
        User fresh = userService.getUser(u.getUserId());
        if (fresh != null) fresh.setPassword(null);
        req.getSession().setAttribute("loginUser", fresh);

        // ✅ 성공 시에는 항상 .do 또는 / 로 리다이렉트 → 레이아웃 타게
        return "redirect:/detailUser.do";
    }
}
