package com.trip.board;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.trip.board.model.RoutePostDAO;
import com.trip.member.model.UserDTO;


/**
 * 경로 게시물 삭제를 처리하는 서블릿
 * @author jsg
 * @version 1.0
 * @since 2025.10.24
 */
@WebServlet("/routepost/delete.do")
public class RoutePostDelete extends HttpServlet {

    /**
     * GET 요청을 처리하여 특정 경로 게시물을 삭제합니다.
     * 게시물 ID(id)를 파라미터로 받습니다.
     * 1. 로그인 상태를 확인합니다.
     * 2. DB에서 해당 게시물의 소유자 ID(getPostOwnerId)를 조회합니다.
     * 3. 현재 로그인한 사용자의 ID와 게시물 소유자 ID가 일치하는지 확인합니다.
     * 4. 일치할 경우에만 게시물을 삭제(delete)합니다.
     * 처리 결과에 따라 JavaScript alert을 포함한 HTML을 응답하여
     * 사용자에게 성공/실패 메시지를 알리고 페이지를 이동시킵니다.
     *
     * @param req  클라이언트의 요청 정보를 담은 HttpServletRequest 객체 (id: 게시물 ID)
     * @param resp 클라이언트에게 응답을 보내기 위한 HttpServletResponse 객체 (text/html 응답)
     * @throws ServletException 서블릿 처리 중 오류가 발생한 경우
     * @throws IOException      입출력 오류가 발생한 경우
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html; charset=UTF-8");

        HttpSession session = req.getSession(false);

        try {
            // ✅ 로그인 세션 확인
            if (session == null || session.getAttribute("user") == null) {
                resp.getWriter().println("<script>alert('로그인 후 이용 가능합니다.');history.back();</script>");
                return;
            }

            UserDTO user = (UserDTO) session.getAttribute("user");
            int userId = (int) user.getUserId();

            // ✅ 게시글 ID 가져오기
            String idParam = req.getParameter("id");
            if (idParam == null || idParam.isEmpty()) {
                resp.getWriter().println("<script>alert('잘못된 요청입니다.');history.back();</script>");
                return;
            }

            int postId = Integer.parseInt(idParam);

            RoutePostDAO dao = new RoutePostDAO();
            int ownerId = dao.getPostOwnerId(postId);

            // ✅ 작성자 본인만 삭제 허용
            if (ownerId != userId) {
                dao.close();
                resp.getWriter().println("<script>alert('본인 게시글만 삭제할 수 있습니다.');history.back();</script>");
                return;
            }

            // ✅ 게시글 삭제
            int count = dao.delete(postId);
            dao.close();

            if (count > 0) {
                // ✅ 삭제 성공 → 목록으로 이동
                resp.getWriter().println("<script>alert('게시글이 삭제되었습니다.'); location.href='"
                        + req.getContextPath() + "/routepost/list.do';</script>");
            } else {
                resp.getWriter().println("<script>alert('게시글 삭제에 실패했습니다.');history.back();</script>");
            }

        } catch (Exception e) {
            e.printStackTrace();
            resp.getWriter().println("<script>alert('서버 오류 발생');history.back();</script>");
        }
    }
}
