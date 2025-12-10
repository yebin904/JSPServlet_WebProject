package com.trip.reservation.model;

import java.sql.Date;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 예약 마스터 정보를 담는 DTO 클래스
 * @author jsg
 * @version 1.0
 * @since 2025.10.24
 */
@Getter
@Setter
@ToString
public class ReservationDTO {
    /** 예약 ID (PK) */
	private long reservation_id;
    /** 대표 숙소 이름 (예약 목록 표시용) */
    private String accom_name;
    /** 대표 차량 이름 (예약 목록 표시용) */
    private String car_name;
    /** 총 예약 금액 */
    private long reservation_price;
    /** 예약 시작일 (전체 일정) */
    private Date reservation_start_date;
    /** 예약 종료일 (전체 일정) */
    private Date reservation_end_date;
    /** 예약 상태명 (예: '예약완료', '취소됨') */
    private String status_name;
}	