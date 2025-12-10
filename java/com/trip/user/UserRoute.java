package com.trip.user;

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

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.trip.user.model.UserRouteDAO;
import com.trip.user.model.UserRouteDTO;

/**
 * 사용자 정의 경로 목록을 조회하는 서블릿 (AJAX '더보기' 기능 포함)
 * (마이페이지 - 내 경로 목록)
 * @author jsg
 * @version 1.0
 * @since 2025.10.24
 */
@WebServlet("/user/userroute.do")
public class UserRoute extends HttpServlet {

    /**
	 * HTTP GET 요청을 처리합니다.
	 * `ajax=true` 파라미터 유무에 따라 일반 페이지 로드 또는 JSON 데이터를 응답합니다.
	 *
	 * 1. 세션에서 사용자 고유 번호(`seq`)를 가져옵니다.
	 * 2. `page` 파라미터를 받아 현재 페이지 번호(`nowPage`)를 설정합니다. (기본 1)
	 * 3. 페이징 처리를 위한 `begin`, `end` 값을 계산합니다. (페이지 당 5개)
	 * 4. `UserRouteDAO`를 통해 해당 페이지의 경로 목록(`list`)을 조회합니다.
	 * 5. **AJAX 요청일 경우 (`ajax=true`):**
	 * - 조회된 목록을 JSONArray로 변환하여 클라이언트에 응답합니다.
	 * 6. **일반 요청일 경우 (AJAX 아님):**
	 * - `UserRouteDAO`로 전체 페이지 수(`totalPage`)를 계산합니다.
	 * - 조회된 목록(`list`), `nowPage`, `totalPage`를 request 속성에 저장합니다.
	 * - `/WEB-INF/views/user/userroute.jsp` 페이지로 포워딩합니다.
	 *
	 * @param req  클라이언트가 서블릿에 보낸 HttpServletRequest 객체
	 * @param resp 서블릿이 클라이언트에 보내는 HttpServletResponse 객체
	 * @throws ServletException 서블릿 처리 중 예외 발생 시
	 * @throws IOException      요청 또는 응답 처리 중 I/O 예외 발생 시
	 */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        // --- 1. 기본 파라미터 및 세션 정보 설정 ---
        HttpSession session = req.getSession();
        String memberSeq = session.getAttribute("seq").toString();
        String ajax = req.getParameter("ajax"); // AJAX 요청인지 확인

        // --- 2. 페이지 번호 설정 ---
        int nowPage = 1; // 기본값은 1페이지
        String pageParam = req.getParameter("page");
        if (pageParam != null && !pageParam.equals("")) {
            nowPage = Integer.parseInt(pageParam);
        }

        // --- 3. 페이징 계산 ---
        int pageSize = 5; // 한 페이지에 보여줄 게시물 수
        int begin = ((nowPage - 1) * pageSize) + 1;
        int end = begin + pageSize - 1;

        // --- 4. 데이터베이스 조회용 Map 준비 ---
        Map<String, String> map = new HashMap<>();
        map.put("seq", memberSeq);
        map.put("begin", String.valueOf(begin));
        map.put("end", String.valueOf(end));
        map.put("search", "n"); 

        UserRouteDAO dao = new UserRouteDAO();
        List<UserRouteDTO> list = dao.UserRouteList(map);

        // --- 5. 요청 종류에 따라 분기 처리 ---
        if ("true".equals(ajax)) {
            // ** AJAX 요청일 경우: JSON 데이터만 응답 **

            resp.setContentType("application/json");
            resp.setCharacterEncoding("UTF-8");

            JSONArray jsonArray = new JSONArray();

            for (UserRouteDTO dto : list) {
                JSONObject routeObject = new JSONObject();
                // JSP의 자바스크립트에서 사용할 이름과 동일하게 key를 설정합니다.
                routeObject.put("seq", dto.getSeq());
                routeObject.put("userroutetitle", dto.getUserroutetitle());
                routeObject.put("userroutedays", dto.getUserroutedays());
                routeObject.put("userroutestartdate", dto.getUserroutestartdate());
                routeObject.put("userrouteenddate", dto.getUserrouteenddate());

                jsonArray.add(routeObject);
            }
            resp.getWriter().print(jsonArray.toJSONString());

        } else {
            // ** 일반 요청일 경우: JSP 페이지로 포워드 **

            // 전체 페이지 수 계산
            int totalCount = dao.getUserRouteTotalCount(map);
            int totalPage = (int) Math.ceil((double) totalCount / pageSize);

            // JSP로 데이터 전달
            req.setAttribute("list", list);
            req.setAttribute("nowPage", nowPage);
            req.setAttribute("totalPage", totalPage);

            RequestDispatcher dispatcher = req.getRequestDispatcher("/WEB-INF/views/user/userroute.jsp");
            dispatcher.forward(req, resp);
        }
    }
}