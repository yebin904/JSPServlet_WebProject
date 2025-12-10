package com.trip.admin;

import com.trip.admin.model.reportDAO;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 관리자용 신고 처리를 담당하는 서블릿
 * @author jsg
 * @version 1.0
 * @since 2025.10.24
 */
@WebServlet("/admin/processReport.do")
public class processReport extends HttpServlet {

    /**
     * 신고 처리 (승인 또는 거절) 요청을 받아 처리합니다.
     * @param req HttpServletRequest 객체
     * @param resp HttpServletResponse 객체
     * @throws ServletException 서블릿 예외
     * @throws IOException 입출력 예외
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        
        req.setCharacterEncoding("UTF-8");

        // 1. JSP에서 보낸 카멜 케이스 파라미터를 받도록 수정
        int reportId = Integer.parseInt(req.getParameter("reportId")); // 수정
        String action = req.getParameter("action");

        reportDAO dao = new reportDAO();

        // 2. 'action' 값에 따라 분기하여 처리
        if ("approve".equals(action)) {
            
            // '숨김' 처리에 필요한 추가 데이터 (카멜 케이스로 수정)
            String targetType = req.getParameter("targetType"); // 수정
            int targetId = Integer.parseInt(req.getParameter("targetId")); // 수정

            dao.hidePost(targetType, targetId);
            dao.updateReportStatus(reportId, "APPROVED");

        } else if ("reject".equals(action)) {
            
            dao.updateReportStatus(reportId, "REJECTED");
        }

        // 3. 처리가 끝난 후, 신고 접수 목록 페이지로 리다이렉트
        resp.sendRedirect(req.getContextPath() + "/admin/report.do");
    }
}