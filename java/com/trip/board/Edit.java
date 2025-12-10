package com.trip.board;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.trip.board.model.BoardDAO;
import com.trip.board.model.BoardDTO;


/**
 * 게시물 수정을 처리하는 서블릿
 * @author jsg
 * @version 1.0
 * @since 2025.10.24
 */
@WebServlet(value = "/board/edit.do")
public class Edit extends HttpServlet {

	/**
	 * GET 요청을 처리하여 기존 게시물 수정 폼 페이지로 이동합니다.
	 * 게시물 ID(seq)에 해당하는 게시물 정보(dao.get)를 조회하여
	 * request에 "dto"라는 이름으로 저장한 후,
	 * `/WEB-INF/views/board/edit.jsp` 페이지로 포워드합니다.
	 *
	 * @param req  클라이언트의 요청 정보를 담은 HttpServletRequest 객체 (seq: 게시물 ID)
	 * @param resp 클라이언트에게 응답을 보내기 위한 HttpServletResponse 객체
	 * @throws ServletException 서블릿 처리 중 오류가 발생한 경우
	 * @throws IOException      입출력 오류가 발생한 경우
	 * @see /WEB-INF/views/board/edit.jsp
	 */
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		//Edit.java
		
		//edit.do?seq=10
		String seq = req.getParameter("seq");
		
		BoardDAO dao = new BoardDAO();
		
		BoardDTO dto = dao.get(seq);
		
		req.setAttribute("dto", dto);		

		RequestDispatcher dispatcher = req.getRequestDispatcher("/WEB-INF/views/board/edit.jsp");
		dispatcher.forward(req, resp);
	}
	
	/**
	 * POST 요청을 처리하여 폼에서 전송된 데이터로 기존 게시물을 수정합니다.
	 * 파라미터로 받은 제목(subject), 내용(content), 게시물 ID(seq)를 DTO에 담아
	 * DB에 업데이트(dao.edit)합니다.
	 * 성공 시, 해당 게시물의 상세 보기 페이지(`/trip/board/view.do`)로 리다이렉트합니다.
	 *
	 * @param req  클라이언트의 요청 정보를 담은 HttpServletRequest 객체 (subject, content, seq)
	 * @param resp 클라이언트에게 응답을 보내기 위한 HttpServletResponse 객체
	 * @throws ServletException 서블릿 처리 중 오류가 발생한 경우
	 * @throws IOException      입출력 오류가 발생한 경우
	 */
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
	
		//EditOk.java 역할
		String subject = req.getParameter("subject");
		String content = req.getParameter("content");
		String seq = req.getParameter("seq");
		
		BoardDAO dao = new BoardDAO();
		
		BoardDTO dto = new BoardDTO();
		dto.setSubject(subject);
		dto.setContent(content);
		dto.setSeq(seq);
		
		if (dao.edit(dto) > 0) {
			resp.sendRedirect("/trip/board/view.do?seq=" + seq);
		} else {
			resp.getWriter().print("<html><meta charset='UTF-8'><script>alert('failed');history.back();</script></html>");
			resp.getWriter().close();
		}
		
	}

}
