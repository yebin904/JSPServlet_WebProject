package com.trip.admin;

import com.trip.admin.model.adminDAO;
import com.trip.admin.model.adminDTO;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * 관리자 정보를 조회하는 서블릿
 * @author jsg
 * @version 1.0
 * @since 2025.10.24
 */
@WebServlet("/admin/info.do")
public class adminInfo extends HttpServlet {

    /**
     * 로그인된 관리자의 정보를 조회하여 JSP 페이지로 전달합니다.
     * @param req HttpServletRequest 객체
     * @param resp HttpServletResponse 객체
     * @throws ServletException 서블릿 예외
     * @throws IOException 입출력 예외
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        
        HttpSession session = req.getSession();
        
        if (session.getAttribute("auth") == null) {
            resp.sendRedirect(req.getContextPath() + "/admin/login.do");
            return;
        }
        
        int adminId = (int) session.getAttribute("auth");
        
        adminDAO dao = new adminDAO();
        adminDTO dto = dao.getAdminInfo(adminId);
        
        req.setAttribute("admin", dto);

        RequestDispatcher dispatcher = req.getRequestDispatcher("/WEB-INF/views/admin/info.jsp");
        dispatcher.forward(req, resp);
    }
}
