package com.trip.ai.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 여행 선호도 정보를 담는 DTO 클래스
 * @author jsg
 * @version 1.0
 * @since 2025.10.24
 */
@Getter
@Setter
@ToString
public class TripPreferencesDTO {
	
	/**
	 * 여행 도시
	 */
	private String city;
    /**
     * 여행 기간
     */
    private String duration;
    /**
     * 여행 스타일
     */
    private String travelStyle;
    /**
     * 활동 시간
     */
    private String activityTime;
    /**
     * 예산
     */
    private String budget;
    /**
     * 선호 지역
     */
    private String preferredArea;
    /**
     * 교통 수단
     */
    private String transportation;
    /**
     * 활동 유형
     */
    private String activityType;
    /**
     * 동반자
     */
    private String companion;
    
    /**
     * 여행 시작일
     */
    private String startDate;
    /**
     * 여행 종료일
     */
    private String endDate;
    
    /**
     * 신체 정보 (중첩된 객체)
     */
    private PhysicalInfo physicalInfo;
    /**
     * 건강 목표
     */
    private String healthGoal;
    /**
     * 음식 선호도
     */
    private String foodPreference;
    /**
     * 건강 상태
     */
    private String healthCondition;
    
    /**
     * 사용자 신체 정보를 담는 중첩 클래스
     */
    @Getter
    @Setter
    @ToString
    public static class PhysicalInfo {
    	/**
    	 * 성별
    	 */
    	private String gender;
    	/**
    	 * 키
    	 */
    	private double height;
    	/**
    	 * 몸무게
    	 */
    	private double weight;
    }
	

}
