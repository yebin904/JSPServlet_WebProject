package com.trip.user;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;

/**
 * 이메일 인증 번호를 세션에서 삭제하는 서블릿 (AJAX 호출 대응)
 * @author jsg
 * @version 1.0
 * @since 2025.10.24
 */
@WebServlet(value = "/user/delmail.do")
public class DelMail extends HttpServlet {

	/**
	 * HTTP POST 요청을 처리합니다. (AJAX)
	 * 1. 현재 세션에서 "validNumber" (이메일 인증번호) 속성을 제거합니다.
	 * 2. 처리가 완료되었음을 알리는 JSON 객체(예: `{"result": 1}`)를 클라이언트에 응답합니다.
	 * (이 서블릿은 클라이언트가 인증번호 입력을 포기하거나 완료했을 때 호출되어 세션을 정리하는 용도로 보입니다.)
	 *
	 * @param req  클라이언트가 서블릿에 보낸 HttpServletRequest 객체
	 * @param resp 서블릿이 클라이언트에 보내는 HttpServletResponse 객체
	 * @throws ServletException 서블릿 처리 중 예외 발생 시
	 * @throws IOException      요청 또는 응답 처리 중 I/O 예외 발생 시
	 */
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		//DelMail.java
		
		req.getSession().removeAttribute("validNumber");
		
		resp.setContentType("application/json");
		
		JSONObject obj = new JSONObject();
		obj.put("result", 1);
		
		resp.getWriter().print(obj.toString());
		resp.getWriter().close();
		
	}

}