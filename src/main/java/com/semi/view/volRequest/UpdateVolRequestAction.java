package com.semi.view.volRequest;

import java.nio.file.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;
import javax.servlet.ServletContext;
import javax.servlet.http.*;
import javax.servlet.http.Part;

import com.semi.domain.VolRequest;
import com.semi.framework.Action;
import com.semi.service.volRequest.VolRequestService;
import com.semi.service.volRequest.impl.VolRequestServiceImpl;

public class UpdateVolRequestAction extends Action {

    private final VolRequestService service = new VolRequestServiceImpl();

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {

        request.setCharacterEncoding("UTF-8");

        long volunteerId = Long.parseLong(request.getParameter("volunteerId"));
        String title    = request.getParameter("title");
        String content  = request.getParameter("content");
        String phone    = request.getParameter("phone");
        String region   = request.getParameter("region");
        String category = request.getParameter("category");

        DateTimeFormatter FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
        LocalDateTime start = LocalDateTime.parse(request.getParameter("start"), FMT);
        LocalDateTime end   = LocalDateTime.parse(request.getParameter("end"),   FMT);

        // 기존 이미지 파일명 확보
        String oldImage = service.findImageByVolunteerId(volunteerId);

        // 새 업로드 여부
        String storedName = null;
        Part imagePart = null;
        try { imagePart = request.getPart("image"); } catch (IllegalStateException ignore) {}
        if (imagePart != null && imagePart.getSize() > 0) {
            storedName = saveToWebappImages(imagePart, getServletContext());
        }

        // VO 구성
        VolRequest vo = new VolRequest();
        vo.setPostId(volunteerId);
        vo.setTitle(title);
        vo.setContent(content);
        vo.setPhone(phone);
        vo.setRegion(region);
        vo.setCategory(category);
        vo.setStartTime(start);
        vo.setEndTime(end);
        // 새 이미지가 있으면 파일명 세팅, 없으면 null → DAO에서 NVL(?, image)로 유지
        vo.setImage(storedName);

        service.updateVolRequest(vo);

        // 새 이미지가 있었다면, DB 업데이트 성공 이후 기존 물리 파일 삭제
        if (storedName != null && oldImage != null && !oldImage.isEmpty()) {
            deleteFromWebappImages(oldImage, getServletContext());
        }

        return "redirect:/detailVolRequest.do?volunteerId=" + volunteerId;
    }

    // ===== 내부 유틸 =====
    private static String saveToWebappImages(Part part, ServletContext ctx) throws Exception {
        String submitted = getSubmittedFileName(part);
        String ext = "";
        if (submitted != null) {
            int dot = submitted.lastIndexOf('.');
            if (dot >= 0) ext = submitted.substring(dot);
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
    private static void deleteFromWebappImages(String storedName, ServletContext ctx) {
        try {
            String imagesPath = ctx.getRealPath("/images");
            Files.deleteIfExists(Paths.get(imagesPath, storedName));
        } catch (Exception ignore) {}
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
