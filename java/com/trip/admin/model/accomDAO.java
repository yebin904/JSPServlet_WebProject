package com.trip.admin.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.test.util.DBUtil;

/**
 * 숙소 관련 데이터베이스 작업을 처리하는 DAO 클래스
 * @author jsg
 * @version 1.0
 * @since 2025.10.24
 */
public class accomDAO {

    private Connection conn ;

    /**
     * accomDAO 객체를 생성하고 데이터베이스 연결을 초기화합니다.
     */
    public accomDAO() {
    	
    	DBUtil dbUtil = new DBUtil();
        this.conn = dbUtil.open();
    }

    /**
     * DB에 등록된 모든 숙소 중 최고 가격을 조회합니다.
     * @return 최고가 (int), 숙소가 없을 경우 기본값 2000000을 반환합니다.
     */
    public int getMaxPrice() {
        int maxPrice = 0;
        try {
            String sql = "SELECT MAX(price_per_night) AS maxPrice FROM tblAccomRoom";
            PreparedStatement pstat = conn.prepareStatement(sql);
            ResultSet rs = pstat.executeQuery();
            if (rs.next()) {
                maxPrice = rs.getInt("maxPrice");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return maxPrice > 0 ? maxPrice : 2000000;
    }


    /**
     * 숙소 목록을 조회합니다. 필터링(숙소 유형, 가격 범위) 및 정렬 기능을 포함합니다.
     * @param accomTypes 필터링할 숙소 유형 배열
     * @param minPrice 최소 가격
     * @param maxPrice 최대 가격
     * @param sortOrder 정렬 순서 (예: "price_asc", "price_desc")
     * @return 필터링 및 정렬된 숙소 목록
     */
    public List<accomDTO> getAllAccommodations(String[] accomTypes, int minPrice, int maxPrice, String sortOrder) {
        List<accomDTO> list = new ArrayList<>();
        try {
            String sql = "SELECT "
                       + "    r.room_id, "
                       + "    p.place_name, "
                       + "    a.accom_type, "
                       + "    r.room_name, "
                       + "    r.price_per_night, "
                       + "    r.capacity, "
                       + "    (SELECT COUNT(*) FROM tblAccomReservation res WHERE res.room_id = r.room_id AND SYSDATE BETWEEN res.checkin_date AND res.checkout_date) as reservation_count "
                       + "FROM tblAccomRoom r "
                       + "    INNER JOIN tblAccom a ON r.place_id = a.place_id "
                       + "    INNER JOIN tblPlace p ON r.place_id = p.place_id "
                       + "WHERE r.price_per_night BETWEEN ? AND ? ";

            if (accomTypes != null && accomTypes.length > 0) {
                sql += "AND a.accom_type IN (";
                for (int i = 0; i < accomTypes.length; i++) {
                    sql += "?,";
                }
                sql = sql.substring(0, sql.length() - 1) + ") ";
            }

            if ("price_asc".equals(sortOrder)) {
                sql += "ORDER BY r.price_per_night ASC, p.place_name ASC";
            } else if ("price_desc".equals(sortOrder)) {
                sql += "ORDER BY r.price_per_night DESC, p.place_name ASC";
            } else {
                sql += "ORDER BY p.place_name ASC, r.room_name ASC";
            }
            
            PreparedStatement pstat = conn.prepareStatement(sql);
            
            int paramIndex = 1;
            pstat.setInt(paramIndex++, minPrice);
            pstat.setInt(paramIndex++, maxPrice);
            
            if (accomTypes != null && accomTypes.length > 0) {
                for (String type : accomTypes) {
                    pstat.setString(paramIndex++, type);
                }
            }
            
            ResultSet rs = pstat.executeQuery();
            while (rs.next()) {
                accomDTO dto = new accomDTO();
                dto.setRoomId(rs.getInt("room_id"));
                dto.setAccomName(rs.getString("place_name"));
                dto.setAccomType(rs.getString("accom_type"));
                dto.setRoomName(rs.getString("room_name"));
                dto.setPricePerNight(rs.getInt("price_per_night"));
                dto.setCapacity(rs.getInt("capacity"));
                dto.setReserved(rs.getInt("reservation_count") > 0);
                list.add(dto);
            }
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }

    /**
     * 특정 객실을 삭제합니다.
     * @param roomId 삭제할 객실 ID
     */
    public void deleteRoom(int roomId) {
        try {
            String sql = "DELETE FROM tblAccomRoom WHERE room_id = ?";
            PreparedStatement pstat = conn.prepareStatement(sql);
            pstat.setInt(1, roomId);
            pstat.executeUpdate();
        } catch (Exception e) { e.printStackTrace(); }
    }

    /**
     * 신규 숙소 정보를 DB에 등록하는 메소드입니다.
     * tblPlace, tblAccom, tblAccomRoom 3개 테이블에 순차적으로 INSERT를 수행합니다.
     * @param dto 숙소 정보가 담긴 DTO
     * @return 성공 시 1, 실패 시 0
     */
    public int addAccommodation(accomAllInfoDTO dto) {
        
        PreparedStatement pstat = null;
        ResultSet rs = null;
        int placeId = 0;

        try {
            conn.setAutoCommit(false); 
            
            String sqlGetId = "SELECT seq_accom.NEXTVAL AS place_id FROM dual";
            pstat = conn.prepareStatement(sqlGetId);
            rs = pstat.executeQuery();
            if (rs.next()) {
                placeId = rs.getInt("place_id");
            }
            rs.close();
            pstat.close();
            
            if (placeId == 0) throw new Exception("Failed to get new place ID.");

            String sqlPlace = "INSERT INTO tblPlace (place_id, place_type_id, place_Location_id, place_name, place_address, place_lat, place_lng) " +
                              "VALUES (?, 1, 1, ?, ?, 0, 0)";
            pstat = conn.prepareStatement(sqlPlace);
            pstat.setInt(1, placeId);
            pstat.setString(2, dto.getPlaceName());
            pstat.setString(3, dto.getPlaceAddress());
            pstat.executeUpdate();
            pstat.close();

            String sqlAccom = "INSERT INTO tblAccom (place_id, accom_type, accom_regdate) VALUES (?, ?, SYSDATE)";
            pstat = conn.prepareStatement(sqlAccom);
            pstat.setInt(1, placeId);
            pstat.setString(2, dto.getAccomType());
            pstat.executeUpdate();
            pstat.close();
            
            String sqlRoom = "INSERT INTO tblAccomRoom (room_id, place_id, room_name, capacity, price_per_night) " +
                             "VALUES (seq_accom_room.NEXTVAL, ?, ?, ?, ?)";
            pstat = conn.prepareStatement(sqlRoom);
            pstat.setInt(1, placeId);
            pstat.setString(2, dto.getRoomName());
            pstat.setInt(3, dto.getCapacity());
            pstat.setInt(4, dto.getPricePerNight());
            pstat.executeUpdate();
            pstat.close();
            
            conn.commit();
            
            return 1;

        } catch (Exception e) {
            e.printStackTrace();
            try {
                conn.rollback();
            } catch (Exception e2) {
                e2.printStackTrace();
            }
            return 0;
        } finally {
            try {
                conn.setAutoCommit(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 특정 객실의 모든 정보를 조회하는 메소드입니다. (수정 페이지 채우기용)
     * @param roomId 조회할 객실 ID
     * @return 숙소 정보가 담긴 DTO
     */
    public accomAllInfoDTO getAccommodationInfo(int roomId) {
        
        accomAllInfoDTO dto = new accomAllInfoDTO();
        
        try {
            String sql = "SELECT "
                       + "    p.place_id, "
                       + "    r.room_id, "
                       + "    p.place_name, "
                       + "    p.place_address, "
                       + "    a.accom_type, "
                       + "    r.room_name, "
                       + "    r.capacity, "
                       + "    r.price_per_night "
                       + "FROM tblAccomRoom r "
                       + "    INNER JOIN tblAccom a ON r.place_id = a.place_id "
                       + "    INNER JOIN tblPlace p ON a.place_id = p.place_id "
                       + "WHERE r.room_id = ?";
            
            PreparedStatement pstat = conn.prepareStatement(sql);
            pstat.setInt(1, roomId);
            ResultSet rs = pstat.executeQuery();
            
            if (rs.next()) {
                dto.setPlaceId(rs.getInt("place_id"));
                dto.setRoomId(rs.getInt("room_id"));
                dto.setPlaceName(rs.getString("place_name"));
                dto.setPlaceAddress(rs.getString("place_address"));
                dto.setAccomType(rs.getString("accom_type"));
                dto.setRoomName(rs.getString("room_name"));
                dto.setCapacity(rs.getInt("capacity"));
                dto.setPricePerNight(rs.getInt("price_per_night"));
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return dto;
    }

    /**
     * 숙소 정보를 DB에서 수정하는 메소드입니다.
     * @param dto 수정할 정보가 담긴 DTO
     * @return 성공 시 1, 실패 시 0
     */
    public int updateAccommodation(accomAllInfoDTO dto) {
        
        PreparedStatement pstat = null;
        
        try {
            conn.setAutoCommit(false);

            String sqlPlace = "UPDATE tblPlace SET place_name = ?, place_address = ? WHERE place_id = ?";
            pstat = conn.prepareStatement(sqlPlace);
            pstat.setString(1, dto.getPlaceName());
            pstat.setString(2, dto.getPlaceAddress());
            pstat.setInt(3, dto.getPlaceId());
            pstat.executeUpdate();
            pstat.close();
            
            String sqlAccom = "UPDATE tblAccom SET accom_type = ? WHERE place_id = ?";
            pstat = conn.prepareStatement(sqlAccom);
            pstat.setString(1, dto.getAccomType());
            pstat.setInt(2, dto.getPlaceId());
            pstat.executeUpdate();
            pstat.close();

            String sqlRoom = "UPDATE tblAccomRoom SET room_name = ?, capacity = ?, price_per_night = ? WHERE room_id = ?";
            pstat = conn.prepareStatement(sqlRoom);
            pstat.setString(1, dto.getRoomName());
            pstat.setInt(2, dto.getCapacity());
            pstat.setInt(3, dto.getPricePerNight());
            pstat.setInt(4, dto.getRoomId());
            pstat.executeUpdate();
            pstat.close();
            
            conn.commit();
            return 1;

        } catch (Exception e) {
            e.printStackTrace();
            try {
                conn.rollback();
            } catch (Exception e2) {
                e2.printStackTrace();
            }
            return 0;
        } finally {
            try {
                conn.setAutoCommit(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
