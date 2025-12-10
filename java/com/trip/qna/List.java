package com.trip.qna;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.trip.qna.model.CategoryDAO;
import com.trip.qna.model.CategoryDTO;
import com.trip.qna.model.QnADAO;
import com.trip.qna.model.QnADTO;

/**
 * Q&A 게시물 목록을 조회하고 페이징을 처리하는 서블릿
 * @author jsg
 * @version 1.0
 * @since 2025.10.24
 */
@WebServlet(value = "/qna/list.do")
public class List extends HttpServlet {

    /**
     * GET 요청을 처리하여 Q&A 게시물 목록을 조회하고 페이징 및 검색 기능을 제공합니다.
     * 검색 조건(컬럼, 단어, 카테고리)에 따라 게시물을 필터링하고, 페이징 정보를 계산하여 JSP로 전달합니다.
     * @param req HttpServletRequest 객체
     * @param resp HttpServletResponse 객체
     * @throws ServletException 서블릿 예외 발생 시
     * @throws IOException 입출력 예외 발생 시
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        // 파라미터 수집
        String col = req.getParameter("col");
        String word = req.getParameter("word");
        String category = req.getParameter("category"); // 카테고리 선택

        // Map 세팅
        Map<String, String> map = new HashMap<>();
        map.put("col", col == null ? "" : col);
        map.put("word", word == null ? "" : word);
        map.put("category", category == null ? "" : category);

        // 검색 여부 판단
        if ((word != null && !word.trim().isEmpty()) || (category != null && !category.trim().isEmpty())) {
            map.put("search", "y");
        } else {
            map.put("search", "n");
        }

        HttpSession session = req.getSession();
        session.setAttribute("read", "n");

        // 페이징 계산
        int pageSize = 20;
        int nowPage = req.getParameter("page") == null ? 1 : Integer.parseInt(req.getParameter("page"));
        int begin = ((nowPage - 1) * pageSize) + 1;
        int end = begin + pageSize - 1;
        map.put("begin", String.valueOf(begin));
        map.put("end", String.valueOf(end));
        map.put("nowPage", String.valueOf(nowPage));

        // DAO 호출
        QnADAO dao = new QnADAO();
        int totalCount = dao.getTotalCount(map);
        int totalPage = (int) Math.ceil((double) totalCount / pageSize);
        map.put("totalCount", String.valueOf(totalCount));
        map.put("totalPage", String.valueOf(totalPage));

        ArrayList<QnADTO> list = new ArrayList<>(dao.list(map));
        dao.close();

        // 카테고리 목록
        CategoryDAO cdao = new CategoryDAO();
        ArrayList<CategoryDTO> categoryList = new ArrayList<>(cdao.list());
        cdao.close();

        // 페이지바 생성
        StringBuilder pagebar = new StringBuilder();
        int blockSize = 10;
        int loop = 1;
        int n = ((nowPage - 1) / blockSize) * blockSize + 1;
        String baseUrl = "/trip/qna/list.do";
        String query = "";
        if ("y".equals(map.get("search"))) {
            query = String.format("&col=%s&word=%s&category=%s",
                    col == null ? "" : col,
                    word == null ? "" : word,
                    category == null ? "" : category);
        }

        // 이전
        if (n == 1)
            pagebar.append("<a href='#!'>[이전 " + blockSize + "페이지]</a> ");
        else
            pagebar.append("<a href='" + baseUrl + "?page=" + (n - 1) + query + "'>[이전 " + blockSize + "페이지]</a> ");

        // 페이지 번호
        while (!(loop > blockSize || n > totalPage)) {
            if (n == nowPage)
                pagebar.append("<a href='#!' style='color:tomato;'>" + n + "</a> ");
            else
                pagebar.append("<a href='" + baseUrl + "?page=" + n + query + "'>" + n + "</a> ");
            loop++;
            n++;
        }

        // 다음
        if (n > totalPage)
            pagebar.append("<a href='#!'>[다음 " + blockSize + "페이지]</a> ");
        else
            pagebar.append("<a href='" + baseUrl + "?page=" + n + query + "'>[다음 " + blockSize + "페이지]</a> ");

        // JSP 전달
        req.setAttribute("list", list);
        req.setAttribute("map", map);
        req.setAttribute("pagebar", pagebar.toString());
        req.setAttribute("categoryList", categoryList);

        RequestDispatcher dispatcher = req.getRequestDispatcher("/WEB-INF/views/qna/list.jsp");
        dispatcher.forward(req, resp);
    }
}
