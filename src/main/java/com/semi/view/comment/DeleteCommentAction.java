// /view/comment/DeleteCommentAction.java

package com.semi.view.comment;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.semi.framework.Action;
import com.semi.service.comment.CommentService;
import com.semi.service.comment.impl.CommentServiceImpl;

public class DeleteCommentAction extends Action {

    private final CommentService commentService = new CommentServiceImpl();

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {

    	HttpSession session = request.getSession(false);
    	Object loginUser = (session == null) ? null : session.getAttribute("user");
    	
    	if (loginUser == null) {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			response.getWriter().write("{\"success\": false, \"message\": \"loginRequired\"}");
			return null;
		}
		// TODO: 권한 확인 로직 추가 필요

        long commentId   = Long.parseLong(request.getParameter("commentId"));
        
        commentService.deleteComment(commentId);

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write("{\"success\": true}");
        return null;
    }
}