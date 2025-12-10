package com.trip.ai.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * AI 여행 경로의 경유지 정보를 담는 DTO 클래스
 * @author jsg
 * @version 1.0
 * @since 2025.10.24
 */
@Getter
@Setter
@ToString
public class RouteStopDTO {
	
	/**
	 * AI 여행 경로 경유지 ID
	 */
	private long ai_route_stop_id;
	/**
	 * AI 여행 경로 ID
	 */
	private long ai_route_id;
	/**
	 * 여행 일차
	 */
	private int ai_route_day;
	/**
	 * 경유지 순서
	 */
	private int ai_route_stop_order;
	/**
	 * 경유지 설명
	 */
	private String ai_route_description;
	/**
	 * 활동 코드
	 */
	private String activity_code;
	/**
	 * 소요 시간 (분)
	 */
	private int duration_in_minutes;
	/**
	 * 식당 카테고리
	 */
	private String restaurant_category;
	/**
	 * 경유지 위도
	 */
	private double ai_route_lat;
	/**
	 * 경유지 경도
	 */
	private double ai_route_long;
	
	/**
	 * 도보 이동 거리 (km)
	 */
	private double walking_distance_km;
	/**
	 * 도보 걸음 수
	 */
	private int walking_steps_count;
	
}
