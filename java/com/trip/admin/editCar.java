package com.trip.admin;

import com.trip.admin.model.carDAO;
import com.trip.admin.model.carDTO;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 관리자용 렌터카 수정을 처리하는 서블릿
 * @author jsg
 * @version 1.0
 * @since 2025.10.24
 */
@WebServlet("/admin/car/edit.do")
public class editCar extends HttpServlet {

    /**
     * 수정할 렌터카 정보를 조회하여 수정 폼 페이지로 이동합니다.
     * @param req HttpServletRequest 객체
     * @param resp HttpServletResponse 객체
     * @throws ServletException 서블릿 예외
     * @throws IOException 입출력 예외
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int carId = Integer.parseInt(req.getParameter("carId"));
        
        carDAO dao = new carDAO();
        carDTO dto = dao.getCarInfo(carId);
        
        req.setAttribute("dto", dto);
        
        RequestDispatcher dispatcher = req.getRequestDispatcher("/WEB-INF/views/admin/caredit.jsp");
        dispatcher.forward(req, resp);
    }

    /**
     * 폼에서 수정된 렌터카 정보를 DB에 저장합니다.
     * @param req HttpServletRequest 객체
     * @param resp HttpServletResponse 객체
     * @throws ServletException 서블릿 예외
     * @throws IOException 입출력 예외
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");

        carDTO dto = new carDTO();
        dto.setCarId(Integer.parseInt(req.getParameter("carId")));
        dto.setCarName(req.getParameter("carName"));
        dto.setCarType(req.getParameter("carType"));
        dto.setFuelType(req.getParameter("fuelType"));
        
        String priceString = req.getParameter("pricePerDay").replace(",", "");
        dto.setPricePerDay(Integer.parseInt(priceString));

        carDAO dao = new carDAO();
        dao.updateCar(dto);

        resp.sendRedirect(req.getContextPath() + "/admin/car/list.do");
    }
}