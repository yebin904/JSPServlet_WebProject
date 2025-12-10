package com.trip.board;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.trip.board.model.BoardDAO;
import com.trip.board.model.BoardDTO;



/**
 * 게시물 신고를 처리하는 서블릿
 * @author jsg
 * @version 1.0
 * @since 2025.10.24
 */
@WebServlet(value = "/board/report.do")
public class report extends HttpServlet {

	/**
	 * POST 요청을 처리하여 특정 게시물(bseq)을 신고합니다.
	 * 세션에서 신고자 ID(useq)를, 파라미터에서 게시물 ID(bseq)를 가져와
	 * DB에 신고 내역(dao.addReport)을 추가합니다.
	 *
	 * @param req  클라이언트의 요청 정보를 담은 HttpServletRequest 객체 (bseq: 게시물 ID)
	 * @param resp 클라이언트에게 응답을 보내기 위한 HttpServletResponse 객체
	 * @throws ServletException 서블릿 처리 중 오류가 발생한 경우
	 * @throws IOException      입출력 오류가 발생한 경우
	 */
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		//AddComment.java
		
		HttpSession session = req.getSession();
		
		String seq = req.getParameter("bseq");
		String useq = session.getAttribute("seq").toString();
		
		BoardDAO dao = new BoardDAO();
		
		BoardDTO dto = new BoardDTO();
		dto.setSeq(seq);
		dto.setUseq(useq);
		
		
		dao.addReport(dto); 
		
	}

}
