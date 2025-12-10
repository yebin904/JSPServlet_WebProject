package com.trip.notice;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.trip.notice.model.NoticeDAO;
import com.trip.notice.model.NoticeDTO;

/**
 * 공지사항 목록을 조회하고 페이징을 처리하는 서블릿(Controller).
 * '/list.do' 요청을 받아 공지사항 목록 페이지(list.jsp)로 포워딩합니다.
 * * @author jsg
 * @version 1.0
 * @since 2025.10.24
 */
@WebServlet(value = "/list.do")
public class List extends HttpServlet {

	/**
	 * HTTP GET 요청을 처리하여 공지사항 목록을 조회하고 페이징을 처리합니다.
	 * * <p>
	 * 1. 검색 조건(column, word)과 현재 페이지(page) 파라미터를 받습니다.
	 * 2. 파라미터를 Map에 저장하여 DAO에 전달할 준비를 합니다.
	 * 3. 페이징 처리를 위한 계산(begin, end)을 수행합니다.
	 * 4. NoticeDAO를 호출하여 해당 페이지의 목록(list)과 총 게시물 수(totalCount)를 가져옵니다.
	 * 5. 목록 데이터를 가공합니다 (새 글 아이콘, 날짜 형식 변경, 제목 자르기).
	 * 6. 페이지바(pagebar) HTML을 생성합니다.
	 * 7. 조회된 데이터와 페이징 정보를 HttpServletRequest에 저장(setAttribute)합니다.
	 * 8. /WEB-INF/views/notice/list.jsp 페이지로 포워딩합니다.
	 * </p>
	 * * @param req 클라이언트의 HttpServletRequest 객체. (검색 조건 및 페이지 정보 포함)
	 * @param resp 클라이언트에게 응답을 보낼 HttpServletResponse 객체.
	 * @throws ServletException 서블릿 관련 에러 발생 시.
	 * @throws IOException 입출력 관련 에러 발생 시.
	 */
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		//List.java
		
		// 1. 파라미터 수신 (검색어)
		String column = req.getParameter("column"); 
		String word = req.getParameter("word");
		String search = "n"; //목록보기(n), 검색하기(y)
		
		// 검색어가 있을 경우 search 플래그를 'y'로 설정
		if (column != null && word != null && !word.trim().equals("")) {
			search = "y";
		}
		
		// 2. DAO에 전달할 파라미터를 Map에 저장
		Map<String, String> map = new HashMap<String, String>();
		map.put("column", column);
		map.put("word", word);
		map.put("search", search);
		
		// 3. 페이징 처리
		String page = req.getParameter("page");
		int nowPage = (page == null || page.equals("")) ? 1 : Integer.parseInt(page); // 현재 페이지 번호
		int pageSize = 10; // 한 페이지에 보여줄 게시물 수
		int begin = ((nowPage - 1) * pageSize) + 1; // ROWNUM 시작 번호
		int end = begin + pageSize - 1; // ROWNUM 끝 번호
		
		map.put("begin", String.valueOf(begin));
		map.put("end", String.valueOf(end));
		
		// 4. DAO 호출
		NoticeDAO dao = new NoticeDAO();
		ArrayList<NoticeDTO> list = dao.getList(map); // 현재 페이지 목록 가져오기
		
		// 총 게시물 수 및 총 페이지 수 계산
		int totalCount = dao.getTotalCount(map); // 총 게시물 수
		int totalPage = (int) Math.ceil((double) totalCount / pageSize); // 총 페이지 수
		
		// 5. 데이터 가공 (JSP에서 표현하기 좋게)
		Calendar now = Calendar.getInstance();
		String nowDate = String.format("%tF", now); // "YYYY-MM-DD" 형식
		
		for (NoticeDTO dto : list) {
			// 오늘 쓴 글 new 아이콘 표시
			if (dto.getNotice_regdate().startsWith(nowDate)) {
				dto.setIsnew("y");
			}
			
			// 날짜 형식 변경 (YYYY-MM-DD)
			dto.setNotice_regdate(dto.getNotice_regdate().substring(0, 10));
			
			// 제목 자르기 (30자 초과 시 "...")
			if (dto.getNotice_header().length() > 30) {
				dto.setNotice_header(dto.getNotice_header().substring(0, 30) + "...");
			}
		}
		
		// 6. 페이지바 생성
		StringBuilder pagebar = new StringBuilder();
		int blockSize = 10; // 한 번에 보여줄 페이지 번호 개수
		int n = ((nowPage - 1 ) / blockSize) * blockSize + 1; // 페이지바 시작 번호
		
		// 이전 페이지
		if (n==1) {
			// 첫 번째 블록일 경우
			pagebar.append("<a href='#1'>[이전]</a>");
		} else {
			// 이전 블록으로 이동
			pagebar.append(String.format("<a href='/trip/notice/list.do?page=%d'>[이전]></a>", n - 1));
		}
		
		// 페이지 번호
		for (int i = 0; i < blockSize && n <= totalPage; i++) {
			if (n == nowPage) {
				// 현재 페이지
				pagebar.append(String.format("<a href='#!' class='active'>%d</a>",n));
			} else {
				// 다른 페이지
				pagebar.append(String.format("<a href='/trip/notice/list.do?page=%d'>%d</a>", n, n));
			}
			n++;
		}
		
		// 다음 페이지
		if (n > totalPage) {
			// 마지막 블록일 경우
			pagebar.append("<a href='#!'>[다음]</a>");
		} else {
			// 다음 블록으로 이동
			pagebar.append(String.format("<a href='/trip/notice/list.do?page=%d'>[다음]</a>", n));
		}
		
		// 7. JSP에게 데이터 전달하기 (request 객체에 저장)
		req.setAttribute("list", list); // 공지사항 목록
		req.setAttribute("map", map); // 검색 조건
		req.setAttribute("totalCount", totalCount); // 총 게시물 수
		req.setAttribute("totalPage", totalPage); // 총 페이지 수
		req.setAttribute("nowPage", nowPage); // 현재 페이지 번호
		req.setAttribute("pagebar", pagebar.toString()); // 페이지바 HTML
		
		// 8. JSP로 포워딩
		RequestDispatcher dispatcher = req.getRequestDispatcher("/WEB-INF/views/notice/list.jsp");
		dispatcher.forward(req, resp);
	}

}
