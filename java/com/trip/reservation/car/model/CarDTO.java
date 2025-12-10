package com.trip.reservation.car.model;

import java.util.Date;

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
public class CarDTO {

    /** 차량 ID (PK) */
    private long car_id;               
    /** 대여지 ID (FK) */
    private long place_id;             
    /** 차량 이름 */
    private String car_name;           
    /** 차량 종류 (세단, SUV 등) */
    private String car_type;           
    /** 차량 번호판 */
    private String car_number;         
    /** 연료 종류 (휘발유, 전기 등) */
    private String car_fuel_type;      
    /** 좌석 수 */
    private int car_seats;             
    /** 1일 요금 */
    private long car_price_per_day;    
    /** 등록일 */
    private Date car_regdate;          
    /** 차량 이미지 URL */
    private String car_image_url;
    /** 지역 이름 */
    private String location_name;
    

}