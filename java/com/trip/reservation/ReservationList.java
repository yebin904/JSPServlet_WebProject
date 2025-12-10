package com.trip.reservation;

import java.io.IOException;
import java.util.List;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import com.trip.reservation.model.ReservationDAO;
import com.trip.reservation.model.ReservationListDTO;

/**
 * 예약 목록을 조회하는 서블릿
 * @author jsg
 * @version 1.0
 * @since 2025.10.24
 */
@WebServlet("/reservation/list.do")
public class ReservationList extends HttpServlet {

    /**
	 * HTTP GET 요청을 처리합니다.
	 * 세션에서 현재 로그인된 사용자의 ID를 가져옵니다. (테스트용 ID "1" 사용)
	 * `ReservationDAO`를 통해 해당 사용자의 전체 예약 목록을 조회합니다.
	 * 조회된 목록을 'reservationList'라는 이름으로 request 속성에 저장한 후,
	 * 예약 목록 페이지(`/WEB-INF/views/reservation/list.jsp`)로 포워딩합니다.
	 * * @param req 클라이언트가 서블릿에 보낸 HttpServletRequest 객체
	 * @param resp 서블릿이 클라이언트에 보내는 HttpServletResponse 객체
	 * @throws ServletException 서블릿 처리 중 예외 발생 시
	 * @throws IOException 요청 또는 응답 처리 중 I/O 예외 발생 시
	 */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession session = req.getSession();
        String userId = (String) session.getAttribute("userId");
        if (userId == null) userId = "1"; // 테스트용

        ReservationDAO dao = new ReservationDAO();
        List<ReservationListDTO> list = dao.getReservationList(Long.parseLong(userId));

        req.setAttribute("reservationList", list);
        RequestDispatcher rd = req.getRequestDispatcher("/WEB-INF/views/reservation/list.jsp");
        rd.forward(req, resp);
    }
}