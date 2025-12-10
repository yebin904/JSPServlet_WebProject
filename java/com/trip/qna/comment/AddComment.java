package com.trip.qna.comment;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import com.trip.qna.model.QnACommentDAO;
import org.json.simple.JSONObject;

/**
 * Q&A 게시물에 댓글을 추가하는 서블릿
 * @author jsg
 * @version 1.0
 * @since 2025.10.24
 */
@WebServlet("/qna/addComment.do")
public class AddComment extends HttpServlet {
    /**
     * POST 요청을 처리하여 Q&A 게시물에 새로운 댓글을 추가합니다.
     * 세션에서 사용자 ID를 확인하고, 게시물 ID와 댓글 내용을 받아 데이터베이스에 저장합니다.
     * 처리 결과를 JSON 형태로 반환합니다.
     * @param req HttpServletRequest 객체
     * @param resp HttpServletResponse 객체
     * @throws ServletException 서블릿 예외 발생 시
     * @throws IOException 입출력 예외 발생 시
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        String userId = (String) session.getAttribute("userId");
        if(userId == null) {
            resp.setStatus(401);
            return;
        }

        String boardId = req.getParameter("boardId");
        String content = req.getParameter("content");

        QnACommentDAO dao = new QnACommentDAO();
        boolean success = dao.addComment(boardId, userId, content);
        dao.close();

        resp.setContentType("application/json;charset=UTF-8");
        PrintWriter out = resp.getWriter();
        JSONObject obj = new JSONObject();
        obj.put("success", success);
        out.print(obj);
    }
}
