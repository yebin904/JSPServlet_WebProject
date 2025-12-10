package com.trip.board.comment;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;

import com.trip.board.model.BoardDAO;

/**
 * 게시물 댓글 삭제를 처리하는 서블릿
 * @author jsg
 * @version 1.0
 * @since 2025.10.24
 */
@WebServlet(value = "/board/delcomment.do")
public class DelComment extends HttpServlet {

	/**
	 * POST 요청을 처리하여 특정 댓글을 삭제합니다. (AJAX용)
	 * 파라미터로 댓글 ID(seq)를 받아 DB에서 삭제(dao.delComment)합니다.
	 * 처리 결과(성공: 1, 실패: 0)를 `JSONObject` (예: `{"result": 1}`) 형식으로 클라이언트에 응답합니다.
	 *
	 * @param req  클라이언트의 요청 정보를 담은 HttpServletRequest 객체 (seq: 댓글 ID)
	 * @param resp 클라이언트에게 응답을 보내기 위한 HttpServletResponse 객체 (JSON 응답)
	 * @throws ServletException 서블릿 처리 중 오류가 발생한 경우
	 * @throws IOException      입출력 오류가 발생한 경우
	 */
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		//DelComment.java

		String seq = req.getParameter("seq");
		
		BoardDAO dao = new BoardDAO();
		
		int result = dao.delComment(seq);
		
		resp.setContentType("application/json");
		
		JSONObject obj = new JSONObject();
		obj.put("result", result);
		
		resp.getWriter().print(obj.toString());
		resp.getWriter().close();
		
	}

}
