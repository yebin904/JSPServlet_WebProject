package com.trip.admin;

import com.trip.admin.model.reportDAO;
import com.trip.admin.model.reportDTO;
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
 * 관리자용 신고 처리 내역 페이지를 담당하는 서블릿
 * @author jsg
 * @version 1.0
 * @since 2025.10.24
 */
@WebServlet("/admin/report/history.do")
public class reportHistory extends HttpServlet {
    /**
     * 처리 완료된 신고 목록을 조회하여 JSP 페이지로 전달합니다.
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

        reportDAO dao = new reportDAO();
        List<reportDTO> list = dao.getProcessedReports();
        req.setAttribute("list", list);

        RequestDispatcher dispatcher = req.getRequestDispatcher("/WEB-INF/views/admin/reporthistory.jsp");
        dispatcher.forward(req, resp);
    }
}
