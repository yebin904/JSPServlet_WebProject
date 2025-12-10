package com.trip.admin;

import com.trip.admin.model.suspendedUserDAO; // 수정: suspendedUserDAO 임포트
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 관리자용 정지된 사용자 복구를 처리하는 서블릿
 * @author jsg
 * @version 1.0
 * @since 2025.10.24
 */
@WebServlet("/admin/user/restore.do")
public class restoreUser extends HttpServlet {
    /**
     * 특정 사용자를 정지 상태에서 복구하고, 정지된 회원 목록 페이지로 리다이렉트합니다.
     * @param req HttpServletRequest 객체
     * @param resp HttpServletResponse 객체
     * @throws ServletException 서블릿 예외
     * @throws IOException 입출력 예외
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        
        int userId = Integer.parseInt(req.getParameter("userId"));
        
        // 수정: userDAO 대신 suspendedUserDAO를 사용합니다.
        suspendedUserDAO dao = new suspendedUserDAO();
        dao.restoreUser(userId);
        
        // 처리가 끝나면 정지된 회원 목록으로 다시 이동합니다.
        resp.sendRedirect(req.getContextPath() + "/admin/user/suspendedList.do");
    }
}