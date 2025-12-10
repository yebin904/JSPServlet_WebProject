package com.trip.info.trend;

import java.util.List;

import com.trip.allplace.model.PlaceDTO;
import com.trip.info.trend.model.TrendDAO;

/**
 * '여행 트렌드' 관련 비즈니스 로직을 처리하는 서비스 클래스.
 * Controller와 DAO 사이의 중개자 역할을 수행합니다.
 * * @author jsg
 * @version 1.0
 * @since 2025.10.24
 */
public class TrendService {

    /** '여행 트렌드' 데이터 접근 객체(DAO) */
    private TrendDAO dao;

    /**
     * TrendService 생성자입니다.
     * 내부적으로 TrendDAO 객체를 생성하여 초기화합니다.
     */
    public TrendService() {
        this.dao = new TrendDAO();
    }

    /**
     * 키워드가 포함된 트렌드 장소 목록을 조회하는 비즈니스 로직을 수행합니다.
     * (현재는 별도 로직 없이 DAO를 바로 호출합니다.)
     * * @return 키워드 정보가 포함된 장소 목록 (List&lt;PlaceDTO&gt;)
     * @see com.trip.info.trend.model.TrendDAO#getTrendList()
     */
    public List<PlaceDTO> getTrendList() {
        // 향후 이 곳에서 추가적인 비즈니스 로직 (데이터 가공, 조합 등) 수행 가능
        return dao.getTrendList();
    }
}
