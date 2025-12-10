package com.trip.allplace.model;

import lombok.Data;

@Data // @Getter, @Setter, @ToString, @EqualsAndHashCode, @RequiredArgsConstructor를 모두 포함합니다.
public class PlaceDTO {

    // tblPlace 테이블의 컬럼과 일치하는 필드들
    private int place_id;
    private int place_type_id;
    private int place_Location_id;
    private String place_name;
    private String place_address;
    private double place_lat;
    private double place_lng;
    private String place_main_image_url;
    private String place_description;
    
    // 트렌드 페이지에서 사용할 키워드(태그) 목록
    private java.util.List<String> keywords;
    
}
