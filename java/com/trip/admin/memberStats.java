package com.trip.admin;

import com.trip.admin.model.statDAO;
import com.trip.admin.model.statDTO;
import com.google.gson.Gson;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 회원 관련 통계 페이지를 처리하는 서블릿
 * @author jsg
 * @version 1.0
 * @since 2025.10.24
 */
@WebServlet("/admin/stats/member.do")
public class memberStats extends HttpServlet {
    
    /**
     * 방문자 수, 연도별 가입/탈퇴, 성별 통계 등 회원 관련 통계 데이터를 조회하여
     * JSON 형태로 변환 후 JSP 페이지로 전달합니다.
     * @param req HttpServletRequest 객체
     * @param resp HttpServletResponse 객체
     * @throws ServletException 서블릿 예외
     * @throws IOException 입출력 예외
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        
        ServletContext context = req.getServletContext();
        AtomicInteger totalVisits = (AtomicInteger) context.getAttribute("totalVisits");
        AtomicInteger memberVisits = (AtomicInteger) context.getAttribute("memberVisits");

        int total = (totalVisits != null) ? totalVisits.get() : 0;
        int member = (memberVisits != null) ? memberVisits.get() : 0;
        int nonMember = total - member;
        
        req.setAttribute("memberVisits", member);
        req.setAttribute("nonMemberVisits", nonMember);
        
        statDAO dao = new statDAO();
        
        List<statDTO> yearlySignUps = dao.getYearlySignUps();
        List<statDTO> yearlyWithdrawals = dao.getYearlyWithdrawals();
        
        List<statDTO> memberGenderStats = dao.getMemberGenderStats();
        
        Gson gson = new Gson();
        req.setAttribute("yearlySignUpsJson", gson.toJson(yearlySignUps));
        req.setAttribute("yearlyWithdrawalsJson", gson.toJson(yearlyWithdrawals));

        req.setAttribute("memberGenderStatsJson", gson.toJson(memberGenderStats));

        RequestDispatcher dispatcher = req.getRequestDispatcher("/WEB-INF/views/admin/stats.jsp");
        dispatcher.forward(req, resp);
    }
}