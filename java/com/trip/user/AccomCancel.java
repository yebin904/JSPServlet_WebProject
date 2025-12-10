package com.trip.user;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.trip.user.model.CarReservationDAO;

/**
 * 숙소 예약을 취소하는 서블릿
 * (AJAX 또는 Form Submit으로 호출되는 것으로 보입니다)
 * @author jsg
 * @version 1.0
 * @since 2025.10.24
 */
@WebServlet(value = "/user/accomcancel.do")
public class AccomCancel extends HttpServlet {

	/**
	 * HTTP POST 요청을 처리합니다.
	 * 1. 요청 파라미터에서 취소할 숙소 예약 번호(`accomseq`)를 가져옵니다.
	 * 2. `CarReservationDAO`의 `addAccomCancel` 메소드를 호출하여 예약을 취소 처리합니다.
	 * (참고: DAO 클래스 이름이 `CarReservationDAO`로 되어 있으나 숙소 취소 로직을 호출하고 있습니다.)
	 *
	 * @param req  클라이언트가 서블릿에 보낸 HttpServletRequest 객체
	 * @param resp 서블릿이 클라이언트에 보내는 HttpServletResponse 객체
	 * @throws ServletException 서블릿 처리 중 예외 발생 시
	 * @throws IOException      요청 또는 응답 처리 중 I/O 예외 발생 시
	 */
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		//AddComment.java
		
		HttpSession session = req.getSession();
		
		String accomseq = req.getParameter("accomseq");
		
		CarReservationDAO dao = new CarReservationDAO();
		
	
		
		
		dao.addAccomCancel(accomseq); 
		
	}


}