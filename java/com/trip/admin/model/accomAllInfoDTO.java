package com.trip.admin.model;

import lombok.Data;

/**
 * 숙소의 모든 정보를 담는 DTO 클래스
 * @author jsg
 * @version 1.0
 * @since 2025.10.24
 */
@Data
public class accomAllInfoDTO {
    /**
     * 장소 ID
     */
    private int placeId;
    /**
     * 장소 이름
     */
    private String placeName;
    /**
     * 장소 주소
     */
    private String placeAddress;
    /**
     * 숙소 유형
     */
    private String accomType;
    /**
     * 방 ID
     */
    private int roomId;
    /**
     * 방 이름
     */
    private String roomName;
    /**
     * 수용 인원
     */
    private int capacity;
    /**
     * 1박당 가격
     */
    private int pricePerNight;
}