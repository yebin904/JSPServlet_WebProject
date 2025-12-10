package com.trip.reservation.model;

import java.sql.*;
import com.test.util.DBUtil;

/**
 * 렌터카 예약 관련 데이터베이스 작업을 처리하는 DAO 클래스
 * @author jsg
 * @version 1.0
 * @since 2025.10.24
 */
public class CarReservationDAO {
    private Connection conn;

    /**
     * 기본 생성자입니다.
     * DBUtil 유틸리티 클래스를 통해 데이터베이스 연결을 초기화합니다.
     */
    public CarReservationDAO() {
        conn = new DBUtil().open();
    }
    
    /**
     * 새로운 렌터카 예약을 데이터베이스에 등록합니다.
     * (이 메소드는 ReservationConfirm 서블릿에서 사용됩니다.)
     * @param dto 삽입할 차량 예약 상세 정보 (ReservationDetailDTO)
     * @return 성공적으로 삽입된 레코드 수 (1이면 성공)
     */
    public int insertCarReservation(ReservationDetailDTO dto) {
        try {
            String sql = "INSERT INTO tblCarReservation "
                       + "(car_reservation_id, reservation_id, car_id, pickup_date, dropoff_date) "
                       + "VALUES (seqCarReservation.NEXTVAL, ?, ?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setLong(1, dto.getR_idx()); // r_idx는 Reservation_id를 의미
            ps.setLong(2, dto.getCar_id());
            ps.setDate(3, new java.sql.Date(dto.getPickup_date().getTime()));
            ps.setDate(4, new java.sql.Date(dto.getDropoff_date().getTime()));
            return ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 새로운 렌터카 예약을 데이터베이스에 등록합니다.
     * (이 메소드는 파라미터가 ReservationConfirm 서블릿의 insertCarReservation과 다릅니다.
     * 용도를 확인하고 중복 시 정리하는 것이 좋습니다.)
     * @param carId 예약할 렌터카의 ID
     * @param reservationId 메인 예약의 ID (FK)
     * @param startDate 렌탈 시작일 (YYYY-MM-DD 형식의 문자열)
     * @param endDate 렌탈 종료일 (YYYY-MM-DD 형식의 문자열)
     * @return 성공적으로 삽입된 레코드 수 (1이면 성공)
     */
    public int insert(String carId, int reservationId, String startDate, String endDate) {
        try {
            String sql = "INSERT INTO tblCarReservation "
                       + "(car_reservation_id, reservation_id, car_id, rental_start_date, rental_end_date) "
                       + "VALUES (seqCarReservation.NEXTVAL, ?, ?, TO_DATE(?, 'YYYY-MM-DD'), TO_DATE(?, 'YYYY-MM-DD'))";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, reservationId);
            ps.setString(2, carId);
            ps.setString(3, startDate);
            ps.setString(4, endDate);
            return ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
}