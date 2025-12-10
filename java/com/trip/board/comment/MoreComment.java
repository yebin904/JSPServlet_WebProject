package com.trip.board.comment;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.trip.board.model.BoardDAO;
import com.trip.board.model.CommentDTO;


/**
 * 게시물에 더 많은 댓글을 로드하는 서블릿
 * @author jsg
 * @version 1.0
 * @since 2025.10.24
 */
@WebServlet(value = "/board/morecomment.do")
public class MoreComment extends HttpServlet {

	/**
	 * GET 요청을 처리하여 특정 게시물(bseq)의 추가 댓글 목록을 JSON 형식으로 반환합니다. (AJAX용)
	 * 파라미터로 게시물 ID(bseq)와 댓글 시작 번호(begin)를 받아
	 * DB에서 추가 댓글 목록(dao.moreComment)을 조회합니다.
	 * 조회된 댓글 목록을 `JSONArray` 형식으로 변환하여 클라이언트에 응답합니다.
	 *
	 * @param req  클라이언트의 요청 정보를 담은 HttpServletRequest 객체 (bseq, begin)
	 * @param resp 클라이언트에게 응답을 보내기 위한 HttpServletResponse 객체 (JSON 응답)
	 * @throws ServletException 서블릿 처리 중 오류가 발생한 경우
	 * @throws IOException      입출력 오류가 발생한 경우
	 */
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

//		try {
//			Thread.sleep(3000);
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
		//MoreComment.java
		
		String bseq = req.getParameter("bseq");
		String begin = req.getParameter("begin"); //end = begin + 5
		
		Map<String,String> map = new HashMap<String,String>();
		map.put("bseq", bseq);
		map.put("begin", begin);
		
		BoardDAO dao = new BoardDAO();
		
		List<CommentDTO> clist = dao.moreComment(map);

		JSONArray arr = new JSONArray();
		
		for (CommentDTO dto : clist) {
			//DTO 1개 > JSONObject 1개
			JSONObject obj = new JSONObject();
			
			obj.put("seq", dto.getSeq());
			obj.put("content", dto.getContent());
			obj.put("id", dto.getId());
			obj.put("name", dto.getName());
			obj.put("regdate",dto.getRegdate());
			
			arr.add(obj);
		}
		resp.setContentType("application/json");
		
		resp.getWriter().print(arr.toString());
		resp.getWriter().close();
	}

}
