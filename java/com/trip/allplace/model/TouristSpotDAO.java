package com.trip.allplace.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import com.test.util.DBUtil;

public class TouristSpotDAO {

    /**
     * 특정 ID에 해당하는 관광지 상세 정보를 DB에서 가져옵니다.
     * @param place_id 장소 ID
     * @return TouristSpotDTO
     */
    public TouristSpotDTO getTouristSpot(int place_id) {
        
        String sql = "SELECT * FROM tblTouristSpot WHERE place_id = ?";
        
        try (Connection conn = new DBUtil().open();
             PreparedStatement pstat = conn.prepareStatement(sql)) {
            
            pstat.setInt(1, place_id);
            
            try (ResultSet rs = pstat.executeQuery()) {
                if (rs.next()) {
                    TouristSpotDTO dto = new TouristSpotDTO();
                    dto.setPlace_id(rs.getInt("place_id"));
                    dto.setAdmission_fee(rs.getString("admission_fee"));
                    dto.setOpening_hours(rs.getString("opening_hours"));
                    dto.setContact_info(rs.getString("contact_info"));
                    dto.setSpot_overinfo(rs.getString("spot_overinfo"));
                    dto.setParking_info(rs.getString("parking_info"));
                    dto.setRest_day(rs.getString("rest_day"));
                    return dto;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return null;
    }
}
