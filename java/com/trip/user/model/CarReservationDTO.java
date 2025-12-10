package com.trip.user.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 사용자 렌터카 예약 정보를 담는 DTO 클래스
 * (마이페이지 예약 내역 조회용)
 * @author jsg
 * @version 1.0
 * @since 2025.10.24
 */
@Getter
@Setter
@ToString
public class CarReservationDTO {
    /** 마스터 예약 번호 (reservation_id) */
	private String seq;
    /** 차량 예약 고유 번호 (car_reservation_id) */
	private String carseq;
    /** 사용자 고유 번호 (user_id) */
	private String useq;
    /** 예약 상태 ID */
	private String status;
    /** 예약 상태명 (예: '예약완료', '취소됨') */
	private String statusname;
    /** 차량 픽업 날짜 */
	private String pickupdate;
    /** 차량 반납 날짜 */
	private String dropoffdate;
    /** 픽업 장소 */
	private String pickuplocation;
    /** 반납 장소 */
	private String dropofflocation;
    /** 차량 총 결제 금액 */
	private String cartotalprice;
    /** 예약 참고 사항 */
	private String carnotes;
    /** 차종 (예: '세단') */
	private String cartype;
    /** 연료 유형 (예: '휘발유') */
	private String carfueltype;
    /** 차량 이름 */
	private String carname;



}