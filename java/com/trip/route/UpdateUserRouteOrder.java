package com.trip.route;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.test.util.DBUtil;
import com.trip.member.model.UserDTO;

/**
 * 사용자 정의 경로의 순서를 업데이트하는 서블릿
 * @author jsg
 * @version 1.0
 * @since 2025.10.24
 */
@WebServlet("/route/updateUserRouteOrder.do")
public class UpdateUserRouteOrder extends HttpServlet {

    /**
     * HTTP POST 요청을 처리합니다.
     * 클라이언트(JSP)에서 드래그앤드롭 등으로 변경된 경로의 순서 정보를 JSON 형태로 받아
     * 데이터베이스에 일괄 업데이트(Batch Update)합니다. 이 작업은 트랜잭션으로 처리됩니다.
     * <p>
     * 1. 세션에서 로그인한 사용자 정보를 확인합니다.
     * 2. 요청 본문(body)에서 JSON 데이터를 읽어옵니다.
     * (JSON 형식: { userRouteId: 123, updatedDays: { "1": [stopId1, stopId2], "2": [stopId3] } })
     * 3. 트랜잭션을 시작합니다.
     * 4. (보안 TODO: 해당 `userRouteId`가 로그인한 사용자의 소유인지 확인해야 함)
     * 5. `updatedDays` 객체를 순회하며 각 경유지(stop)의 `user_route_day`와 `user_route_stop_order`를
     * `tblUserRouteStop` 테이블에 업데이트하는 SQL을 배치(batch)에 추가합니다.
     * 6. `executeBatch()`를 실행하여 모든 변경 사항을 한 번에 적용합니다.
     * 7. 모든 작업이 성공하면 commit하고, 실패하면 rollback합니다.
     * 8. 처리 결과를 JSON 형태로 클라이언트에 응답합니다.
     *
     * @param req  클라이언트가 서블릿에 보낸 HttpServletRequest 객체
     * @param resp 서블릿이 클라이언트에 보내는 HttpServletResponse 객체
     * @throws ServletException 서블릿 처리 중 예외 발생 시
     * @throws IOException      요청 또는 응답 처리 중 I/O 예외 발생 시
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        PrintWriter out = resp.getWriter();
        JSONObject jsonResponse = new JSONObject();

        // 1. 로그인 상태 확인
        HttpSession session = req.getSession();
        Object userObj = session.getAttribute("user");
        if (userObj == null) {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            jsonResponse.put("success", false);
            jsonResponse.put("message", "로그인이 필요합니다.");
            out.print(jsonResponse.toJSONString());
            return;
        }
        UserDTO userDto = (UserDTO) userObj;
        int userId = (int) userDto.getUserId(); // UserDTO에 getUserId() 메서드가 있다고 가정

        // 2. 요청 본문(body)에서 JSON 데이터 읽기
        StringBuilder sb = new StringBuilder();
        BufferedReader reader = req.getReader();
        String line;
        while ((line = reader.readLine()) != null) {
            sb.append(line);
        }

        Long userRouteId = null;
        JSONObject updatedDays = null;

        try {
            JSONParser parser = new JSONParser();
            JSONObject jsonRequest = (JSONObject) parser.parse(sb.toString());
            userRouteId = (Long) jsonRequest.get("userRouteId");
            updatedDays = (JSONObject) jsonRequest.get("updatedDays");
            if (userRouteId == null || updatedDays == null) {
                throw new Exception();
            }
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            jsonResponse.put("success", false);
            jsonResponse.put("message", "요청 데이터 형식이 잘못되었습니다.");
            out.print(jsonResponse.toJSONString());
            return;
        }

        // 3. 데이터베이스 처리 (트랜잭션)
        Connection conn = null;
        PreparedStatement pstat = null;
        DBUtil util = new DBUtil();

        try {
            conn = util.open();
            conn.setAutoCommit(false); // 트랜잭션 시작

            // TODO: 이 경로의 소유자가 현재 로그인한 사용자인지 확인하는 로직 추가 필요

            String sql = "UPDATE tblUserRouteStop SET user_route_day = ?, user_route_stop_order = ? WHERE user_route_stop_id = ? AND user_route_id = ?";
            pstat = conn.prepareStatement(sql);

            for (Object dayKeyObj : updatedDays.keySet()) {
                String dayKey = (String) dayKeyObj;
                int day = Integer.parseInt(dayKey);
                JSONArray stopIdsForDay = (JSONArray) updatedDays.get(dayKey);

                for (int i = 0; i < stopIdsForDay.size(); i++) {
                    long stopId = (Long) stopIdsForDay.get(i);
                    int newOrder = i + 1;

                    pstat.setInt(1, day); // Set user_route_day
                    pstat.setInt(2, newOrder); // Set user_route_stop_order
                    pstat.setLong(3, stopId); // WHERE user_route_stop_id
                    pstat.setLong(4, userRouteId);
                    pstat.addBatch();
                }
            }

            pstat.executeBatch();
            conn.commit(); // 트랜잭션 완료

            jsonResponse.put("success", true);
            jsonResponse.put("message", "경로 순서가 성공적으로 저장되었습니다.");

        } catch (Exception e) {
            try {
                if (conn != null) conn.rollback();
            } catch (SQLException se) {
                se.printStackTrace();
            }
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            jsonResponse.put("success", false);
            jsonResponse.put("message", "순서 저장 중 오류가 발생했습니다: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try { if (pstat != null) pstat.close(); } catch (Exception e) {}
            try { if (conn != null) conn.close(); } catch (Exception e) {}
        }

        out.print(jsonResponse.toJSONString());
        out.close();
    }
}