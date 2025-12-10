package com.trip.user;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.trip.user.model.AccomReservationDAO;
import com.trip.user.model.AccomReservationDTO;

/**
 * 숙소 예약 상세 정보를 조회하는 서블릿
 * (마이페이지 - 내 숙소 예약 내역 상세)
 * @author jsg
 * @version 1.0
 * @since 2025.10.24
 */
@WebServlet(value = "/user/accomreservationview.do")
public class AccomReservationView extends HttpServlet {

	/**
	 * HTTP GET 요청을 처리합니다.
	 * 1. 요청 파라미터에서 마스터 예약 번호(`seq`)와 숙소 예약 번호(`accomseq`)를 가져옵니다.
	 * 2. `AccomReservationDAO`를 통해 특정 숙소 예약 상세 정보(`dto`)를 조회합니다.
	 * (참고: DAO의 메소드 이름이 `carGet`으로 되어있습니다.)
	 * 3. 조회된 `dto`를 request 속성에 저장합니다.
	 * 4. 숙소 예약 상세 페이지(`/WEB-INF/views/user/accomreservationview.jsp`)로 포워딩합니다.
	 *
	 * @param req  클라이언트가 서블릿에 보낸 HttpServletRequest 객체
	 * @param resp 서블릿이 클라이언트에 보내는 HttpServletResponse 객체
	 * @throws ServletException 서블릿 처리 중 예외 발생 시
	 * @throws IOException      요청 또는 응답 처리 중 I/O 예외 발생 시
	 */
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		HttpSession session = req.getSession();
		
		
		
		//View.java
		
		String seq = req.getParameter("seq");
		String accomseq = req.getParameter("accomseq");
		if (session.getAttribute("seq") != null) {
		    String useq = session.getAttribute("seq").toString();
		}
		
		AccomReservationDAO dao = new AccomReservationDAO();
		
		
		AccomReservationDTO dto = dao.carGet(seq,accomseq);
		
	
		
			
		
		req.setAttribute("dto", dto);

		
		RequestDispatcher dispatcher = req.getRequestDispatcher("/WEB-INF/views/user/accomreservationview.jsp");
		dispatcher.forward(req, resp);
	}
}