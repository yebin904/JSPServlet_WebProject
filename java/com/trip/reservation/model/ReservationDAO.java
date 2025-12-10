package com.trip.reservation.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.test.util.DBUtil;
import com.trip.reservation.accom.model.AccomDTO;
import com.trip.reservation.car.model.CarDTO;

/**
 * 예약 관련 데이터베이스 작업을 처리하는 DAO 클래스.
 * @author jsg
 * @version 1.0
 * @since 2025.10.24
 */
public class ReservationDAO implements AutoCloseable {

	private Connection conn;

	/**
     * 기본 생성자입니다.
     * DBUtil 유틸리티 클래스를 통해 데이터베이스 연결을 시도합니다.
     * @throws RuntimeException 데이터베이스 연결 실패 시.
     */
	public ReservationDAO() {
		try {
			conn = new DBUtil().open();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
     * 데이터베이스 연결 리소스를 닫습니다.
     * AutoCloseable 인터페이스 구현으로 try-with-resources 구문에서 사용됩니다.
     */
	@Override
	public void close() {
		if (conn != null)
			try {
				conn.close();
			} catch (Exception ignore) {
			}
	}

	/**
     * 특정 사용자의 전체 예약 목록을 조회합니다.
     * 숙소, 차량 정보를 LEFT JOIN하여 예약 요약 정보를 반환합니다.
     * @param userId 조회할 사용자의 ID.
     * @return 예약 목록.
     */
    public List<ReservationListDTO> getReservationList(long userId) {
        List<ReservationListDTO> list = new ArrayList<>();

        String sql = """
            SELECT
                r.reservation_id,
                r.reservation_start_date,
                r.reservation_end_date,
                r.reservation_price,
                s.status_name,
                NVL(ar.checkin_date, r.reservation_start_date) AS checkin_date,
                NVL(ar.checkout_date, r.reservation_end_date) AS checkout_date,
                rm.room_name,
                c.car_name
            FROM tblReservation r
                JOIN tblReservationStatus s ON r.status_id = s.status_id
                LEFT JOIN tblAccomReservation ar ON r.reservation_id = ar.reservation_id
                LEFT JOIN tblAccomRoom rm ON ar.room_id = rm.room_id
                LEFT JOIN tblCarReservation cr ON r.reservation_id = cr.reservation_id
                LEFT JOIN tblRentalCar c ON cr.car_id = c.car_id
            WHERE r.user_id = ?
            ORDER BY r.reservation_id DESC
        """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ReservationListDTO dto = new ReservationListDTO();
                    dto.setReservation_id(rs.getLong("reservation_id"));
                    dto.setReservation_start_date(rs.getDate("reservation_start_date"));
                    dto.setReservation_end_date(rs.getDate("reservation_end_date"));
                    dto.setReservation_price(rs.getLong("reservation_price"));
                    dto.setStatus_name(rs.getString("status_name"));
                    dto.setCheckin_date(rs.getDate("checkin_date"));
                    dto.setCheckout_date(rs.getDate("checkout_date"));
                    dto.setRoom_name(rs.getString("room_name"));
                    dto.setCar_name(rs.getString("car_name"));
                    list.add(dto);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

	/**
     * 예약을 취소 상태로 변경합니다.
     * tblReservation 테이블의 status_id를 '취소됨' 상태의 ID로 업데이트합니다.
     * @param reservationId 취소할 예약의 ID.
     * @param userId 예약을 취소하려는 사용자의 ID (본인 확인용).
     * @return 업데이트된 레코드 수 (1이면 성공).
     */
	public int cancelReservation(long reservationId, long userId) {
		// 상태 테이블에서 '취소됨' id 가져와서 업데이트 (하드코딩 방지)
		String sql = """
				    UPDATE tblReservation
				       SET status_id = (SELECT status_id FROM tblReservationStatus WHERE status_name = '취소됨')
				     WHERE reservation_id = ? AND user_id = ?
				""";
		try (PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setLong(1, reservationId);
			ps.setLong(2, userId);
			return ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	/**
     * 숙소(객실) ID로 간단한 정보를 조회합니다.
     * @param roomId 조회할 객실 ID.
     * @return 조회된 숙소 정보 (AccomDTO), 없으면 null.
     */
	public AccomDTO getRoomInfo(String roomId) {
		String sql = "SELECT room_id, room_name, room_price FROM tblAccomRoom WHERE room_id = ?";
		try (PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setString(1, roomId);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				AccomDTO dto = new AccomDTO();
				dto.setRoom_id(rs.getLong("room_id"));
				dto.setRoom_name(rs.getString("room_name"));
				dto.setPrice_per_night(rs.getLong("price_per_night"));
				return dto;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
     * 차량 ID로 간단한 정보를 조회합니다.
     * @param carId 조회할 차량 ID.
     * @return 조회된 차량 정보 (CarDTO), 없으면 null.
     */
	public CarDTO getCarInfo(String carId) {
		String sql = "SELECT car_id, car_name, car_price FROM tblRentalCar WHERE car_id = ?";
		try (PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setString(1, carId);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				CarDTO dto = new CarDTO();
				dto.setCar_id(rs.getLong("car_id"));
				dto.setCar_name(rs.getString("car_name"));
				dto.setCar_price_per_day(rs.getLong("car_price_per_day"));
				return dto;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}


	/**
	 * 통합 예약(숙소, 차량)을 생성합니다. (트랜잭션 처리).
     * 1. 마스터 예약 테이블(tblReservation)에 레코드를 삽입합니다.
     * 2. 생성된 예약 ID(reservationId)를 가져옵니다.
     * 3. 숙소 예약 테이블(tblAccomReservation)에 상세 정보를 삽입합니다. (필수)
     * 4. 차량 ID가 있는 경우, 차량 예약 테이블(tblCarReservation)에 상세 정보를 삽입합니다. (선택)
     * 모든 과정이 성공하면 commit, 하나라도 실패하면 rollback 합니다.
     *
	 * @param userId 사용자 ID.
	 * @param userRouteId 사용자 경로 ID.
	 * @param statusId 예약 상태 ID.
	 * @param totalPrice 총 결제 금액.
	 * @param startDate 예약 시작일 (YYYY-MM-DD).
	 * @param endDate 예약 종료일 (YYYY-MM-DD).
	 * @param accomId 숙소(객실) ID.
	 * @param checkinDate 체크인 날짜 (YYYY-MM-DD).
	 * @param checkoutDate 체크아웃 날짜 (YYYY-MM-DD).
	 * @param carId 차량 ID (선택 사항, 없으면 null 또는 0).
	 * @param rentalStart 차량 렌탈 시작일 (YYYY-MM-DD).
	 * @param rentalEnd 차량 렌탈 종료일 (YYYY-MM-DD).
	 * @return 생성된 마스터 예약 ID (reservation_id).
	 * @throws SQLException 트랜잭션 처리 중 오류 발생 시.
	*/

	public long createIntegratedReservation(
	        long userId,
	        long userRouteId,
	        int statusId,
	        long totalPrice,
	        String startDate,
	        String endDate,
	        long accomId,
	        String checkinDate,
	        String checkoutDate,
	        Long carId,
	        String rentalStart,
	        String rentalEnd
	) throws SQLException {

	    long reservationId = 0;

	    // ✅ null-safe 날짜 처리
	    if (checkinDate == null || checkinDate.isBlank()) checkinDate = startDate;
	    if (checkoutDate == null || checkoutDate.isBlank()) checkoutDate = endDate;
	    if (rentalStart == null || rentalStart.isBlank()) rentalStart = startDate;
	    if (rentalEnd == null || rentalEnd.isBlank()) rentalEnd = endDate;

	    try {
	        conn.setAutoCommit(false);

	        // 1️⃣ tblReservation 등록
	        String sql1 = """
	            INSERT INTO tblReservation
	                (reservation_id, user_id, user_route_id, status_id,
	                 reservation_price, reservation_start_date, reservation_end_date)
	            VALUES (seqReservation.NEXTVAL, ?, ?, ?, ?, TO_DATE(?, 'YYYY-MM-DD'), TO_DATE(?, 'YYYY-MM-DD'))
	        """;

	        try (PreparedStatement ps = conn.prepareStatement(sql1, new String[] {"reservation_id"})) {
	            ps.setLong(1, userId);
	            ps.setLong(2, userRouteId);
	            ps.setInt(3, statusId);
	            ps.setLong(4, totalPrice);
	            ps.setString(5, startDate);
	            ps.setString(6, endDate);
	            ps.executeUpdate();

	            try (ResultSet rs = ps.getGeneratedKeys()) {
	                if (rs.next()) reservationId = rs.getLong(1);
	            }
	        }

	        if (reservationId == 0) throw new SQLException("❌ 예약번호 생성 실패");

	        // 2️⃣ 숙소 예약 (필수)
	        String sql2 = """
	            INSERT INTO tblAccomReservation
	                (accom_reservation_id, room_id, reservation_id, user_route_id,
	                 status_id, guest_count, room_total_price, checkin_date, checkout_date)
	            VALUES (seqAccomReservation.NEXTVAL, ?, ?, ?, ?, 2, ?, TO_DATE(?, 'YYYY-MM-DD'), TO_DATE(?, 'YYYY-MM-DD'))
	        """;

	        try (PreparedStatement ps = conn.prepareStatement(sql2)) {
	            ps.setLong(1, accomId);
	            ps.setLong(2, reservationId);
	            ps.setLong(3, userRouteId);
	            ps.setInt(4, statusId);
	            ps.setLong(5, totalPrice);
	            ps.setString(6, checkinDate);
	            ps.setString(7, checkoutDate);
	            ps.executeUpdate();
	        }

	        // 3️⃣ 차량 예약 (선택)
	        if (carId != null && carId > 0) {
	            String sql3 = """
	                INSERT INTO tblCarReservation
	                    (car_reservation_id, car_id, reservation_id, user_route_id, status_id,
	                     pickup_date, dropoff_date, car_total_price)
	                VALUES (seqCarReservation.NEXTVAL, ?, ?, ?, ?, TO_DATE(?, 'YYYY-MM-DD'), TO_DATE(?, 'YYYY-MM-DD'), ?)
	            """;

	            try (PreparedStatement ps = conn.prepareStatement(sql3)) {
	                ps.setLong(1, carId);
	                ps.setLong(2, reservationId);
	                ps.setLong(3, userRouteId);
	                ps.setInt(4, statusId);
	                ps.setString(5, rentalStart);
	                ps.setString(6, rentalEnd);
	                ps.setLong(7, totalPrice);
	                ps.executeUpdate();
	            }
	        }

	        conn.commit();


	    } catch (Exception e) {
	        conn.rollback();
	        System.out.println("[❌ ERROR] Reservation creation failed: " + e.getMessage());
	        throw new SQLException("통합 예약 생성 실패", e);
	    }

	    return reservationId;
	}


    /**
     * 예약 상세 정보를 조회합니다.
     * 마스터 예약 정보, 숙소 정보, 차량 정보를 JOIN하여 하나의 DTO로 반환합니다.
     * @param reservationId 조회할 예약의 ID.
     * @param userId 예약을 조회하려는 사용자의 ID (본인 확인용).
     * @return 예약 상세 정보 (ReservationDetailDTO), 없거나 본인 예약이 아니면 null.
     */
    public ReservationDetailDTO getReservationDetail(long reservationId, long userId) {
        String sql = """
            SELECT
                r.reservation_id,
                r.user_id,
                r.user_route_id,
                r.reservation_price,
                r.reservation_start_date,
                r.reservation_end_date,
                r.status_id,
                s.status_name,
                rm.room_name,
                ar.checkin_date,
                ar.checkout_date,
                c.car_name,
                cr.pickup_date,
                cr.dropoff_date
            FROM tblReservation r
                JOIN tblReservationStatus s ON r.status_id = s.status_id
                LEFT JOIN tblAccomReservation ar ON r.reservation_id = ar.reservation_id
                LEFT JOIN tblAccomRoom rm ON ar.room_id = rm.room_id
                LEFT JOIN tblCarReservation cr ON r.reservation_id = cr.reservation_id
                LEFT JOIN tblRentalCar c ON cr.car_id = c.car_id
            WHERE r.reservation_id = ? AND r.user_id = ?
        """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, reservationId);
            ps.setLong(2, userId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    ReservationDetailDTO d = new ReservationDetailDTO();
                    d.setReservation_id(rs.getLong("reservation_id"));
                    d.setUser_id(rs.getLong("user_id"));
                    d.setUser_route_id(rs.getLong("user_route_id"));
                    d.setReservation_price(rs.getLong("reservation_price"));
                    d.setReservation_start_date(rs.getDate("reservation_start_date"));
                    d.setReservation_end_date(rs.getDate("reservation_end_date"));
                    d.setStatus_id(rs.getInt("status_id"));
                    d.setStatus_name(rs.getString("status_name"));
                    d.setRoom_name(rs.getString("room_name"));
                    d.setCheckin_date(rs.getDate("checkin_date"));
                    d.setCheckout_date(rs.getDate("checkout_date"));
                    d.setCar_name(rs.getString("car_name"));
                    d.setPickup_date(rs.getDate("pickup_date"));
                    d.setDropoff_date(rs.getDate("dropoff_date"));
                    return d;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }




}// class