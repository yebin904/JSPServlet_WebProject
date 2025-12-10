package com.trip.user;

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
 * 사용자 로그인을 처리하는 서블릿
 * @author jsg
 * @version 1.0
 * @since 2025.10.24
 */
@WebServlet(value = "/user/login.do")
public class Login extends HttpServlet {

	private UserService userService;

    /**
     * 기본 생성자입니다.
     * UserService 객체를 초기화합니다.
     */
    public Login() {
        this.userService = new UserService();
    }

	/**
	 * HTTP GET 요청을 처리합니다.
	 * 로그인 페이지(`/WEB-INF/views/user/login.jsp`)로 포워딩합니다.
	 *
	 * @param req  클라이언트가 서블릿에 보낸 HttpServletRequest 객체
	 * @param resp 서블릿이 클라이언트에 보내는 HttpServletResponse 객체
	 * @throws ServletException 서블릿 처리 중 예외 발생 시
	 * @throws IOException      요청 또는 응답 처리 중 I/O 예외 발생 시
	 */
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		RequestDispatcher dispatcher = req.getRequestDispatcher("/WEB-INF/views/user/login.jsp");
		dispatcher.forward(req, resp);
	}
	
	/**
	 * HTTP POST 요청을 처리합니다.
	 * 1. 요청 파라미터에서 ID와 PW를 받습니다.
	 * 2. `UserService`를 통해 로그인을 시도합니다.
	 * 3. 로그인 성공 시, `loginCheck`로 추가 검증을 수행합니다.
	 * 4. 최종 성공 시, 세션에 사용자 정보(UserDTO)를 "user"라는 이름으로 저장하고 메인 페이지로 리디렉션합니다.
	 * 5. 로그인 실패 시, 알림창을 띄우고 이전 페이지로 돌아가도록 합니다.
	 *
	 * @param req  클라이언트가 서블릿에 보낸 HttpServletRequest 객체
	 * @param resp 서블릿이 클라이언트에 보내는 HttpServletResponse 객체
	 * @throws ServletException 서블릿 처리 중 예외 발생 시
	 * @throws IOException      요청 또는 응답 처리 중 I/O 예외 발생 시
	 */
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		String id = req.getParameter("id");
		String pw = req.getParameter("pw");

		UserDTO user = new UserDTO();
		user.setUsername(id);
		user.setPassword(pw);
		
		UserDTO result = userService.login(user); 
		
		if(result != null) {
			int loginCheck = userService.loginCheck(result);
			if(loginCheck == 1) {
				HttpSession session = req.getSession();
				
				session.setAttribute("user", result); // Save the whole user object
				
				resp.sendRedirect("/trip/main.do");
			} else {
				// Login failed
				resp.setCharacterEncoding("UTF-8");
				resp.getWriter().print("<html><meta charset='UTF-8'><script>alert('로그인 실패');history.back();</script></html>");
				resp.getWriter().close();
			}
		} else {
			// Login failed
			resp.setCharacterEncoding("UTF-8");
			resp.getWriter().print("<html><meta charset='UTF-8'><script>alert('로그인 실패');history.back();</script></html>");
			resp.getWriter().close();
		}
	}

}