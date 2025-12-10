package com.trip.admin;

import com.trip.member.model.UserDAO;
import com.trip.member.model.UserDTO;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * 관리자용 사용자 목록을 조회하는 서블릿
 * @author jsg
 * @version 1.0
 * @since 2025.10.24
 */
@WebServlet("/admin/user/list.do")
public class userList extends HttpServlet {
    /**
     * 검색 및 필터링 조건에 따라 사용자 목록을 조회하여 JSP 페이지로 전달합니다.
     * @param req HttpServletRequest 객체
     * @param resp HttpServletResponse 객체
     * @throws ServletException 서블릿 예외
     * @throws IOException 입출력 예외
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        
        // ★★★★★ 핵심 수정 1: JSP에서 보낸 검색/필터 파라미터 받기 ★★★★★
        String searchType = req.getParameter("searchType");
        String keyword = req.getParameter("keyword");
        String status = req.getParameter("status");
        
        UserDAO dao = new UserDAO();

        // ★★★★★ 핵심 수정 2: 파라미터를 DAO 메소드로 전달 ★★★★★
        List<UserDTO> userlist = dao.getAllUsers(searchType, keyword, status);
        
        req.setAttribute("userlist", userlist);
        
        RequestDispatcher dispatcher = req.getRequestDispatcher("/WEB-INF/views/admin/userlist.jsp");
        dispatcher.forward(req, resp);
    }
}