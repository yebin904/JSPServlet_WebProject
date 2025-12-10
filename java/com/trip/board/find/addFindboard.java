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

/**
 * 동행찾기 게시판에 새 게시물을 추가하는 서블릿
 * @author jsg
 * @version 1.0
 * @since 2025.10.24
 */
@WebServlet("/findboard/add.do")
public class addFindboard extends HttpServlet {

    /**
     * GET 요청을 처리하여 동행찾기 게시물 작성 폼 페이지로 이동합니다.
     * `/WEB-INF/views/findboard/add.jsp` 페이지로 포워드합니다.
     *
     * @param req  클라이언트의 요청 정보를 담은 HttpServletRequest 객체
     * @param resp 클라이언트에게 응답을 보내기 위한 HttpServletResponse 객체
     * @throws ServletException 서블릿 처리 중 오류가 발생한 경우
     * @throws IOException      입출력 오류가 발생한 경우
     * @see /WEB-INF/views/findboard/add.jsp
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        RequestDispatcher dispatcher = req.getRequestDispatcher("/WEB-INF/views/findboard/add.jsp");
        dispatcher.forward(req, resp);
    }

    /**
     * POST 요청을 처리하여 폼에서 전송된 새 동행찾기 게시물을 등록합니다.
     * 세션에서 사용자 ID(userId)를 확인하여 로그인 상태가 아니면 로그인 페이지로 리다이렉트합니다.
     * 로그인 상태이면 파라미터(title, content)와 사용자 ID를 DTO에 담아
     * DB에 게시물을 추가(dao.addPost)합니다.
     * 성공 시, 동행찾기 목록 페이지(`/findboard/list.do`)로 리다이렉트합니다.
     *
     * @param req  클라이언트의 요청 정보를 담은 HttpServletRequest 객체 (title, content)
     * @param resp 클라이언트에게 응답을 보내기 위한 HttpServletResponse 객체
     * @throws ServletException 서블릿 처리 중 오류가 발생한 경우
     * @throws IOException      입출력 오류가 발생한 경우
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        
        // ★★★★★ 실제 로그인 세션에서 회원 번호(user_id)를 가져오는 코드로 변경 ★★★★★
        HttpSession session = req.getSession();
        Integer userId = (Integer) session.getAttribute("userId"); // Object이므로 Integer로 형변환
        
        // 로그인 상태가 아니라면 글쓰기 불가 (로그인 페이지로 보냄)
        if (userId == null) {
            resp.sendRedirect(req.getContextPath() + "/member/login.do");
            return;
        }
        
        findboardDTO dto = new findboardDTO();
        dto.setFind_board_title(req.getParameter("title"));
        dto.setFind_board_content(req.getParameter("content"));
        dto.setUser_id(userId); // DTO에 실제 로그인한 회원 번호 저장
        
        findboardDAO dao = new findboardDAO();
        dao.addPost(dto);
        
        resp.sendRedirect(req.getContextPath() + "/findboard/list.do");
    }
}
