package com.trip.board;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.simple.JSONObject;

import com.trip.board.model.RoutePostDAO;
import com.trip.member.model.UserDTO;


/**
 * 경로 게시물 좋아요 및 스크랩 상태를 확인하는 서블릿
 * @author jsg
 * @version 1.0
 * @since 2025.10.24
 */
@WebServlet("/routepost/status.do")
public class RoutePostStatus extends HttpServlet {

    /**
     * GET 요청을 처리하여 현재 로그인한 사용자의 특정 경로 게시물(id)에 대한
     * 좋아요(liked) 및 스크랩(scrapped) 상태를 확인합니다.
     * 로그인 상태가 아니면 "liked": false, "scrapped": false 를 반환합니다.
     * 로그인 상태이면 DB에서 각 상태를 확인(checkLike, checkScrap)하여
     * 결과를 JSON 형식으로 응답합니다.
     *
     * @param req  클라이언트의 요청 정보를 담은 HttpServletRequest 객체 (id: 게시물 ID)
     * @param resp 클라이언트에게 응답을 보내기 위한 HttpServletResponse 객체 (JSON 응답)
     * @throws ServletException 서블릿 처리 중 오류가 발생한 경우
     * @throws IOException      입출력 오류가 발생한 경우
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        resp.setContentType("application/json; charset=UTF-8");
        JSONObject obj = new JSONObject();

        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            obj.put("liked", false);
            obj.put("scrapped", false);
            resp.getWriter().print(obj.toJSONString());
            return;
        }

        int postId = Integer.parseInt(req.getParameter("id"));
        UserDTO user = (UserDTO) session.getAttribute("user");
        int userId = (int) user.getUserId();

        RoutePostDAO dao = new RoutePostDAO();
        boolean liked = dao.checkLike(userId, postId);
        boolean scrapped = dao.checkScrap(userId, postId);
        dao.close();

        obj.put("liked", liked);
        obj.put("scrapped", scrapped);
        resp.getWriter().print(obj.toJSONString());
    }
}
