package com.trip.ai;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.trip.ai.model.RouteDTO;

/**
 * AI 추천 결과 페이지(result.jsp)를 보여주는 서블릿입니다.
 * @author jsg
 * @version 1.0
 * @since 2025.10.24
 */
@WebServlet("/ai/result.do")
public class ResultController extends HttpServlet {

	/**
	 * AI 추천 결과 페이지로 리다이렉트하거나, 세션에 데이터가 없을 경우 계획 페이지로 이동합니다.
	 * @param req HttpServletRequest 객체
	 * @param resp HttpServletResponse 객체
	 * @throws ServletException 서블릿 예외
	 * @throws IOException 입출력 예외
	 */
	@Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        
        System.out.println("AI 결과 처리 및 리다이렉트 서블릿 진입");

        HttpSession session = req.getSession();
        RouteDTO resultRoute = (RouteDTO) session.getAttribute("resultRoute");
        
        	if (resultRoute != null) {
            
            // 1. 세션에서 가져온 DTO 객체로부터 경로 ID를 꺼냅니다.
            // (RouteDTO에 getAi_route_id() 메서드가 있다고 가정합니다.)
            long routeId = resultRoute.getAi_route_id(); 
            
            // 2. 사용한 세션 데이터는 즉시 삭제합니다.
            session.removeAttribute("resultRoute"); 
            
            // 3. 최종적으로 이동할 URL을 만듭니다.
            String redirectUrl = req.getContextPath() + "/route/aiMapViewPage.do?id=" + 26;
            System.out.println("이동할 최종 URL: " + redirectUrl);
            
            // 4. 브라우저에게 해당 URL로 이동하라는 명령을 보냅니다.
            resp.sendRedirect(redirectUrl);
            
        } else {
            // 세션에 데이터가 없는 경우
            System.out.println("세션에 AI 루트 데이터가 없습니다. plan 페이지로 이동합니다.");
            resp.sendRedirect(req.getContextPath() + "/ai/plan.do");
        }
        
    }
}
