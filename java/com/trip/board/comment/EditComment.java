package com.trip.board.comment;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;

import com.trip.board.model.BoardDAO;
import com.trip.board.model.CommentDTO;


/**
 * 게시물 댓글 수정을 처리하는 서블릿
 * @author jsg
 * @version 1.0
 * @since 2025.10.24
 */
@WebServlet(value = "/board/editcomment.do")
public class EditComment extends HttpServlet {

	/**
	 * POST 요청을 처리하여 기존 댓글을 수정합니다. (AJAX용)
	 * 파라미터로 댓글 ID(seq)와 수정된 내용(content)을 받아
	 * DB에 업데이트(dao.editComment)합니다.
	 * 처리 결과(성공: 1, 실패: 0)를 `JSONObject` (예: `{"result": 1}`) 형식으로 클라이언트에 응답합니다.
	 *
	 * @param req  클라이언트의 요청 정보를 담은 HttpServletRequest 객체 (seq, content)
	 * @param resp 클라이언트에게 응답을 보내기 위한 HttpServletResponse 객체 (JSON 응답)
	 * @throws ServletException 서블릿 처리 중 오류가 발생한 경우
	 * @throws IOException      입출력 오류가 발생한 경우
	 */
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		//EditComment.java
		
		String seq = req.getParameter("seq");
		String content = req.getParameter("content");
		
		BoardDAO dao = new BoardDAO();
		
		CommentDTO dto = new CommentDTO();
		dto.setSeq(seq);
		dto.setContent(content);
		
		int result = dao.editComment(dto);
		
		resp.setContentType("application/json");
		
		JSONObject obj = new JSONObject();
		obj.put("result", result);
		
		resp.getWriter().print(obj.toString());
		resp.getWriter().close();
		
	}

}
