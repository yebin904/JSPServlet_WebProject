package com.trip.allplace.model;

import lombok.Data;

@Data
public class TouristSpotDTO {
    private int place_id;
    private String admission_fee;
    private String opening_hours;
    private String contact_info;
    private String spot_overinfo; // spot_overview -> spot_overinfo 로 컬럼명 반영
    private String parking_info;
    private String rest_day;
}
