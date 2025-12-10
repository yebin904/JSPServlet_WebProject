package com.trip.board;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.trip.board.model.BoardDAO;


/**
 * 게시물 삭제를 처리하는 서블릿
 * @author jsg
 * @version 1.0
 * @since 2025.10.24
 */
@WebServlet(value = "/board/del.do")
public class Del extends HttpServlet {

	/**
	 * GET 요청을 처리하여 게시물 삭제 확인 페이지로 이동합니다.
	 * 삭제할 게시물 ID(seq)를 request에 저장한 후,
	 * `/WEB-INF/views/board/del.jsp` 페이지로 포워드합니다.
	 *
	 * @param req  클라이언트의 요청 정보를 담은 HttpServletRequest 객체 (seq: 게시물 ID)
	 * @param resp 클라이언트에게 응답을 보내기 위한 HttpServletResponse 객체
	 * @throws ServletException 서블릿 처리 중 오류가 발생한 경우
	 * @throws IOException      입출력 오류가 발생한 경우
	 * @see /WEB-INF/views/board/del.jsp
	 */
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		//Del.java
		String seq = req.getParameter("seq");
		
		req.setAttribute("seq", seq);

		RequestDispatcher dispatcher = req.getRequestDispatcher("/WEB-INF/views/board/del.jsp");
		dispatcher.forward(req, resp);
	}
	
	/**
	 * POST 요청을 처리하여 게시물을 삭제합니다.
	 * 파라미터로 받은 게시물 ID(seq)를 사용하여 DB에서 게시물을 삭제(dao.del)합니다.
	 * (첨부 파일 삭제 로직은 주석 처리되어 있습니다.)
	 * 성공 시, 게시물 목록 페이지(`/trip/board/list.do`)로 리다이렉트합니다.
	 *
	 * @param req  클라이언트의 요청 정보를 담은 HttpServletRequest 객체 (seq: 게시물 ID)
	 * @param resp 클라이언트에게 응답을 보내기 위한 HttpServletResponse 객체
	 * @throws ServletException 서블릿 처리 중 오류가 발생한 경우
	 * @throws IOException      입출력 오류가 발생한 경우
	 */
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
	
		//DelOk.java 역할
		String seq = req.getParameter("seq");
		
		BoardDAO dao = new BoardDAO();
		
//		File file;
//		file.delete();
		
		if (dao.del(seq) > 0) { //1, 0
			resp.sendRedirect("/trip/board/list.do");
		} else {
			resp.getWriter().print("<html><meta charset='UTF-8'><script>alert('failed');history.back();</script></html>");
			resp.getWriter().close();
		}
		
	}
	

}
