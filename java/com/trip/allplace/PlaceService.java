package com.trip.allplace;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.trip.allplace.model.PlaceDAO;
import com.trip.allplace.model.PlaceDTO;
import com.trip.allplace.model.PlaceSearchCriteria;
import com.trip.allplace.model.TouristSpotDAO;
import com.trip.allplace.model.TouristSpotDTO;

/**
 * 관광지 관련 비즈니스 로직을 처리하는 클래스
 */
public class PlaceService {

    private PlaceDAO placeDAO;
    private TouristSpotDAO touristSpotDAO;

    public PlaceService() {
        this.placeDAO = new PlaceDAO();
        this.touristSpotDAO = new TouristSpotDAO();
    }

    /**
     * 검색 조건에 맞는 장소 목록을 가져옵니다.
     */
    public List<PlaceDTO> getPlaceList(PlaceSearchCriteria criteria) {
        return placeDAO.getPlaceList(criteria);
    }

    /**
     * 특정 장소의 상세 정보 (기본 정보 + 관광지 상세)를 가져옵니다.
     * @param place_id 장소 ID
     * @return Map<String, Object> (key: "place", "touristSpot")
     */
    public Map<String, Object> getPlaceDetails(int place_id) {
        
        Map<String, Object> details = new HashMap<>();
        
        PlaceDTO place = placeDAO.getPlace(place_id);
        TouristSpotDTO touristSpot = touristSpotDAO.getTouristSpot(place_id);
        
        details.put("place", place);
        details.put("touristSpot", touristSpot);
        
        // TODO: 조회수 증가 로직 등 추가 가능
        
        return details;
    }

    /**
     * 키워드 기반으로 연관 장소 목록을 가져옵니다.
     * @param currentPlaceId 현재 장소 ID
     * @return List<PlaceDTO>
     */
    public List<PlaceDTO> getRelatedPlacesByKeyword(int currentPlaceId) {
        return placeDAO.getRelatedPlacesByKeyword(currentPlaceId);
    }
}
