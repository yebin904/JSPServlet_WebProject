package com.trip.user;

import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.trip.board.model.BoardDAO;
import com.trip.board.model.BoardDTO;

/**
 * 사용자 댓글 활동 내역을 조회하고 페이징을 처리하는 서블릿
 * (마이페이지 - 내 댓글 목록)
 * @author jsg
 * @version 1.0
 * @since 2025.10.24
 */
@WebServlet(value = "/user/commentactivities.do")
public class CommentActivities extends HttpServlet {

	/**
	 * HTTP GET 요청을 처리합니다.
	 * 1. 세션에서 현재 로그인한 사용자의 고유 번호(`seq`)를 가져옵니다.
	 * 2. 검색어(`column`, `word`) 및 페이징(`page`) 파라미터를 받습니다.
	 * 3. 검색 조건과 페이징 정보를 `Map` 객체에 저장합니다.
	 * 4. `BoardDAO`를 통해 해당 사용자가 작성한 총 댓글 수(`totalCount`)를 조회합니다.
	 * 5. `totalCount`를 기반으로 총 페이지 수(`totalPage`)를 계산합니다.
	 * 6. `BoardDAO`를 통해 현재 페이지에 해당하는 댓글 목록(`list`)을 조회합니다.
	 * (DAO에서는 댓글이 달린 원본 게시물 정보를 가져오는 것으로 보입니다.)
	 * 7. 목록의 각 게시물 제목(`subject`)을 15자로 자르고 HTML 태그를 비활성화하는 등 데이터를 가공합니다.
	 * 8. 페이지 번호를 기반으로 페이지바(HTML)를 생성합니다.
	 * 9. 조회된 목록(`list`), 페이징 정보(`map`), 페이지바(`pagebar`)를 request 속성에 저장합니다.
	 * 10. 댓글 활동 내역 페이지(`/WEB-INF/views/user/commentactivities.jsp`)로 포워딩합니다.
	 *
	 * @param req  클라이언트가 서블릿에 보낸 HttpServletRequest 객체
	 * @param resp 서블릿이 클라이언트에 보내는 HttpServletResponse 객체
	 * @throws ServletException 서블릿 처리 중 예외 발생 시
	 * @throws IOException      요청 또는 응답 처리 중 I/O 예외 발생 시
	 */
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		//목록보기
		//- /toy/board/list.do
		//검색 결과보기
		//- /toy/board/list.do?column=subject&word=java
		
		String column = req.getParameter("column");
		String word = req.getParameter("word");
		String search = "n"; //목록보기(n), 검색하기(y)
		
		if ((column == null && word == null) || word.trim().equals("")) {
			search = "n";
		} else {
			search = "y";
		}
		
		HttpSession session = req.getSession();
		
		String seq = session.getAttribute("seq").toString();
		
		Map<String,String> map = new HashMap<String,String>();
		
		if ("y".equals(search)) {
		    // 검색 모드일 경우, 사용자가 어떤 제목(hotdeal_title, question_board_title 등)을
		    // 선택했더라도, 뷰의 공통 컬럼명인 "title"로 검색하도록 고정합니다.
		    map.put("column", "title"); 
		} else {
		    // 검색 모드가 아닐 때는 column 값이 null이므로 상관없습니다.
		    map.put("column", column);
		}
		map.put("word", word);
		map.put("search", search);
		map.put("seq", seq);
		
		
			
		
	
		//조회수 증가 방지
		session.setAttribute("read", "n");
		
		
		
		//페이징
		//- list.do > 1페이지
		//- list.do?page=1
		//- list.do?page=2
		
		String page = req.getParameter("page");

		int nowPage = 0;	//현재 페이지 번호
		int totalCount = 0; //총 게시물 수
		int pageSize = 10;	//한 페이지에서 보여줄 게시물 수
		int totalPage = 0;	//총 페이지 수
		int begin = 0;		//페이징 시작 위치
		int end = 0;		//페이지 끝 위치
		int n = 0;			//페이지바의 페이지 번호
		int loop = 0;		//페이지바 루프변수
		int blockSize = 10;	//페이지바의 페이지 수
		
		if (page == null || page.equals("")) {
			nowPage = 1;
		} else {
			nowPage = Integer.parseInt(page);
		}

