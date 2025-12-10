package com.trip.admin;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * 관리자 메인 페이지로 이동하는 서블릿
 * @author jsg
 * @version 1.0
 * @since 2025.10.24
 */
@WebServlet("/admin/main.do")
public class adminMain extends HttpServlet {

    /**
     * 관리자 로그인 상태를 확인하고, 로그인 되어있으면 메인 페이지로 이동합니다.
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
        RequestDispatcher dispatcher = req.getRequestDispatcher("/WEB-INF/views/admin/main.jsp");
        dispatcher.forward(req, resp);
    }
}
