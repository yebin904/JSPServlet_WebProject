package com.trip.user;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.trip.user.model.CarReservationDAO;
import com.trip.user.model.CarReservationDTO;




/**
 * 렌터카 예약 상세 정보를 조회하는 서블릿
 * @author jsg
 * @version 1.0
 * @since 2025.10.24
 */
@WebServlet(value = "/user/carreservationview.do")
public class CarReservationView extends HttpServlet {

	/**
	 * HTTP GET 요청을 처리합니다.
	 * 1. 세션에서 사용자 정보(`seq`)를 가져옵니다 (현재 코드에서는 사용되지 않음).
	 * 2. 요청 파라미터에서 마스터 예약 번호(`seq`)와 차량 예약 번호(`carseq`)를 가져옵니다.
	 * 3. `CarReservationDAO`를 통해 `seq`와 `carseq`를 사용하여 특정 렌터카 예약 상세 정보(`dto`)를 조회합니다.
	 * 4. 조회된 `dto`를 request 속성에 저장합니다.
	 * 5. 렌터카 예약 상세 페이지(`/WEB-INF/views/user/carreservationview.jsp`)로 포워딩합니다.
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
		String carseq = req.getParameter("carseq");

		if (session.getAttribute("seq") != null) {
		    String useq = session.getAttribute("seq").toString();
		}
		
		CarReservationDAO dao = new CarReservationDAO();
		
		
		CarReservationDTO dto = dao.carGet(seq,carseq);
		
		System.out.println("carseq 테스트:"+carseq);
	
		
			
		
		req.setAttribute("dto", dto);

		
		RequestDispatcher dispatcher = req.getRequestDispatcher("/WEB-INF/views/user/carreservationview.jsp");
		dispatcher.forward(req, resp);
	}
}