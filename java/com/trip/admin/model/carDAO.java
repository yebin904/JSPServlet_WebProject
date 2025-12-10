package com.trip.admin.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.test.util.DBUtil;

/**
 * 렌터카 관련 데이터베이스 작업을 처리하는 DAO 클래스
 * @author jsg
 * @version 1.0
 * @since 2025.10.24
 */
public class carDAO {

    private Connection conn;

    /**
     * carDAO 객체를 생성하고 데이터베이스 연결을 초기화합니다.
     */
    public carDAO() {
    	DBUtil dbUtil = new DBUtil();
        this.conn = dbUtil.open();
    }

    /**
     * 렌터카 목록을 조회합니다. 필터링(연료 유형, 가격 범위) 및 정렬 기능을 포함합니다.
     * @param fuelTypes 필터링할 연료 유형 배열
     * @param minPrice 최소 가격
     * @param maxPrice 최대 가격
     * @param sortOrder 정렬 순서 (예: "price_asc", "price_desc")
     * @return 필터링 및 정렬된 렌터카 목록
     */
    public List<carDTO> getAllCars(String[] fuelTypes, int minPrice, int maxPrice, String sortOrder) {
        List<carDTO> list = new ArrayList<>();
        try {
            String sql = "SELECT c.car_id, c.car_name, c.car_type, c.fuel_type, c.price_per_day, "
                       + "(SELECT COUNT(*) FROM tblCarReservation res WHERE res.car_id = c.car_id AND SYSDATE BETWEEN res.start_date AND res.end_date) as reservation_count "
                       + "FROM tblRentalCar c "
                       + "WHERE c.price_per_day BETWEEN ? AND ? ";

            if (fuelTypes != null && fuelTypes.length > 0) {
                sql += "AND c.fuel_type IN (";
                for (int i = 0; i < fuelTypes.length; i++) { sql += "?,"; }
                sql = sql.substring(0, sql.length() - 1) + ") ";
            }

            if ("price_asc".equals(sortOrder)) { sql += "ORDER BY c.price_per_day ASC, c.car_name ASC"; }
            else if ("price_desc".equals(sortOrder)) { sql += "ORDER BY c.price_per_day DESC, c.car_name ASC"; }
            else { sql += "ORDER BY c.car_name ASC"; }
            
            PreparedStatement pstat = conn.prepareStatement(sql);
            
            int paramIndex = 1;
            pstat.setInt(paramIndex++, minPrice);
            pstat.setInt(paramIndex++, maxPrice);
            
            if (fuelTypes != null && fuelTypes.length > 0) {
                for (String type : fuelTypes) { pstat.setString(paramIndex++, type); }
            }
            
            ResultSet rs = pstat.executeQuery();
            while (rs.next()) {
                carDTO dto = new carDTO();
                dto.setCarId(rs.getInt("car_id"));
                dto.setCarName(rs.getString("car_name"));
                dto.setCarType(rs.getString("car_type"));
                dto.setFuelType(rs.getString("fuel_type"));
                dto.setPricePerDay(rs.getInt("price_per_day"));
                dto.setReserved(rs.getInt("reservation_count") > 0);
                list.add(dto);
            }
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }

    /**
     * 새로운 렌터카 정보를 DB에 추가합니다.
     * @param dto 추가할 렌터카 정보를 담은 carDTO 객체
     */
    public void addCar(carDTO dto) {
        try {
            String sql = "INSERT INTO tblRentalCar (car_id, car_name, car_type, fuel_type, price_per_day) "
                       + "VALUES (seq_rental_car.NEXTVAL, ?, ?, ?, ?)";
            
            PreparedStatement pstat = conn.prepareStatement(sql);
            pstat.setString(1, dto.getCarName());
            pstat.setString(2, dto.getCarType());
            pstat.setString(3, dto.getFuelType());
            pstat.setInt(4, dto.getPricePerDay());
            pstat.executeUpdate();
            pstat.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 특정 렌터카를 DB에서 삭제합니다.
     * @param carId 삭제할 렌터카 ID
     */
    public void deleteCar(int carId) {
        try {
            String sql = "DELETE FROM tblRentalCar WHERE car_id = ?";
            PreparedStatement pstat = conn.prepareStatement(sql);
            pstat.setInt(1, carId);
            pstat.executeUpdate();
        } catch (Exception e) { e.printStackTrace(); }
    }
 // carDAO.java 파일 가장 아래에 이 메소드들을 추가하세요.

    /**
     * 특정 차량의 정보를 조회하는 메소드 (수정 페이지 채우기용)
     * @param carId 조회할 차량 ID
     * @return 차량 정보가 담긴 DTO
     */
    public carDTO getCarInfo(int carId) {
        carDTO dto = null;
        try {
            String sql = "SELECT car_id, car_name, car_type, fuel_type, price_per_day FROM tblRentalCar WHERE car_id = ?";
            PreparedStatement pstat = conn.prepareStatement(sql);
            pstat.setInt(1, carId);
            ResultSet rs = pstat.executeQuery();
            
            if (rs.next()) {
                dto = new carDTO();
                dto.setCarId(rs.getInt("car_id"));
                dto.setCarName(rs.getString("car_name"));
                dto.setCarType(rs.getString("car_type"));
                dto.setFuelType(rs.getString("fuel_type"));
                dto.setPricePerDay(rs.getInt("price_per_day"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dto;
    }

    /**
     * 차량 정보를 DB에서 수정하는 메소드
     * @param dto 수정할 정보가 담긴 DTO
     * @return 성공 시 1, 실패 시 0
     */
    public int updateCar(carDTO dto) {
        try {
            String sql = "UPDATE tblRentalCar SET car_name = ?, car_type = ?, fuel_type = ?, price_per_day = ? WHERE car_id = ?";
            PreparedStatement pstat = conn.prepareStatement(sql);
            pstat.setString(1, dto.getCarName());
            pstat.setString(2, dto.getCarType());
            pstat.setString(3, dto.getFuelType());
            pstat.setInt(4, dto.getPricePerDay());
            pstat.setInt(5, dto.getCarId());
            
            return pstat.executeUpdate();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
}
