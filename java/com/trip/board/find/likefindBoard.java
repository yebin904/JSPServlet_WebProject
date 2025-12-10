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
 * 동행찾기 게시판 게시물 추천/추천 취소를 처리하는 서블릿
 * @author jsg
 * @version 1.0
 * @since 2025.10.24
 */
@WebServlet("/findboard/like.do")
public class likefindBoard extends HttpServlet {

    /**
     * GET 요청을 처리하여 특정 동행찾기 게시물(seq)에 대한 좋아요 상태를 토글(추가/삭제)합니다.
     * 세션에서 사용자 ID(userId)를 확인하여 로그인 상태가 아니면 로그인 페이지로 리다이렉트합니다.
     * 로그인 상태이면, 현재 좋아요 여부(dao.checkLike)를 확인하여
     * 좋아요 상태이면 삭제(dao.removeLike)하고, 그렇지 않으면 추가(dao.addLike)합니다.
     * 처리 완료 후, 다시 상세 보기 페이지(`/findboard/view.do`)로 리다이렉트합니다.
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

        // 비로그인 시 로그인 페이지로
        if (userId == null) {
            resp.sendRedirect(req.getContextPath() + "/member/login.do");
            return;
        }

        int seq = Integer.parseInt(req.getParameter("seq"));

        findboardDAO dao = new findboardDAO();

        // 이미 추천했는지 확인
        if (dao.checkLike(seq, userId)) {
            // 추천했다면 > 추천 취소
            dao.removeLike(seq, userId);
        } else {
            // 추천 안 했다면 > 추천하기
            dao.addLike(seq, userId);
        }
        
        // 처리 후 다시 상세보기 페이지로 이동
        resp.sendRedirect(req.getContextPath() + "/findboard/view.do?seq=" + seq);
    }
}
