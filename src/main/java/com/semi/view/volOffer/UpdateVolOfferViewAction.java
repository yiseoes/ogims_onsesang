package com.semi.view.volOffer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.semi.domain.VolOffer;
import com.semi.framework.Action;
import com.semi.service.volOffer.VolOfferService;
import com.semi.service.volOffer.impl.VolOfferServiceImpl;


public class UpdateVolOfferViewAction extends Action{

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {	
		// 
		String userId=request.getParameter("userId");
		String postIdStr = request.getParameter("postId");
		Long postId = Long.parseLong(postIdStr);
		
		System.out.println("==UpdateVolOfferViewAction - execute - getParameter userId : " + userId );
		System.out.println("==UpdateVolOfferViewAction - execute - getParameter postId : " + postId );
		
		VolOfferService volOfferService=new VolOfferServiceImpl();
		VolOffer volOffer=volOfferService.getVolOffer(postId);
		
		System.out.println( "==UpdateVolOfferViewAction - execute - volOffer ::  " + volOffer);
		//request.setAttribute("user", user);
		request.setAttribute("volOffer", volOffer);
		
		return "forward:/volOffer/updateVolOffer.jsp";
	}
}