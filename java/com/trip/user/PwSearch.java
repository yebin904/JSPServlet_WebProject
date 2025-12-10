package com.trip.user;

import java.io.IOException;
import java.util.Random;

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
 * 사용자 비밀번호 찾기(재설정)를 처리하는 서블릿
 * @author jsg
 * @version 1.0
 * @since 2025.10.24
 */
@WebServlet(value = "/user/pwsearch.do")
public class PwSearch extends HttpServlet {

	/**
	 * HTTP GET 요청을 처리합니다.
	 * 비밀번호 찾기 페이지(`/WEB-INF/views/user/pwsearch.jsp`)로 포워딩합니다.
	 *
	 * @param req  클라이언트가 서블릿에 보낸 HttpServletRequest 객체
	 * @param resp 서블릿이 클라이언트에 보내는 HttpServletResponse 객체
	 * @throws ServletException 서블릿 처리 중 예외 발생 시
	 * @throws IOException      요청 또는 응답 처리 중 I/O 예외 발생 시
	 */
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {


		RequestDispatcher dispatcher = req.getRequestDispatcher("/WEB-INF/views/user/pwsearch.jsp");
		dispatcher.forward(req, resp);
	}
	
	/**
	 * HTTP POST 요청을 처리합니다. (AJAX 호출 대응)
	 * 1. 요청 파라미터에서 사용자 ID(`id`)와 이메일(`email`)을 받습니다.
	 * 2. 6자리의 임시 비밀번호(`strNumber`)를 생성합니다. (100000 ~ 999999)
	 * 3. `UserDAO`를 통해 ID와 이메일이 일치하는 사용자가 있는지 확인합니다 (`dao.PwSelect`).
	 * 4. 사용자가 존재하면 (`pw != null`):
	 * a. `UserDAO`를 통해 해당 사용자의 비밀번호를 임시 비밀번호로 업데이트합니다 (`dao.PwUpdate`).
	 * b. `MailSender`를 통해 임시 비밀번호를 이메일로 발송합니다 (`sender.sendPwVerificationMail`).
	 * c. 결과를 1(성공)로 설정합니다.
	 * 5. 사용자가 없으면 결과는 0(실패)으로 유지됩니다.
	 * 6. 처리 결과를 JSON 객체(예: `{"result": 1}`) 형태로 클라이언트에 응답합니다.
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
	    String id = req.getParameter("id");
	    
	    Random rnd = new Random();
		int validNumber = rnd.nextInt(900000) + 100000;
		String strNumber = String.valueOf(validNumber);

		
	    
	    int result = 0; 
	    
	    try {
	        UserDAO dao = new UserDAO();
	        String pw = dao.PwSelect(email, id); 
	        
	        if (pw != null) {
	        	dao.PwUpdate(email, id, strNumber);
	            MailSender sender = new MailSender();
	            sender.sendPwVerificationMail(email, strNumber); // 이메일 발송
	            result = 1; // 성공으로 처리합니다.
	        }
	        
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