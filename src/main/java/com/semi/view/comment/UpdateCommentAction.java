// /view/comment/UpdateCommentAction.java

package com.semi.view.comment;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.semi.domain.User;
import com.semi.framework.Action;
import com.semi.service.comment.CommentService;
import com.semi.service.comment.impl.CommentServiceImpl;

public class UpdateCommentAction extends Action {

    private final CommentService commentService = new CommentServiceImpl();

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {

		// 세션을 통해 로그인 여부 확인
    	HttpSession session = request.getSession(false);
    	Object loginUser = (session == null) ? null : session.getAttribute("user");
    	
    	if (loginUser == null) {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			response.getWriter().write("{\"success\": false, \"message\": \"loginRequired\"}");
			return null;
		}
		// TODO: 더 정확한 권한 확인을 위해서는 commentId로 댓글 정보를 조회하여
		//       작성자와 세션 사용자가 일치하는지 확인하는 로직이 필요합니다.

        long commentId   = Long.parseLong(request.getParameter("commentId"));
        String content   = nvl(request.getParameter("content"));

        commentService.updateComment(commentId, content);

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write("{\"success\": true}");
        return null;
    }

    private static String nvl(String s){ return s==null? "" : s.trim(); }
}