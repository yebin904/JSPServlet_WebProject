package com.trip.reservation.car.model;

import java.sql.*;
import java.util.*;
import com.test.util.DBUtil;

/**
 * 렌터카 관련 데이터베이스 작업을 처리하는 DAO 클래스.
 * @author jsg
 * @version 1.0
 * @since 2025.10.24
 */
public class CarDAO implements AutoCloseable {

    private Connection conn;

    /**
     * 기본 생성자입니다.
     * DBUtil 유틸리티 클래스를 통해 데이터베이스 연결을 초기화합니다.
     */
    public CarDAO() {
        conn = new DBUtil().open();
    }

    /**
     * 특정 조건에 맞는 렌터카 목록을 조회합니다. (단일 필터 조건용).
     * 지역(필수), 차종, 연료, 좌석 수(선택)를 기준으로 필터링합니다.
     *
     * @param locationName 지역 이름 (필수).
     * @param type         차량 유형 (예: 세단, SUV).
     * @param fuel         연료 유형 (예: 휘발유, 디젤).
     * @param seats        좌석 수.
     * @return 조건에 맞는 렌터카 목록.
     */
    public List<CarDTO> list(String locationName, String type, String fuel, String seats) {
        List<CarDTO> result = new ArrayList<>();

        StringBuilder sql = new StringBuilder("""
            SELECT c.car_id, c.car_name, c.car_type, c.car_fuel_type,
                   c.car_seats, c.car_price_per_day, l.location_name
              FROM tblRentalCar c
              JOIN tblPlaceLocation l ON c.place_location_id = l.place_location_id
             WHERE l.location_name = ?
               AND c.car_status = 'y'
        """);

        if (type != null && !type.isEmpty()) sql.append(" AND c.car_type = ? ");
        if (fuel != null && !fuel.isEmpty()) sql.append(" AND c.car_fuel_type = ? ");
        if (seats != null && !seats.isEmpty()) sql.append(" AND c.car_seats = ? ");
        sql.append(" ORDER BY c.car_id ");

        try (PreparedStatement ps = conn.prepareStatement(sql.toString())) {
            int idx = 1;
            ps.setString(idx++, locationName);

            if (type != null && !type.isEmpty()) ps.setString(idx++, type);
            if (fuel != null && !fuel.isEmpty()) ps.setString(idx++, fuel);
            if (seats != null && !seats.isEmpty()) ps.setInt(idx++, Integer.parseInt(seats));

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                CarDTO dto = new CarDTO();
                dto.setCar_id(rs.getLong("car_id"));
                dto.setCar_name(rs.getString("car_name"));
                dto.setCar_type(rs.getString("car_type"));
                dto.setCar_fuel_type(rs.getString("car_fuel_type"));
                dto.setCar_seats(rs.getInt("car_seats"));
                dto.setCar_price_per_day(rs.getLong("car_price_per_day"));
                result.add(dto);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }


    /**
     * 차량 ID로 단일 렌터카 정보를 조회합니다.
     * 예약 가능한('y') 상태의 차량만 조회합니다.
     *
     * @param carId 조회할 렌터카의 ID.
     * @return 조회된 렌터카 정보 (CarDTO), 없으면 null.
     */
    public CarDTO findById(String carId) {
    	CarDTO dto = null;
    	String sql = """
	    	        SELECT car_id,
	    	               car_name,
	    	               car_type,
	    	               car_fuel_type,
	    	               car_seats,
	    	               car_price_per_day,
	    	               car_image_url
	    	          FROM tblRentalCar
	    	         WHERE car_id = ?
	    	           AND car_status = 'y'
    	    """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, carId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                dto = new CarDTO();
                dto.setCar_id(rs.getLong("car_id"));
                dto.setCar_name(rs.getString("car_name"));
                dto.setCar_type(rs.getString("car_type"));
                dto.setCar_fuel_type(rs.getString("car_fuel_type"));
                dto.setCar_seats(rs.getInt("car_seats"));
                dto.setCar_price_per_day(rs.getLong("car_price_per_day"));
                dto.setCar_image_url(rs.getString("car_image_url"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dto;
    }

    /**
     * 데이터베이스 연결 리소스를 닫습니다.
     * AutoCloseable 인터페이스 구현으로 try-with-resources 구문에서 사용됩니다.
     */
    @Override
    public void close() {
        try { if (conn != null) conn.close(); } catch (Exception e) {}
    }

    /**
     * 다양한 조건으로 렌터카 목록을 필터링하여 조회합니다. (다중 선택 필터용).
     * 지역(선택), 차종(다중), 연료(다중), 좌석 수(다중) 등 여러 필터 조건을
     * 동적으로 SQL(IN절)에 추가합니다.
     *
     * @param region 지역 필터 (단일 값, location_name 기준).
     * @param types  차량 유형 필터 (다중 값 배열, 예: ["세단", "SUV"]).
     * @param fuels  연료 유형 필터 (다중 값 배열, 예: ["휘발유", "전기"]).
     * @param seats  좌석 수 필터 (다중 값 배열, 예: ["5", "7"]).
     * @return 필터링된 렌터카 목록.
     */
	public List<CarDTO> filteredList(String region, String[] types, String[] fuels, String[] seats) {
        List<CarDTO> result = new ArrayList<>();

        StringBuilder sql = new StringBuilder("""
            SELECT c.car_id,
        		   c.car_name,
        		   c.car_type,
        		   c.car_fuel_type,
                   c.car_seats,
                   c.car_price_per_day,
                   c.car_image_url,
                   l.location_name
              FROM tblRentalCar c
              JOIN tblPlaceLocation l ON c.place_location_id = l.place_location_id
             WHERE c.car_status = 'y'
        """);

        // 지역 필터
        if (region != null && !region.isEmpty()) {
            sql.append(" AND l.location_name = ? ");
        }

        // ✅ 체크박스 다중 필터 처리
        if (types != null && types.length > 0) {
            sql.append(" AND c.car_type IN (")
               .append(String.join(",", Collections.nCopies(types.length, "?")))
               .append(")");
        }

        if (fuels != null && fuels.length > 0) {
            sql.append(" AND c.car_fuel_type IN (")
               .append(String.join(",", Collections.nCopies(fuels.length, "?")))
               .append(")");
        }

        if (seats != null && seats.length > 0) {
            sql.append(" AND c.car_seats IN (")
               .append(String.join(",", Collections.nCopies(seats.length, "?")))
               .append(")");
        }

        sql.append(" ORDER BY c.car_price_per_day ASC");

        try (PreparedStatement ps = conn.prepareStatement(sql.toString())) {
            int idx = 1;

            if (region != null && !region.isEmpty()) {
                ps.setString(idx++, region);
            }

            if (types != null) {
                for (String t : types) ps.setString(idx++, t);
            }
            if (fuels != null) {
                for (String f : fuels) ps.setString(idx++, f);
            }
            if (seats != null) {
                for (String s : seats) ps.setInt(idx++, Integer.parseInt(s));
            }

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                CarDTO dto = new CarDTO();
                dto.setCar_id(rs.getLong("car_id"));
                dto.setCar_name(rs.getString("car_name"));
                dto.setCar_type(rs.getString("car_type"));
                dto.setCar_fuel_type(rs.getString("car_fuel_type"));
                dto.setCar_seats(rs.getInt("car_seats"));
                dto.setCar_price_per_day(rs.getLong("car_price_per_day"));
                dto.setCar_image_url(rs.getString("car_image_url"));
                dto.setLocation_name(rs.getString("location_name"));
                result.add(dto);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

}