package com.trip.admin;

import com.trip.admin.model.accomDAO;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 관리자용 숙소 삭제를 처리하는 서블릿
 * @author jsg
 * @version 1.0
 * @since 2025.10.24
 */
@WebServlet("/admin/accom/delete.do")
public class deleteAccom extends HttpServlet {
    /**
     * 특정 숙소(방)를 삭제하고 목록 페이지로 리다이렉트합니다.
     * @param req HttpServletRequest 객체
     * @param resp HttpServletResponse 객체
     * @throws ServletException 서블릿 예외
     * @throws IOException 입출력 예외
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int roomId = Integer.parseInt(req.getParameter("roomId"));
        
        accomDAO dao = new accomDAO();
        dao.deleteRoom(roomId);
        
        resp.sendRedirect(req.getContextPath() + "/admin/accom/list.do");
    }
}