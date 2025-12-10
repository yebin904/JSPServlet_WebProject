package com.trip.admin;

import com.trip.admin.model.accomAllInfoDTO;
import com.trip.admin.model.accomDAO;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 관리자용 숙소 등록을 처리하는 서블릿
 * @author jsg
 * @version 1.0
 * @since 2025.10.24
 */
@WebServlet("/admin/accom/add.do")
public class addAccom extends HttpServlet {

    /**
     * 숙소 등록 폼 페이지로 이동합니다.
     * @param req HttpServletRequest 객체
     * @param resp HttpServletResponse 객체
     * @throws ServletException 서블릿 예외
     * @throws IOException 입출력 예외
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        RequestDispatcher dispatcher = req.getRequestDispatcher("/WEB-INF/views/admin/accomadd.jsp");
        dispatcher.forward(req, resp);
    }

    /**
     * 폼에서 입력된 숙소 정보를 DB에 저장합니다.
     * @param req HttpServletRequest 객체
     * @param resp HttpServletResponse 객체
     * @throws ServletException 서블릿 예외
     * @throws IOException 입출력 예외
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        
        req.setCharacterEncoding("UTF-8");

        accomAllInfoDTO dto = new accomAllInfoDTO();
        dto.setPlaceName(req.getParameter("placeName"));
        dto.setAccomType(req.getParameter("accomType"));
        dto.setPlaceAddress(req.getParameter("placeAddress"));
        dto.setRoomName(req.getParameter("roomName"));
        dto.setCapacity(Integer.parseInt(req.getParameter("capacity")));
        dto.setPricePerNight(Integer.parseInt(req.getParameter("pricePerNight")));

        accomDAO dao = new accomDAO();
        int result = dao.addAccommodation(dto);

        if (result == 1) {
            resp.sendRedirect(req.getContextPath() + "/admin/accom/list.do");
        } else {
            resp.getWriter().write("<script>alert('숙소 등록에 실패했습니다.'); history.back();</script>");
        }
    }
}