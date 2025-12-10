package com.trip.comment;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.simple.JSONObject;

import com.trip.comment.model.CommentDAO;
import com.trip.member.model.UserDTO;


/**
 * 경로 게시물 댓글 추가(Add) 요청을 처리하는 서블릿(AJAX).
 * '/comment/add.do' URL로의 POST 요청을 처리합니다.
 *
 * @author jsg
 * @version 1.0
 * @since 2025.10.24
 */
@WebServlet("/comment/add.do")
public class RouteCommAdd extends HttpServlet {

    /**
     * 댓글 등록 요청(POST)을 처리합니다.
     * <p>
     * 1. 응답 타입을 JSON으로 설정합니다.
     * 2. 세션을 확인하여 로그인 상태(UserDTO)를 체크합니다.
     * 3. 비로그인 시, JSON으로 에러 메시지(로그인 필요)를 반환합니다.
     * 4. 로그인 상태인 경우, 파라미터(postId, content)와 세션의 userId를 가져옵니다.
     * 5. CommentDAO의 addComment() 메서드를 호출하여 댓글을 DB에 등록합니다.
     * 6. 처리 결과(성공/실패)를 JSON 객체(success, message)로 생성하여 클라이언트에 응답합니다.
     * 7. 예외 발생 시, 서버 오류 메시지를 JSON으로 응답합니다.
     * </p>
     *
     * @param req  클라이언트의 HttpServletRequest 객체 (postId, content 포함)
     * @param resp 클라이언트에게 응답을 보낼 HttpServletResponse 객체 (JSON 응답)
     * @throws ServletException 서블릿 관련 에러 발생 시
     * @throws IOException      입출력 관련 에러 발생 시
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        // AJAX 응답을 위한 설정
        resp.setContentType("application/json; charset=UTF-8");
        JSONObject result = new JSONObject(); // JSON-Simple 라이브러리 사용

        try {
            HttpSession session = req.getSession(false); // 세션이 없으면 새로 생성 안함
            UserDTO user = (session != null) ? (UserDTO) session.getAttribute("user") : null;

            // ✅ 1. 로그인 안 된 경우 차단
            if (user == null) {
                result.put("success", false);
                result.put("message", "로그인 후 이용 가능합니다.");
                resp.getWriter().print(result.toJSONString());
                return; // 서블릿 종료
            }

            // ✅ 2. 파라미터 수집
            int userId = (int) user.getUserId(); // 세션에서 사용자 ID
            int postId = Integer.parseInt(req.getParameter("postId")); // 원본 글 ID
            String content = req.getParameter("content"); // 댓글 내용

            // ✅ 3. DAO를 통한 댓글 등록
            CommentDAO dao = new CommentDAO();
            int count = dao.addComment(userId, postId, content);
            dao.close(); // 자원 해제

            // ✅ 4. 결과 JSON 응답
            if (count > 0) {
                result.put("success", true);
                result.put("message", "댓글이 등록되었습니다.");
            } else {
                result.put("success", false);
                result.put("message", "댓글 등록 실패");
            }

        } catch (Exception e) {
            e.printStackTrace();
            // ✅ 5. 예외 처리
            result.put("success", false);
            result.put("message", "서버 오류 발생");
        }

        // 최종 JSON 응답 전송
        resp.getWriter().print(result.toJSONString());
    }
}
