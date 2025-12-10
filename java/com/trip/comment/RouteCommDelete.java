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
 * 경로 게시물 댓글 삭제(Delete)를 처리하는 서블릿(AJAX).
 * '/comment/delete.do' URL로의 POST 요청을 처리합니다.
 *
 * @author jsg
 * @version 1.0
 * @since 2025.10.24
 */
@WebServlet("/comment/delete.do")
public class RouteCommDelete extends HttpServlet {

    /**
     * 댓글 삭제 요청(POST)을 처리합니다.
     * <p>
     * 1. 응답 타입을 JSON으로 설정합니다.
     * 2. 세션을 확인하여 로그인 상태(UserDTO)를 체크합니다.
     * 3. 비로그인 시, JSON으로 에러 메시지(로그인 필요)를 반환합니다.
     * 4. 로그인 상태인 경우, 파라미터(commentId)와 세션의 userId를 가져옵니다.
     * 5. CommentDAO의 getCommentOwnerId()를 호출하여 댓글 작성자 ID(ownerId)를 조회합니다.
     * 6. 세션의 userId와 ownerId를 비교하여 본인 여부를 확인합니다.
     * 7. 본인이 아닐 경우, JSON으로 에러 메시지(삭제 권한 없음)를 반환합니다.
     * 8. 본인일 경우, CommentDAO의 deleteComment(commentId)를 호출하여 댓글을 (물리적) 삭제합니다.
     * 9. 처리 결과(성공/실패)를 JSON 객체로 생성하여 클라이언트에 응답합니다.
     * 10. 예외 발생 시, 서버 오류 메시지를 JSON으로 응답합니다.
     * </p>
     *
     * @param req  클라이언트의 HttpServletRequest 객체 (commentId 포함)
     * @param resp 클라이언트에게 응답을 보낼 HttpServletResponse 객체 (JSON 응답)
     * @throws ServletException 서블릿 관련 에러 발생 시
     * @throws IOException      입출력 관련 에러 발생 시
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        // AJAX 응답을 위한 설정
        resp.setContentType("application/json; charset=UTF-8");
        JSONObject result = new JSONObject();

        try {
            HttpSession session = req.getSession(false);
            UserDTO user = (session != null) ? (UserDTO) session.getAttribute("user") : null;

            // ✅ 1. 로그인 안 된 경우
            if (user == null) {
                result.put("success", false);
                result.put("message", "로그인 후 이용 가능합니다.");
                resp.getWriter().print(result.toJSONString());
                return;
            }

            // ✅ 2. 파라미터 수집
            int userId = (int) user.getUserId(); // 현재 로그인한 사용자 ID
            int commentId = Integer.parseInt(req.getParameter("commentId")); // 삭제할 댓글 ID

            CommentDAO dao = new CommentDAO();

            // ✅ 3. 댓글 작성자 확인 (본인 확인)
            int ownerId = dao.getCommentOwnerId(commentId);

            if (ownerId != userId) {
                dao.close();
                result.put("success", false);
                result.put("message", "본인 댓글만 삭제할 수 있습니다.");
                resp.getWriter().print(result.toJSONString());
                return;
            }

            // ✅ 4. 본인 댓글이면 (물리적) 삭제
            // 참고: DAO에는 논리삭제(status 'N') 메서드도 있지만, 여기서는 물리삭제(DELETE) 메서드 호출
            int count = dao.deleteComment(commentId);
            dao.close(); // 자원 해제

            // ✅ 5. 결과 JSON 응답
            if (count > 0) {
                result.put("success", true);
                result.put("message", "댓글이 삭제되었습니다.");
            } else {
                result.put("success", false);
                result.put("message", "댓글 삭제 실패");
            }

        } catch (Exception e) {
            e.printStackTrace();
            // ✅ 6. 예외 처리
            result.put("success", false);
            result.put("message", "서버 오류 발생");
        }

        // 최종 JSON 응답 전송
        resp.getWriter().print(result.toJSONString());
    }
}
