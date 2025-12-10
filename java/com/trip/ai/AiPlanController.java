package com.trip.ai;

import java.io.IOException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.trip.member.model.UserDTO;
import com.trip.member.service.UserService;

/**
 * AI 기반 여행 계획 페이지를 담당하는 서블릿
 * @author jsg
 * @version 1.0
 * @since 2025.10.24
 */
@WebServlet("/ai/plan.do")
public class AiPlanController extends HttpServlet {
	
	/**
	 * 사용자 서비스 객체
	 */
	private final UserService userService = new UserService();
	
    /**
     * AI 여행 계획 페이지를 로드하고, 로그인된 사용자 정보를 전달합니다.
     * @param req HttpServletRequest 객체
     * @param resp HttpServletResponse 객체
     * @throws ServletException 서블릿 예외
     * @throws IOException 입출력 예외
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    	
    	
        try {
	       //1. 세션에서 로그인된 사용자 ID 가져오기
	    	HttpSession session = req.getSession();
	    	String userId = (String) session.getAttribute("id");
	    	
	    	if (userId != null) {
	    	    UserDTO user = (UserDTO) session.getAttribute("user"); // 세션에서 UserDTO 꺼내기
	    	    req.setAttribute("userInfo", user);
	    	}
	    	
	    	//4. jsp로 화면 전환
	        RequestDispatcher dispatcher = req.getRequestDispatcher("/WEB-INF/views/ai/plan.jsp");
	        dispatcher.forward(req, resp);
	        
        } catch (Exception e) {
            System.out.println("--- AiPlanController doGet 오류 발생 ---");
			e.printStackTrace();
			            
			// 사용자에게는 간단한 오류 메시지를 보여줄 수도 있습니다.
			resp.setContentType("text/html; charset=UTF-8");
			resp.getWriter().println("<h1>페이지를 로드하는 중 오류가 발생했습니다.</h1>");
			resp.getWriter().println("<p>서버 로그를 확인해주세요.</p>");
		}
    }
}
