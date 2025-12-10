package com.trip.qna;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import com.trip.qna.model.CategoryDTO;
import com.trip.qna.model.QnADAO;
import com.trip.qna.model.QnADTO;

/**
 * Q&A 게시물 수정을 처리하는 서블릿
 * @author jsg
 * @version 1.0
 * @since 2025.10.24
 */
@WebServlet("/qna/edit.do")
public class Edit extends HttpServlet {
    /**
     * 서블릿의 직렬화 버전을 나타내는 고유 ID입니다.
     */
    private static final long serialVersionUID = 1L;

    /**
     * GET 요청을 처리하여 Q&A 게시물 수정 페이지를 렌더링합니다.
     * 게시물 ID를 통해 기존 게시물 정보를 불러오고, 사용자 권한을 확인합니다.
     * 카테고리 목록을 불러와 JSP로 전달합니다.
     * @param request HttpServletRequest 객체
     * @param response HttpServletResponse 객체
     * @throws ServletException 서블릿 예외 발생 시
     * @throws IOException 입출력 예외 발생 시
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String seq = request.getParameter("seq");
        HttpSession session = request.getSession();
        String sessionUser = (String) session.getAttribute("userId");

        QnADAO dao = new QnADAO();
        QnADTO dto = dao.view(seq);

        // ✅ 로그인 세션 없을 때 방지
        if (sessionUser == null) {
            dao.close();
            response.sendRedirect(request.getContextPath() + "/qna/list.do");
            return;
        }

        // ✅ 본인 글이 아니고 관리자도 아닐 경우 접근 차단
        if (!sessionUser.equals(dto.getUser_id())
                && !"admin".equals(session.getAttribute("role"))) {
            dao.close();
            response.sendRedirect(request.getContextPath() + "/qna/list.do");
            return;
        }

        // ✅ 카테고리 목록
        ArrayList<CategoryDTO> categories = dao.getCategoryList();
        dao.close();

        request.setAttribute("dto", dto);
        request.setAttribute("categories", categories);
        request.getRequestDispatcher("/WEB-INF/views/qna/edit.jsp").forward(request, response);
    }

    /**
     * POST 요청을 처리하여 Q&A 게시물 정보를 업데이트합니다.
     * 요청 파라미터에서 게시물 ID, 제목, 내용, 카테고리를 추출하여 데이터베이스에 반영합니다.
     * 수정 성공 시 해당 게시물 상세보기 페이지로 리다이렉트하고, 실패 시 에러 메시지를 표시합니다.
     * @param request HttpServletRequest 객체
     * @param response HttpServletResponse 객체
     * @throws ServletException 서블릿 예외 발생 시
     * @throws IOException 입출력 예외 발생 시
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        String seq = request.getParameter("seq");
        String title = request.getParameter("title");
        String content = request.getParameter("content");
        String category = request.getParameter("category");

        

        QnADTO dto = new QnADTO();
        dto.setQuestion_board_id(seq);
        dto.setQuestion_board_title(title);
        dto.setQuestion_board_content(content);
        dto.setQuestion_category_id(category);

        QnADAO dao = new QnADAO();
        int result = dao.edit(dto); // ✅ DAO에서 UPDATE 결과 반환
        dao.close();

        if (result == 1) {
            // ✅ 수정 성공 시 해당 글 상세보기로 이동
            response.sendRedirect(request.getContextPath() + "/qna/view.do?seq=" + seq);
        } else {
            // ✅ 실패 시 알림창 표시
            response.setContentType("text/html; charset=UTF-8");
            PrintWriter out = response.getWriter();
            out.println("<script>");
            out.println("alert('수정 실패');");
            out.println("history.back();");
            out.println("</script>");
            out.close();
        }
    }
}
