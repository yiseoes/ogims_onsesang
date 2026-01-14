// com.semi.view.notice.AddNoticeViewAction
package com.semi.view.notice;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.semi.framework.Action;

public class AddNoticeViewAction extends Action {
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
        // 등록 화면으로 단순 진입
        return "forward:/notice/addNoticeView.jsp";
    }
}
