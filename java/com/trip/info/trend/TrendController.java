package com.trip.info.trend; // '여행 트렌드' 기능 패키지

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.trip.allplace.model.PlaceDTO;

/**
 * '여행 트렌드' 갤러리 페이지 요청을 처리하는 서블릿(Controller).
 * '/info/trend/trend.do' URL 요청을 받아 처리합니다.
 * * @author jsg
 * @version 1.0
 * @since 2025.10.24
 */
@WebServlet("/info/trend/trend.do") // '여행 트렌드' 갤러리 페이지 URL
public class TrendController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	/** '여행 트렌드' 비즈니스 로직을 처리하는 서비스 객체 */
	private TrendService trendService = new TrendService();

	/**
	 * HTTP GET 요청을 처리합니다.
	 * {@link #processRequest(HttpServletRequest, HttpServletResponse)} 메서드를 호출하여
	 * GET/POST 요청을 동일하게 처리합니다.
	 * * @param req 클라이언트의 HttpServletRequest 객체
	 * @param resp 클라이언트에게 응답을 보낼 HttpServletResponse 객체
	 * @throws ServletException 서블릿 관련 에러 발생 시
	 * @throws IOException 입출력 관련 에러 발생 시
	 */
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		processRequest(req, resp);
	}

	/**
	 * HTTP POST 요청을 처리합니다.
	 * {@link #processRequest(HttpServletRequest, HttpServletResponse)} 메서드를 호출하여
	 * GET/POST 요청을 동일하게 처리합니다.
	 * * @param req 클라이언트의 HttpServletRequest 객체
	 * @param resp 클라이언트에게 응답을 보낼 HttpServletResponse 객체
	 * @throws ServletException 서블릿 관련 에러 발생 시
	 * @throws IOException 입출력 관련 에러 발생 시
	 */
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		processRequest(req, resp);
	}
	
	/**
	 * GET/POST 요청을 공통으로 처리하는 메서드입니다.
	 * <p>
	 * 1. [STEP 1] 현재 세션을 확인하여 로그인 상태(isLoggedIn)를 판별합니다.
	 * 2. [STEP 2] TrendService를 호출하여 키워드가 포함된 장소 목록(trendList)을 조회합니다.
	 * 3. [STEP 3] 조회된 목록(trendList)과 로그인 상태(isLoggedIn)를 request attribute에 저장합니다.
	 * 4. [STEP 4] 결과를 표시할 View(JSP) 페이지(/WEB-INF/views/trend/gallery.jsp)로 포워딩합니다.
	 * </p>
	 * * @param req 클라이언트의 HttpServletRequest 객체
	 * @param resp 클라이언트에게 응답을 보낼 HttpServletResponse 객체
	 * @throws ServletException 서블릿 관련 에러 발생 시
	 * @throws IOException 입출력 관련 에러 발생 시
	 */
	protected void processRequest(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		// [STEP 1] 로그인 상태 확인
		HttpSession session = req.getSession(false); // 세션이 없으면 새로 생성하지 않음
        boolean isLoggedIn = (session != null && session.getAttribute("auth") != null);
        req.setAttribute("isLoggedIn", isLoggedIn); // JSP에서 로그인 상태에 따라 다르게 표시하기 위함

		// [STEP 2] 비즈니스 로직 처리 (Service 위임)
		List<PlaceDTO> trendList = trendService.getTrendList();
		
		// [STEP 3] 결과 데이터를 View에 전달하기 위해 request attribute에 저장
		req.setAttribute("trendList", trendList);
		
		// [STEP 4] 결과를 보여줄 View(JSP)로 포워딩
		String viewPath = "/WEB-INF/views/trend/gallery.jsp"; // 갤러리 JSP 파일 경로
		RequestDispatcher dispatcher = req.getRequestDispatcher(viewPath);
		dispatcher.forward(req, resp);
	}

}
