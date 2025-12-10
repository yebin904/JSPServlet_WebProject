package com.trip.qna.comment;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.trip.qna.model.QnACommentDAO;

/**
 * Q&A 게시물 댓글 삭제를 처리하는 서블릿
 * @author jsg
 * @version 1.0
 * @since 2025.10.24
 */
@WebServlet("/trip/qna/comment/DeleteComment")
public class DelComment extends HttpServlet {
    /**
     * 서블릿의 직렬화 버전을 나타내는 고유 ID입니다.
     */
    private static final long serialVersionUID = 1L;

    /**
     * POST 요청을 처리하여 Q&A 게시물의 댓글을 삭제합니다.
     * 세션에서 사용자 ID와 관리자 권한을 확인하고, 댓글 ID를 통해 댓글을 삭제합니다.
     * 삭제 성공 시 게시물 상세 페이지로 리다이렉트하고, 실패 시 에러 메시지를 표시합니다.
     * @param request HttpServletRequest 객체
     * @param response HttpServletResponse 객체
     * @throws ServletException 서블릿 예외 발생 시
     * @throws IOException 입출력 예외 발생 시
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        HttpSession session = request.getSession();
        String sessionUserId = (String) session.getAttribute("userId");
        String role = (String) session.getAttribute("role"); // admin 여부 확인

        String commentId = request.getParameter("commentId");
        String boardId = request.getParameter("boardId");

        if (sessionUserId == null) {
            response.sendRedirect(request.getContextPath() + "/trip/qna/list.do");
            return;
        }

        QnACommentDAO dao = new QnACommentDAO();
        boolean isAdmin = "admin".equals(role);
        boolean success = dao.deleteComment(commentId, sessionUserId, isAdmin);
        dao.close();

        if (success) {
            response.sendRedirect(request.getContextPath() + "/trip/qna/view.do?seq=" + boardId);
        } else {
            response.getWriter().println("<script>alert('댓글 삭제 실패'); history.back();</script>");
        }
    }

    /**
     * GET 요청을 처리합니다. 현재는 POST 요청과 동일하게 처리됩니다.
     * @param request HttpServletRequest 객체
     * @param response HttpServletResponse 객체
     * @throws ServletException 서블릿 예외 발생 시
     * @throws IOException 입출력 예외 발생 시
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doPost(request, response);
    }
}