		//- list.do?page=1 > where rnum between 1 and 10
		//- list.do?page=2 > where rnum between 11 and 20
		//- list.do?page=3 > where rnum between 21 and 30
		begin = ((nowPage - 1) * pageSize) + 1;
		end = begin + pageSize - 1;
		
		map.put("begin", begin + "");
		map.put("end", end + "");
		map.put("nowPage", nowPage + "");
			

		//List.java
		BoardDAO dao = new BoardDAO();
		
		
		//총 게시물 수?
		totalCount = dao.getAllCommentTotalCount(map);
		//System.out.println(totalCount);
		//262 / 10 = 26.2 > 27
		totalPage = (int)Math.ceil((double)totalCount / pageSize); 
		
		map.put("totalCount", totalCount + "");
		map.put("totalPage", totalPage + "");
		
		
		
		//해시 태그
		/*
		 * String tag = req.getParameter("tag"); map.put("tag", tag);
		 */
		
		
		java.util.List<BoardDTO> list = dao.totalCommentList(map);
		System.out.println(list);

		
		//데이터 조작
		Calendar now = Calendar.getInstance();
		String nowDate = String.format("%tF", now); //2025-09-17
		
		
		for (BoardDTO dto : list) {
			
			//날짜 조작 > 오늘 날짜?
			/*
			 * String regdate = dto.getRegdate();
			 * * if (regdate.startsWith(nowDate)) { //System.out.println("오늘 쓴 글");
			 * dto.setRegdate(regdate.substring(11)); } else {
			 * //System.out.println("과거 쓴 글"); dto.setRegdate(regdate.substring(0, 10)); }
			 */
			
			
			//제목 자르기
			String subject = dto.getSubject();
			if (subject.length() > 15) {
				subject = subject.substring(0, 15) + "..";
			}
			
			//태그 비활성화
			subject = subject.replace("<", "&lt;").replace(">", "&gt;");
			
			dto.setSubject(subject);			
			
		}//for
		
		
		
		
		//페이지바 생성
		String pagebar = "";
		
		/*
		for (int i=1; i<=totalPage; i++) { 
			pagebar += String.format(" <a href='/toy/board/list.do?page=%d'>%d</a> ", i, i); 
		}
		*/
		
		
		//list.do?page=1
		//[] 1 2 3 4 5 6 7 8 9 10 []
		
		//list.do?page=2
		//[] 1 2 3 4 5 6 7 8 9 10 []
		
		//list.do?page=10
		//[] 1 2 3 4 5 6 7 8 9 10 []
		
		//list.do?page=11
		//[] 11 12 13 14 15 16 17 18 19 20 []
		
		//list.do?page=15
		//[] 11 12 13 14 15 16 17 18 19 20 []
		
		
		loop = 1; //루프 변수(10바퀴)
		n = ((nowPage - 1) / blockSize) * blockSize + 1; //페이지 번호
		
		
		//이전 10페이지
		if (n == 1) {
			pagebar += String.format(" <a href='#!'>이전</a> ", blockSize);
		} else {		
			pagebar += String.format(" <a href='/trip/user/commentactivities.do?page=%d'>이전</a> ", n - 1, blockSize);
		}
		
		
		while (!(loop > blockSize || n > totalPage)) {
			
			if (n == nowPage) {
				pagebar += String.format(" <a href='#!' style='color:tomato;' class='page'>%d</a> ", n);
			} else {			
				pagebar += String.format(" <a href='/trip/user/commentactivities.do?page=%d' class='page'>%d</a> ", n, n);
			}
			
			loop++;
			n++;
		}
		
		
		//다음 10페이지
		if (n >= totalPage) {
			pagebar += String.format(" <a href='#!'>다음</a> ", blockSize);
		} else {		
			pagebar += String.format(" <a href='/trip/user/commentactivities.do?page=%d'>다음</a> ", n, blockSize);
		}
		
			
		req.setAttribute("list", list);
		req.setAttribute("map", map);
		req.setAttribute("pagebar", pagebar);

		RequestDispatcher dispatcher = req.getRequestDispatcher("/WEB-INF/views/user/commentactivities.jsp");
		dispatcher.forward(req, resp);
	}

		
}