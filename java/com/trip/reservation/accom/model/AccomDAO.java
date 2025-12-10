package com.trip.reservation.accom.model;

import java.sql.*;
import java.util.*;
import com.test.util.DBUtil;

/**
 * 숙소 관련 데이터베이스 작업을 처리하는 DAO 클래스.
 * @author jsg
 * @version 1.0
 * @since 2025.10.24
 */
public class AccomDAO implements AutoCloseable {
    private Connection conn;

    /**
     * 기본 생성자입니다.
     * DBUtil 유틸리티 클래스를 통해 데이터베이스 연결을 초기화합니다.
     */
    public AccomDAO() {
        conn = new DBUtil().open();
    }

    /**
     * 특정 지역, 기간 및 인원 수에 따른 숙소 목록을 조회합니다.
     * (현재 SQL 쿼리는 지역과 인원 수만 사용합니다.)
     * @param region 숙소 지역 (예: "부산").
     * @param start 예약 시작일 (현재 쿼리에서 사용되지 않음).
     * @param end 예약 종료일 (현재 쿼리에서 사용되지 않음).
     * @param people 숙박 인원 (capacity와 비교).
     * @return 조건에 맞는 숙소 목록.
     */
    public List<AccomDTO> list(String region, String start, String end, String people) {
        List<AccomDTO> result = new ArrayList<>();

        String sql = """
            SELECT ar.room_id, ar.room_name, ar.price_per_night, ar.room_image_url
              FROM tblAccomRoom ar
              JOIN tblAccom a ON ar.accom_id = a.accom_id
              JOIN tblPlace p ON a.place_id = p.place_id
              JOIN tblPlaceLocation pl ON pl.place_location_id = p.place_location_id
             WHERE pl.location_name = ? AND ar.capacity >= ?
             ORDER BY ar.room_id
        """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, region);
            ps.setInt(2, Integer.parseInt(people));

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                AccomDTO dto = new AccomDTO();
                dto.setRoom_id(rs.getLong("room_id"));
                dto.setRoom_name(rs.getString("room_name"));
                dto.setPrice_per_night(rs.getLong("price_per_night"));
                dto.setRoom_image_url(rs.getString("room_image_url"));
                result.add(dto);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    /**
     * 숙소(객실) ID로 단일 숙소 정보를 조회합니다.
     * 객실 정보와 함께 숙소가 위치한 장소(place_name) 이름도 조회합니다.
     * @param roomId 조회할 숙소(객실)의 ID.
     * @return 조회된 숙소 정보 (AccomDTO), 없으면 null.
     */
    public AccomDTO findRoomById(String roomId) {
    String sql = """
        SELECT r.room_id,
               r.room_name,
               r.price_per_night,
               r.room_image_url,
               p.place_name
          FROM tblAccomRoom r
          JOIN tblAccom a ON r.accom_id = a.accom_id
          JOIN tblPlace p ON a.place_id = p.place_id
         WHERE r.room_id = ?
    """;

    AccomDTO dto = null;

    try (PreparedStatement ps = conn.prepareStatement(sql)) {
        ps.setString(1, roomId);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            dto = new AccomDTO();
            dto.setRoom_id(rs.getLong("room_id"));
            dto.setRoom_name(rs.getString("room_name"));
            dto.setPrice_per_night(rs.getLong("price_per_night"));
            dto.setRoom_image_url(rs.getString("room_image_url"));
            dto.setPlace_name(rs.getString("place_name")); // ✅ 숙소 이름 추가
        }

    } catch (Exception e) {
        e.printStackTrace();
    }

    return dto;
}


    /**
     * 다양한 조건으로 숙소 목록을 필터링하여 조회합니다.
     * 지역, 숙소 유형, 객실 유형, 수용 인원 등 다중 필터 조건을 동적으로 SQL에 추가합니다.
     * @param region 지역 필터 (단일 값, location_name 기준).
     * @param accomTypes 숙소 유형 필터 (다중 값 배열, 예: ["호텔", "리조트"]).
     * @param roomTypes 객실 유형 필터 (다중 값 배열, 예: ["스탠다드", "디럭스"]).
     * @param capacities 수용 인원 필터 (다중 값 배열, 예: ["2", "4"]).
     * @return 필터링된 숙소 목록.
     */
    public List<AccomDTO> filteredList(String region, String[] accomTypes, String[] roomTypes, String[] capacities) {


        List<AccomDTO> result = new ArrayList<>();

        StringBuilder sql = new StringBuilder("""
            SELECT r.room_id,
                   r.room_name,
                   r.room_type,
                   r.capacity,
                   r.price_per_night,
                   r.room_image_url,
                   r.room_status,
                   a.accom_type,
                   p.place_name,
                   l.location_name
              FROM tblAccomRoom r
              JOIN tblAccom a ON r.accom_id = a.accom_id
              JOIN tblPlace p ON a.place_id = p.place_id
              JOIN tblPlaceLocation l ON p.place_location_id = l.place_location_id
             WHERE r.room_status = 'y'
        """);

        // ✅ 지역 필터 - location_name 기준으로 수정
        if (region != null && !region.isEmpty()) {
            sql.append(" AND l.location_name = ? ");
        }

        // 숙소 유형 필터
        if (accomTypes != null && accomTypes.length > 0) {
            sql.append(" AND a.accom_type IN (")
               .append(String.join(",", Collections.nCopies(accomTypes.length, "?")))
               .append(")");
        }

        // 객실 유형 필터
        if (roomTypes != null && roomTypes.length > 0) {
            sql.append(" AND r.room_type IN (")
               .append(String.join(",", Collections.nCopies(roomTypes.length, "?")))
               .append(")");
        }

        // 수용 인원 필터
        if (capacities != null && capacities.length > 0) {
            sql.append(" AND r.capacity IN (")
               .append(String.join(",", Collections.nCopies(capacities.length, "?")))
               .append(")");
        }

        sql.append(" ORDER BY r.price_per_night ASC");

        try (PreparedStatement ps = conn.prepareStatement(sql.toString())) {
            int idx = 1;

            // ✅ 바인딩 순서
            if (region != null && !region.isEmpty()) {
                ps.setString(idx++, region);
            }
            if (accomTypes != null) {
                for (String a : accomTypes) ps.setString(idx++, a);
            }
            if (roomTypes != null) {
                for (String rt : roomTypes) ps.setString(idx++, rt);
            }
            if (capacities != null) {
                for (String c : capacities) ps.setInt(idx++, Integer.parseInt(c));
            }

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                AccomDTO dto = new AccomDTO();
                dto.setRoom_id(rs.getLong("room_id"));
                dto.setRoom_name(rs.getString("room_name"));
                dto.setRoom_type(rs.getString("room_type"));
                dto.setCapacity(rs.getInt("capacity"));
                dto.setPrice_per_night(rs.getLong("price_per_night"));
                dto.setRoom_image_url(rs.getString("room_image_url"));
                dto.setRoom_status(rs.getString("room_status"));
                dto.setAccom_type(rs.getString("accom_type"));
                dto.setPlace_name(rs.getString("place_name"));
                dto.setLocation_name(rs.getString("location_name")); // ✅ 지역 이름 DTO에 추가
                result.add(dto);
            }

            rs.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }



    /**
     * 데이터베이스 연결 리소스를 닫습니다.
     * AutoCloseable 인터페이스 구현으로 try-with-resources 구문에서 사용됩니다.
     */
    @Override
    public void close() {
        try { if (conn != null) conn.close(); } catch (Exception e) {}
    }
}