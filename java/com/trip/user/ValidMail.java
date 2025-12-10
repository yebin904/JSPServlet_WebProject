package com.trip.user;

import java.io.IOException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;

/**
 * 이메일 인증 번호를 확인하는 서블릿 (AJAX 호출 대응)
 * @author jsg
 * @version 1.0
 * @since 2025.10.24
 */
@WebServlet(value = "/user/validmail.do")
public class ValidMail extends HttpServlet {

	/**
	 * HTTP POST 요청을 처리합니다. (AJAX)
	 * 1. 요청 파라미터에서 사용자가 입력한 인증번호(`validNumber`)를 받습니다.
	 * 2. 세션에 저장된, 이메일로 발송했던 인증번호(`validNumber`)를 가져옵니다.
	 * 3. 두 번호가 일치하는지 비교합니다.
	 * 4. 일치하면 1, 불일치하면 0을 `result` 값으로 설정합니다.
	 * 5. 처리 결과를 JSON 객체(예: `{"result": 1}`) 형태로 클라이언트에 응답합니다.
	 *
	 * @param req  클라이언트가 서블릿에 보낸 HttpServletRequest 객체
	 * @param resp 서블릿이 클라이언트에 보내는 HttpServletResponse 객체
	 * @throws ServletException 서블릿 처리 중 예외 발생 시
	 * @throws IOException      요청 또는 응답 처리 중 I/O 예외 발생 시
	 */
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		//ValidMail.java
		//1. 데이터 가져오기(validNumber)
		//2. 세션값과 비교
		//3. 마무리
		
		String validNumber = req.getParameter("validNumber");
		
		int result = 0;
		
		if (req.getSession().getAttribute("validNumber").toString().equals(validNumber)) {
			result = 1;
		}
		
		resp.setContentType("application/json");
		
		JSONObject obj = new JSONObject();
		obj.put("result", result);
		
		resp.getWriter().print(obj.toString());
		resp.getWriter().close();		
		
	}

}