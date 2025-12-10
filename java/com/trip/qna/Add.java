package com.trip.qna;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.trip.qna.model.CategoryDTO;
import com.trip.qna.model.QnADAO;
import com.trip.qna.model.QnADTO;

/**
 * Q&A 게시물 추가를 처리하는 서블릿
 * @author jsg
 * @version 1.0
 * @since 2025.10.24
 */
@WebServlet("/qna/add.do")
public class Add extends HttpServlet {

    /**
     * GET 요청을 처리하여 Q&A 게시물 추가 페이지를 렌더링합니다.
     * 카테고리 목록을 불러와 JSP로 전달합니다.
     * @param req HttpServletRequest 객체
     * @param resp HttpServletResponse 객체
     * @throws ServletException 서블릿 예외 발생 시
     * @throws IOException 입출력 예외 발생 시
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 카테고리 목록 불러오기 (선택형)
        QnADAO dao = new QnADAO();
        ArrayList<CategoryDTO> categories = dao.getCategoryList(); // ✅ 여기서 리스트 받아오기

        req.setAttribute("categories", categories); // ✅ JSP로 전달
        dao.close();

        RequestDispatcher dispatcher = req.getRequestDispatcher("/WEB-INF/views/qna/add.jsp");
        dispatcher.forward(req, resp);
    }

    /**
     * POST 요청을 처리하여 새로운 Q&A 게시물을 데이터베이스에 추가합니다.
     * 세션에서 사용자 ID를 가져오고, 요청 파라미터에서 제목, 내용, 카테고리 ID를 추출하여 게시물을 등록합니다.
     * 등록 성공 시 목록 페이지로 리다이렉트하고, 실패 시 에러 메시지를 표시합니다.
     * @param req HttpServletRequest 객체
     * @param resp HttpServletResponse 객체
     * @throws ServletException 서블릿 예외 발생 시
     * @throws IOException 입출력 예외 발생 시
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");
        HttpSession session = req.getSession();
        
        // ✅ [테스트용 로그인 대체 코드]
        // 실제 로그인 기능이 없으므로 강제로 user_id를 세션에 넣어줌
        if (session.getAttribute("userId") == null) {
            session.setAttribute("userId", "5"); // tblUser에 5번 회원이 있어야 함
        }

        String userId = (String) session.getAttribute("userId"); // 로그인 사용자
        String title = req.getParameter("title");
        String content = req.getParameter("content");
        String categoryId = req.getParameter("categoryId");

        QnADTO dto = new QnADTO();
        dto.setUser_id(userId);
        dto.setQuestion_board_title(title);
        dto.setQuestion_board_content(content);
        dto.setQuestion_category_id(categoryId);

        QnADAO dao = new QnADAO();
        int result = dao.insert(dto);
        dao.close();
        


        if (result == 1) {
            resp.sendRedirect(req.getContextPath() + "/qna/list.do");
        } else {
            resp.setContentType("text/html; charset=UTF-8");
            resp.getWriter().println("<script>alert('등록 실패'); history.back();</script>");
        }
    }
}
