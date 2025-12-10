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
import java.util.Arrays;
import java.util.List;

/**
 * 관리자용 렌터카 목록 조회를 처리하는 서블릿
 * @author jsg
 * @version 1.0
 * @since 2025.10.24
 */
@WebServlet("/admin/car/list.do")
public class carList extends HttpServlet {
    /**
     * 렌터카 목록을 조회하고, 필터링 및 정렬 옵션을 적용하여 결과를 JSP 페이지로 전달합니다.
     * @param req HttpServletRequest 객체
     * @param resp HttpServletResponse 객체
     * @throws ServletException 서블릿 예외
     * @throws IOException 입출력 예외
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        
        String[] fuelTypes = req.getParameterValues("fuel");
        String minPriceParam = req.getParameter("minPrice");
        String maxPriceParam = req.getParameter("maxPrice");
        String sortOrder = req.getParameter("sort");

        int minPrice = (minPriceParam != null && !minPriceParam.isEmpty()) ? Integer.parseInt(minPriceParam) : 0;
        int maxPrice = (maxPriceParam != null && !maxPriceParam.isEmpty()) ? Integer.parseInt(maxPriceParam) : 9999999;
        
        carDAO dao = new carDAO();
        List<carDTO> list = dao.getAllCars(fuelTypes, minPrice, maxPrice, sortOrder);
        
        req.setAttribute("list", list);
        req.setAttribute("selectedFuels", (fuelTypes != null) ? Arrays.asList(fuelTypes) : null);
        req.setAttribute("minPrice", minPrice);
        req.setAttribute("maxPrice", maxPrice);
        req.setAttribute("sortOrder", sortOrder);

        RequestDispatcher dispatcher = req.getRequestDispatcher("/WEB-INF/views/admin/carlist.jsp");
        dispatcher.forward(req, resp);
    }
}
