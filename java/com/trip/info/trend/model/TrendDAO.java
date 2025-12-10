package com.trip.info.trend.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.test.util.DBUtil;
import com.trip.allplace.model.PlaceDTO;

/**
 * '여행 트렌드' 관련 데이터베이스 작업을 처리하는 DAO(Data Access Object) 클래스.
 * tblPlace, tblKeyword, tblKeywordLink 테이블과 상호작용합니다.
 * * @author jsg
 * @version 1.0
 * @since 2025.10.24
 */
public class TrendDAO {

    /**
     * 키워드가 연결된 장소 목록을 조회합니다.
     * <p>
     * 1. tblPlace(p), tblKeywordLink(kl), tblKeyword(k) 테이블을 조인합니다.
     * 2. 키워드가 하나 이상 연결된(tblKeywordLink에 데이터가 있는) 장소(p)만 조회합니다.
     * 3. Oracle의 LISTAGG 함수를 사용하여 각 장소(place_id)에 연결된 모든 키워드(k.keyword)를
     * 쉼표(,)로 구분된 하나의 문자열(keywords)로 집계합니다.
     * 4. 조회된 ResultSet을 PlaceDTO 객체로 매핑합니다.
     * 5. 집계된 키워드 문자열(keywords)을 쉼표(,) 기준으로 분리하여 List<String> 형태로 PlaceDTO에 설정합니다.
     * </p>
     * * @return 키워드 정보가 포함된 장소 목록 (List&lt;PlaceDTO&gt;). 키워드가 없는 장소는 제외됩니다.
     */
    public List<PlaceDTO> getTrendList() {
        List<PlaceDTO> list = new ArrayList<>();
        // ERD(42.png)를 기반으로, tblPlace, tblKeyword, tblKeywordLink 테이블을 조인합니다.
        // LISTAGG 함수를 사용해 각 장소에 연결된 키워드들을 쉼표(,)로 구분된 하나의 문자열로 합칩니다.
        String sql = "SELECT p.*, "
                   + " (SELECT LISTAGG(k.keyword, ',') WITHIN GROUP (ORDER BY k.keyword) " // 키워드를 쉼표로 연결
                   + "  FROM tblKeywordLink kl JOIN tblKeyword k ON kl.keyword_id = k.keyword_id "
                   + "  WHERE kl.place_id = p.place_id) as keywords "
                   + "FROM tblPlace p "
                   + "WHERE p.place_id IN (SELECT DISTINCT place_id FROM tblKeywordLink)"; // 키워드가 있는 장소만 선택

        // try-with-resources 구문을 사용하여 Connection, PreparedStatement, ResultSet 자동 close
        try (Connection conn = new DBUtil().open();
             PreparedStatement pstat = conn.prepareStatement(sql);
             ResultSet rs = pstat.executeQuery()) {

            while (rs.next()) {
                PlaceDTO dto = new PlaceDTO();
                
                // tblPlace의 컬럼들을 DTO에 매핑
                dto.setPlace_id(rs.getInt("place_id"));
                dto.setPlace_type_id(rs.getInt("place_type_id"));
                dto.setPlace_Location_id(rs.getInt("place_Location_id"));
                dto.setPlace_name(rs.getString("place_name"));
                dto.setPlace_address(rs.getString("place_address"));
                dto.setPlace_lat(rs.getDouble("place_lat"));
                dto.setPlace_lng(rs.getDouble("place_lng"));
                dto.setPlace_main_image_url(rs.getString("place_main_image_url"));
                dto.setPlace_description(rs.getString("place_description"));

                // 집계된 keywords 문자열을 가져옵니다. (예: "힐링,맛집,경치좋은")
                String keywordStr = rs.getString("keywords");
                
                if (keywordStr != null && !keywordStr.isEmpty()) {
                    // 문자열을 쉼표(,) 기준으로 분리하여 List<String>으로 변환 후 DTO에 설정합니다.
                    dto.setKeywords(Arrays.asList(keywordStr.split(",")));
                } else {
                    // 키워드가 없는 경우(null), 빈 리스트를 설정합니다. (NullPointerException 방지)
                    dto.setKeywords(new ArrayList<>());
                }
                
                list.add(dto);
            }
        } catch (Exception e) {
            e.printStackTrace(); // 예외 발생 시 콘솔에 로그 출력
        }
        return list; // 조회된 목록 반환
    }
}
