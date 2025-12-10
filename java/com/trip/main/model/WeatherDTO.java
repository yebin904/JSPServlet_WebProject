package com.trip.main.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 날씨 정보를 담는 DTO(Data Transfer Object) 클래스.
 * OpenWeatherMap API에서 받은 데이터를 가공하여 저장하는 데 사용됩니다.
 * * @author jsg
 * @version 1.0
 * @since 2025.10.24
 */
@Getter
@Setter
@ToString
public class WeatherDTO {
    
    /** 도시명 (예: "서울", "부산") */
    private String city; 
    
    /** 현재 기온 (섭씨, "%.1f" 형식의 문자열) */
    private String temperature; 
    
    /** * 하늘상태 코드 (기상청 API 호환)
     * 맑음(1), 구름많음(3), 흐림(4)
     * @see com.trip.main.WeatherService#getWeatherData() OpenWeatherMap 코드 변환 로직 참고
     */
    private String skyCondition; 
    
    /** * 강수형태 코드 (기상청 API 호환)
     * 없음(0), 비(1), 비/눈(2), 눈(3), 소나기(4)
     * @see com.trip.main.WeatherService#getWeatherData() OpenWeatherMap 코드 변환 로직 참고
     */
    private String precipitationForm; 
}
