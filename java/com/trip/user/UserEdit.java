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
import com.trip.board.model.BoardDTO;
import com.trip.member.model.UserDAO;
import com.trip.member.model.UserDTO;

/**
 * 사용자 정보 수정을 처리하는 서블릿
 * (마이페이지 - 내 정보 수정)
 * @author jsg
 * @version 1.0
 * @since 2025.10.24
 */
@WebServlet(value = "/user/useredit.do")
public class UserEdit extends HttpServlet {

	/**
	 * HTTP GET 요청을 처리합니다.
	 * 1. 세션에서 현재 로그인한 사용자의 고유 번호(`seq`)를 가져옵니다.
	 * 2. `UserDAO`를 통해 해당 사용자의 현재 정보를 조회합니다 (`dao.userInfo`).
	 * 3. 조회된 `UserDTO`를 request 속성에 "dto"라는 이름으로 저장합니다.
	 * 4. 사용자 정보 수정 페이지(`/WEB-INF/views/user/useredit.jsp`)로 포워딩합니다.
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
		
		
		req.setAttribute("dto", dto);

		RequestDispatcher dispatcher = req.getRequestDispatcher("/WEB-INF/views/user/useredit.jsp");
		dispatcher.forward(req, resp);
	}
	
	/**
	 * HTTP POST 요청을 처리합니다. (정보 수정 폼 제출)
	 * 1. 수정 폼에서 전송된 모든 파라미터(비밀번호, 닉네임, 주소, 성별, 키, 몸무게 등)를 받습니다.
	 * 2. 받은 파라미터를 `UserDTO`에 담습니다.
	 * 3. 세션에서 사용자 고유 번호(`seq`)를 가져와 DTO에 설정합니다 (본인 확인).
	 * 4. `UserDAO`를 통해 사용자 정보를 업데이트합니다 (`dao.userEdit`).
	 * 5. 성공 시: "수정 완료" 알림창을 띄우고, 내 정보 보기 페이지(`userinfo.do`)로 리디렉션합니다.
	 * 6. 실패 시: "failed" 알림창을 띄우고 이전 페이지로 돌아갑니다.
	 *
	 * @param req  클라이언트가 서블릿에 보낸 HttpServletRequest 객체
	 * @param resp 서블릿이 클라이언트에 보내는 HttpServletResponse 객체
	 * @throws ServletException 서블릿 처리 중 예외 발생 시
	 * @throws IOException      요청 또는 응답 처리 중 I/O 예외 발생 시
	 */
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
	
		//EditOk.java 역할
		String seq = req.getParameter("seq");
		String id = req.getParameter("id");
		String pw = req.getParameter("pw");
		String phoneNumber = req.getParameter("phoneNumber");
		String nickName = req.getParameter("nickName");
		String name = req.getParameter("name");
		String email = req.getParameter("email");
		String address = req.getParameter("address");
		String gender = req.getParameter("gender");
		String height = req.getParameter("height");
		String weight = req.getParameter("weight");
		String healthGoals = req.getParameter("healthGoals");
		
		

		UserDAO dao = new UserDAO();
		
		UserDTO dto = new UserDTO();
		dto.setId(id);
		dto.setPhoneNumber(phoneNumber);
		dto.setPw(pw);
		dto.setSeq(seq);
		dto.setNickName(nickName);
		dto.setName(name);
		dto.setEmail(email);
		dto.setAddress(address);
		dto.setGender(gender);
		dto.setHeight(height);
		dto.setWeight(weight);
		dto.setHealthGoals(healthGoals);
		
		HttpSession session = req.getSession();
		dto.setSeq(session.getAttribute("seq").toString());
		
		System.out.println(dto);
		if (dao.userEdit(dto) > 0) {
			 resp.setContentType("text/html; charset=UTF-8");

			    resp.getWriter().print("<html><meta charset='UTF-8'><script>");
			    resp.getWriter().print("alert('수정이 완료되었습니다.');");
			    resp.getWriter().print("location.href='/trip/user/userinfo.do';");
			    resp.getWriter().print("</script></html>");
			    resp.getWriter().close();
		} else {
			resp.getWriter().print("<html><meta charset='UTF-8'><script>alert('failed');history.back();</script></html>");
			resp.getWriter().close();
		}
		
	}

}