package com.trip.qna;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.trip.qna.model.QnACommentDAO;
import com.trip.qna.model.QnACommentDTO;
import com.trip.qna.model.QnADAO;
import com.trip.qna.model.QnADTO;

/**
 * Q&A 게시물 상세보기를 처리하는 서블릿
 * @author jsg
 * @version 1.0
 * @since 2025.10.24
 */
@WebServlet("/qna/view.do")
public class View extends HttpServlet {

    /**
     * GET 요청을 처리하여 Q&A 게시물의 상세 내용을 조회합니다.
     * 게시물 ID를 통해 게시물 정보, 댓글 목록을 불러오고 조회수를 증가시킵니다.
     * 사용자 권한(작성자 여부, 관리자 여부)을 확인하여 JSP로 전달합니다.
     * @param req HttpServletRequest 객체
     * @param resp HttpServletResponse 객체
     * @throws ServletException 서블릿 예외 발생 시
     * @throws IOException 입출력 예외 발생 시
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

    	HttpSession session = req.getSession();
    	
    	// *** [테스트용 코드] 시작: 로그인 없이 권한 테스트를 위해 userId와 isAdmin 임시 설정 ***
    	// (실제 서비스에서는 로그인 기능을 사용하고 이 블록은 제거해야 합니다.)
        // 1. userId를 '1'로 설정 (로그인한 사용자의 ID)
        // 2. isAdmin을 true로 설정 (관리자 권한 부여)
    	if(session.getAttribute("userId") == null) {
    	    session.setAttribute("userId", "1"); // <<< 테스트용 ID 고정
    	    session.setAttribute("isAdmin", false); // <<< 관리자 권한 고정
    	}
    	// *** [테스트용 코드] 끝 ***

    	String userId = (String) session.getAttribute("userId"); // 로그인한 회원 아이디
        // isAdmin이 null일 경우 false로 처리.
        boolean isAdmin = session.getAttribute("isAdmin") != null && (Boolean) session.getAttribute("isAdmin"); // 관리자 여부
    	
        String seq = req.getParameter("seq");
        if(seq == null || seq.trim().isEmpty()) {
            resp.sendRedirect("/trip/qna/list.do");
            return;
        }

        QnADAO dao = new QnADAO();
        
        // dao.get(seq)를 사용자 요청에 따라 dao.view(seq)로 수정합니다.
        QnADTO dto = dao.view(seq); 
        
        if(dto == null) {
            resp.sendRedirect("/trip/qna/list.do");
            return;
        }

        // 조회수 증가
        dao.updateReadCount(seq);
        
        // 작성자 여부 확인: 현재 로그인 유저 ID와 게시글 작성자 ID가 같은지 확인
        boolean isOwner = userId != null && userId.equals(dto.getUser_id());
        
        
        // 댓글 목록
        QnACommentDAO cdao = new QnACommentDAO();
        java.util.List<QnACommentDTO> comments = cdao.list(seq);

        cdao.close();
        dao.close();
        
        req.setAttribute("dto", dto);
        // JSP에서 권한 확인을 위해 두 플래그를 모두 전달
        req.setAttribute("isOwner", isOwner); 
        req.setAttribute("isAdmin", isAdmin); // 관리자 권한 플래그 추가
        
        req.setAttribute("comments", comments);
        
        RequestDispatcher rd = req.getRequestDispatcher("/WEB-INF/views/qna/view.jsp");
        rd.forward(req, resp);
    }
}