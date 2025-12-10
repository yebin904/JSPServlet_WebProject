package com.trip.reservation.model;
import java.util.Date;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 예약 목록 조회를 위한 정보를 담는 DTO 클래스
 * (ReservationDAO의 getReservationList 메소드에서 사용됩니다)
 * @author jsg
 * @version 1.0
 * @since 2025.10.24
 */
@Data
@Getter
@Setter
@ToString
public class ReservationListDTO {
    /** 예약 ID (PK) */
    private long reservation_id;
    /** 예약 시작일 (전체 일정) */
    private Date reservation_start_date;
    /** 예약 종료일 (전체 일정) */
    private Date reservation_end_date;
    /** 총 예약 금액 */
    private long reservation_price;
    /** 예약 상태명 (예: '예약완료') */
    private String status_name;
    /** 숙소 체크인 날짜 */
    private Date checkin_date;
    /** 숙소 체크아웃 날짜 */
    private Date checkout_date;
    /** 예약된 객실 이름 */
    private String room_name;
    /** 예약된 차량 이름 */
    private String car_name;
}