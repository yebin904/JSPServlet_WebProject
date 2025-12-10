package com.trip.reservation.accom.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 숙소 정보를 담는 DTO 클래스.
 * @author jsg
 * @version 1.0
 * @since 2025.10.24
 */
@Getter
@Setter
@ToString
public class AccomDTO {
    /** 숙소 객실 ID */
    private long room_id;
    /** 객실 이름 */
    private String room_name;
    /** 객실 유형 */
    private String room_type;
    /** 객실 수용 인원 */
    private int capacity;
    /** 1박당 가격 */
    private long price_per_night;
    /** 객실 이미지 URL */
    private String room_image_url;
    /** 객실 상태 (예: 'y' for available) */
    private String room_status;

    /** 숙소 유형 (예: 호텔, 리조트) */
    private String accom_type;
    /** 장소 이름 */
    private String place_name;
	/** 지역 이름 */
	private String location_name;
    
 
}