package com.trip.board;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.simple.JSONObject;

import com.trip.board.model.LikeDAO;
import com.trip.board.model.LikeDTO;

;


/**
 * 게시물 좋아요/좋아요 취소를 처리하는 서블릿
 * @author jsg
 * @version 1.0
 * @since 2025.10.24
 */
@WebServlet(value = "/board/like.do")
public class Like extends HttpServlet {

	/**
	 * POST 요청을 처리하여 특정 게시물(bseq)에 대한 '좋아요' 상태를 토글(추가/삭제)합니다.
	 * 세션에서 사용자 ID(seq)를, 파라미터에서 게시물 ID(bseq)를 가져옵니다.
	 * DB에서 현재 '좋아요' 상태를 확인(dao.likeCheck)하여,
	 * 이미 '좋아요' 상태이면 삭제(dao.likeDel)하고, 그렇지 않으면 추가(dao.likeAdd)합니다.
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
		
		if ( dao.likeCheck(seq, bseq) == 1 ) {
			dao.likeDel(seq, bseq);
		} else if ( dao.likeCheck(seq, bseq) == 0 ) {
			dao.likeAdd(seq, bseq);
		}
		
		
		
		
		
	}

}
