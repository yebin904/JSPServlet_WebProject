package com.trip.user;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.trip.board.model.BoardDAO;
import com.trip.member.model.UserDAO;
import com.trip.member.model.UserDTO;

/**
 * 사용자 회원 탈퇴를 처리하는 서블릿
 * @author jsg
 * @version 1.0
 * @since 2025.10.24
 */
@WebServlet(value = "/user/userdel.do")
public class UserDel extends HttpServlet {

	/**
	 * HTTP GET 요청을 처리합니다.
	 * 회원 탈퇴를 위한 비밀번호 확인 페이지(`/WEB-INF/views/user/userdel.jsp`)로 포워딩합니다.
	 *
	 * @param req  클라이언트가 서블릿에 보낸 HttpServletRequest 객체
	 * @param resp 서블릿이 클라이언트에 보내는 HttpServletResponse 객체
	 * @throws ServletException 서블릿 처리 중 예외 발생 시
	 * @throws IOException      요청 또는 응답 처리 중 I/O 예외 발생 시
	 */
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		//Del.java
		String seq = req.getParameter("seq");
		
		req.setAttribute("seq", seq);

		RequestDispatcher dispatcher = req.getRequestDispatcher("/WEB-INF/views/user/userdel.jsp");
		dispatcher.forward(req, resp);
	}
	
	/**
	 * HTTP POST 요청을 처리합니다. (회원 탈퇴 폼 제출)
	 * 1. 요청 파라미터에서 탈퇴 확인용 비밀번호(`pw`)를 받습니다.
	 * 2. 세션에서 현재 로그인한 사용자 고유 번호(`seq`)를 가져옵니다.
	 * 3. `UserDAO`를 통해 비밀번호가 일치하는지 확인합니다 (`dao.userDelCheck`).
	 * 4. 비밀번호가 일치하면 (`result == 1`):
	 * a. `UserDAO`를 통해 회원 탈퇴를 처리합니다 (`dao.userDel`).
	 * b. 세션을 무효화시킵니다 (`session.invalidate`).
	 * c. "탈퇴 완료" 알림창을 띄우고 메인 페이지(`/trip/main.do`)로 리디렉션합니다.
	 * 5. 비밀번호가 틀리면 (`result == 0`):
	 * a. "비밀번호 다시 확인" 알림창을 띄우고 이전 페이지로 돌아갑니다.
	 *
	 * @param req  클라이언트가 서블릿에 보낸 HttpServletRequest 객체
	 * @param resp 서블릿이 클라이언트에 보내는 HttpServletResponse 객체
	 * @throws ServletException 서블릿 처리 중 예외 발생 시
	 * @throws IOException      요청 또는 응답 처리 중 I/O 예외 발생 시
	 */
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
	
		String pw = req.getParameter("pw");
		System.out.println(pw);
		
		HttpSession session = req.getSession();
		
		String seq = session.getAttribute("seq").toString();

		UserDAO dao = new UserDAO();
		
		UserDTO dto = new UserDTO();
		dto.setPw(pw);
		dto.setSeq(seq);
		
		System.out.println(dto.getPw());

		//int result = dao.login(dto); //1, 0
		int result = dao.userDelCheck(dto); //dto, null
		System.out.println("result확인 "+result);
		
		if (result == 1) {
			
		dao.userDel(dto);
	    session.invalidate();
		resp.setCharacterEncoding("UTF-8");
		 resp.setContentType("text/html; charset=UTF-8");
		    resp.getWriter().print("<html><meta charset='UTF-8'><script>");
		    resp.getWriter().print("alert('탈퇴가 완료되었습니다.');");
		    resp.getWriter().print("location.href='/trip/main.do';"); // 로그아웃 후 메인 페이지로 이동
		    resp.getWriter().print("</script></html>");
		    resp.getWriter().close();


			
		} else {
			
			resp.setCharacterEncoding("UTF-8");
			resp.getWriter().print("<html><meta charset='UTF-8'><script>alert('비밀번호를 다시 확인해주세요.');history.back();</script></html>");
			resp.getWriter().close();
			
			
		}
		
	}
	

}