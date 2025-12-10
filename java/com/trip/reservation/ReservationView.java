package com.trip.reservation;

import java.io.IOException;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import com.trip.reservation.model.ReservationDAO;
import com.trip.reservation.model.ReservationDetailDTO;

/**
 * 예약 상세 정보를 조회하는 서블릿
 * @author jsg
 * @version 1.0
 * @since 2025.10.24
 */
@WebServlet("/reservation/view.do")
public class ReservationView extends HttpServlet {

    /**
	 * HTTP GET 요청을 처리합니다.
	 * 세션에서 사용자 ID를 확인하고, 요청 파라미터에서 예약 ID('id')를 가져옵니다.
	 * 예약 ID가 없으면 목록 페이지로 리디렉션합니다.
	 * `ReservationDAO`를 통해 해당 예약 ID와 사용자 ID로 예약 상세 정보(숙소, 차량 포함)를 조회합니다.
	 * 조회된 DTO가 없거나(본인 예약이 아님) 오류 발생 시, 알림창을 띄우고 이전 페이지로 돌아갑니다.
	 * 정상 조회 시, DTO를 'dto'라는 이름으로 request 속성에 저장하고
	 * 예약 상세 페이지(`/WEB-INF/views/reservation/view.jsp`)로 포워딩합니다.
	 * * @param req 클라이언트가 서블릿에 보낸 HttpServletRequest 객체
	 * @param resp 서블릿이 클라이언트에 보내는 HttpServletResponse 객체
	 * @throws ServletException 서블릿 처리 중 예외 발생 시
	 * @throws IOException 요청 또는 응답 처리 중 I/O 예외 발생 시
	 */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession session = req.getSession();
        String userIdStr = (String) session.getAttribute("userId");
        if (userIdStr == null) {
            userIdStr = "1"; // 테스트용
            session.setAttribute("userId", userIdStr);
        }

        String reservationId = req.getParameter("id");
        if (reservationId == null) {
            resp.sendRedirect("list.do");
            return;
        }

        long userId = Long.parseLong(userIdStr);
        long resvId = Long.parseLong(reservationId);

        try (ReservationDAO dao = new ReservationDAO()) {
            ReservationDetailDTO dto = dao.getReservationDetail(resvId, userId);

            if (dto == null) {
                resp.getWriter().write("<script>alert('예약 정보를 불러올 수 없습니다.');history.back();</script>");
                return;
            }

            req.setAttribute("dto", dto);
            RequestDispatcher rd = req.getRequestDispatcher("/WEB-INF/views/reservation/view.jsp");
            rd.forward(req, resp);
        } catch (Exception e) {
            e.printStackTrace();
            resp.getWriter().write("<script>alert('예약 상세 조회 중 오류 발생');history.back();</script>");
        }
    }
}