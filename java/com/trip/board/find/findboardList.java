package com.trip.board.find;

import com.trip.board.find.model.findboardDAO;
import com.trip.board.find.model.findboardDTO;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

/**
 * 동행찾기 게시판 목록을 조회하는 서블릿
 * @author jsg
 * @version 1.0
 * @since 2025.10.24
 */
@WebServlet("/findboard/list.do")
public class findboardList extends HttpServlet {
    /**
     * GET 요청을 처리하여 동행찾기 게시물 목록 페이지를 표시합니다.
     * 검색 파라미터(searchType, searchKeyword)와 페이지 번호(page)를 받아,
     * 해당 조건에 맞는 게시물 목록(dao.getList)과 페이징 정보(dao.getTotalCount)를 조회합니다.
     * 조회된 데이터를 기반으로 페이징 HTML(paging)을 생성합니다.
     * 조회된 목록(list), 검색/페이징 정보(map), 페이징 HTML(paging)을
     * request에 담아 `/WEB-INF/views/findboard/list.jsp`로 포워드합니다.
     *
     * @param req  클라이언트의 요청 정보를 담은 HttpServletRequest 객체 (searchType, searchKeyword, page)
     * @param resp 클라이언트에게 응답을 보내기 위한 HttpServletResponse 객체
     * @throws ServletException 서블릿 처리 중 오류가 발생한 경우
     * @throws IOException      입출력 오류가 발생한 경우
     * @see /WEB-INF/views/findboard/list.jsp
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        
        // --- 1. 파라미터 수집 ---
        String searchType = req.getParameter("searchType");
        String searchKeyword = req.getParameter("searchKeyword");
        String page = req.getParameter("page");

        // --- 2. 페이징 처리 ---
        int currentPage = 1; // 현재 페이지 번호
        if (page != null && !page.equals("")) {
            currentPage = Integer.parseInt(page);
        }

        int postPerPage = 10; // 한 페이지당 보여줄 게시글 수
        int end = currentPage * postPerPage;
        int start = end - postPerPage + 1;

        // --- 3. 데이터 조회 ---
        // HashMap에 페이징과 검색에 필요한 모든 정보를 담는다.
        HashMap<String, String> map = new HashMap<>();
        map.put("start", String.valueOf(start));
        map.put("end", String.valueOf(end));
        map.put("searchType", searchType);
        map.put("searchKeyword", searchKeyword);

        findboardDAO dao = new findboardDAO();
        
        // DAO에 HashMap을 전달하여 페이징 처리된 목록을 가져온다.
        List<findboardDTO> list = dao.getList(map);
        
        // 전체 게시글 수를 가져온다. (검색 조건 포함)
        int totalCount = dao.getTotalCount(map);

        // --- 4. 페이징 HTML 생성 ---
        // 전체 페이지 수 계산
        int totalPage = (int) Math.ceil((double) totalCount / postPerPage);
        int pageSize = 10; // 한 번에 보여줄 페이지 번호 개수
        int startPage = ((currentPage - 1) / pageSize) * pageSize + 1;
        int endPage = startPage + pageSize - 1;

        if (endPage > totalPage) {
            endPage = totalPage;
        }

        StringBuilder sb = new StringBuilder();

        // [이전] 버튼
        if (startPage > 1) {
            sb.append(String.format("<a href='/trip/findboard/list.do?page=%d&searchType=%s&searchKeyword=%s'>이전</a>",
                                    startPage - 1, searchType != null ? searchType : "", searchKeyword != null ? searchKeyword : ""));
        }

        // 페이지 번호
        for (int i = startPage; i <= endPage; i++) {
            if (i == currentPage) {
                sb.append(String.format("<a href='#!' class='active'>%d</a>", i));
            } else {
                sb.append(String.format("<a href='/trip/findboard/list.do?page=%d&searchType=%s&searchKeyword=%s'>%d</a>",
                                        i, searchType != null ? searchType : "", searchKeyword != null ? searchKeyword : "", i));
            }
        }

        // [다음] 버튼
        if (endPage < totalPage) {
            sb.append(String.format("<a href='/trip/findboard/list.do?page=%d&searchType=%s&searchKeyword=%s'>다음</a>",
                                    endPage + 1, searchType != null ? searchType : "", searchKeyword != null ? searchKeyword : ""));
        }
        
        // --- 5. JSP로 데이터 전달 ---
        // HashMap에 페이징 HTML과 현재 페이지 정보도 추가해서 전달한다.
        map.put("totalCount", String.valueOf(totalCount));
        map.put("page", String.valueOf(currentPage));

        req.setAttribute("list", list);
        req.setAttribute("map", map);
        req.setAttribute("paging", sb.toString());
        
        RequestDispatcher dispatcher = req.getRequestDispatcher("/WEB-INF/views/findboard/list.jsp");
        dispatcher.forward(req, resp);
    }
}
