package com.trip.ai.service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

/**
 * Gemini AI API와 통신하는 클라이언트 클래스
 * @author jsg
 * @version 1.0
 * @since 2025.10.24
 */
public class GeminiClient {

    /**
     * Gemini API 키
     */
    private final String apiKey;
    /**
     * HTTP 요청을 위한 HttpClient 객체
     */
    private final HttpClient client;
    /**
     * 사용할 Gemini 모델 이름
     */
    private final String model = "gemini-2.5-flash";

    /**
     * GeminiClient 객체를 생성합니다.
     * @param apiKey Gemini API 인증 키
     */
    public GeminiClient(String apiKey) {
        this.apiKey = apiKey;
        this.client = HttpClient.newHttpClient();
    }

    /**
     * 주어진 프롬프트를 사용하여 Gemini AI로부터 응답을 생성합니다.
     * @param prompt AI에 전달할 프롬프트 문자열
     * @return Gemini AI로부터 받은 응답 본문 (JSON 형식)
     * @throws Exception API 호출 중 발생할 수 있는 예외
     */
    public String generate(String prompt) throws Exception {
        String url = "https://generativelanguage.googleapis.com/v1beta/models/" + model + ":generateContent";

        String jsonBody = """
        {
            "contents": [
                {"parts": [{"text": "%s"}]}
            ]
        }
        """.formatted(prompt.replace("\"", "\\\""));

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url + "?key=" + apiKey))
                .timeout(Duration.ofSeconds(60))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            // 오류 메시지를 더 명확하게 변경
            throw new RuntimeException("Gemini API Error: " + response.statusCode() + " " + response.body());
        }

        return response.body();
    }
}