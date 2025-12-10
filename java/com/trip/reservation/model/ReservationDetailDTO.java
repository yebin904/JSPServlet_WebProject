package com.trip.reservation.model;
import java.util.Date;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 예약 상세 정보(숙소, 차량 포함)를 담는 DTO 클래스
 * (ReservationDAO의 getReservationDetail 메소드 및
 * ReservationConfirm 서블릿에서 세션 데이터 전송용으로 사용됩니다)
 * @author jsg
 * @version 1.0
 * @since 2025.10.24
 */
@Data
@Getter
@Setter
@ToString
public class ReservationDetailDTO {
    /** 예약 ID (PK) */
	private long reservation_id;
    /** 사용자 ID (FK) */
    private long user_id;
    /** 사용자 경로 ID (FK) */
    private long user_route_id;
    /** 총 예약 금액 */
    private long reservation_price;
    /** 예약 시작일 (전체 일정) */
    private Date reservation_start_date;
    /** 예약 종료일 (전체 일정) */
    private Date reservation_end_date;
    /** 예약 상태 ID (FK) */
    private int status_id;
    /** 예약 상태명 */
    private String status_name;
    
    // --- 숙소 정보 ---
    /** 예약된 객실 이름 */
    private String room_name;
    /** 숙소 체크인 날짜 */
    private Date checkin_date;
    /** 숙소 체크아웃 날짜 */
    private Date checkout_date;

    // --- 차량 정보 ---
    /** 예약된 차량 이름 */
    private String car_name;
    /** 차량 픽업 날짜 */
    private Date pickup_date;
    /** 차량 반납 날짜 */
    private Date dropoff_date;

    // --- ReservationConfirm에서 사용되는 임시 필드 ---
    /** (임시) 삽입될 마스터 예약 ID (r_idx) */
    private long r_idx; 
    /** (임시) 삽입될 객실 ID */
    private long room_id;
    /** (임시) 삽입될 차량 ID */
    private long car_id;
}