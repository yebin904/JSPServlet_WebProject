package com.trip.admin.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 렌터카 정보를 담는 DTO 클래스
 * @author jsg
 * @version 1.0
 * @since 2025.10.24
 */
@Getter
@Setter
@ToString
public class carDTO {
    /**
     * 렌터카 ID
     */
    private int carId;
    /**
     * 렌터카 이름
     */
    private String carName;
    /**
     * 렌터카 종류
     */
    private String carType;
    /**
     * 연료 타입
     */
    private String fuelType;
    /**
     * 1일당 가격
     */
    private int pricePerDay;
    /**
     * 예약 여부
     */
    private boolean isReserved; // 예약 여부
}
