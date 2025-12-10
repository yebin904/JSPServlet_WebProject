package com.trip.reservation.accom;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.trip.reservation.accom.model.AccomDAO;
import com.trip.reservation.accom.model.AccomDTO;

/**
 * 숙소 목록을 조회하고 필터링하는 서블릿
 * @author jsg
 * @version 1.0
 * @since 2025.10.24
 */
@WebServlet("/reservation/accomList.do")
public class AccomList extends HttpServlet {

    /**
     * HTTP GET 요청을 처리합니다.
     * * 1. 세션에서 현재 사용자의 여행 기본 정보(지역, 시작일, 종료일, 인원)를 가져옵니다.
     * (정보가 없을 경우 테스트용 기본값 설정)
     * 2. 요청 파라미터에서 숙소 필터링 조건(숙소 유형, 객실 유형, 수용 인원)을 다중 값(배열)으로 받습니다.
     * 3. `AccomDAO`의 `filteredList` 메소드를 호출하여 조건에 맞는 숙소 목록을 조회합니다.
     * 4. 조회된 목록과 세션의 여행 정보를 request 속성에 저장합니다.
     * 5. 숙소 목록 페이지(`/WEB-INF/views/reservation/accomList.jsp`)로 포워딩합니다.
     * * @param req 클라이언트가 서블릿에 보낸 HttpServletRequest 객체
     * @param resp 서블릿이 클라이언트에 보내는 HttpServletResponse 객체
     * @throws ServletException 서블릿 처리 중 예외 발생 시
     * @throws IOException 요청 또는 응답 처리 중 I/O 예외 발생 시
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

    	req.setCharacterEncoding("UTF-8");
    	HttpSession session = req.getSession();
    	
    	// [1] 루트 정보 세션에서 불러오기
        if (session.getAttribute("region") == null) {
            session.setAttribute("region", "부산");
        }
        if (session.getAttribute("region") == null) {
            session.setAttribute("region", "부산");
        }
        if (session.getAttribute("user_route_startdate") == null) {
            session.setAttribute("user_route_startdate", "2025-10-20"); // 테스트용
        }
        if (session.getAttribute("user_route_enddate") == null) {
            session.setAttribute("user_route_enddate", "2025-10-23");
        }
        if (session.getAttribute("user_route_people") == null) {
            session.setAttribute("user_route_people", "2");
        }
        
        //세션 값 불러오기
        String region = (String) session.getAttribute("region");
        String start_date = (String) session.getAttribute("user_route_startdate");
        String end_date = (String) session.getAttribute("user_route_enddate");
        String people = (String) session.getAttribute("user_route_people");
        
        String[] accomTypes = req.getParameterValues("accom_type");
        String[] roomTypes = req.getParameterValues("room_type");
        String[] capacities = req.getParameterValues("capacity");

        AccomDAO dao = new AccomDAO();
        List<AccomDTO> list = dao.filteredList(region, accomTypes, roomTypes, capacities);

        req.setAttribute("region", region);
        req.setAttribute("start_date", start_date);
        req.setAttribute("end_date", end_date);
        req.setAttribute("people", people);
        req.setAttribute("list", list);
       

        RequestDispatcher rd = req.getRequestDispatcher("/WEB-INF/views/reservation/accomList.jsp");
        rd.forward(req, resp);
    }
}