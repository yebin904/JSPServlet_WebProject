package com.trip.user;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.trip.member.model.UserDAO;
import com.trip.member.model.UserDTO;

/**
 * 사용자 정보를 조회하는 서블릿 (마이페이지 - 내 정보 보기)
 * @author jsg
 * @version 1.0
 * @since 2025.10.24
 */
@WebServlet(value = "/user/userinfo.do")
public class UserInfo extends HttpServlet {

	/**
	 * HTTP GET 요청을 처리합니다.
	 * 1. 세션에서 현재 로그인한 사용자의 고유 번호(`seq`)를 가져옵니다.
	 * 2. `UserDAO`를 통해 해당 사용자의 모든 정보를 조회합니다 (`dao.userInfo`).
	 * 3. 조회된 `UserDTO`를 request 속성에 "dto"라는 이름으로 저장합니다.
	 * 4. 사용자 정보 조회 페이지(`/WEB-INF/views/user/userinfo.jsp`)로 포워딩합니다.
	 *
	 * @param req  클라이언트가 서블릿에 보낸 HttpServletRequest 객체
	 * @param resp 서블릿이 클라이언트에 보내는 HttpServletResponse 객체
	 * @throws ServletException 서블릿 처리 중 예외 발생 시
	 * @throws IOException      요청 또는 응답 처리 중 I/O 예외 발생 시
	 */
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		//UserInfo.java
		
		HttpSession session = req.getSession();
		
		String seq = session.getAttribute("seq").toString();
		
		UserDAO dao = new UserDAO();
		
		UserDTO dto = new UserDTO();
		
		dto = dao.userInfo(seq);
		
		System.out.println(dto);
		
		req.setAttribute("dto", dto);

		RequestDispatcher dispatcher = req.getRequestDispatcher("/WEB-INF/views/user/userinfo.jsp");
		dispatcher.forward(req, resp);
	}

}