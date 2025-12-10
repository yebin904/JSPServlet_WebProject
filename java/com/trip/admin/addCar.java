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
 * 관리자용 렌터카 등록을 처리하는 서블릿
 * @author jsg
 * @version 1.0
 * @since 2025.10.24
 */
@WebServlet("/admin/car/add.do")
public class addCar extends HttpServlet {

    /**
     * 렌터카 등록 폼 페이지로 이동합니다.
     * @param req HttpServletRequest 객체
     * @param resp HttpServletResponse 객체
     * @throws ServletException 서블릿 예외
     * @throws IOException 입출력 예외
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        RequestDispatcher dispatcher = req.getRequestDispatcher("/WEB-INF/views/admin/addcar.jsp");
        dispatcher.forward(req, resp);
    }
    
    /**
     * 폼에서 입력된 렌터카 정보를 DB에 저장합니다.
     * @param req HttpServletRequest 객체
     * @param resp HttpServletResponse 객체
     * @throws ServletException 서블릿 예외
     * @throws IOException 입출력 예외
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");

        String carName = req.getParameter("carName");
        String carType = req.getParameter("carType");
        String fuelType = req.getParameter("fuelType");
        
        String priceString = req.getParameter("pricePerDay").replace(",", "");
        int pricePerDay = Integer.parseInt(priceString);

        carDTO dto = new carDTO();
        dto.setCarName(carName);
        dto.setCarType(carType);
        dto.setFuelType(fuelType);
        dto.setPricePerDay(pricePerDay);

        carDAO dao = new carDAO();
        dao.addCar(dto);

        resp.sendRedirect(req.getContextPath() + "/admin/car/list.do");
    }
}