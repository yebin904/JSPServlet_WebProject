package com.trip.board;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.trip.board.model.BoardDAO;
import com.trip.board.model.BoardDTO;
import com.trip.board.model.CommentDTO;
import com.trip.board.model.LikeDAO;
import com.trip.board.model.LikeDTO;




/**
 * 게시물 상세보기를 처리하는 서블릿
 * @author jsg
 * @version 1.0
 * @since 2025.10.24
 */
@WebServlet(value = "/board/view.do")
public class View extends HttpServlet {

	/**
	 * GET 요청을 처리하여 게시물 상세 보기 페이지로 이동합니다.
	 * 게시물 ID(seq)를 파라미터로 받아, 해당 게시물의 정보(dto)와 댓글 목록(clist)을 조회합니다.
	 * 로그인 상태일 경우, 해당 게시물에 대한 사용자의 좋아요/스크랩 상태(isLiked, isScrapped)를 확인합니다.
	 * 세션의 "read" 속성을 확인하여 조회수를 증가시킬지 여부를 결정합니다.
	 * 조회된 모든 정보를 request에 담아 `/WEB-INF/views/board/view.jsp` 페이지로 포워드합니다.
	 *
	 * @param req  클라이언트의 요청 정보를 담은 HttpServletRequest 객체
	 * @param resp 클라이언트에게 응답을 보내기 위한 HttpServletResponse 객체
	 * @throws ServletException 서블릿 처리 중 오류가 발생한 경우
	 * @throws IOException      입출력 오류가 발생한 경우
	 * @see /WEB-INF/views/board/view.jsp
	 */
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		HttpSession session = req.getSession();
		
		
		
		//View.java
		
		String seq = req.getParameter("seq");
		String column = req.getParameter("column");
		String word = req.getParameter("word");
		boolean isLiked = false;
		boolean isScrapped = false;

		// 1. 로그인 상태인지 먼저 확인!
		if (session.getAttribute("seq") != null) {
		    String useq = session.getAttribute("seq").toString();
		    
		    LikeDAO ldao = new LikeDAO();
		    
		    // likeCheck/scrapCheck 메서드가 1이면 true, 0이면 false로 변환
		    isLiked = (ldao.likeCheck(useq, seq) == 1);
		    isScrapped = (ldao.scrapCheck(useq, seq) == 1);
		}
		
		BoardDAO dao = new BoardDAO();
		
		if (session.getAttribute("read") != null && session.getAttribute("read").toString().equals("n")) {
			//조회수 증가
			dao.updateReadcount(seq);
			session.setAttribute("read", "y");
		}
		
		String id = "";
		
		if (session.getAttribute("id") != null) {
			id = session.getAttribute("id").toString();
		}
		
		BoardDTO dto = dao.get(seq);
		
		System.out.println(dto);
		
		



		
		/*
		 * //비밀글 열람 통제 //- 작성자 or 관리자 if (dto.getSecret().equals("1") &&
		 * (!session.getAttribute("id").toString().equals(dto.getId()) &&
		 * session.getAttribute("lv").toString().equals("1"))) {
		 * resp.sendRedirect("/toy/board/list.do");
		 * * //응답이 이미 커밋된 후에는 forward할 수 없습니다. return; }
		 */
		
		
		//데이터 조작
		
		String subject = dto.getSubject();
		subject = subject.replace("<", "&lt;").replace(">", "&gt;");
		dto.setSubject(subject);

		String content = dto.getContent();
		
		content = content.replace("<", "&lt;").replace(">", "&gt;");
		dto.setContent(content);
		
		
		//검색어 부각시키기
		//- 오늘 자바 수업 중입니다.
		//- 오늘 <span style="background-color:gold;color:tomato;">자바</span> 수업 중입니다.
		
		//검색중 + 내용 검색
		/*
		 * if (((column != null && word != null) || (!column.equals("") &&
		 * !word.equals(""))) && column.equals("content")) {
		 * * content = content.replace(word,
		 * "<span style='background-color:gold;color:tomato;'>" + word + "</span>");
		 * dto.setContent(content);
		 * * }
		 */
		
		
		
		//댓글 가져오기
		List<CommentDTO> clist = dao.listComment(seq);
		
		
		req.setAttribute("dto", dto);
		req.setAttribute("column", column);
		req.setAttribute("word", word);
		req.setAttribute("clist", clist);
		req.setAttribute("isLiked", isLiked);
		req.setAttribute("isScrapped", isScrapped);
		System.out.println(isLiked);
		System.out.println(isScrapped);
		
		RequestDispatcher dispatcher = req.getRequestDispatcher("/WEB-INF/views/board/view.jsp");
		dispatcher.forward(req, resp);
	}
}
