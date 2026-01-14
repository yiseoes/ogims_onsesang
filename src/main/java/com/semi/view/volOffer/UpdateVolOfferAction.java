package com.semi.view.volOffer;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import com.semi.common.util.FileUtil;
import com.semi.domain.VolOffer;
import com.semi.framework.Action;
import com.semi.service.volOffer.VolOfferService;
import com.semi.service.volOffer.impl.VolOfferServiceImpl;

public class UpdateVolOfferAction extends Action {

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {

        // 요청 파라미터
    	String postId = request.getParameter("postId");
    	if (postId == null || postId.trim().isEmpty()) {
    		postId = request.getParameter("volunteerId");
    	}

        String title    = request.getParameter("title");
        String content  = request.getParameter("content");
        String phone    = request.getParameter("phone");
        String region   = request.getParameter("region");
        String category = request.getParameter("category");
        String authorId = request.getParameter("authorId");

        String startTime = request.getParameter("startTime");
        String endTime   = request.getParameter("endTime");

        // 이미지 관련: 기존 파일명, 삭제 체크
        String existingImage = request.getParameter("existingImage"); // hidden
        String deleteImage   = request.getParameter("deleteImage");   // "Y" or null

        // 디버깅 로그
        System.out.println("[UpdateVolOfferAction] postId=" + postId);
        System.out.println("[UpdateVolOfferAction] title=" + title);
        System.out.println("[UpdateVolOfferAction] startTime=" + startTime + " endTime=" + endTime);
        System.out.println("[UpdateVolOfferAction] existingImage=" + existingImage + ", deleteImage=" + deleteImage);

        // 시간 파싱
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
        LocalDateTime st = (startTime != null && !startTime.isEmpty()) ? LocalDateTime.parse(startTime, fmt) : null;
        LocalDateTime et = (endTime   != null && !endTime.isEmpty())   ? LocalDateTime.parse(endTime,   fmt) : null;

        // 새 이미지 업로드 여부 확인
        String finalImageName = null;
        try {
            Part imagePart = null;
            try { imagePart = request.getPart("image"); } catch (IllegalStateException ise) {
                System.err.println("[UpdateVolOfferAction] getPart 실패: " + ise.getMessage());
            }

            if (imagePart != null && imagePart.getSize() > 0) {
                ServletContext ctx = request.getServletContext();
                finalImageName = FileUtil.saveImage(imagePart, ctx); // 신규 업로드 저장
                System.out.println("[UpdateVolOfferAction] saved new image = " + finalImageName);
            } else {
                // 새 업로드가 없으면 기존 유지 or 삭제
                if ("Y".equalsIgnoreCase(deleteImage)) {
                    finalImageName = null; // 삭제 의도
                } else {
                    finalImageName = (existingImage != null && !existingImage.isEmpty()) ? existingImage : null;
                }
            }
        } catch (IOException ioe) {
            System.err.println("[UpdateVolOfferAction] 이미지 저장 실패: " + ioe.getMessage());
            // 저장 실패 시 기존 이미지 유지
            finalImageName = (existingImage != null && !existingImage.isEmpty()) ? existingImage : null;
        }

        // 도메인 조립
        VolOffer vo = new VolOffer();
        vo.setPostId(Long.parseLong(postId));
        vo.setTitle(title);
        vo.setContent(content);
        vo.setAuthorId(authorId);
        vo.setPhone(phone);
        vo.setRegion(region);
        vo.setCategory(category);
        vo.setOfferFlag("o");
        vo.setStartTime(st);
        vo.setEndTime(et);
        vo.setImage(finalImageName); // null이면 삭제, 값 있으면 교체/유지

        // 서비스 호출
        VolOfferService service = new VolOfferServiceImpl();
        service.updateVolOffer(vo);

        return "redirect:/detailVolOffer.do?volunteerId=" + postId;
    }
}
