package com.trip.user;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;

import com.trip.member.model.UserDAO;
import com.trip.member.model.UserDTO;

/**
 * 사용자 ID 찾기를 처리하는 서블릿
 * @author jsg
 * @version 1.0
 * @since 2025.10.24
 */
@WebServlet(value = "/user/idsearch.do")
public class IdSearch extends HttpServlet {

	/**
	 * HTTP GET 요청을 처리합니다.
	 * 아이디 찾기 페이지(`/WEB-INF/views/user/idsearch.jsp`)로 포워딩합니다.
	 *
	 * @param req  클라이언트가 서블릿에 보낸 HttpServletRequest 객체
	 * @param resp 서블릿이 클라이언트에 보내는 HttpServletResponse 객체
	 * @throws ServletException 서블릿 처리 중 예외 발생 시
	 * @throws IOException      요청 또는 응답 처리 중 I/O 예외 발생 시
	 */
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {


		RequestDispatcher dispatcher = req.getRequestDispatcher("/WEB-INF/views/user/idsearch.jsp");
		dispatcher.forward(req, resp);
	}
	
	/**
	 * HTTP POST 요청을 처리합니다. (AJAX 호출 대응)
	 * 1. 요청 파라미터에서 사용자 이름(`name`)과 이메일(`email`)을 받습니다.
	 * 2. `UserDAO`를 통해 해당 정보와 일치하는 사용자 ID를 조회합니다.
	 * 3. ID를 찾은 경우(`id != null`), `MailSender`를 통해 해당 이메일로 ID를 발송하고 결과를 1(성공)로 설정합니다.
	 * 4. ID를 찾지 못한 경우, 결과는 0(실패)으로 유지됩니다.
	 * 5. 처리 결과를 JSON 객체(예: `{"result": 1}`) 형태로 클라이언트에 응답합니다.
	 *
	 * @param req  클라이언트가 서블릿에 보낸 HttpServletRequest 객체
	 * @param resp 서블릿이 클라이언트에 보내는 HttpServletResponse 객체
	 * @throws ServletException 서블릿 처리 중 예외 발생 시
	 * @throws IOException      요청 또는 응답 처리 중 I/O 예외 발생 시
	 */
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

	    // 1. 클라이언트가 보낸 이름과 이메일을 받습니다.
	    String email = req.getParameter("email");
	    String name = req.getParameter("name");
	    
	    // 2. 결과를 담을 변수를 선언합니다. (0: 실패, 1: 성공)
	    int result = 0; 
	    
	    try {
	        UserDAO dao = new UserDAO();
	        String id = dao.idSelect(email, name); // DB에서 id를 조회합니다.
	        
	        // 3. 아이디를 찾았을 경우에만 메일을 보냅니다.
	        if (id != null) {
	            MailSender sender = new MailSender();
	            sender.sendIdVerificationMail(email, id); // 이메일 발송
	            result = 1; // 성공으로 처리합니다.
	        }
	        // 아이디를 못 찾으면 result는 초기값 0을 그대로 유지합니다.
	        
	    } catch (Exception e) {
	        System.out.println("IdSearch.doPost() error");
	        e.printStackTrace();
	    }
	    
	    // 4. 성공이든 실패든 결과를 JSON으로 응답합니다.
	    resp.setContentType("application/json");
	    resp.setCharacterEncoding("UTF-8");
	    
	    JSONObject obj = new JSONObject();
	    obj.put("result", result);
	    
	    resp.getWriter().print(obj.toString());
	    resp.getWriter().close();        
	}
}