package com.trip.reservation;

import java.io.IOException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import com.trip.reservation.model.ReservationDAO;
import com.trip.reservation.accom.model.AccomDAO;
import com.trip.reservation.accom.model.AccomDTO;
import com.trip.reservation.car.model.CarDAO;
import com.trip.reservation.car.model.CarDTO;

@WebServlet("/reservation/insert.do")
public class ReservationInsert extends HttpServlet {
	

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");
        HttpSession session = req.getSession();

        // [1] 사용자 정보
        String userIdStr = (String) session.getAttribute("userId");
        if (userIdStr == null) userIdStr = "1"; // 테스트용
        long userId = Long.parseLong(userIdStr);

        String userRouteIdStr = req.getParameter("user_route_id");
        if (userRouteIdStr == null || userRouteIdStr.isBlank()) {
            userRouteIdStr = (String) session.getAttribute("userRouteId");
        }
        long userRouteId = 1L;
        try {
            if (userRouteIdStr != null && !userRouteIdStr.isBlank()) {
                userRouteId = Long.parseLong(userRouteIdStr);
            }
        } catch (NumberFormatException ignore) {}

        // [2] 예약 기본 정보
        String roomIdStr = req.getParameter("room_id");
        String carIdStr = req.getParameter("car_id");
        String startDate = req.getParameter("start_date");
        String endDate = req.getParameter("end_date");

        // ===== 숙소 요금 계산 =====
        AccomDTO room = null;
        long roomPrice = 0L;
        try {
            AccomDAO aDao = new AccomDAO();
            room = aDao.findRoomById(roomIdStr);
            if (room != null) roomPrice = room.getPrice_per_night();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // ===== 차량 요금 계산 (선택) =====
        CarDTO car = null;
        long carPrice = 0L;
        try {
            if (carIdStr != null && !carIdStr.isBlank()) {
                CarDAO cDao = new CarDAO();
                car = cDao.findById(carIdStr);
                if (car != null) carPrice = car.getCar_price_per_day();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // ===== 여행일수 계산 =====
        long days = 1;
        try {
            LocalDate s = LocalDate.parse(startDate);
            LocalDate e = LocalDate.parse(endDate);
            days = ChronoUnit.DAYS.between(s, e);
            if (days <= 0) days = 1;
        } catch (Exception e) {
            System.out.println("[WARN] 날짜 계산 실패, 기본 1일로 처리");
        }

        long totalPrice = (roomPrice + carPrice) * days;

        // [3] DB 처리 (통합 예약)
        try (ReservationDAO dao = new ReservationDAO()) {

            int statusId = 1; // 초기 상태: 예약요청
            long carId = (carIdStr != null && !carIdStr.isBlank()) ? Long.parseLong(carIdStr) : 0L;
            long roomId = Long.parseLong(roomIdStr);

            long reservationId = dao.createIntegratedReservation(
                    userId,
                    userRouteId,
                    statusId,
                    totalPrice,
                    startDate,
                    endDate,
                    roomId,
                    startDate,  // checkinDate
                    endDate,    // checkoutDate
                    carId > 0 ? carId : null,
                    startDate,  // rentalStart
                    endDate     // rentalEnd
            );

            if (reservationId > 0) {
                req.setAttribute("reservationId", reservationId);
                req.setAttribute("totalPrice", totalPrice);
                req.setAttribute("roomName", room != null ? room.getRoom_name() : "숙소 정보 없음");
                req.setAttribute("placeName", room != null ? room.getPlace_name() : "숙소 정보 없음");
                req.setAttribute("carName", (car != null) ? car.getCar_name() : "선택하지 않음");
                req.setAttribute("startDate", startDate);
                req.setAttribute("endDate", endDate);
                req.setAttribute("people", session.getAttribute("user_route_people") != null ? session.getAttribute("user_route_people") : "정보 없음");

                RequestDispatcher rd = req.getRequestDispatcher("/WEB-INF/views/reservation/success.jsp");
                rd.forward(req, resp);
            } else {
                resp.getWriter().write("<script>alert('예약 중 오류가 발생했습니다.');history.back();</script>");
            }

        } catch (Exception e) {
            e.printStackTrace();
            resp.getWriter().write("<script>alert('서버 오류로 예약 실패했습니다.');history.back();</script>");
        }
    }
}
