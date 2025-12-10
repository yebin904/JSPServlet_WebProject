package com.trip.ai.service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

import org.json.JSONArray;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.trip.ai.model.AiDAO;
import com.trip.ai.model.RouteDTO;
import com.trip.ai.model.TripPreferencesDTO;

/**
 * AI 기반 여행 경로 생성 및 관련 기능을 제공하는 서비스 클래스
 * @author jsg
 * @version 1.0
 * @since 2025.10.24
 */
public class AiService {

    // API URL 및 모델 정보
    /**
     * Gemini API의 기본 URL
     */
    private static final String API_BASE_URL = "https://generativelanguage.googleapis.com/v1beta/models/";
    /**
     * 사용할 Gemini 모델의 이름
     */
    private static final String MODEL_NAME = "gemini-2.5-flash"; // 안정적인 모델 이름
    /**
     * API 호출 액션
     */
    private static final String ACTION = ":generateContent";

    // API 키 (환경 변수에서 가져오기)
    /**
     * Gemini API 인증 키
     */
    private static final String API_KEY = System.getenv("GEMINI_API_KEY");
    
    // JSON 변환을 위한 Gson 객체
    /**
     * JSON 직렬화/역직렬화를 위한 Gson 객체
     */
    private final Gson gson = new Gson();
    
    // 최신 방식의 HttpClient (애플리케이션에서 한 번만 생성하여 재사용)
    /**
     * HTTP 요청을 위한 HttpClient 객체
     */
    private final HttpClient client = HttpClient.newHttpClient();

