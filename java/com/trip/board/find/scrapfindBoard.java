package com.trip.board.find;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.trip.board.find.model.findboardDAO;

/**
 * 동행찾기 게시판 게시물 스크랩/스크랩 취소를 처리하는 서블릿
 * @author jsg
 * @version 1.0
 * @since 2025.10.24
 */
@WebServlet("/findboard/scrap.do")
public class scrapfindBoard extends HttpServlet {
    /**
     * GET 요청을 처리하여 특정 동행찾기 게시물(seq)에 대한 스크랩 상태를 토글(추가/삭제)합니다.
     * 세션에서 사용자 ID(userId)를 확인하여 로그인 상태가 아니면 상세 보기 페이지로 리다이렉트(msg=login_required)합니다.
     * 로그인 상태이면, 현재 스크랩 여부(dao.checkScrap)를 확인하여
     * 스크랩 상태이면 삭제(dao.removeScrap)하고, 그렇지 않으면 추가(dao.addScrap)합니다.
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
        int seq = Integer.parseInt(req.getParameter("seq"));

        // 비로그인 사용자는 로그인 페이지로 리다이렉트 (필요하다면)
        if (userId == null) {
            // resp.sendRedirect("/trip/user/login.do"); 
            // 현재는 그냥 view로 돌려보내 경고창을 띄우는 것이 더 사용자 경험이 좋을 수 있습니다.
             resp.sendRedirect("/trip/findboard/view.do?seq=" + seq + "&msg=login_required");
            return;
        }
        
        findboardDAO dao = new findboardDAO();
        
        // 1. 현재 스크랩 상태를 확인합니다.
        boolean isScrapped = dao.checkScrap(seq, userId);
        
        // 2. 상태에 따라 스크랩 추가 또는 제거를 처리합니다.
        if (isScrapped) {
            dao.removeScrap(seq, userId); // 스크랩 취소
        } else {
            dao.addScrap(seq, userId);    // 스크랩 추가
        }
        
        // 3. 게시글 상세 페이지로 리다이렉트하여 변경된 결과를 확인합니다.
        resp.sendRedirect("/trip/findboard/view.do?seq=" + seq);
    }
}
