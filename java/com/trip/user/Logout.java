package com.trip.user;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * 사용자 로그아웃을 처리하는 서블릿
 * @author jsg
 * @version 1.0
 * @since 2025.10.24
 */
@WebServlet(value = "/user/logout.do")
public class Logout extends HttpServlet {

	/**
	 * HTTP GET 요청을 처리합니다.
	 * 1. 현재 세션을 가져옵니다.
	 * 2. `session.invalidate()` 메소드를 호출하여 세션을 무효화(초기화)합니다.
	 * (세션에 저장된 모든 인증 정보 및 속성이 제거됩니다.)
	 * 3. 메인 페이지(`/trip/main.do`)로 리디렉션합니다.
	 *
	 * @param req  클라이언트가 서블릿에 보낸 HttpServletRequest 객체
	 * @param resp 서블릿이 클라이언트에 보내는 HttpServletResponse 객체
	 * @throws ServletException 서블릿 처리 중 예외 발생 시
	 * @throws IOException      요청 또는 응답 처리 중 I/O 예외 발생 시
	 */
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		//Logout.java
		//세션 초기화
		//- 인증 티켓 제거
		HttpSession session = req.getSession();
		
		//session.removeAttribute("id");
		//session.removeAttribute("name");
		//session.removeAttribute("lv");
		
		session.invalidate();
		
		resp.sendRedirect("/trip/main.do");		
	}

}