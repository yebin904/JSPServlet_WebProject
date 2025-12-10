package com.trip.admin;

import com.trip.admin.model.suspendedUserDAO; // 수정: suspendedUserDAO 임포트
import com.trip.admin.model.suspendedUserDTO;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * 관리자용 정지된 사용자 목록을 조회하는 서블릿
 * @author jsg
 * @version 1.0
 * @since 2025.10.24
 */
@WebServlet("/admin/user/suspendedList.do")
public class suspendedList extends HttpServlet {
    /**
     * 정지된 사용자 목록을 조회하여 JSP 페이지로 전달합니다.
     * @param req HttpServletRequest 객체
     * @param resp HttpServletResponse 객체
     * @throws ServletException 서블릿 예외
     * @throws IOException 입출력 예외
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        
        // 수정: userDAO 대신 suspendedUserDAO를 사용합니다.
        suspendedUserDAO dao = new suspendedUserDAO();
        
        // 수정: getSuspendedUsers() 대신 getSuspendedUserList()를 호출합니다.
        List<suspendedUserDTO> suspendedlist = dao.getSuspendedUserList(); 
        
        req.setAttribute("suspendedlist", suspendedlist);
        
        RequestDispatcher dispatcher = req.getRequestDispatcher("/WEB-INF/views/admin/suspendedlist.jsp");
        dispatcher.forward(req, resp);
    }
}