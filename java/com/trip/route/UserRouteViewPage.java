package com.trip.route;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 사용자 정의 경로 상세보기를 처리하는 서블릿
 * @author jsg
 * @version 1.0
 * @since 2025.10.24
 */
@WebServlet("/route/userRouteView.do")
public class UserRouteViewPage extends HttpServlet {

    /**
     * HTTP GET 요청을 처리합니다.
     * 사용자 정의 경로 ID(`id`)를 요청 파라미터로 받습니다.
     * 이 ID를 request 속성에 `userRouteId`라는 이름으로 저장하여
     * `/WEB-INF/views/route/userRouteView.jsp` 페이지로 포워딩합니다.
     * <p>
     * JSP 페이지는 이 ID를 사용하여 `getUserRouteData.do` 서블릿을 AJAX로 호출,
     * 실제 경로 데이터를 받아옵니다.
     *
     * @param req  클라이언트가 서블릿에 보낸 HttpServletRequest 객체
     * @param resp 서블릿이 클라이언트에 보내는 HttpServletResponse 객체
     * @throws ServletException 서블릿 처리 중 예외 발생 시
     * @throws IOException      요청 또는 응답 처리 중 I/O 예외 발생 시
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        
        String userRouteId = req.getParameter("id");
        
        req.setAttribute("userRouteId", userRouteId);

        RequestDispatcher dispatcher = req.getRequestDispatcher("/WEB-INF/views/route/userRouteView.jsp");
        dispatcher.forward(req, resp);
    }
}