package com.trip.board.find;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.trip.board.find.model.findboardDAO;
import com.trip.board.find.model.findboardDTO;
import com.trip.board.find.model.findcommentDTO;

/**
 * 동행찾기 게시판 게시물 상세보기를 처리하는 서블릿
 * @author jsg
 * @version 1.0
 * @since 2025.10.24
 */
@WebServlet("/findboard/view.do")
public class viewfindBoard extends HttpServlet {
	/**
	 * GET 요청을 처리하여 특정 동행찾기 게시물의 상세 보기 페이지로 이동합니다.
	 * 관리자 페이지(id) 또는 일반 목록(seq)에서 전달된 게시물 ID를 받아 처리합니다.
	 * 1. 해당 게시물의 조회수를 증가(dao.updateViewCount)시킵니다.
	 * 2. 게시물 상세 정보(dao.getPost)를 조회합니다.
	 * 3. 게시물의 총 좋아요 수(dao.getLikeCount) 및 로그인 사용자의 좋아요 여부(dao.checkLike)를 DTO에 설정합니다.
	 * 4. 게시물의 총 스크랩 수(dao.getScrapCount) 및 로그인 사용자의 스크랩 여부(dao.checkScrap)를 DTO에 설정합니다.
	 * 5. 해당 게시물의 댓글 목록(dao.getCommentList)을 조회합니다.
	 * 6. 모든 정보를 request에 담아 `/WEB-INF/views/findboard/view.jsp` 페이지로 포워드합니다.
	 *
	 * @param req  클라이언트의 요청 정보를 담은 HttpServletRequest 객체 (id 또는 seq)
	 * @param resp 클라이언트에게 응답을 보내기 위한 HttpServletResponse 객체
	 * @throws ServletException 서블릿 처리 중 오류가 발생한 경우
	 * @throws IOException      입출력 오류가 발생한 경우
	 * @see /WEB-INF/views/findboard/view.jsp
	 */
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
	    
	    HttpSession session = req.getSession();
	    Integer userId = (Integer) session.getAttribute("userId");
	    
	    String id = req.getParameter("id"); // 관리자 페이지에서 오는 값
	    String seqParam = req.getParameter("seq"); // 일반 게시판에서 오는 값

	    int postId = 0; // 최종 게시글 번호를 저장할 변수

	    if (id != null && !id.isEmpty()) {
	        postId = Integer.parseInt(id);
	    } else if (seqParam != null && !seqParam.isEmpty()) {
	        postId = Integer.parseInt(seqParam);
	    } else {
	        resp.sendRedirect(req.getContextPath() + "/error.do");
	        return; 
	    }

	    findboardDAO dao = new findboardDAO();

	    // 조회수 증가
	    dao.updateViewCount(postId);

	    // 게시글 정보 가져오기
	    findboardDTO dto = dao.getPost(postId);

	    // 추천 정보 조회 및 DTO에 설정
	    dto.setLikeCount(dao.getLikeCount(postId));
	    if (userId != null) {
	        dto.setLiked(dao.checkLike(postId, userId));
	    }

	    // 스크랩 정보 조회 및 DTO에 설정
	    dto.setScrapCount(dao.getScrapCount(postId));
	    if (userId != null) {
	        dto.setScrapped(dao.checkScrap(postId, userId));
	    }
	    
	    // 댓글 목록 가져오기
	    List<findcommentDTO> commentList = dao.getCommentList(postId);
	    
	    req.setAttribute("dto", dto);
	    req.setAttribute("commentList", commentList);
	    
	    RequestDispatcher dispatcher = req.getRequestDispatcher("/WEB-INF/views/findboard/view.jsp");
	    dispatcher.forward(req, resp);
	}
}
