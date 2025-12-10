package com.trip.reservation;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.trip.reservation.accom.model.AccomDAO;
import com.trip.reservation.accom.model.AccomDTO;
import com.trip.reservation.car.model.CarDAO;
import com.trip.reservation.car.model.CarDTO;

/**
 * 숙소 및 차량 선택 후, 최종 예약 확인 페이지로 이동을 처리하는 서블릿입니다.
 * (참고: 파일명은 ReservationConfirm이지만, URL 매핑이 /reservation/confirm.do이고
 * 로직상 ReservationInsert.java와 매우 유사합니다.
 * 실제 예약 '확정'(DB 삽입)이 아닌 '확인 페이지로의 데이터 전달' 역할을 합니다.)
 */
@WebServlet("/reservation/confirm.do")
public class ReservationConfirm extends HttpServlet {

    /**
	 * HTTP GET 요청을 처리합니다.
	 * 1. 요청 파라미터(숙소 ID, 차량 ID 등)와 세션 정보(날짜, 인원, 사용자 경로 ID)를 가져옵니다.
	 * 2. 값이 누락된 경우(날짜, 인원 등) 세션 값이나 기본값으로 보정합니다.
	 * 3. `AccomDAO`를 통해 숙소 상세 정보(필수)를 조회합니다.
	 * 4. `CarDAO`를 통해 차량 상세 정보(선택)를 조회합니다.
	 * 5. 조회된 모든 정보(숙소, 차량, 날짜, 지역, 인원 등)를 request 속성에 저장합니다.
	 * 6. 최종 예약 확인 페이지(`/WEB-INF/views/reservation/confirm.jsp`)로 포워딩합니다.
	 *
	 * @param req  클라이언트가 서블릿에 보낸 HttpServletRequest 객체
	 * @param resp 서블릿이 클라이언트에 보내는 HttpServletResponse 객체
	 * @throws ServletException 서블릿 처리 중 예외 발생 시
	 * @throws IOException      요청 또는 응답 처리 중 I/O 예외 발생 시
	 */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession session = req.getSession();

        // 테스트용 기본 세션 값
        if (session.getAttribute("userId") == null) session.setAttribute("userId", "1");
        if (session.getAttribute("start_date") == null) session.setAttribute("start_date", "2025-10-20");
        if (session.getAttribute("end_date") == null) session.setAttribute("end_date", "2025-10-23");
        if (session.getAttribute("people") == null) session.setAttribute("people", "2");

        req.setCharacterEncoding("UTF-8");

        String roomId = req.getParameter("room_id"); // 숙소 필수
        String carId = req.getParameter("car_id");   // 차량 선택
        String region = req.getParameter("region");
        String startDate = req.getParameter("start_date");
        String endDate = req.getParameter("end_date");
        String people = req.getParameter("people");
        String userRouteId = req.getParameter("user_route_id");

        // 기본값 보정
        if (userRouteId == null || userRouteId.isEmpty()) {
            userRouteId = (String) session.getAttribute("userRouteId");
        }
        if (userRouteId == null || userRouteId.isEmpty()) {
            userRouteId = "1";
            
        }
        session.setAttribute("userRouteId", userRouteId);
        req.setAttribute("user_route_id", userRouteId);

        // 세션 값 보정
        if (startDate == null || startDate.isEmpty()) startDate = (String) session.getAttribute("start_date");
        if (endDate == null || endDate.isEmpty()) endDate = (String) session.getAttribute("end_date");
        if (people == null || people.isEmpty()) people = (String) session.getAttribute("people");

        // 숙소 정보 로드
        AccomDTO room = null;
        if (roomId != null && !roomId.isEmpty()) {
            AccomDAO aDao = new AccomDAO();
            room = aDao.findRoomById(roomId);
        }

        // 차량 정보 로드 (없을 수도 있음)
        CarDTO car = null;
        if (carId != null && !carId.equals("0") && !carId.isEmpty()) {
            CarDAO cDao = new CarDAO();
            car = cDao.findById(carId);
        } else {
            System.out.println("[INFO] 차량 미선택 (car_id=0)");
        }

        req.setAttribute("room", room);
        req.setAttribute("car", car);
        req.setAttribute("region", region);
        req.setAttribute("start_date", startDate);
        req.setAttribute("end_date", endDate);
        req.setAttribute("people", people);

        RequestDispatcher rd = req.getRequestDispatcher("/WEB-INF/views/reservation/confirm.jsp");
        rd.forward(req, resp);
    }
}