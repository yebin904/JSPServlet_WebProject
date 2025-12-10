package com.trip.reviewboard;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.trip.member.model.UserDTO;
import com.trip.reviewboard.model.ReviewBoardDTO;
import com.trip.reviewboard.model.ReviewCommentDTO;

/**
 * 리뷰 게시판 관련 요청을 처리하는 컨트롤러 클래스
 * @author jsg
 * @version 1.0
 * @since 2025.10.24
 */
@WebServlet({"/reviewboard/list.do", "/reviewboard/write.do", "/reviewboard/detail.do", "/reviewboard/delete.do", "/reviewboard/like.do", "/reviewboard/scrap.do", "/reviewboard/comment/add.do", "/reviewboard/comment/del.do"})
public class ReviewBoardController extends HttpServlet {

    /**
     * GET 요청을 처리합니다.
     * @param req HttpServletRequest 객체
     * @param resp HttpServletResponse 객체
     * @throws ServletException 서블릿 예외
     * @throws IOException 입출력 예외
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        
        String uri = req.getRequestURI();

        if (uri.endsWith("/list.do")) {
            showList(req, resp);
        } else if (uri.endsWith("/write.do")) {
            showWriteForm(req, resp);
        } else if (uri.endsWith("/detail.do")) {
            showDetail(req, resp);
        }
    }

    /**
     * POST 요청을 처리합니다.
     * @param req HttpServletRequest 객체
     * @param resp HttpServletResponse 객체
     * @throws ServletException 서블릿 예외
     * @throws IOException 입출력 예외
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        
        String uri = req.getRequestURI();

        if (uri.endsWith("/write.do")) {
            addPost(req, resp);
        } else if (uri.endsWith("/delete.do")) {
            deletePost(req, resp);
        } else if (uri.endsWith("/like.do")) {
            toggleLike(req, resp);
        } else if (uri.endsWith("/scrap.do")) {
            toggleScrap(req, resp);
        } else if (uri.endsWith("/comment/add.do")) {
            addComment(req, resp);
        } else if (uri.endsWith("/comment/del.do")) {
            deleteComment(req, resp);
        }
    }

    /**
     * 리뷰 게시판 목록을 보여줍니다.
     * @param req HttpServletRequest 객체
     * @param resp HttpServletResponse 객체
     * @throws ServletException 서블릿 예외
     * @throws IOException 입출력 예외
     */
    private void showList(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        
        String col = req.getParameter("col");
        String word = req.getParameter("word");

        Map<String, String> map = new HashMap<>();
        map.put("col", col == null ? "" : col);
        map.put("word", word == null ? "" : word);
        map.put("search", (word != null && !word.trim().isEmpty()) ? "y" : "n");

        int pageSize = 10;
        int nowPage = req.getParameter("page") == null ? 1 : Integer.parseInt(req.getParameter("page"));
        int begin = ((nowPage - 1) * pageSize) + 1;
        int end = begin + pageSize - 1;
        map.put("begin", String.valueOf(begin));
        map.put("end", String.valueOf(end));

        ReviewBoardService service = new ReviewBoardService();
        List<ReviewBoardDTO> list = service.list(map);
        int totalCount = service.getTotalCount(map);
        int totalPage = (int) Math.ceil((double) totalCount / pageSize);
        
        StringBuilder pagebar = new StringBuilder();
        int blockSize = 10;
        int loop = 1;
        int n = ((nowPage - 1) / blockSize) * blockSize + 1;
        
        if (n == 1) {
            pagebar.append("<a href='#!'>[이전]</a>");
        } else {
            pagebar.append(String.format("<a href='/trip/reviewboard/list.do?page=%d'>[이전]</a>", n - 1));
        }

        while (!(loop > blockSize || n > totalPage)) {
            if (n == nowPage) {
                pagebar.append(String.format(" <a href='#!' style='color:tomato;'>%d</a> ", n));
            } else {
                pagebar.append(String.format(" <a href='/trip/reviewboard/list.do?page=%d'>%d</a> ", n, n));
            }
            loop++;
            n++;
        }

        if (n > totalPage) {
            pagebar.append("<a href='#!'>[다음]</a>");
        } else {
            pagebar.append(String.format("<a href='/trip/reviewboard/list.do?page=%d'>[다음]</a>", n));
        }

        req.setAttribute("list", list);
        req.setAttribute("map", map);
        req.setAttribute("totalCount", totalCount);
        req.setAttribute("totalPage", totalPage);
        req.setAttribute("nowPage", nowPage);
        req.setAttribute("pagebar", pagebar.toString());
        
        service.close();

        RequestDispatcher dispatcher = req.getRequestDispatcher("/WEB-INF/views/reviewboard/list.jsp");
        dispatcher.forward(req, resp);
    }

