package com.trip.qna;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.trip.qna.model.QnADAO;
import com.trip.qna.model.QnADTO;

/**
 * Q&A 게시물 삭제를 처리하는 서블릿
 * @author jsg
 * @version 1.0
 * @since 2025.10.24
 */
@WebServlet("/qna/delete.do")
public class Del extends HttpServlet {
    /**
     * 서블릿의 직렬화 버전을 나타내는 고유 ID입니다.
     */
    private static final long serialVersionUID = 1L;

    /**
     * POST 요청을 처리하여 Q&A 게시물을 삭제합니다.
     * 게시물 작성자 또는 관리자만 삭제할 수 있습니다.
     * @param request HttpServletRequest 객체
     * @param response HttpServletResponse 객체
     * @throws ServletException 서블릿 예외 발생 시
     * @throws IOException 입출력 예외 발생 시
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String seq = request.getParameter("seq");
        HttpSession session = request.getSession();
        String sessionUser = (String) session.getAttribute("userId");

        QnADAO dao = new QnADAO();
        QnADTO dto = dao.view(seq);

        // 본인 글이 아니면 삭제 금지
        if (!sessionUser.equals(dto.getUser_id()) && !"admin".equals(session.getAttribute("role"))) {
            dao.close();
            response.sendRedirect(request.getContextPath() + "/qna/list.do");
            return;
        }

        // 댓글까지 같이 삭제하려면 DAO에 메소드 추가 필요
        dao.del(seq);
        dao.close();

        response.sendRedirect(request.getContextPath() + "/qna/list.do");
    }
}