    /**
     * 사용자 선호도에 기반하여 AI 여행 경로를 생성합니다.
     * Gemini API를 호출하여 여행 계획을 요청하고, 응답을 RouteDTO 객체로 변환합니다.
     * @param preferences 사용자 여행 선호도 정보를 담은 TripPreferencesDTO 객체
     * @return AI가 생성한 여행 경로 정보를 담은 RouteDTO 객체
     */
    public RouteDTO createAiRoute(TripPreferencesDTO preferences) {
        try {
            if (API_KEY == null || API_KEY.isEmpty()) {
                throw new IllegalStateException("환경변수 'GEMINI_API_KEY'가 설정되지 않았습니다.");
            }

            // 1. 프롬프트 생성
            String prompt = buildPrompt(preferences);
            System.out.println("\n--- AI에게 보낼 프롬프트 ---\n" + prompt + "\n--------------------------\n");

            // 2. 요청 바디 구성 (org.json 사용)
            JSONObject requestBody = new JSONObject();
            requestBody.put("contents", new JSONArray()
                    .put(new JSONObject()
                            .put("parts", new JSONArray()
                                    .put(new JSONObject().put("text", prompt))
                            )));

            // 3. 안전 설정 추가
            JSONArray safetySettings = new JSONArray();
            safetySettings.put(new JSONObject().put("category", "HARM_CATEGORY_HARASSMENT").put("threshold", "BLOCK_NONE"));
            safetySettings.put(new JSONObject().put("category", "HARM_CATEGORY_HATE_SPEECH").put("threshold", "BLOCK_NONE"));
            safetySettings.put(new JSONObject().put("category", "HARM_CATEGORY_SEXUALLY_EXPLICIT").put("threshold", "BLOCK_NONE"));
            safetySettings.put(new JSONObject().put("category", "HARM_CATEGORY_DANGEROUS_CONTENT").put("threshold", "BLOCK_NONE"));
            requestBody.put("safetySettings", safetySettings);

            // 4. HttpClient를 사용하여 API 요청 생성 및 전송
            String apiUrl = API_BASE_URL + MODEL_NAME + ACTION + "?key=" + API_KEY;

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(apiUrl))
                    .timeout(Duration.ofSeconds(60)) // 타임아웃 60초 설정
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody.toString()))
                    .build();
            
            // 동기 방식으로 응답 수신
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            // 5. 응답 코드 확인
            if (response.statusCode() != 200) {
                System.err.println("Gemini API Error Response: " + response.body());
                throw new RuntimeException("Gemini API Error: " + response.statusCode());
            }

            // 6. 응답 JSON에서 실제 텍스트 내용만 추출
            JSONObject jsonResponse = new JSONObject(response.body());
            String textContent = jsonResponse.getJSONArray("candidates")
                                             .getJSONObject(0)
                                             .getJSONObject("content")
                                             .getJSONArray("parts")
                                             .getJSONObject(0)
                                             .getString("text");

            // 7. 마크다운 코드 블록 제거
            String cleanedJson = textContent.replace("```json", "").replace("```", "").trim();
            System.out.println("\n--- AI가 생성한 최종 JSON ---\n" + cleanedJson + "\n--------------------------\n");

            // 8. 최종 JSON을 RouteDTO 객체로 변환
            return gson.fromJson(cleanedJson, RouteDTO.class);

        } catch (Exception e) {
            e.printStackTrace();
            RouteDTO errorRoute = new RouteDTO();
            errorRoute.setAi_route_title("AI 루트 생성 중 오류가 발생했습니다.");
            return errorRoute;
        }
    }
    
    /**
     * AI가 생성한 여행 경로를 데이터베이스에 저장합니다.
     * @param route 저장할 여행 경로 정보를 담은 RouteDTO 객체
     * @param userId 사용자 ID
     * @param conversationId 대화 ID
     * @return 저장된 여행 경로 정보를 담은 RouteDTO 객체
     */
    public RouteDTO saveAiRoute(RouteDTO route, String userId, Long conversationId) {
        AiDAO dao = new AiDAO();
        return dao.saveAiRoute(route, userId, conversationId);
    }
    
    /*
     * TODO: [다음 단계] 사용자 질문 저장 기능
     * 이 메서드를 호출하려면 먼저 AiDAO의 saveConversationAndAnswers 메서드 내부를
     * 실제 DB 데이터와 연동하는 로직으로 구현해야 합니다.
    */
    /**
     * 사용자 질문과 답변을 데이터베이스에 저장합니다.
     * @param preferences 여행 선호도 정보를 담은 TripPreferencesDTO 객체
     * @param userId 사용자 ID
     * @return 저장된 대화 ID
     */
    public Long saveConversationAndAnswers(TripPreferencesDTO preferences, String userId) {
        AiDAO dao = new AiDAO();
        // return dao.saveConversationAndAnswers(preferences, userId);
        return null; // 임시로 null 반환
    }

    /**
     * TripPreferencesDTO 객체를 바탕으로 AI 모델에 전달할 프롬프트를 생성합니다.
     * @param dto 사용자 여행 선호도 정보를 담은 TripPreferencesDTO 객체
     * @return AI 모델용 프롬프트 문자열
     */
    private String buildPrompt(TripPreferencesDTO dto) {
        StringBuilder prompt = new StringBuilder();

        prompt.append("당신은 한국 최고의 여행 계획 전문가입니다.\n");
        prompt.append("아래 조건에 맞춰 최적의 여행 루트를 JSON 형식으로만 응답해주세요. 설명이나 다른 말은 절대 추가하지 마세요.\n");
     // ★★★ JSON 출력 형식 예시를 DB 스키마와 완벽하게 일치시킴 ★★★
        prompt.append("JSON 출력 형식: {\"ai_route_title\":\"...\", \"ai_route_region\":\"...\", \"ai_route_days\":..., \"weather_consideration\":\"...\", ");
        prompt.append("\"stops\":[{\"ai_route_day\":..., \"ai_route_stop_order\":..., \"ai_route_description\":\"...\", \"ai_route_lat\":..., \"ai_route_long\":..., ");
        prompt.append("\"activity_code\":\"...\", \"duration_in_minutes\":..., \"restaurant_category\":\"...\"}]}\n\n");
        
        prompt.append("### 조건 1: 날씨 고려\n");
        prompt.append("- 여행 날짜(").append(dto.getStartDate()).append(" ~ ").append(dto.getEndDate()).append(")의 평균적인 날씨를 예측하세요.\n");
        prompt.append("- 만약 비가 올 확률이 높은 시즌이라면, 'weatherConsideration' 필드에 '비가 올 가능성을 고려해 실내 활동 위주로 계획했습니다.' 와 같이 기록하고, 'stops'의 'activityCode'를 'VIEWING'(실내 관람) 등으로 조정하세요.\n\n");
        
        prompt.append("### 조건 2: 장소 정보 규칙 (매우 중요)\n");
        prompt.append("- 각 경유지('stops'의 각 요소)에 대해 다음 규칙을 반드시 준수하세요:\n");
        prompt.append("  1. 'ai_route_day': 여행 몇 일차인지 숫자로 표기하세요.\n");
        prompt.append("  2. 'ai_route_stop_order': 해당 날짜 내에서의 방문 순서를 숫자로 표기하세요.\n");
        prompt.append("  3. 'ai_route_description': 해당 장소명으로 작성하세요.\n");
        prompt.append("  4. 'ai_route_lat': 추천 장소의 위도(latitude)를 숫자로 표기하세요. (예: 33.450701)\n");
        prompt.append("  5. 'ai_route_long': 추천 장소의 경도(longitude)를 숫자로 표기하세요. (예: 126.570667)\n\n");
        
        prompt.append("  6. 식사 추천: **여행일마다 점심과 저녁, 총 2개의 식당을 반드시 추천**해야 합니다. 식당 추천 시 'ai_route_description'에 식당 정보와 메뉴를 포함시키세요.\n\n");
        prompt.append("  7. 'activity_code': 활동 종류를 다음 중에서만 선택하세요: VIEWING, WALK_SLOW, WALK_NORMAL, WALK_FAST, HIKE_LIGHT, SHOPPING, EATING.\n");
        prompt.append("  8. 'duration_in_minutes': 해당 장소에서 머무는 예상 소요 시간을 분 단위 숫자로 표기하세요.\n");
        prompt.append("  9. 'restaurant_category': 추천 장소가 식당일 경우에만 음식 종류(예: '한식', '일식')를 표기하고, 식당이 아니면 null로 표기하세요.\n");
        prompt.append("  10. 식사 추천: 여행일마다 점심과 저녁, 총 2개의 식당을 반드시 추천해야 합니다. 식당도 하나의 경유지이므로 모든 규칙을 동일하게 적용해야 합니다.\n\n");
        
        if ("헬스케어".equals(dto.getTravelStyle())) {
            prompt.append("### 조건 3: 헬스케어 맞춤 계획\n");
            prompt.append("아래 프로필과 규칙에 따라 헬스케어 여행을 계획하세요.\n");
            prompt.append("- 사용자의 신체 정보(성별: ").append(dto.getPhysicalInfo().getGender());
            prompt.append(", 키: ").append(dto.getPhysicalInfo().getHeight()).append("cm");
            prompt.append(", 몸무게: ").append(dto.getPhysicalInfo().getWeight()).append("kg)를 바탕으로 활동 강도를 조절하세요.\n");
            
            prompt.append("#### 준수 규칙:\n");
            prompt.append("1. - 식단 계획: 사용자의 음식 선호도('").append(dto.getFoodPreference()).append("')에 맞는 건강 식단을 제공하는 실제 식당을 점심과 저녁에 추천하세요.\n\n");
            prompt.append("  - 건강식 (저염·영양식 위주): 샐러드, 건강 한정식, 사찰 음식 전문점 등\n");
            prompt.append("  - 균형식 (일반식/저칼로리 위주): 일반 백반, 저칼로리 도시락, 샤브샤브 전문점 등\n");
            prompt.append("  - 고단백식 (운동 후 단백질 보충): 단백질이 풍부한 장어, 소고기, 닭가슴살 요리 전문점 등\n");
            prompt.append("- 식당 추천 시, 'restaurantCategory' 필드에 해당 식당의 종류(예: '한식', '샐러드', '일식')를 명시해주세요.\n\n");
        }
        	prompt.append("2. 활동 규칙: 위 '건강 목표'에 맞는 'activity_code'를 중심으로 계획하세요.\n");
        	prompt.append("2. 거리/걸음 수 계산: 'activity_code'가 'WALK_' 또는 'HIKE_'로 시작하는 활동에 대해, 예상 도보 거리('walking_distance_km')와 예상 걸음 수('walking_steps_count')를 반드시 숫자로 예측하여 포함하세요. 그 외 활동은 null로 표기하세요.\n");
        


       
        prompt.append("### 사용자 요청 정보\n");
        prompt.append("- 여행 도시: ").append(dto.getCity()).append("\n");
        prompt.append("- 여행 기간: ").append(dto.getDuration()).append("\n");
        prompt.append("- 여행 스타일: ").append(dto.getTravelStyle()).append("\n");
        prompt.append("- 선호 지역: ").append(dto.getPreferredArea()).append("\n");
        prompt.append("- 이동 수단: ").append(dto.getTransportation()).append("\n");
        prompt.append("- 실내/실외: ").append(dto.getActivityType()).append("\n");
        prompt.append("- 동행: ").append(dto.getCompanion()).append("\n");

        if ("헬스케어".equals(dto.getTravelStyle()) && dto.getPhysicalInfo() != null) {
            prompt.append("\n### 헬스케어 상세 정보\n");
            prompt.append("- 성별: ").append(dto.getPhysicalInfo().getGender()).append("\n");
            prompt.append("- 키: ").append(dto.getPhysicalInfo().getHeight()).append("cm\n");
            prompt.append("- 몸무게: ").append(dto.getPhysicalInfo().getWeight()).append("kg\n");
            prompt.append("- 건강 목표: ").append(dto.getHealthGoal()).append("\n");
            prompt.append("- 음식 선호: ").append(dto.getFoodPreference()).append("\n");
            prompt.append("- 특이 건강: ").append(dto.getHealthCondition()).append("\n");
        }
        
        return prompt.toString();
    }
}