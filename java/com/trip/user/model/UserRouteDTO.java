package com.trip.user.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 사용자 경로 정보를 담는 DTO 클래스
 * (마이페이지 경로 목록 조회용)
 * @author jsg
 * @version 1.0
 * @since 2025.10.24
 */
@Getter
@Setter
@ToString
public class UserRouteDTO {
    /** 사용자 경로 ID (user_route_id) */
	private String seq;
    /** 사용자 ID (user_id) */
	private String useq;
    /** 경로 제목 */
	private String userroutetitle;
    /** 여행 일수 */
	private String userroutedays;
    /** 여행 시작일 */
	private String userroutestartdate;
    /** 여행 종료일 */
	private String userrouteenddate;

}