package com.trip.qna;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.simple.JSONObject;
import com.trip.qna.model.QnADAO;

/**
 * Q&A 게시물 좋아요 토글을 처리하는 서블릿
 * @author jsg
 * @version 1.0
 * @since 2025.10.24
 */
@WebServlet("/qna/likeToggle.do")
public class LikeToggle extends HttpServlet {
    /**
     * GET 요청을 처리하여 Q&A 게시물의 좋아요 상태를 토글하고, 업데이트된 좋아요 수를 반환합니다.
     * 사용자 ID와 게시물 ID를 기반으로 좋아요를 추가하거나 제거합니다.
     * @param req HttpServletRequest 객체
     * @param resp HttpServletResponse 객체
     * @throws ServletException 서블릿 예외 발생 시
     * @throws IOException 입출력 예외 발생 시
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        String userId = (String) session.getAttribute("userId");
        String seq = req.getParameter("seq");

        resp.setContentType("application/json;charset=UTF-8");
        PrintWriter out = resp.getWriter();

        if (userId == null || seq == null) {
            resp.setStatus(400);
            out.print("{\"error\":\"invalid request\"}");
            return;
        }

        QnADAO dao = new QnADAO();
        boolean status = dao.toggleLike(seq, userId);
        int count = dao.getLikeCount(seq);
        dao.close();

        JSONObject obj = new JSONObject();
        obj.put("status", status);
        obj.put("count", count);

        out.print(obj.toJSONString());
    }
}
