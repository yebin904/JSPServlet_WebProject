package com.trip.user.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 사용자 숙소 예약 정보를 담는 DTO 클래스
 * (마이페이지 예약 내역 조회용)
 * @author jsg
 * @version 1.0
 * @since 2025.10.24
 */
@Getter
@Setter
@ToString
public class AccomReservationDTO {
    /** 마스터 예약 번호 (reservation_id) */
	private String seq;
    /** 사용자 고유 번호 (user_id) */
	private String useq;
    /** 객실 총 결제 금액 */
	private String roomtotalprice;
    /** 숙소(장소) 이름 */
	private String placename;
    /** 숙소(장소) 주소 */
	private String placeaddress;
    /** 숙소 예약 고유 번호 (accom_reservation_id) */
	private String accomseq;
    /** 객실 이름 */
	private String roomname;
    /** 객실 유형 (예: '디럭스') */
	private String roomtype;
    /** 숙소 유형 (예: '호텔') */
	private String accomtype;
    /** 숙박 인원 */
	private String guestcount;
    /** 체크인 날짜 */
	private String checkindate;
    /** 체크아웃 날짜 */
	private String checkoutdate;

}