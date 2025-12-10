package com.trip.board.find;

import com.trip.board.find.model.findboardDAO;
import com.trip.board.find.model.findcommentDTO;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * 동행찾기 게시판에 댓글을 추가하는 서블릿
 * @author jsg
 * @version 1.0
 * @since 2025.10.24
 */
@WebServlet("/findboard/addcomment.do")
public class addComment extends HttpServlet {

    /**
     * POST 요청을 처리하여 동행찾기 게시물에 새 댓글을 추가합니다.
     * 세션에서 사용자 ID(userId)를 확인하여 로그인 상태가 아니면 로그인 페이지로 리다이렉트합니다.
     * 로그인 상태이면 파라미터(boardSeq, content)와 사용자 ID를 `findcommentDTO`에 담아
     * DB에 댓글을 추가(dao.addComment)합니다.
     * 완료 후, 원래 게시물의 상세 보기 페이지(`/findboard/view.do`)로 리다이렉트합니다.
     *
     * @param req  클라이언트의 요청 정보를 담은 HttpServletRequest 객체 (boardSeq: 게시물 ID, content: 댓글 내용)
     * @param resp 클라이언트에게 응답을 보내기 위한 HttpServletResponse 객체
     * @throws ServletException 서블릿 처리 중 오류가 발생한 경우
     * @throws IOException      입출력 오류가 발생한 경우
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");

        HttpSession session = req.getSession();
        Integer userId = (Integer) session.getAttribute("userId");

        if (userId == null) {
            resp.sendRedirect(req.getContextPath() + "/member/login.do");
            return;
        }

        int boardSeq = Integer.parseInt(req.getParameter("boardSeq"));
        String content = req.getParameter("content");

        findcommentDTO dto = new findcommentDTO();
        dto.setFind_board_id(boardSeq);
        dto.setUser_id(userId);
        dto.setFind_comment_content(content);

        findboardDAO dao = new findboardDAO();
        dao.addComment(dto);

        // 댓글 작성 후 다시 해당 게시글 상세보기 페이지로 이동
        resp.sendRedirect(req.getContextPath() + "/findboard/view.do?seq=" + boardSeq);
    }
}
