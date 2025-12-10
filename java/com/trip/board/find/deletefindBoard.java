package com.trip.board.find;

import com.trip.board.find.model.findboardDAO;
import com.trip.board.find.model.findboardDTO;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 동행찾기 게시판 게시물 삭제를 처리하는 서블릿
 * @author jsg
 * @version 1.0
 * @since 2025.10.24
 */
@WebServlet("/findboard/delete.do")
public class deletefindBoard extends HttpServlet {

    /**
     * GET 요청을 처리하여 특정 동행찾기 게시물을 삭제합니다.
     * 세션에서 사용자 ID(userId)를 확인하여 로그인 상태가 아니면 로그인 페이지로 리다이렉트합니다.
     * 게시물 ID(seq)에 해당하는 게시물 정보(dao.getPost)를 조회하여,
     * 게시물 작성자 ID와 현재 사용자 ID가 일치하는지 확인합니다.
     * 일치하지 않으면 '삭제 권한 없음' alert을 응답합니다.
     * 일치하면 게시물을 삭제(dao.deletePost)하고,
     * 동행찾기 목록 페이지(`/findboard/list.do`)로 리다이렉트합니다.
     *
     * @param req  클라이언트의 요청 정보를 담은 HttpServletRequest 객체 (seq: 게시물 ID)
     * @param resp 클라이언트에게 응답을 보내기 위한 HttpServletResponse 객체
     * @throws ServletException 서블릿 처리 중 오류가 발생한 경우
     * @throws IOException      입출력 오류가 발생한 경우
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        HttpSession session = req.getSession();
        Integer userId = (Integer) session.getAttribute("userId");

        // 1. 비로그인 시 접근 불가
        if (userId == null) {
            resp.sendRedirect(req.getContextPath() + "/member/login.do");
            return;
        }

        int seq = Integer.parseInt(req.getParameter("seq"));

        findboardDAO dao = new findboardDAO();
        findboardDTO dto = dao.getPost(seq); // 글쓴이 정보를 확인하기 위해 게시글 정보 가져오기

        // 2. 본인이 작성한 글이 아닐 경우 삭제 불가
        if (dto.getUser_id() != userId) {
            resp.setContentType("text/html; charset=UTF-8");
            PrintWriter writer = resp.getWriter();
            writer.print("<script>alert('삭제 권한이 없습니다.'); history.back();</script>");
            writer.close();
            return;
        }
        
        // 3. 권한이 있으면 삭제 처리
        dao.deletePost(seq);

        // 4. 목록 페이지로 이동
        resp.sendRedirect(req.getContextPath() + "/findboard/list.do");
    }
}
