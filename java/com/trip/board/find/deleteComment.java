package com.trip.board.find;

import com.trip.board.find.model.findboardDAO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * 동행찾기 게시판 댓글 삭제를 처리하는 서블릿
 * @author jsg
 * @version 1.0
 * @since 2025.10.24
 */
@WebServlet("/findboard/deletecomment.do")
public class deleteComment extends HttpServlet {

    /**
     * GET 요청을 처리하여 특정 댓글을 삭제합니다.
     * 세션에서 사용자 ID(sessionUserId)를 확인합니다.
     * 파라미터(commentId, boardSeq)를 받아, 해당 댓글의 작성자 ID(dao.getCommentAuthor)를 조회합니다.
     * 현재 로그인한 사용자와 댓글 작성자가 일치하는지 권한을 확인합니다.
     * 권한이 없으면, 원래 게시물 상세 보기 페이지로 리다이렉트합니다.
     * 권한이 있으면, 댓글을 삭제(dao.deleteComment)하고
     * 원래 게시물 상세 보기 페이지(`/findboard/view.do`)로 리다이렉트합니다.
     *
     * @param req  클라이언트의 요청 정보를 담은 HttpServletRequest 객체 (commentId: 댓글 ID, boardSeq: 게시물 ID)
     * @param resp 클라이언트에게 응답을 보내기 위한 HttpServletResponse 객체
     * @throws ServletException 서블릿 처리 중 오류가 발생한 경우
     * @throws IOException      입출력 오류가 발생한 경우
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        Integer sessionUserId = (Integer) session.getAttribute("userId");

        // 1. 파라미터 받기
        int commentId = Integer.parseInt(req.getParameter("commentId"));
        int boardSeq = Integer.parseInt(req.getParameter("boardSeq"));

        findboardDAO dao = new findboardDAO();

        // 2. 권한 확인
        int commentAuthorId = dao.getCommentAuthor(commentId);

        if (sessionUserId == null || sessionUserId != commentAuthorId) {
            // 권한이 없으면 그냥 원래 글로 돌려보냄
            resp.sendRedirect(req.getContextPath() + "/findboard/view.do?seq=" + boardSeq);
            return;
        }

        // 3. DAO를 통해 DB에서 삭제
        dao.deleteComment(commentId);

        // 4. 원래 게시글로 리다이렉트
        resp.sendRedirect(req.getContextPath() + "/findboard/view.do?seq=" + boardSeq);
    }
}
