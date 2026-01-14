// /framework/ActionServlet.java

package com.semi.framework;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.semi.common.util.HttpUtil;

public class ActionServlet extends HttpServlet {
	
	///Field
	private RequestMapping requestMapping;
	
	///Method
	@Override
	public void init() throws ServletException {
		super.init();
		String resources=getServletConfig().getInitParameter("resources");
		requestMapping=RequestMapping.getInstance(resources);
	}

	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response) {
		// ActionServlet.service(...) 내부: path 계산부 교체
		String ctx = request.getContextPath();
	
		// include/forward 우선
		String includePath = (String) request.getAttribute("javax.servlet.include.servlet_path");
		String forwardPath = (String) request.getAttribute("javax.servlet.forward.servlet_path");
		String servletPath = request.getServletPath();
		String uri        = request.getRequestURI();
	
		String reqeustPath;
		if (includePath != null) {
			reqeustPath = includePath;                    // <c:import> 같은 include 시: /listComment.do
		} else if (forwardPath != null) {
			reqeustPath = forwardPath;                    // forward 시
		} else if (servletPath != null && !servletPath.isEmpty()) {
			reqeustPath = servletPath;                    // 평소 *.do 매핑
		} else {
		    // 최후의 수단: URI에서 컨텍스트 제거
			reqeustPath = uri;
		}

		// 항상 컨텍스트 경로 제거 (어떤 경로든 /semiProject가 붙어있으면 제거)
		if (ctx != null && !ctx.isEmpty() && reqeustPath.startsWith(ctx)) {
			reqeustPath = reqeustPath.substring(ctx.length());
		}

		System.out.println("ActionServlet.service() resolved path : " + reqeustPath);
		
		try{
			Action action = requestMapping.getAction(reqeustPath);
			action.setServletContext(getServletContext());
			
			String resultPage=action.execute(request, response);

			// ========================[수정된 부분 시작]========================
			// Action의 execute() 메서드가 null을 반환하면,
			// 해당 Action이 이미 응답(JSON 등)을 직접 처리했음을 의미합니다.
			// 따라서 서블릿은 포워딩이나 리다이렉션 없이 처리를 종료합니다.
			if (resultPage == null) {
				System.out.println("---ActionServlet execute null return---");
				return;
			}
			// ========================[수정된 부분 끝]==========================

			System.out.println("[ActionServlet] resultPage=" + resultPage);

			// redirect: 로 시작하면 redirect, 그 외에는 forward
			if(resultPage.startsWith("redirect:")){
				String path = resultPage.substring("redirect:".length());
				// redirect 경로에 context path 추가 (절대 경로인 경우)
				String redirectPath = path;
				if (path.startsWith("/") && !path.startsWith(ctx) && ctx != null && !ctx.isEmpty()) {
					redirectPath = ctx + path;
				}
				System.out.println("[ActionServlet] >>> REDIRECT to: " + redirectPath);
				HttpUtil.redirect(response, redirectPath);
			}else{
				// forward: prefix가 있으면 제거, 없으면 그대로 사용
				String path = resultPage.startsWith("forward:")
					? resultPage.substring("forward:".length())
					: resultPage;
				System.out.println("[ActionServlet] >>> FORWARD to: " + path);
				HttpUtil.forward(request, response, path);
			}
		}catch(Exception ex){
			ex.printStackTrace();
			// 에러 페이지로 이동 또는 에러 메시지 표시
			try {
				request.setAttribute("errorMessage", ex.getMessage());
				request.setAttribute("errorDetail", ex.toString());
				response.setContentType("text/html; charset=UTF-8");
				response.getWriter().println("<html><head><meta charset='UTF-8'></head><body>");
				response.getWriter().println("<h2>오류가 발생했습니다</h2>");
				response.getWriter().println("<p><b>메시지:</b> " + ex.getMessage() + "</p>");
				response.getWriter().println("<p><a href='javascript:history.back()'>뒤로가기</a></p>");
				response.getWriter().println("</body></html>");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}