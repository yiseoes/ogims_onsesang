package com.semi.view.volRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.semi.domain.User;
import com.semi.domain.VolRequest;
import com.semi.framework.Action;
import com.semi.service.volRequest.VolRequestService;
import com.semi.service.volRequest.impl.VolRequestServiceImpl;

public class ProcessVolRequestAction extends Action {

    private final VolRequestService service = new VolRequestServiceImpl();

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {

    	HttpSession session = request.getSession(false);
    	Object loginUser = (session == null) ? null : session.getAttribute("user");
    	if (loginUser == null) return "redirect:/loginView.do?msg=loginRequired";

        String applicantId = ((User)loginUser).getUserId();
//        if (applicantId == null || applicantId.isEmpty()) {
//            throw new IllegalArgumentException("로그인 정보가 없습니다.(applicant)");
//        }

        long volunteerId = Long.parseLong(request.getParameter("volunteerId"));
        VolRequest item = service.getVolRequest(volunteerId);
        if (item == null) {
            throw new IllegalArgumentException("대상 게시글이 존재하지 않습니다.");
        }

        // 본인 글은 신청 불가(요구 의도)
        if (applicantId.equals(item.getAuthorId())) {
            // 필요 시 메시지를 쿼리스트링으로 전달 가능
            String qs = buildContextQS(request, "region","category","page","pageSize");
            return "redirect:/detailVolRequest.do?volunteerId=" + volunteerId + qs;
        }

        service.processVolRequest(volunteerId);

        String qs = buildContextQS(request, "region","category","page","pageSize");
        return "redirect:/detailVolRequest.do?volunteerId=" + volunteerId + qs;
    }

//    private static String getLoginUserId(HttpServletRequest req){
//        if(req.getSession(false)!=null){
//            Object v1=req.getSession(false).getAttribute("userId"); if(v1!=null) return String.valueOf(v1);
//            Object v2=req.getSession(false).getAttribute("loginUserId"); if(v2!=null) return String.valueOf(v2);
//        }
//        String p=req.getParameter("authorId"); return p==null?null:p.trim();
//    }
    private static String buildContextQS(HttpServletRequest req, String... keys){
        StringBuilder sb = new StringBuilder();
        for(String k: keys){
            String v=req.getParameter(k);
            if(v!=null && !v.trim().isEmpty()){
                sb.append("&").append(k).append("=").append(url(v.trim()));
            }
        }
        return sb.toString();
    }
    private static String url(String s){
        try{ return java.net.URLEncoder.encode(s,"UTF-8"); }catch(Exception e){ return s; }
    }
}
