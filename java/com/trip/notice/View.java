package com.trip.notice;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 공지사항 상세보기를 처리하는 서블릿(Controller).
 * '/notice/view.do' 요청을 받아 공지사항 상세 페이지(view.jsp)로 포워딩합니다.
 * * @author jsg
 * @version 1.0
 * @since 2025.10.24
 */
@WebServlet(value = "/notice/view.do")
public class View extends HttpServlet {

	/**
	 * HTTP GET 요청을 처리하여 특정 공지사항의 상세 내용을 조회합니다.
	 * * <p>현재 구현에서는 비즈니스 로직(DAO 호출) 없이
	 * 바로 /WEB-INF/views/notice/view.jsp 페이지로 포워딩합니다.
	 * (추후 파라미터로 받은 notice_post_id를 이용해 DAO로 데이터를 조회하고,
	 * 조회수를 증가시킨 뒤, DTO를 req에 담아 JSP로 전달하는 로직이 추가되어야 합니다.)</p>
	 * * @param req 클라이언트의 HttpServletRequest 객체. (조회할 공지사항 ID(notice_post_id)를 포함할 수 있음)
	 * @param resp 클라이언트에게 응답을 보낼 HttpServletResponse 객체.
	 * @throws ServletException 서블릿 관련 에러 발생 시.
	 * @throws IOException 입출력 관련 에러 발생 시.
	 */
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		//View.java
		
		// 1. 파라미터 수신 (예: String notice_post_id = req.getParameter("notice_post_id");)
		
		// 2. DAO를 통해 데이터 조회 (예: NoticeDAO dao = new NoticeDAO(); NoticeDTO dto = dao.get(notice_post_id);)
		
		// 3. 조회수 증가 (예: dao.updateViewCount(notice_post_id);)
		
		// 4. JSP에 데이터 전달 (예: req.setAttribute("dto", dto);)

		RequestDispatcher dispatcher = req.getRequestDispatcher("/WEB-INF/views/notice/view.jsp");
		dispatcher.forward(req, resp);
	}

}
