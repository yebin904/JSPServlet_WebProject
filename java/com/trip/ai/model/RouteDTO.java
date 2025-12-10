package com.trip.ai.model;

import java.util.Date;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * AI가 생성한 여행 경로 정보를 담는 DTO 클래스
 * @author jsg
 * @version 1.0
 * @since 2025.10.24
 */
@Getter
@Setter
@ToString
public class RouteDTO {
	
	/**
	 * AI 여행 경로 ID
	 */
	private long ai_route_id;
	/**
	 * 사용자 ID
	 */
	private long user_id;
	/**
	 * 대화 ID
	 */
	private long conversation_id;
	
	/**
	 * AI 여행 경로 제목
	 */
	private String ai_route_title;
	/**
	 * AI 여행 경로 일수
	 */
	private int ai_route_days;
	/**
	 * AI 여행 경로 생성일
	 */	
	private Date ai_route_created;
	/**
	 * AI 여행 경로 지역
	 */
	private String ai_route_region;
	/**
	 * AI 여행 경로 시작일
	 */
	private String ai_route_startdate;
	/**
	 * AI 여행 경로 종료일
	 */
	private String ai_route_enddate;
	/**
	 * 날씨 고려 여부
	 */
	private String weather_consideration;
    
    
    /**
     * AI 여행 경로의 경유지 목록
     */
    private List<RouteStopDTO> stops;

}
