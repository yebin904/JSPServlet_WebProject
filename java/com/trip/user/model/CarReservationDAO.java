package com.trip.user.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.test.util.DBUtil;
import com.trip.board.model.BoardDTO;

/**
 * 사용자 렌터카 예약 관련 데이터베이스 작업을 처리하는 DAO 클래스
 * (숙소 예약 취소 기능도 포함하고 있습니다)
 * @author jsg
 * @version 1.0
 * @since 2025.10.24
 */
public class CarReservationDAO {
	private DBUtil util;
	private Connection conn;
	private Statement stat;
	private PreparedStatement pstat;
	private ResultSet rs;

	/**
     * 기본 생성자입니다.
     * DBUtil 유틸리티 클래스를 통해 데이터베이스 연결을 초기화합니다.
     */
	public CarReservationDAO() {
		try {
			util = new DBUtil();
			conn = util.open();
			stat = conn.createStatement();
		} catch (Exception e) {
			e.printStackTrace();
		}
	 }
	
	/**
	 * 특정 사용자의 렌터카 예약 총 건수를 조회합니다.
	 * `vwCarReservationDetails` 뷰를 사용하며, 검색 조건을 지원합니다.
	 *
	 * @param map 검색 조건(`search`, `column`, `word`)과 사용자 ID(`seq`)가 담긴 맵
	 * @return 조건에 맞는 총 렌터카 예약 건수
	 */
	public int getCarResrvationTotalCount(Map<String, String> map) {
		try {

			String where = "";

			if (map.get("search").equals("y")) {

				where = String.format("and %s like '%%%s%%'", map.get("column"), map.get("word"));

			}

			String sql = "select count(*) as cnt from vwCarReservationDetails where user_id = ?" + where;

			pstat = conn.prepareStatement(sql);
			pstat.setString(1, map.get("seq"));
			
			rs = pstat.executeQuery();

			if (rs.next()) {

				return rs.getInt("cnt");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return 0;
	}

	/**
	 * 특정 사용자의 렌터카 예약 목록을 페이징 처리하여 조회합니다.
	 * `vwCarReservationDetails` 뷰를 사용하며, '취소됨'(status_id=3) 상태를 제외하고 조회합니다.
	 *
	 * @param map 페이징 정보(`begin`, `end`), 검색 조건(`search`, `column`, `word`)이 담긴 맵
	 * @return 조회된 렌터카 예약 목록 (List<CarReservationDTO>)
	 */
	public List<CarReservationDTO> totalCarList(Map<String, String> map) {
		try {
			
			//목록보기 > select * from vwBoard
			//검색하기 > select * from vwBoard where 조건
			
			String where = "";
			
			
			if (map.get("search").equals("y")) {
				
				//where = "where 조건";
				//where subject like '%검색어%'
				//where content like '%검색어%'
				//where name like '%검색어%'

				where = String.format("and %s like '%%%s%%'"
										, map.get("column")
										, map.get("word"));

			}
			
			String sql = "";
				sql = String.format("select * from (select a.*, rownum as rnum FROM ( SELECT * FROM vwCarReservationDetails ORDER BY reservation_id DESC ) a WHERE status_id != 3 %s) where rnum between %s and %s"
								, where
								, map.get("begin")
								, map.get("end"));
		
			
			stat = conn.createStatement();
			rs = stat.executeQuery(sql);
			
			List<CarReservationDTO> list = new ArrayList<CarReservationDTO>();
			
			while (rs.next()) {
				
				CarReservationDTO dto = new CarReservationDTO();
				
				dto.setSeq(rs.getString("reservation_id"));
				dto.setUseq(rs.getString("user_id"));
				dto.setCarseq(rs.getString("car_reservation_id"));
				dto.setStatusname(rs.getString("status_name"));
				dto.setPickupdate(rs.getString("pickup_date"));
				dto.setDropoffdate(rs.getString("dropoff_date"));
				dto.setPickuplocation(rs.getString("pickup_location"));
				dto.setDropofflocation(rs.getString("dropoff_location"));
				dto.setCartotalprice(rs.getString("car_total_price"));
				dto.setCarnotes(rs.getString("car_notes"));
				dto.setCartype(rs.getString("car_type"));
				dto.setCarfueltype(rs.getString("car_fuel_type"));
				dto.setCarname(rs.getString("car_name"));


				
				list.add(dto);		
			}	
			
			
			return list;
			
		} catch (Exception e) {
			e.printStackTrace();
		}
				
		
		return null;
	}

	/**
	 * 특정 렌터카 예약 상세 정보를 조회합니다.
	 * `vwCarReservationDetails` 뷰를 사용합니다.
	 *
	 * @param seq    마스터 예약 ID (reservation_id)
	 * @param carseq 차량 예약 ID (car_reservation_id)
	 * @return 조회된 렌터카 예약 상세 정보 (CarReservationDTO), 없으면 null
	 */
	public CarReservationDTO carGet(String seq, String carseq) {
try {
			
			
			String sql = "";
				sql = String.format("select * from vwCarReservationDetails where reservation_id = ? and car_reservation_id = ?");

		
			
				pstat = conn.prepareStatement(sql);
				pstat.setString(1, seq);
				pstat.setString(2, carseq);

				rs = pstat.executeQuery();
			
			while (rs.next()) {
				
				CarReservationDTO dto = new CarReservationDTO();
				
				dto.setSeq(rs.getString("reservation_id"));
				dto.setUseq(rs.getString("user_id"));
				dto.setCarseq(rs.getString("car_reservation_id"));
				dto.setStatusname(rs.getString("status_name"));
				dto.setPickupdate(rs.getString("pickup_date"));
				dto.setDropoffdate(rs.getString("dropoff_date"));
				dto.setPickuplocation(rs.getString("pickup_location"));
				dto.setDropofflocation(rs.getString("dropoff_location"));
				dto.setCartotalprice(rs.getString("car_total_price"));
				dto.setCarnotes(rs.getString("car_notes"));
				dto.setCartype(rs.getString("car_type"));
				dto.setCarfueltype(rs.getString("car_fuel_type"));
				dto.setCarname(rs.getString("car_name"));

				return dto;
				
			}	
			
			
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
				
		
		return null;
	}


	/**
	 * 특정 렌터카 예약을 '취소됨'(status_id=3) 상태로 업데이트합니다.
	 *
	 * @param carseq 취소할 차량 예약 ID (car_reservation_id)
	 */
	public void addCarCancel(String carseq) {
		// queryParamNoReturn
		try {

			String sql = "update tblCarReservation set status_id = 3 where car_reservation_id = ?";

			pstat = conn.prepareStatement(sql);
			pstat.setString(1, carseq);

			pstat.executeUpdate();

		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	/**
	 * 특정 숙소 예약을 '취소됨'(status_id=3) 상태로 업데이트합니다.
	 * (주의: 이 메소드는 렌터카 DAO 내에 정의되어 있습니다.)
	 *
	 * @param accomseq 취소할 숙소 예약 ID (accom_reservation_id)
	 */
	public void addAccomCancel(String accomseq) {
		// queryParamNoReturn
				try {

					String sql = "update tblAccomReservation set status_id = 3 where accom_reservation_id = ?";

					pstat = conn.prepareStatement(sql);
					pstat.setString(1, accomseq);

					pstat.executeUpdate();

				} catch (Exception e) {
					e.printStackTrace();
				}
		
	}
}