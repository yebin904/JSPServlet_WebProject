package com.trip.user;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;

/**
 * 이메일 인증 번호를 발송하는 서블릿 (AJAX 호출 대응)
 * @author jsg
 * @version 1.0
 * @since 2025.10.24
 */
@WebServlet(value = "/user/sendmail.do")
public class SendMail extends HttpServlet {

	/**
	 * HTTP POST 요청을 처리합니다. (AJAX)
	 * 1. 요청 파라미터에서 수신자 이메일 주소(`email`)를 받습니다.
	 * 2. 5자리의 랜덤 인증번호(`validNumber`)를 생성합니다. (10000 ~ 99999)
	 * 3. 생성된 인증번호를 세션에 "validNumber"라는 이름으로 저장합니다.
	 * 4. `MailSender`를 이용해 해당 이메일로 인증번호를 발송합니다.
	 * 5. 발송 성공 여부를 JSON 객체(예: `{"result": 1}`) 형태로 클라이언트에 응답합니다.
	 *
	 * @param req  클라이언트가 서블릿에 보낸 HttpServletRequest 객체
	 * @param resp 서블릿이 클라이언트에 보내는 HttpServletResponse 객체
	 * @throws ServletException 서블릿 처리 중 예외 발생 시
	 * @throws IOException      요청 또는 응답 처리 중 I/O 예외 발생 시
	 */
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		//SendMail.java
		//1. 데이터 가져오기(email)
		//2. 인증번호 생성 > 세션
		//3. 이메일 발송
		//4. 마무리
		
		String email = req.getParameter("email");
		
		//인증번호: 5자리 > 0 ~ 89999 + 10000 > 10000 ~ 99999
		Random rnd = new Random();
		int validNumber = rnd.nextInt(90000) + 10000;
		
		req.getSession().setAttribute("validNumber", validNumber);
		
		int result = 0;
		
		try {
			
			MailSender sender = new MailSender();
			
			Map<String,String> map = new HashMap<String,String>();
			
			map.put("email", email);
			map.put("validNumber", validNumber + "");
			
			sender.sendVerificationMail(map);	
			
			result = 1;
			
		} catch (Exception e) {
			System.out.println("SendMail.doGet()");
			e.printStackTrace();
		}
		
		resp.setContentType("application/json");
		
		JSONObject obj = new JSONObject();
		obj.put("result", result);
		
		resp.getWriter().print(obj.toString());
		resp.getWriter().close();		
		
	}

}