package com.trip.board;

import java.io.IOException;
import java.util.HashMap;
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
import org.json.simple.parser.JSONParser;

import com.oreilly.servlet.MultipartRequest;
import com.oreilly.servlet.multipart.DefaultFileRenamePolicy;
import com.trip.board.model.BoardDAO;
import com.trip.board.model.BoardDTO;

/**
 * 게시물 추가를 처리하는 서블릿
 * @author jsg
 * @version 1.0
 * @since 2025.10.24
 */
@WebServlet(value = "/board/add.do")
public class Add extends HttpServlet {

	/**
	 * GET 요청을 처리하여 새 게시물 작성 폼 페이지로 이동합니다.
	 * `/WEB-INF/views/board/add.jsp`로 포워드합니다.
	 *
	 * @param req  클라이언트의 요청 정보를 담은 HttpServletRequest 객체
	 * @param resp 클라이언트에게 응답을 보내기 위한 HttpServletResponse 객체
	 * @throws ServletException 서블릿 처리 중 오류가 발생한 경우
	 * @throws IOException      입출력 오류가 발생한 경우
	 * @see /WEB-INF/views/board/add.jsp
	 */
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		//Add.java
		

		RequestDispatcher dispatcher = req.getRequestDispatcher("/WEB-INF/views/board/add.jsp");
		dispatcher.forward(req, resp);
	}
	
	/**
	 * POST 요청을 처리하여 새 게시물을 등록합니다.
	 * `MultipartRequest`를 사용하여 폼 데이터(제목, 내용, 카테고리 등)와 첨부 파일(img)을 처리합니다.
	 * 세션에서 사용자 ID를 가져와 DTO에 설정한 후, DB에 게시물(dao.add)과 이미지 정보(dao.addimg)를 저장합니다.
	 * (주석 처리된 해시태그 로직이 포함되어 있습니다.)
	 * 성공 시 게시물 목록(`/trip/board/list.do`)으로 리다이렉트합니다.
	 *
	 * @param req  클라이언트의 요청 정보를 담은 HttpServletRequest 객체 (multipart/form-data)
	 * @param resp 클라이언트에게 응답을 보내기 위한 HttpServletResponse 객체
	 * @throws ServletException 서블릿 처리 중 오류가 발생한 경우
	 * @throws IOException      입출력 오류가 발생한 경우
	 */
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
	
		//AddOk.java 역할
		//req.setCharacterEncoding("UTF-8"); > 필터를 통해서 구현
		
		//req > multi
		MultipartRequest multi = new MultipartRequest(
							req,
							req.getServletContext().getRealPath("/asset/place"),
							1024 * 1024 * 30,
							"UTF-8",
							new DefaultFileRenamePolicy()
						);
		
		System.out.println(req.getServletContext().getRealPath("/asset/place"));
		
		
		
		
		
		
		String subject = multi.getParameter("subject");
		String content = multi.getParameter("content");
		String img = multi.getFilesystemName("img");
		String status = multi.getParameter("status");
		String category = multi.getParameter("category");
		String itemname = multi.getParameter("itemname");
		String price = multi.getParameter("price");
		String url = multi.getParameter("url");
		//String hashtag = multi.getParameter("hashtag");
		//String secret = multi.getParameter("secret"); //1 or null
		//if (secret == null) secret = "0";
		
		//String notice = multi.getParameter("notice"); //1 or null
		//if (notice == null) notice = "0";
		
		//System.out.println(subject);
		//System.out.println(content);
		//System.out.println(hashtag == null);
		//System.out.println(hashtag.equals(""));
		
		//[{"value":"게시판"},{"value":"태그"},{"value":"JSP"},{"value":"프로젝트"}]
		
		
		BoardDAO dao = new BoardDAO();
		
		BoardDTO dto = new BoardDTO();
		dto.setStatus(status);
		dto.setCategory(category);
		dto.setSubject(subject);
		dto.setContent(content);
		dto.setImg(img);
		dto.setPrice(price);
		dto.setItemName(itemname);
		dto.setUrl(url);
		//dto.setSecret(secret);
		//dto.setNotice(notice);
		
		System.out.println(dto);
		
		HttpSession session = req.getSession();
		dto.setId(session.getAttribute("id").toString());
		dto.setSeq(session.getAttribute("seq").toString());
		
		int result = dao.add(dto);
		String recent = dao.recent(dto);
		int imgResult = dao.addimg(dto, recent);
		String bseq = dao.getBseq();
		
		
		/*
		 * //해시태그 if (!hashtag.equals("")) {
		 *
		 * //[{"value":"게시판"},{"value":"태그"},{"value":"JSP"}] try {
		 *
		 * JSONParser parser = new JSONParser(); JSONArray arr =
		 * (JSONArray)parser.parse(hashtag);
		 *
		 * for (Object obj : arr) {
		 *
		 * JSONObject tag = (JSONObject)obj; String tagName =
		 * tag.get("value").toString(); //System.out.println(tagName);
		 *
		 * //해시태그 > DB 추가 String hseq = dao.addHashtag(tagName);
		 *
		 * //연결 테이블 > 관계 추가 Map<String,String> map = new HashMap<String,String>();
		 * map.put("bseq", bseq); map.put("hseq", hseq);
		 *
		 * dao.addTagging(map);
		 *
		 * }
		 *
		 * } catch (Exception e) { System.out.println("Add.doPost()");
		 * e.printStackTrace(); } 
		 *
		 * }
		 */
		
		if (result > 0 || imgResult > 0) {
			resp.sendRedirect("/trip/board/list.do");
		} else {
			resp.getWriter().print("<html><meta charset='UTF-8'><script>alert('failed');history.back();</script></html>");
			resp.getWriter().close();
		}
		
	}
	

}











}
