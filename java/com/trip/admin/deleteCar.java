package com.trip.admin;

import com.trip.admin.model.carDAO;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 관리자용 렌터카 삭제를 처리하는 서블릿
 * @author jsg
 * @version 1.0
 * @since 2025.10.24
 */
@WebServlet("/admin/car/delete.do")
public class deleteCar extends HttpServlet {
    /**
     * 특정 렌터카를 삭제하고 목록 페이지로 리다이렉트합니다.
     * @param req HttpServletRequest 객체
     * @param resp HttpServletResponse 객체
     * @throws ServletException 서블릿 예외
     * @throws IOException 입출력 예외
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int carId = Integer.parseInt(req.getParameter("carId"));
        
        carDAO dao = new carDAO();
        dao.deleteCar(carId);
        
        resp.sendRedirect(req.getContextPath() + "/admin/car/list.do");
    }
}
