package com.semi.view.user;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import com.semi.framework.Action;
import com.semi.service.user.UserService;
import com.semi.service.user.impl.UserServiceImpl;
import com.semi.domain.User;

public class AddUserAction extends Action {

    private static final DateTimeFormatter BIRTH_FMT = DateTimeFormatter.ISO_LOCAL_DATE;
    private final UserService userService = new UserServiceImpl();

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        req.setCharacterEncoding("UTF-8");

        // 1) 파라미터
        final String userId   = trim(req.getParameter("userid"));
        final String password = trim(req.getParameter("password"));
        final String region   = trim(req.getParameter("region"));
        final String name     = trim(req.getParameter("name"));
        final String birthStr = trim(req.getParameter("birthdate"));
        final String phone    = trim(req.getParameter("phone"));
        final String gender   = trim(req.getParameter("gender"));
        final String category = trim(req.getParameter("category"));

        // 2) 기초 검증
        if (isEmpty(userId)) {                         // ★ 아이디는 빈값만 막음
            return alert(resp, "아이디는 필수입니다.");
        }
        if (isEmpty(password)) {
            return alert(resp, "비밀번호는 필수입니다.");
        }
        if (password.length() < 6) {                 // ★ 비밀번호 길이 검증 추가
            return alert(resp, "비밀번호는 6자 이상 입력하세요.");
        }

        // 3) 생년월일 파싱
        LocalDate birthDate;
        try {
            if (isEmpty(birthStr)) {
                return alert(resp, "생년월일은 필수입니다. (예: 1990-01-31)");
            }
            birthDate = LocalDate.parse(birthStr, BIRTH_FMT);
        } catch (DateTimeParseException e) {
            return alert(resp, "생년월일 형식이 올바르지 않습니다. (예: 1990-01-31)");
        }

        // 4) 아이디 중복 체크 (Service.exists 사용)
        if (userService.exists(userId)) {
            return alert(resp, "이미 사용 중인 아이디입니다.");
        }

        // 5) VO 구성
        User u = new User();
        u.setUserId(userId);
        u.setPassword(password);
        u.setRegion(region);
        u.setName(name);
        u.setBirthDate(birthDate);
        u.setPhone(phone);
        u.setGender(gender);
        u.setCategory(category);

        System.out.println("[USER][ACT] AddUser userId=" + u.getUserId());

        // 6) 가입 처리
        try {
            userService.addUser(u);
        } catch (IllegalArgumentException ex) {
            return alert(resp, ex.getMessage()); // ex.getMessage() = "이미 존재하는 아이디입니다." 등
        } catch (Exception e) {
            if (isOracleUniqueViolation(e)) {
                return alert(resp, "이미 사용 중인 아이디입니다.");
            }
            throw e;
        }

        // 7) fresh 조회 후 세션 저장 (비번 제거)
        User fresh = userService.getUser(userId);
        if (fresh == null) {
            return alert(resp, "회원가입 처리 중 오류가 발생했습니다. 다시 시도해주세요.");
        }
        fresh.setPassword(null);

        HttpSession session = req.getSession(true);
        session.setAttribute("loginUser", fresh);
        session.setAttribute("user", fresh);
        session.setMaxInactiveInterval(60 * 30);

        // 8) 홈 화면으로 (forward)
        req.setAttribute("toast", "회원가입이 완료되었습니다.");
        req.setAttribute("contentPage", "/home/home.jsp");
        return "forward:/index.jsp";
    }

    // ---------- 유틸 ----------
    private static boolean isEmpty(String s) { return s == null || s.isBlank(); }
    private static String trim(String s) { return s == null ? null : s.trim(); }

    // alert 출력용
    private static String alert(HttpServletResponse resp, String msg) throws Exception {
        resp.setContentType("text/html; charset=UTF-8");
        resp.getWriter().write("<script>alert('" + msg + "'); history.back();</script>");
        resp.getWriter().flush();
        return null; // JSP forward 안 함
    }

    private static boolean isOracleUniqueViolation(Throwable t) {
        Throwable cur = t;
        while (cur != null) {
            if (cur instanceof java.sql.SQLIntegrityConstraintViolationException) {
                return true; 
            }
            if (cur instanceof java.sql.BatchUpdateException) {
                java.sql.BatchUpdateException be = (java.sql.BatchUpdateException) cur;
                if (be.getErrorCode() == 1) return true;
                if (be.getNextException() != null && be.getNextException().getErrorCode() == 1) return true;
            }
            if (cur instanceof java.sql.SQLException) {
                java.sql.SQLException se = (java.sql.SQLException) cur;
                if (se.getErrorCode() == 1 || 
                    (se.getMessage() != null && se.getMessage().contains("ORA-00001"))) {
                    return true;
                }
            }
            cur = cur.getCause();
        }
        return false;
    }
}
