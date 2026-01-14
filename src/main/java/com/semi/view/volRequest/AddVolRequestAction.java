package com.semi.view.volRequest;

import java.nio.file.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;
import javax.servlet.ServletContext;
import javax.servlet.http.*;
import javax.servlet.http.Part;

import com.semi.domain.User;
import com.semi.domain.VolRequest;
import com.semi.framework.Action;
import com.semi.service.volRequest.VolRequestService;
import com.semi.service.volRequest.impl.VolRequestServiceImpl;

public class AddVolRequestAction extends Action {

    private final VolRequestService service = new VolRequestServiceImpl();

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {

        request.setCharacterEncoding("UTF-8");

        // 로그인 사용자
        HttpSession session = request.getSession(false);
        User login = (session != null) ? (User) session.getAttribute("user") : null;
        String authorId = (login != null) ? login.getUserId() : request.getParameter("authorId");

        // 폼 파라미터
        String title    = request.getParameter("title");
        String content  = request.getParameter("content");
        String phone    = request.getParameter("phone");
        String region   = request.getParameter("region");
        String category = request.getParameter("category");

        DateTimeFormatter FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
        LocalDateTime start = LocalDateTime.parse(request.getParameter("start"), FMT);
        LocalDateTime end   = LocalDateTime.parse(request.getParameter("end"),   FMT);

        // 이미지 업로드(선택)
        String storedName = null;
        Part imagePart = null;
        try { imagePart = request.getPart("image"); } catch (IllegalStateException ignore) {}
        if (imagePart != null && imagePart.getSize() > 0) {
            storedName = saveToWebappImages(imagePart, getServletContext());
        }

        // VO 구성
        VolRequest vo = new VolRequest();
        vo.setAuthorId(authorId);
        vo.setTitle(title);
        vo.setContent(content);
        vo.setPhone(phone);
        vo.setRegion(region);
        vo.setCategory(category);
        vo.setStartTime(start);
        vo.setEndTime(end);
        vo.setImage(storedName); // 새 필드

        service.addVolRequest(vo);

        // 목록으로
        return "redirect:/listVolRequest.do";
    }

    // ===== 내부 유틸: webapp/images 에 저장 =====
    private static String saveToWebappImages(Part part, ServletContext ctx) throws Exception {
        String submitted = getSubmittedFileName(part);
        String ext = "";
        if (submitted != null) {
            int dot = submitted.lastIndexOf('.');
            if (dot >= 0) ext = submitted.substring(dot); // ".png"
        }
        String storedName = UUID.randomUUID().toString().replace("-", "") + ext;
        String imagesPath = ctx.getRealPath("/images");
        Files.createDirectories(Paths.get(imagesPath));
        Path target = Paths.get(imagesPath, storedName);
        try (java.io.InputStream in = part.getInputStream()) {
            Files.copy(in, target, StandardCopyOption.REPLACE_EXISTING);
        }
        return storedName;
    }
    private static String getSubmittedFileName(Part part) {
        String cd = part.getHeader("content-disposition");
        if (cd == null) return null;
        for (String token : cd.split(";")) {
            token = token.trim();
            if (token.startsWith("filename")) {
                String name = token.substring(token.indexOf('=') + 1).trim().replace("\"", "");
                return name;
            }
        }
        return null;
    }
}
