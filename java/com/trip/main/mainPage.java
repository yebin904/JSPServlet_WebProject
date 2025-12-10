// 파일 경로: /src/main/java/com/trip/main/MainPage.java (패키지는 편하신 대로)
package com.trip.main;

import com.trip.admin.model.statDAO; // admin 패키지의 statDAO를 import
import com.trip.main.model.WeatherDTO;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * 메인 페이지 요청을 처리하는 서블릿(Controller).
 * '/main.do' 요청을 받아 메인 페이지(main.jsp)에 필요한 데이터를 조회하여 포워딩합니다.
 * * @author jsg
 * @version 1.0
 * @since 2025.10.24
 */
@WebServlet("/main.do")
public class mainPage extends HttpServlet {
   
    /**
     * HTTP GET 요청을 처리하여 메인 페이지를 렌더링합니다.
     * <p>
     * 1. {@link statDAO}를 호출하여 사이트 전체 통계 데이터를 조회합니다.
     * (총 사용자 수, 총 게시물 수, 총 리뷰 수, 총 찾기 게시판 수)
     * 2. {@link WeatherService}를 호출하여 주요 도시의 날씨 정보를 조회합니다.
     * 3. 조회된 통계 데이터와 날씨 리스트를 {@link HttpServletRequest} 속성(attribute)에 저장합니다.
     * 4. 날씨 정보 조회 중 예외 발생 시, 콘솔에 에러 로그를 출력하고 날씨 정보 없이 진행합니다.
     * 5. /WEB-INF/views/main/main.jsp 페이지로 포워드합니다.
     * </p>
     * * @param req 클라이언트의 HttpServletRequest 객체
     * @param resp 클라이언트의 HttpServletResponse 객체
     * @throws ServletException 서블릿 관련 에러 발생 시
     * @throws IOException 입출력 관련 에러 발생 시
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        
        statDAO dao = new statDAO();
        
        // 1. statDAO(통계 DAO)를 호출하여 데이터 조회
        int totalUsers = dao.getTotalUserCount();
        int totalPosts = dao.getTotalPostCount();
        int totalReviews = dao.getTotalReviewCount();
        int totalFinds = dao.getTotalFindBoardCount();
        
        // 2. 조회한 통계 데이터를 request 객체에 담아 JSP로 전달
        req.setAttribute("totalUsers", totalUsers);
        req.setAttribute("totalPosts", totalPosts);
        req.setAttribute("totalReviews", totalReviews);
        req.setAttribute("totalFinds", totalFinds);
        
        // 3. WeatherService(날씨 서비스)를 호출하여 데이터 조회
        try {
            WeatherService weatherService = new WeatherService();
            List<WeatherDTO> weatherList = weatherService.getWeatherData();
            req.setAttribute("weatherList", weatherList); // 조회된 날씨 리스트를 request에 담기
        } catch (Exception e) {
            System.out.println("날씨 정보 조회 중 오류 발생: " + e.getMessage());
            e.printStackTrace(); // API 호출 실패 시 에러 로그 출력 (JSP에서는 weatherList가 null이 됨)
        }
        
        // 4. 메인 JSP 페이지로 이동
        RequestDispatcher dispatcher = req.getRequestDispatcher("/WEB-INF/views/main/main.jsp");
        dispatcher.forward(req, resp);
    }
}
