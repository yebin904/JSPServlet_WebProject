package com.trip.route;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import com.test.util.DBUtil;

/**
 * AI 추천 경로의 지도 뷰 데이터를 제공하는 서블릿
 * @author jsg
 * @version 1.0
 * @since 2025.10.24
 */
@WebServlet("/route/aiMapView.do")
public class AiRouteMapView extends HttpServlet {

    /**
     * HTTP GET 요청을 처리합니다.
     * `aiMapViewPage.do`에 의해 포워딩된 JSP 페이지에서 AJAX를 통해 호출됩니다.
     * <p>
     * 1. 요청 파라미터로 `id`(AI 경로 ID)를 받습니다.
     * 2. `tblAiRouteStop` 테이블에서 해당 ID를 가진 모든 경유지(stop)의
     * 일차(`day`), 순서(`order`), 이름(`name`), 위도(`lat`), 경도(`lng`)를 조회합니다.
     * 3. 조회된 결과를 JSON 배열(JSONArray) 형태로 변환하여 클라이언트에 응답합니다.
     * 4. ID가 유효하지 않거나 조회 중 오류 발생 시 빈 JSON 배열(`[]`)을 반환합니다.
     *
     * @param req  클라이언트가 서블릿에 보낸 HttpServletRequest 객체
     * @param resp 서블릿이 클라이언트에 보내는 HttpServletResponse 객체
     * @throws ServletException 서블릿 처리 중 예외 발생 시
     * @throws IOException      요청 또는 응답 처리 중 I/O 예외 발생 시
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        resp.setContentType("application/json; charset=UTF-8");
        PrintWriter out = resp.getWriter();

        String id = req.getParameter("id");
        int aiRouteId = 0;

        try {
            aiRouteId = Integer.parseInt(id);
        } catch (Exception e) {
            System.out.println("❌ 잘못된 id 값: " + id);
            out.write("[]");
            return;
        }

        DBUtil util = new DBUtil();
        Connection conn = null;
        PreparedStatement pstat = null;
        ResultSet rs = null;

        JSONArray arr = new JSONArray();

        try {
            conn = util.open();
            String sql = "SELECT ai_route_day AS day, ai_route_stop_order AS \"order\", "
                    + "ai_route_description AS name, ai_route_lat AS lat, ai_route_long AS lng "
                    + "FROM tblAiRouteStop "
                    + "WHERE ai_route_id = ? "
                    + "ORDER BY ai_route_day, ai_route_stop_order";

            pstat = conn.prepareStatement(sql);
            pstat.setInt(1, aiRouteId);

            rs = pstat.executeQuery();

            while (rs.next()) {
                JSONObject obj = new JSONObject();
                obj.put("day", rs.getInt("day"));
                obj.put("order", rs.getInt("order"));
                obj.put("name", rs.getString("name"));
                obj.put("lat", rs.getDouble("lat"));
                obj.put("lng", rs.getDouble("lng"));
                arr.add(obj);
            }

            out.print(arr.toJSONString());

        } catch (Exception e) {
            e.printStackTrace();
            out.print("[]");
        } finally {
            try { rs.close(); } catch (Exception e) {}
            try { pstat.close(); } catch (Exception e) {}
            try { conn.close(); } catch (Exception e) {}
        }
    }
}