package com.trip.ai;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 메인 경로 페이지를 담당하는 서블릿
 * @author jsg
 * @version 1.0
 * @since 2025.10.24
 */
@WebServlet(value = "/route/mainroute.do")
public class RouteMainController extends HttpServlet {

	/**
	 * 메인 경로 페이지를 로드합니다.
	 * @param req HttpServletRequest 객체
	 * @param resp HttpServletResponse 객체
	 * @throws ServletException 서블릿 예외
	 * @throws IOException 입출력 예외
	 */
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		//MainController.java

		RequestDispatcher dispatcher = req.getRequestDispatcher("/WEB-INF/views/route/mainroute.jsp");
		dispatcher.forward(req, resp);
	}

}
