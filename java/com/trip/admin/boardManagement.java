package com.trip.admin;

import com.trip.admin.model.boardDAO;
import com.trip.admin.model.integratedBoardDTO;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

/**
 * 관리자용 게시판 관리 서블릿
 * @author jsg
 * @version 1.0
 * @since 2025.10.24
 */
@WebServlet("/admin/board.do")
public class boardManagement extends HttpServlet {

    /**
     * 게시판 종류, 검색 조건에 따라 게시물 목록을 조회하여 JSP 페이지로 전달합니다.
     * @param req HttpServletRequest 객체
     * @param resp HttpServletResponse 객체
     * @throws ServletException 서블릿 예외
     * @throws IOException 입출력 예외
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        if (session.getAttribute("auth") == null) {
            resp.sendRedirect("/trip/admin/login.do");
            return;
        }

        String boardType = req.getParameter("boardType");
        String searchType = req.getParameter("searchType");
        String searchKeyword = req.getParameter("searchKeyword");

        if (boardType == null) {
            boardType = "all";
        }
        if (searchType == null) {
            searchType = "title";
        }

        boardDAO dao = new boardDAO();
        List<integratedBoardDTO> list = dao.list(boardType, searchType, searchKeyword);
        
        req.setAttribute("list", list);
        req.setAttribute("boardType", boardType);
        req.setAttribute("searchType", searchType);
        req.setAttribute("searchKeyword", searchKeyword);

        RequestDispatcher dispatcher = req.getRequestDispatcher("/WEB-INF/views/admin/board.jsp");
        dispatcher.forward(req, resp);
    }
}