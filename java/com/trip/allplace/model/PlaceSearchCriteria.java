package com.trip.allplace.model;

import com.trip.BaseSearchCriteria;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PlaceSearchCriteria extends BaseSearchCriteria {
    
    // '관광지 검색'에만 필요한 '테마' 변수를 추가
    private String theme; 
    
  
}
