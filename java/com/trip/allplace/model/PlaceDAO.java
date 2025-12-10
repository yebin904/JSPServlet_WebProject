package com.trip.allplace.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import com.test.util.DBUtil;

public class PlaceDAO {

    public PlaceDAO() {
        // The constructor is now empty. Connections are managed per-method.
    }

    /**
     * 모든 장소 목록을 DB에서 가져옵니다.
     */
    public List<PlaceDTO> getPlaceList(PlaceSearchCriteria criteria) {
        
        List<PlaceDTO> list = new ArrayList<>();
        String sql = "SELECT * FROM tblPlace";
        
        try (Connection conn = new DBUtil().open();
             PreparedStatement pstat = conn.prepareStatement(sql);
             ResultSet rs = pstat.executeQuery()) {
            
            while (rs.next()) {
                PlaceDTO dto = new PlaceDTO();
                dto.setPlace_id(rs.getInt("place_id"));
                dto.setPlace_type_id(rs.getInt("place_type_id"));
                dto.setPlace_Location_id(rs.getInt("place_Location_id"));
                dto.setPlace_name(rs.getString("place_name"));
                dto.setPlace_address(rs.getString("place_address"));
                dto.setPlace_lat(rs.getDouble("place_lat"));
                dto.setPlace_lng(rs.getDouble("place_lng"));
                dto.setPlace_main_image_url(rs.getString("place_main_image_url"));
                dto.setPlace_description(rs.getString("place_description"));
                list.add(dto);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * 특정 ID에 해당하는 장소 하나의 상세 정보를 DB에서 가져옵니다.
     * @param place_id 장소 ID
     * @return PlaceDTO
     */
    public PlaceDTO getPlace(int place_id) {
        
        String sql = "SELECT * FROM tblPlace WHERE place_id = ?";
        
        try (Connection conn = new DBUtil().open();
             PreparedStatement pstat = conn.prepareStatement(sql)) {
            
            pstat.setInt(1, place_id);
            
            try (ResultSet rs = pstat.executeQuery()) {
                if (rs.next()) {
                    PlaceDTO dto = new PlaceDTO();
                    dto.setPlace_id(rs.getInt("place_id"));
                    dto.setPlace_type_id(rs.getInt("place_type_id"));
                    dto.setPlace_Location_id(rs.getInt("place_Location_id"));
                    dto.setPlace_name(rs.getString("place_name"));
                    dto.setPlace_address(rs.getString("place_address"));
                    dto.setPlace_lat(rs.getDouble("place_lat"));
                    dto.setPlace_lng(rs.getDouble("place_lng"));
                    dto.setPlace_main_image_url(rs.getString("place_main_image_url"));
                    dto.setPlace_description(rs.getString("place_description"));
                    return dto;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return null;
    }

    /**
     * 키워드 기준으로 연관 장소 목록을 DB에서 가져옵니다.
     * @param currentPlaceId 현재 장소 ID
     * @return List<PlaceDTO>
     */
    public List<PlaceDTO> getRelatedPlacesByKeyword(int currentPlaceId) {
        List<PlaceDTO> list = new ArrayList<>();
        
        String sql = "SELECT place_id, place_name, place_main_image_url FROM ("
                   + "    SELECT"
                   + "        p.place_id,"
                   + "        p.place_name,"
                   + "        p.place_main_image_url,"
                   + "        COUNT(kl.place_id) as shared_keyword_count"
                   + "    FROM"
                   + "        tblKeywordLink kl"
                   + "    JOIN"
                   + "        tblPlace p ON kl.place_id = p.place_id"
                   + "    WHERE"
                   + "        kl.keyword_id IN (SELECT keyword_id FROM tblKeywordLink WHERE place_id = ?)"
                   + "        AND kl.place_id != ?"
                   + "    GROUP BY"
                   + "        p.place_id, p.place_name, p.place_main_image_url"
                   + "    ORDER BY"
                   + "        shared_keyword_count DESC, p.place_id"
                   + ") WHERE ROWNUM <= 4";

        try (Connection conn = new DBUtil().open();
             PreparedStatement pstat = conn.prepareStatement(sql)) {
            
            pstat.setInt(1, currentPlaceId);
            pstat.setInt(2, currentPlaceId);
            
            try (ResultSet rs = pstat.executeQuery()) {
                while (rs.next()) {
                    PlaceDTO dto = new PlaceDTO();
                    dto.setPlace_id(rs.getInt("place_id"));
                    dto.setPlace_name(rs.getString("place_name"));
                    dto.setPlace_main_image_url(rs.getString("place_main_image_url"));
                    list.add(dto);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return list;
    }
}