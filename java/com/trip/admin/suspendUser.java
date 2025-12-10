package com.trip.admin;

import com.trip.member.model.UserDAO;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * 관리자용 사용자 정지를 처리하는 서블릿
 * @author jsg
 * @version 1.0
 * @since 2025.10.24
 */
@WebServlet("/admin/user/suspend.do")
public class suspendUser extends HttpServlet {
    /**
     * 특정 사용자를 정지시키고, 사용자 목록 페이지로 리다이렉트합니다.
     * @param req HttpServletRequest 객체
     * @param resp HttpServletResponse 객체
     * @throws ServletException 서블릿 예외
     * @throws IOException 입출력 예외
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        HttpSession session = req.getSession();
        
        int userId = Integer.parseInt(req.getParameter("userId"));
        String reason = req.getParameter("reason");
        int duration = Integer.parseInt(req.getParameter("duration"));
        int adminId = (int) session.getAttribute("auth");

        UserDAO dao = new UserDAO();
        dao.suspendUser(userId, adminId, reason, duration);

        resp.sendRedirect(req.getContextPath() + "/admin/user/list.do");
    }
}