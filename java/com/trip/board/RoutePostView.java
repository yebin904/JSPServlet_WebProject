package com.trip.board;

import java.io.IOException;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import com.trip.board.model.RoutePostDAO;
import com.trip.board.model.RoutePostDTO;

/**
 * 경로 게시물 상세보기를 처리하는 서블릿
 * @author jsg
 * @version 1.0
 * @since 2025.10.24
 */
@WebServlet("/routepost/view.do")
public class RoutePostView extends HttpServlet {

    private static final long serialVersionUID = 1L;

    /**
     * GET 요청을 처리하여 특정 경로 게시물의 상세 보기 페이지로 이동합니다.
     * 요청된 게시물 ID(id)에 해당하는 조회수를 1 증가(increaseViewCount)시키고,
     * 게시물 상세 정보(작성자, 이미지 포함)를 조회(getDetail)하여
     * request에 "dto"라는 이름으로 저장한 후, `/WEB-INF/views/board/routepost/view.jsp` 페이지로 포워드합니다.
     *
     * @param req  클라이언트의 요청 정보를 담은 HttpServletRequest 객체 (id: 게시물 ID)
     * @param resp 클라이언트에게 응답을 보내기 위한 HttpServletResponse 객체
     * @throws ServletException 서블릿 처리 중 오류가 발생한 경우
     * @throws IOException      입출력 오류가 발생한 경우
     * @see /WEB-INF/views/board/routepost/view.jsp
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        try {
            // ✅ 1. 글번호 파라미터 받기
            String idParam = req.getParameter("id");
            if (idParam == null || idParam.isEmpty()) {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "잘못된 접근입니다.");
                return;
            }

            int postId = Integer.parseInt(idParam);

            // ✅ 2. DAO 생성
            RoutePostDAO dao = new RoutePostDAO();

            // ✅ 3. 조회수 증가 (글이 실제 존재할 때만)
            dao.increaseViewCount(idParam);

            // ✅ 4. 상세 정보 가져오기 (작성자 + 이미지 포함)
            RoutePostDTO dto = dao.getDetail(idParam);

            if (dto != null) {
                req.setAttribute("dto", dto);

                // ✅ JSP 포워드
                RequestDispatcher dispatcher =
                        req.getRequestDispatcher("/WEB-INF/views/board/routepost/view.jsp");
                dispatcher.forward(req, resp);
            } else {
                // 글이 존재하지 않으면 404
                resp.sendError(HttpServletResponse.SC_NOT_FOUND, "게시글을 찾을 수 없습니다.");
            }

            dao.close();

        } catch (NumberFormatException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "유효하지 않은 글 번호입니다.");
        } catch (Exception e) {
            e.printStackTrace();
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "게시글 조회 중 오류 발생");
        }
    }
}
