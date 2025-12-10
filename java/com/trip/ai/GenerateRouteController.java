package com.trip.ai;

import java.io.BufferedReader;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.gson.Gson;
import com.trip.ai.model.RouteDTO;
import com.trip.ai.model.TripPreferencesDTO;
import com.trip.ai.service.AiService;

/**
 * AI 기반 여행 경로 생성을 처리하는 서블릿
 * @author jsg
 * @version 1.0
 * @since 2025.10.24
 */
@WebServlet(value = "/ai/generate.do")
public class GenerateRouteController extends HttpServlet {

	/**
	 * AI 서비스 객체
	 */
	private final AiService aiService = new AiService();
	/**
	 * JSON 직렬화/역직렬화를 위한 Gson 객체
	 */	
	private final Gson gson = new Gson();
	
	/**
	 * AI 여행 경로 생성 요청을 처리합니다.
	 * 클라이언트로부터 여행 선호도 데이터를 받아 AI 서비스를 통해 경로를 생성하고 저장합니다.
	 * @param req HttpServletRequest 객체
	 * @param resp HttpServletResponse 객체
	 * @throws ServletException 서블릿 예외
	 * @throws IOException 입출력 예외
	 */
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		req.setCharacterEncoding("UTF-8");
		
		// 1. AJAX 요청 body 데이터 읽기 및 DTO 변환
		StringBuilder sb = new StringBuilder();
		BufferedReader reader = req.getReader();
		String line;
		while((line = reader.readLine()) != null) {
			sb.append(line);
		}
		String jsonBody = sb.toString();
		TripPreferencesDTO preferences = gson.fromJson(jsonBody, TripPreferencesDTO.class);
		
		System.out.println("--AJAX 데이터 수신 확인--");
		System.out.println(preferences.toString());
		System.out.println("----------------------------------");
		
		HttpSession session = req.getSession();
        String userId = (String) session.getAttribute("id");
        
        if (userId == null) {
            userId = "1"; // 테스트용 임시 ID
        }
        
        /*
         * TODO: [다음 단계] 사용자 질문 저장 기능
         * 아래 주석을 해제하여 사용자 답변 저장 기능을 활성화할 수 있습니다.
         * 이 기능을 사용하려면 AiDAO의 saveConversationAndAnswers 메서드 내부의
         * question_id와 option_id를 실제 DB 값과 연동하는 로직을 먼저 구현해야 합니다.
        */
        // Long conversationId = aiService.saveConversationAndAnswers(preferences, userId);
        Long conversationId = null; // 우선 null로 설정하여 AI 루트 저장부터 구현
		
		// 2. AiService를 호출하여 AI 루트 생성
		RouteDTO resultRoute = aiService.createAiRoute(preferences);
		
        // 3. 생성된 결과를 DB에 저장
        RouteDTO savedRoute = aiService.saveAiRoute(resultRoute, userId, conversationId);
        
        // 4. DB에 저장 완료된 데이터를 Session에 저장
        session.setAttribute("resultRoute", savedRoute);
		
        // 5. 클라이언트에게 성공 신호와 함께 이동할 URL 응답
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        
        String jsonResponse = "{\"success\": true, \"redirectUrl\": \"/trip/ai/result.do\"}";
        resp.getWriter().print(jsonResponse);
	}
}
