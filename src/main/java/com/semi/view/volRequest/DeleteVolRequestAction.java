package com.semi.view.volRequest;

import java.nio.file.*;
import javax.servlet.ServletContext;
import javax.servlet.http.*;

import com.semi.framework.Action;
import com.semi.service.volRequest.VolRequestService;
import com.semi.service.volRequest.impl.VolRequestServiceImpl;

public class DeleteVolRequestAction extends Action {

    private final VolRequestService service = new VolRequestServiceImpl();

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {

        long volunteerId = Long.parseLong(request.getParameter("volunteerId"));

        // 삭제 전 파일명 확보
        String oldImage = service.findImageByVolunteerId(volunteerId);

        // DB 삭제
        service.deleteVolRequest(volunteerId);

        // 물리 파일 삭제
        if (oldImage != null && !oldImage.isEmpty()) {
            deleteFromWebappImages(oldImage, getServletContext());
        }

        return "redirect:/listVolRequest.do";
    }

    private static void deleteFromWebappImages(String storedName, ServletContext ctx) {
        try {
            String imagesPath = ctx.getRealPath("/images");
            Files.deleteIfExists(Paths.get(imagesPath, storedName));
        } catch (Exception ignore) {}
    }
}
