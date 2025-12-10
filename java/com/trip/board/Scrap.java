package com.trip.board;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.trip.board.model.LikeDAO;
import com.trip.board.model.LikeDTO;

/**
 * 게시물 스크랩 기능을 처리하는 서블릿
 * @author jsg
 * @version 1.0
 * @since 2025.10.24
 */
@WebServlet(value = "/board/scrap.do")
public class Scrap extends HttpServlet {

	/**
	 * POST 요청을 처리하여 특정 게시물에 대한 스크랩을 토글(추가/삭제)합니다.
	 * 세션에서 사용자 ID(seq)를, 파라미터에서 게시물 ID(bseq)를 가져옵니다.
	 * DB에서 현재 스크랩 상태를 확인(scrapCheck)하여,
	 * 스크랩되어 있으면 삭제(scrapDel)하고, 그렇지 않으면 추가(scrapAdd)합니다.
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
		
		String bseq = req.getParameter("bseq");
		String seq = session.getAttribute("seq").toString();
		
		LikeDAO dao = new LikeDAO();
		
		LikeDTO dto = new LikeDTO();
		dto.setBseq(bseq);
		dto.setSeq(seq);
		
		if (dao.scrapCheck(seq, bseq) == 1 ) {
			dao.scrapDel(seq, bseq);
		} else if (dao.scrapCheck(seq, bseq) == 0 ) {
			dao.scrapAdd(seq, bseq);
		}
		
		
		
		
		
	}

}
