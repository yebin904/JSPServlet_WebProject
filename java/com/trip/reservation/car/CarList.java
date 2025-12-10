package com.trip.reservation.car;

import java.io.*;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.util.*;
import com.trip.reservation.car.model.CarDAO;
import com.trip.reservation.car.model.CarDTO;

/**
 * 렌터카 목록을 조회하고 필터링하는 서블릿
 * @author jsg
 * @version 1.0
 * @since 2025.10.24
 */
@WebServlet("/reservation/carList.do")
public class CarList extends HttpServlet {

    /**
	 * 기본 생성자입니다.
	 */
    public CarList() {
        super();
    }

    /**
     * HTTP GET 요청을 처리합니다.
     * * 1. 이전 페이지(숙소 목록)에서 전달받은 파라미터(숙소 ID, 지역, 날짜, 인원)를 받습니다.
     * 2. 렌터카 필터링을 위한 파라미터(차종, 연료, 좌석 수)를 다중 값(배열)으로 받습니다.
     * 3. `CarDAO`의 `filteredList` 메소드를 호출하여 조건에 맞는 렌터카 목록을 조회합니다.
     * 4. 조회된 목록과 이전에 전달받은 파라미터들을 request 속성에 저장합니다.
     * 5. 렌터카 목록 페이지(`/WEB-INF/views/reservation/carList.jsp`)로 포워딩합니다.
     * * @param req 클라이언트가 서블릿에 보낸 HttpServletRequest 객체
     * @param resp 서블릿이 클라이언트에 보내는 HttpServletResponse 객체
     * @throws ServletException 서블릿 처리 중 예외 발생 시
     * @throws IOException 요청 또는 응답 처리 중 I/O 예외 발생 시
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");

        String roomId = req.getParameter("room_id");
        String region = req.getParameter("region"); // "부산", "제주" 등
        String start = req.getParameter("start_date");
        String end = req.getParameter("end_date");
        String people = req.getParameter("people");

        // ✅ 필터 다중값 (체크박스)
        String[] carTypes = req.getParameterValues("car_type");          // 세단, SUV 등
        String[] carFuels = req.getParameterValues("car_fuel_type");     // 휘발유, 디젤 등
        String[] carSeats = req.getParameterValues("car_seats");         // 5,7,9 등

        try (CarDAO dao = new CarDAO()) {
            List<CarDTO> list = dao.filteredList(region, carTypes, carFuels, carSeats);
            req.setAttribute("list", list);
        }

        req.setAttribute("room_id", roomId);
        req.setAttribute("region", region);
        req.setAttribute("start_date", start);
        req.setAttribute("end_date", end);
        req.setAttribute("people", people);

        RequestDispatcher rd = req.getRequestDispatcher("/WEB-INF/views/reservation/carList.jsp");
        rd.forward(req, resp);
    }
}