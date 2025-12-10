package com.trip.board;

import java.io.IOException;
import java.util.List;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import com.trip.board.model.RoutePostDAO;
import com.trip.board.model.RoutePostDTO;

/**
 * 여행 루트 게시글 목록을 조회하고 JSP로 전달하는 서블릿
 * @author jsg
 * @version 1.0
 * @since 2025.10.24
 */
@WebServlet("/routepost/list.do")
public class RoutePostList extends HttpServlet {

    private static final long serialVersionUID = 1L; // 직렬화 경고 제거

    /**
     * GET 요청을 처리하여 전체 여행 루트 게시글 목록을 조회합니다.
     * DB에서 게시글 목록(List<RoutePostDTO>)을 조회(getList)하여
     * request에 "list"라는 이름으로 저장한 후,
     * `/WEB-INF/views/board/routepost/list.jsp` 페이지로 포워드합니다.
     *
     * @param req  클라이언트의 요청 정보를 담은 HttpServletRequest 객체
     * @param resp 클라이언트에게 응답을 보내기 위한 HttpServletResponse 객체
     * @throws ServletException 서블릿 처리 중 오류가 발생한 경우
     * @throws IOException      입출력 오류가 발생한 경우
     * @see /WEB-INF/views/board/routepost/list.jsp
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        try {
            //  1. DAO 객체 생성
            RoutePostDAO dao = new RoutePostDAO();

            //  2. 게시글 목록 조회
            List<RoutePostDTO> list = dao.getList();

            //  3. JSP로 전달
            req.setAttribute("list", list);

            //  4. JSP 포워드
            RequestDispatcher dispatcher =
                    req.getRequestDispatcher("/WEB-INF/views/board/routepost/list.jsp");
            dispatcher.forward(req, resp);

            dao.close();

        } catch (Exception e) {
            e.printStackTrace();
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "게시글 목록 조회 중 오류 발생");
        }
    }
}
