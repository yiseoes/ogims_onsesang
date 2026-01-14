package com.semi.view.volOffer;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

import com.semi.common.util.FileUtil;
import com.semi.domain.User;
import com.semi.domain.VolOffer;
import com.semi.framework.Action;
import com.semi.service.volOffer.VolOfferService;
import com.semi.service.volOffer.impl.VolOfferServiceImpl;

/**
 * 봉사제공 등록 액션
 * - addVolOffer.jsp에서 넘어온 폼 데이터를 받아 DB에 저장
 * - 이미지 업로드는 공통 FileUtil.saveImage(Part, ServletContext)를 사용
 * - 날짜/시간은 datetime-local 형식(yyyy-MM-dd'T'HH:mm)을 기본으로 파싱
 */
public class AddVolOfferAction extends Action {

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
        // ====================== 1) 기본 파라미터 수신 ======================
        // 화면 name과 매핑: addVolOffer.jsp는 start/end 또는 startTime/endTime 중 하나를 보낼 수 있어 안전하게 모두 수용
        String title    = nvl(request.getParameter("title"));
        String content  = nvl(request.getParameter("content"));
        String phone    = nvl(request.getParameter("phone"));
        String region   = nvl(request.getParameter("region"));
        String category = nvl(request.getParameter("category"));

        // datetime-local 입력: 이름 호환(start 혹은 startTime), end도 동일
        String startStr = nvl(request.getParameter("startTime"));
        if (startStr.isEmpty()) startStr = nvl(request.getParameter("start"));
        String endStr   = nvl(request.getParameter("endTime"));
        if (endStr.isEmpty())   endStr   = nvl(request.getParameter("end"));

        // 작성자: 폼에 authorId를 안 보낼 수 있어 세션에서 보조
        String authorId = nvl(request.getParameter("authorId"));
        if (authorId.isEmpty()) {
            HttpSession session = request.getSession(false);
            if (session != null) {
                Object userObj = session.getAttribute("user");
                if (userObj instanceof User) {
                    authorId = ((User) userObj).getUserId();
                }
            }
        }

        // ====================== 2) 날짜/시간 파싱 ======================
        LocalDateTime startTime = parseDateTime(startStr);
        LocalDateTime endTime   = parseDateTime(endStr);

        // ====================== 3) 이미지 저장(공통 FileUtil 사용) ======================
        // - form에 enctype="multipart/form-data" 필수
        // - ActionServlet(수신 서블릿)에 @MultipartConfig 또는 web.xml 설정이 필요
        String savedImageName = null;
        try {
            Part imagePart = null;
            try {
                imagePart = request.getPart("image");
            } catch (IllegalStateException ise) {
                // 업로드 용량 초과 혹은 Multipart 설정 문제
                System.err.println("[AddVolOfferAction] getPart 실패: " + ise.getMessage());
            }

            if (imagePart != null && imagePart.getSize() > 0) {
                ServletContext ctx = request.getServletContext();
                savedImageName = FileUtil.saveImage(imagePart, ctx); // 이미지 저장 후 파일명 반환
            }
        } catch (IOException ioe) {
            // 저장 실패는 치명적이지 않으므로 로깅만 하고 계속 진행
            System.err.println("[AddVolOfferAction] 이미지 저장 실패: " + ioe.getMessage());
        }

        // ====================== 4) 도메인 객체 구성 ======================
        VolOffer vo = new VolOffer();
        vo.setTitle(title);
        vo.setContent(content);
        vo.setAuthorId(authorId);
        vo.setPhone(phone);
        vo.setRegion(region);
        vo.setCategory(category);
        vo.setStartTime(startTime);
        vo.setEndTime(endTime);
        vo.setStatus("모집중");  // 기본 상태
        vo.setOfferFlag("o");   // 제공 플래그

        // 이미지 파일명이 존재할 때만 세팅(도메인에 image 필드가 있다고 가정)
        try {
            // 도메인에 setImage(String)이 있는 경우
            VolOffer.class.getMethod("setImage", String.class);
            if (savedImageName != null && !savedImageName.isEmpty()) {
                vo.getClass().getMethod("setImage", String.class).invoke(vo, savedImageName);
            }
        } catch (NoSuchMethodException nsme) {
            // 이미지 필드가 없는 도메인이라면 조용히 무시
        } catch (Exception e) {
            // 리플렉션 예외는 로깅
            System.err.println("[AddVolOfferAction] setImage 반영 중 오류: " + e.getMessage());
        }

        // ====================== 5) 서비스 호출(등록) ======================
        System.out.println("=== AddVolOfferAction :: " + vo);
        VolOfferService service = new VolOfferServiceImpl();
        service.addVolOffer(vo);

        // ====================== 6) 후처리/리다이렉트 ======================
        // 등록 후 목록으로 이동(원하면 상세로 변경 가능)
        return "redirect:/listVolOffer.do";
    }

    // ---------------------- 유틸리티 메서드들 ----------------------

    /** null-safe trim */
    private static String nvl(String s) {
        return (s == null) ? "" : s.trim();
    }

    /** datetime-local 형식 우선 파싱. 실패 시 공백, 초 없는 변형에 유연하게 대응 */
    private static LocalDateTime parseDateTime(String s) {
        if (s == null || s.trim().isEmpty()) return null;
        String v = s.trim();
        // 가장 흔한 형식: yyyy-MM-dd'T'HH:mm
        DateTimeFormatter f1 = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
        try { return LocalDateTime.parse(v, f1); } catch (DateTimeParseException ignore) {}

        // 공백 구분도 허용: yyyy-MM-dd HH:mm
        DateTimeFormatter f2 = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        try { return LocalDateTime.parse(v, f2); } catch (DateTimeParseException ignore) {}

        // 초가 포함된 변형도 허용: yyyy-MM-dd'T'HH:mm:ss
        DateTimeFormatter f3 = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        try { return LocalDateTime.parse(v, f3); } catch (DateTimeParseException ignore) {}

        // 최종 실패 시 런타임으로 알려줌(로그 확인 위해 메시지 포함)
        throw new IllegalArgumentException("유효하지 않은 날짜/시간 형식: " + v);
    }
}
