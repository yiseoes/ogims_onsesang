// /view/comment/AddCommentAction.java

package com.semi.view.comment;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.semi.domain.Comment;
import com.semi.domain.User;
import com.semi.framework.Action;
import com.semi.service.comment.CommentService;
import com.semi.service.comment.impl.CommentServiceImpl;

public class AddCommentAction extends Action {

    private final CommentService commentService = new CommentServiceImpl();

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {

    	HttpSession session = request.getSession(false);
    	Object loginUser = (session == null) ? null : session.getAttribute("user");
    	
    	if (loginUser == null) {
			// 비동기 요청 시에는 로그인 페이지로 리다이렉트하는 대신, 
			// 권한 없음(401) 상태 코드와 에러 메시지를 JSON으로 반환합니다.
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			response.getWriter().write("{\"success\": false, \"message\": \"loginRequired\"}");
			return null;
		}
    	
        String authorId = ((User)loginUser).getUserId();
        
        long volunteerId = Long.parseLong(request.getParameter("volunteerId"));
        String content   = nvl(request.getParameter("content"));
        if (content.isEmpty()) {
            // 간단한 유효성 검사 실패 시 400 상태 코드와 메시지 반환
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			response.getWriter().write("{\"success\": false, \"message\": \"댓글 내용을 입력하세요.\"}");
			return null;
        }

        Comment c = new Comment();
        c.setAuthorId(authorId);
        c.setVolunteerId(volunteerId);
        c.setContent(content);

        commentService.addComment(c);
		
        // 1. 응답 헤더를 JSON 타입으로 설정
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        // 2. 클라이언트에게 보낼 JSON 데이터 작성 (성공 여부)
        response.getWriter().write("{\"success\": true}");

        // 3. ActionServlet에게 응답이 완료되었음을 알리기 위해 null 반환
        return null;
    }

    private static String nvl(String s){ return s==null? "" : s.trim(); }
}