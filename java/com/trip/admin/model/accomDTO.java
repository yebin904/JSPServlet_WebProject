package com.trip.admin.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 숙소 정보를 담는 DTO 클래스
 * @author jsg
 * @version 1.0
 * @since 2025.10.24
 */
@Getter
@Setter
@ToString
public class accomDTO {
    /**
     * 객실 ID
     */
    private int roomId;
    /**
     * 숙소 이름
     */
    private String accomName;
    /**
     * 숙소 유형
     */
    private String accomType;
    /**
     * 객실 이름
     */
    private String roomName;
    /**
     * 1박당 가격
     */
    private int pricePerNight;
    /**
     * 수용 인원
     */
    private int capacity;
    
    /**
     * 예약 여부
     */
    private boolean isReserved;
}
