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
 * 관리자 로그인을 처리하는 서블릿
 * @author jsg
 * @version 1.0
 * @since 2025.10.24
 */
@WebServlet("/admin/login.do")
public class adminLogin extends HttpServlet {

    /**
     * 로그인 폼 페이지로 이동합니다.
     * @param req HttpServletRequest 객체
     * @param resp HttpServletResponse 객체
     * @throws ServletException 서블릿 예외
     * @throws IOException 입출력 예외
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        RequestDispatcher dispatcher = req.getRequestDispatcher("/WEB-INF/views/admin/login.jsp");
        dispatcher.forward(req, resp);
    }

    /**
     * 입력된 관리자 정보로 로그인을 시도합니다.
     * @param req HttpServletRequest 객체
     * @param resp HttpServletResponse 객체
     * @throws ServletException 서블릿 예외
     * @throws IOException 입출력 예외
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        
        String adminName = req.getParameter("admin_name");
        String adminPassword = req.getParameter("admin_password");
        
        adminDTO dto = new adminDTO();
        dto.setAdminName(adminName);
        dto.setAdminPassword(adminPassword);
        
        adminDAO dao = new adminDAO();
        adminDTO result = dao.login(dto);
        
        if (result != null) {
            HttpSession session = req.getSession();
            session.setAttribute("auth", result.getAdminId()); 
            session.setAttribute("adminName", result.getAdminName());
            
            resp.sendRedirect(req.getContextPath() + "/admin/main.do");
        } else {
            resp.setContentType("text/html; charset=UTF-8");
            java.io.PrintWriter out = resp.getWriter();
            out.println("<script>alert('아이디 또는 비밀번호가 일치하지 않습니다.'); location.href='" + req.getContextPath() + "/admin/login.do';</script>");
            out.flush();
        }
    }
}