    /**
     * 새 게시물 작성 폼을 보여줍니다.
     * @param req HttpServletRequest 객체
     * @param resp HttpServletResponse 객체
     * @throws ServletException 서블릿 예외
     * @throws IOException 입출력 예외
     */
    private void showWriteForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        RequestDispatcher dispatcher = req.getRequestDispatcher("/WEB-INF/views/reviewboard/write.jsp");
        dispatcher.forward(req, resp);
    }

    /**
     * 새 게시물을 추가합니다.
     * @param req HttpServletRequest 객체
     * @param resp HttpServletResponse 객체
     * @throws ServletException 서블릿 예외
     * @throws IOException 입출력 예외
     */
    private void addPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        HttpSession session = req.getSession();
        Object userObj = session.getAttribute("user");

        if (!(userObj instanceof UserDTO)) {
            resp.setContentType("text/html; charset=UTF-8");
            resp.getWriter().write("<script>alert('세션에 잘못된 사용자 정보가 있습니다.'); location.href='/trip/user/login.do';</script>");
            return;
        }

        UserDTO user = (UserDTO) userObj;

        String title = req.getParameter("title");
        String content = req.getParameter("content");

        ReviewBoardDTO dto = new ReviewBoardDTO();
        dto.setReview_board_title(title);
        dto.setReview_board_content(content);

        dto.setUser_id((int) user.getUserId());

        ReviewBoardService service = new ReviewBoardService();
        int result = service.addPost(dto);
        service.close();

        if (result > 0) {
            resp.sendRedirect(req.getContextPath() + "/reviewboard/list.do");
        } else {
            resp.setContentType("text/html; charset=UTF-8");
            resp.getWriter().write("<script>alert('글쓰기에 실패했습니다.'); history.back();</script>");
        }
    }
    
    /**
     * 게시물 상세 정보를 보여줍니다.
     * @param req HttpServletRequest 객체
     * @param resp HttpServletResponse 객체
     * @throws ServletException 서블릿 예외
     * @throws IOException 입출력 예외
     */
    private void showDetail(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int review_post_id = Integer.parseInt(req.getParameter("id"));
        HttpSession session = req.getSession();
        
        Object userObj = session.getAttribute("user");
        String userId = null;
        if (userObj instanceof UserDTO) {
            userId = String.valueOf(((UserDTO) userObj).getUserId());
        }

        ReviewBoardService service = new ReviewBoardService();
        ReviewBoardDTO dto = service.getPost(review_post_id);
        
        boolean isLiked = (userId != null) && service.isLiked(String.valueOf(review_post_id), userId);
        boolean isScrapped = (userId != null) && service.isScrapped(String.valueOf(review_post_id), userId);
        
        List<ReviewCommentDTO> comments = service.listComment(String.valueOf(review_post_id));
        
        service.close();

        req.setAttribute("dto", dto);
        req.setAttribute("isLiked", isLiked);
        req.setAttribute("isScrapped", isScrapped);
        req.setAttribute("comments", comments);

        RequestDispatcher dispatcher = req.getRequestDispatcher("/WEB-INF/views/reviewboard/detail.jsp");
        dispatcher.forward(req, resp);
    }
    
    /**
     * 게시물을 삭제합니다.
     * @param req HttpServletRequest 객체
     * @param resp HttpServletResponse 객체
     * @throws IOException 입출력 예외
     */
    private void deletePost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String review_post_id = req.getParameter("id");
        HttpSession session = req.getSession();
        Object userObj = session.getAttribute("user");

        if (!(userObj instanceof UserDTO)) {
            resp.setContentType("text/html; charset=UTF-8");
            resp.getWriter().write("<script>alert('세션에 잘못된 사용자 정보가 있습니다.'); location.href='/trip/user/login.do';</script>");
            return;
        }

        UserDTO user = (UserDTO) userObj;

        ReviewBoardService service = new ReviewBoardService();
        ReviewBoardDTO dto = service.getPost(Integer.parseInt(review_post_id));

        if (dto.getUser_id() != (int) user.getUserId()) {
            resp.setContentType("text/html; charset=UTF-8");
            resp.getWriter().write("<script>alert('권한이 없습니다.'); history.back();</script>");
            return;
        }

        int result = service.deletePost(review_post_id);
        service.close();
        if (result > 0) {
            resp.sendRedirect(req.getContextPath() + "/reviewboard/list.do");
        } else {
            resp.setContentType("text/html; charset=UTF-8");
            resp.getWriter().write("<script>alert('삭제에 실패했습니다.'); history.back();</script>");
        }
    }

    /**
     * 게시물의 좋아요 상태를 토글합니다.
     * @param req HttpServletRequest 객체
     * @param resp HttpServletResponse 객체
     * @throws IOException 입출력 예외
     */
    private void toggleLike(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String postId = req.getParameter("postId");
        HttpSession session = req.getSession();
        Object userObj = session.getAttribute("user");

        if (!(userObj instanceof UserDTO)) {
            resp.setContentType("application/json");
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            resp.getWriter().write("{\"error\": \"세션에 잘못된 사용자 정보가 있습니다.\"}");
            return;
        }

        UserDTO user = (UserDTO) userObj;
        String userId = String.valueOf(user.getUserId());
        
        ReviewBoardService service = new ReviewBoardService();
        boolean result = service.toggleLike(postId, userId);
        service.close();
        
        resp.setContentType("application/json");
        resp.getWriter().write(String.format("{\"result\": %b}", result));
    }

    /**
     * 게시물의 스크랩 상태를 토글합니다.
     * @param req HttpServletRequest 객체
     * @param resp HttpServletResponse 객체
     * @throws IOException 입출력 예외
     */
    private void toggleScrap(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String postId = req.getParameter("postId");
        HttpSession session = req.getSession();
        Object userObj = session.getAttribute("user");

        if (!(userObj instanceof UserDTO)) {
            resp.setContentType("application/json");
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            resp.getWriter().write("{\"error\": \"세션에 잘못된 사용자 정보가 있습니다.\"}");
            return;
        }

        UserDTO user = (UserDTO) userObj;

        String userId = String.valueOf(user.getUserId());
        
        ReviewBoardService service = new ReviewBoardService();
        boolean result = service.toggleScrap(postId, userId);
        service.close();
        
        resp.setContentType("application/json");
        resp.getWriter().write(String.format("{\"result\": %b}", result));
    }

    /**
     * 게시물에 새 댓글을 추가합니다.
     * @param req HttpServletRequest 객체
     * @param resp HttpServletResponse 객체
     * @throws ServletException 서블릿 예외
     * @throws IOException 입출력 예외
     */
    private void addComment(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String postId = req.getParameter("postId");
        String content = req.getParameter("content");
        HttpSession session = req.getSession();
        Object userObj = session.getAttribute("user");

        if (!(userObj instanceof UserDTO)) {
            resp.sendError(HttpServletResponse.SC_UNAUTHORIZED, "세션에 잘못된 사용자 정보가 있습니다.");
            return;
        }

        UserDTO user = (UserDTO) userObj;

        String userId = String.valueOf(user.getUserId());

        ReviewCommentDTO dto = new ReviewCommentDTO();
        dto.setReview_post_id(Integer.parseInt(postId));
        dto.setUser_id(Integer.parseInt(userId));
        dto.setReview_comment_content(content);

        ReviewBoardService service = new ReviewBoardService();
        int result = service.addComment(dto);
        service.close();

        resp.setContentType("application/json");
        resp.getWriter().write(String.format("{\"result\": %d}", result));
    }

    /**
     * 게시물에서 댓글을 삭제합니다.
     * @param req HttpServletRequest 객체
     * @param resp HttpServletResponse 객체
     * @throws ServletException 서블릿 예외
     * @throws IOException 입출력 예외
     */
    private void deleteComment(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String commentId = req.getParameter("commentId");
        String postId = req.getParameter("postId");
        HttpSession session = req.getSession();
        Object userObj = session.getAttribute("user");

        if (!(userObj instanceof UserDTO)) {
            resp.sendError(HttpServletResponse.SC_UNAUTHORIZED, "세션에 잘못된 사용자 정보가 있습니다.");
            return;
        }

        UserDTO user = (UserDTO) userObj;

        String userId = String.valueOf(user.getUserId());

        ReviewBoardService service = new ReviewBoardService();
        int result = service.deleteComment(commentId, postId);
        service.close();

        resp.setContentType("application/json");
        resp.getWriter().write(String.format("{\"result\": %d}", result));
    }
}