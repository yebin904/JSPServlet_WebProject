package com.trip.comment;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import com.trip.comment.model.*;

/**
 * 경로 게시물 댓글 목록(List)을 조회하는 서블릿(AJAX).
 * '/comment/list.do' URL로의 GET 요청을 처리합니다.
 *
 * @author jsg
 * @version 1.0
 * @since 2025.10.24
 */
@WebServlet("/comment/list.do")
public class RouteCommList extends HttpServlet {

    /**
     * 댓글 목록 조회 요청(GET)을 처리합니다.
     * <p>
     * 1. 응답 타입을 JSON으로 설정합니다.
     * 2. 필수 파라미터(postId)와 선택 파라미터(page)를 받습니다.
     * 3. postId가 없으면 400 Bad Request 에러를 응답합니다.
     * 4. 페이지네이션을 위한 'begin'과 'end' 값을 계산합니다. (페이지 당 20개)
     * 5. CommentDAO의 getCommentPage()와 getTotalCount()를 호출하여 데이터를 조회합니다.
     * 6. 조회된 List&lt;CommentDTO&gt;를 JSONArray로 변환합니다.
     * 7. 최종 결과를 JSONArray(list)와 총 개수(totalCount)를 포함하는 JSONObject로 감싸 클라이언트에 응답합니다.
     * 8. 예외 발생 시, 400 또는 500 에러를 응답합니다.
     * </p>
     *
     * @param req  클라이언트의 HttpServletRequest 객체 (postId, page? 포함)
     * @param resp 클라이언트에게 응답을 보낼 HttpServletResponse 객체 (JSON 응답)
     * @throws ServletException 서블릿 관련 에러 발생 시
     * @throws IOException      입출력 관련 에러 발생 시
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        // AJAX 응답을 위한 설정
        resp.setContentType("application/json; charset=UTF-8");

        try {
            // ✅ 1. 파라미터 검증
            String postParam = req.getParameter("postId");
            String pageParam = req.getParameter("page");

            if (postParam == null || postParam.isEmpty()) {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "게시글 번호가 없습니다.");
                return;
            }

            // ✅ 2. 페이지네이션 계산
            int postId = Integer.parseInt(postParam);
            int page = (pageParam == null || pageParam.isEmpty()) ? 1 : Integer.parseInt(pageParam);
            int pageSize = 20; // 한 페이지에 20개씩

            int begin = (page - 1) * pageSize + 1;
            int end = begin + pageSize - 1;

            CommentDAO dao = new CommentDAO();

            // ✅ 3. DAO를 통한 데이터 조회
            List<CommentDTO> list = dao.getCommentPage(postId, begin, end);
            int totalCount = dao.getTotalCount(postId);
            dao.close(); // 자원 해제

            // ✅ 4. JSON 생성 (List -> JSONArray)
            JSONArray arr = new JSONArray();
            for (CommentDTO dto : list) {
                JSONObject obj = new JSONObject();
                obj.put("commentId", dto.getRoutepostCommentId());
                obj.put("userId", dto.getUserId());
                obj.put("nickname", dto.getNickname());
                obj.put("content", dto.getRoutepostContent());
                obj.put("regdate", dto.getRoutepostRegdate());
                arr.add(obj);
            }

            // ✅ 5. 최종 결과 JSON 생성 (목록 + 총 개수)
            JSONObject result = new JSONObject();
            result.put("list", arr);
            result.put("totalCount", totalCount);

            // ✅ 6. JSON 응답 출력
            resp.getWriter().print(result.toJSONString());

        } catch (NumberFormatException e) {
            // postId, page 파라미터가 숫자가 아닐 경우
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "유효하지 않은 요청입니다.");
        } catch (Exception e) {
            e.printStackTrace();
            // 그 외 DB 오류 등
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "댓글 조회 중 오류 발생");
        }
    }
}
