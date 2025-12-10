package com.trip.board.comment;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.simple.JSONObject;

import com.trip.board.model.BoardDAO;
import com.trip.board.model.CommentDTO;


/**
 * 게시물에 댓글을 추가하는 서블릿
 * @author jsg
 * @version 1.0
 * @since 2025.10.24
 */
@WebServlet(value = "/board/addcomment.do")
public class AddComment extends HttpServlet {

	/**
	 * POST 요청을 처리하여 새 댓글을 추가합니다. (AJAX용)
	 * 세션에서 사용자 ID(seq)를, 파라미터에서 내용(content)과 게시물 ID(bseq)를 가져옵니다.
	 * DB에 새 댓글을 추가(dao.addComment)한 후,
	 * 방금 추가된 댓글의 상세 정보(dao.getComment)를 다시 조회합니다.
	 * 처리 결과(result)와 새 댓글 정보(dto)를 `JSONObject` 형식으로 클라이언트에 응답합니다.
	 *
	 * @param req  클라이언트의 요청 정보를 담은 HttpServletRequest 객체 (content, bseq)
	 * @param resp 클라이언트에게 응답을 보내기 위한 HttpServletResponse 객체 (JSON 응답)
	 * @throws ServletException 서블릿 처리 중 오류가 발생한 경우
	 * @throws IOException      입출력 오류가 발생한 경우
	 */
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		//AddComment.java
		
		HttpSession session = req.getSession();
		
		String content = req.getParameter("content");
		String bseq = req.getParameter("bseq");
		String seq = session.getAttribute("seq").toString();
		
		BoardDAO dao = new BoardDAO();
		
		CommentDTO dto = new CommentDTO();
		dto.setContent(content);
		dto.setBseq(bseq);
		dto.setSeq(seq);
		
		
		int result = dao.addComment(dto); //댓글 쓰기
		
		CommentDTO dto2 = dao.getComment(); //방금 쓴 댓글 가져오기
		
		resp.setContentType("application/json");
		
		/*
		 	}
		 	"result": 1,
		 	"dto": {
		 		"seq": 5,
		 		"name": "홍길동",
		 		"id": "hong",
		 		"content": "댓글내용",
		 		"regdate": "날짜"
		 		}
		 	}
		 */
		JSONObject obj = new JSONObject();
		obj.put("result", result);
		
		
		JSONObject subObj = new JSONObject();
		subObj.put("seq", dto2.getSeq());
		subObj.put("name", dto2.getName());
		subObj.put("id", dto2.getId());
		subObj.put("content", dto2.getContent());
		subObj.put("regdate", dto2.getRegdate());
		
		obj.put("dto", subObj);
		
		//System.out.println(obj.toString());
		
		
		resp.getWriter().print(obj.toString());
		resp.getWriter().close();
	}

}
