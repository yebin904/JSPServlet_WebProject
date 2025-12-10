package com.trip.board.find;

import com.trip.board.find.model.findboardDAO;
import com.trip.board.find.model.findboardDTO;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 동행찾기 게시판 게시물 수정을 처리하는 서블릿
 * @author jsg
 * @version 1.0
 * @since 2025.10.24
 */
@WebServlet("/findboard/edit.do")
public class editfindBoard extends HttpServlet {

    /**
     * GET 요청을 처리하여 기존 동행찾기 게시물 수정 폼 페이지로 이동합니다.
     * 세션에서 사용자 ID(userId)를 확인하여 로그인 상태가 아니면 로그인 페이지로 리다이렉트합니다.
     * 게시물 ID(seq)에 해당하는 게시물 정보(dao.getPost)를 조회하여,
     * 게시물 작성자 ID와 현재 사용자 ID가 일치하는지 확인합니다.
     * 일치하지 않으면 '수정 권한 없음' alert을 응답합니다.
     * 일치하면 게시물 정보(dto)를 request에 담아 `/WEB-INF/views/findboard/edit.jsp` 페이지로 포워드합니다.
     *
     * @param req  클라이언트의 요청 정보를 담은 HttpServletRequest 객체 (seq: 게시물 ID)
     * @param resp 클라이언트에게 응답을 보내기 위한 HttpServletResponse 객체
     * @throws ServletException 서블릿 처리 중 오류가 발생한 경우
     * @throws IOException      입출력 오류가 발생한 경우
     * @see /WEB-INF/views/findboard/edit.jsp
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        
        HttpSession session = req.getSession();
        Integer userId = (Integer) session.getAttribute("userId");
        
        // 비로그인 시 접근 불가
        if (userId == null) {
            resp.sendRedirect(req.getContextPath() + "/member/login.do");
            return;
        }

        int seq = Integer.parseInt(req.getParameter("seq"));
        
        findboardDAO dao = new findboardDAO();
        findboardDTO dto = dao.getPost(seq);
        
        // 본인이 작성한 글이 아닐 경우 수정 불가
        if (dto.getUser_id() != userId) {
            resp.setContentType("text/html; charset=UTF8");
            PrintWriter writer = resp.getWriter();
            writer.print("<script>alert('수정 권한이 없습니다.'); history.back();</script>");
            writer.close();
            return;
        }
        
        req.setAttribute("dto", dto);
        
        RequestDispatcher dispatcher = req.getRequestDispatcher("/WEB-INF/views/findboard/edit.jsp");
        dispatcher.forward(req, resp);
    }

    /**
     * POST 요청을 처리하여 폼에서 전송된 데이터로 기존 동행찾기 게시물을 수정합니다.
     * 파라미터로 받은 제목(title), 내용(content), 게시물 ID(seq)를 DTO에 담아
     * DB에 업데이트(dao.updatePost)합니다.
     * 성공 시, 해당 게시물의 상세 보기 페이지(`/findboard/view.do`)로 리다이렉트합니다.
     *
     * @param req  클라이언트의 요청 정보를 담은 HttpServletRequest 객체 (seq, title, content)
     * @param resp 클라이언트에게 응답을 보내기 위한 HttpServletResponse 객체
     * @throws ServletException 서블릿 처리 중 오류가 발생한 경우
     * @throws IOException      입출력 오류가 발생한 경우
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");

        int seq = Integer.parseInt(req.getParameter("seq"));
        String title = req.getParameter("title");
        String content = req.getParameter("content");

        findboardDTO dto = new findboardDTO();
        dto.setFind_board_id(seq);
        dto.setFind_board_title(title);
        dto.setFind_board_content(content);

        findboardDAO dao = new findboardDAO();
        dao.updatePost(dto);

        // 수정이 완료되면 해당 게시글 상세보기 페이지로 이동
        resp.sendRedirect(req.getContextPath() + "/findboard/view.do?seq=" + seq);
    }
}
