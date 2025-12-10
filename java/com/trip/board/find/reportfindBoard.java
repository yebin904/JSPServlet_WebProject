package com.trip.board.find;

import com.trip.admin.model.reportDAO;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * 동행찾기 게시판 게시물 신고를 처리하는 서블릿
 * @author jsg
 * @version 1.0
 * @since 2025.10.24
 */
@WebServlet("/findboard/report.do")
public class reportfindBoard extends HttpServlet {

    /**
     * GET 요청을 처리하여 게시물 신고 폼 페이지로 이동합니다.
     * `/WEB-INF/views/findboard/report.jsp` 페이지로 포워드합니다.
     *
     * @param req  클라이언트의 요청 정보를 담은 HttpServletRequest 객체
     * @param resp 클라이언트에게 응답을 보내기 위한 HttpServletResponse 객체
     * @throws ServletException 서블릿 처리 중 오류가 발생한 경우
     * @throws IOException      입출력 오류가 발생한 경우
     * @see /WEB-INF/views/findboard/report.jsp
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        RequestDispatcher dispatcher = req.getRequestDispatcher("/WEB-INF/views/findboard/report.jsp");
        dispatcher.forward(req, resp);
    }

    /**
     * POST 요청을 처리하여 동행찾기 게시물을 신고합니다.
     * 세션에서 신고자 ID(reporterId)를 확인하여 로그인 상태가 아니면 로그인 페이지로 리다이렉트합니다.
     * 파라미터(boardSeq, reportedUserId, reason)를 받아 DB에 신고 내역(dao.addReport)을 추가합니다.
     * 처리 결과(result)에 따라 JavaScript alert(성공/실패)을 포함한 HTML을 응답합니다.
     *
     * @param req  클라이언트의 요청 정보를 담은 HttpServletRequest 객체 (boardSeq, reportedUserId, reason)
     * @param resp 클라이언트에게 응답을 보내기 위한 HttpServletResponse 객체 (text/html 응답)
     * @throws ServletException 서블릿 처리 중 오류가 발생한 경우
     * @throws IOException      입출력 오류가 발생한 경우
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");

        HttpSession session = req.getSession();
        Integer reporterId = (Integer) session.getAttribute("userId");

        if (reporterId == null) {
            // 로그인 페이지로 이동시키는 로직은 그대로 유지
            resp.sendRedirect(req.getContextPath() + "/member/login.do");
            return;
        }

        int boardSeq = Integer.parseInt(req.getParameter("boardSeq"));
        int reportedUserId = Integer.parseInt(req.getParameter("reportedUserId"));
        String reason = req.getParameter("reason");

        reportDAO dao = new reportDAO();
        
        // ▼▼▼ [핵심 수정] DAO의 처리 결과를 변수에 저장 ▼▼▼
        int result = dao.addReport("findboard", boardSeq, reporterId, reportedUserId, reason);

        resp.setContentType("text/html; charset=UTF-8");
        PrintWriter writer = resp.getWriter();

        // ▼▼▼ [핵심 수정] 처리 결과에 따라 다른 알림창을 띄우도록 변경 ▼▼▼
        if (result > 0) {
            writer.print("<script>alert('신고가 정상적으로 접수되었습니다.'); window.close();</script>");
        } else {
            writer.print("<script>alert('신고 접수에 실패했습니다. 다시 시도해주세요.'); history.back();</script>");
        }
        writer.close();
    }
}
