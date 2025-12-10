package com.trip.user.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.test.util.DBUtil;

/**
 * 사용자 숙소 예약 관련 데이터베이스 작업을 처리하는 DAO 클래스
 * @author jsg
 * @version 1.0
 * @since 2025.10.24
 */
public class AccomReservationDAO {
	private DBUtil util;
	private Connection conn;
	private Statement stat;
	private PreparedStatement pstat;
	private ResultSet rs;

	/**
     * 기본 생성자입니다.
     * DBUtil 유틸리티 클래스를 통해 데이터베이스 연결을 초기화합니다.
     */
	public AccomReservationDAO() {
		try {
			util = new DBUtil();
			conn = util.open();
			stat = conn.createStatement();
		} catch (Exception e) {
			e.printStackTrace();
		}
}

	/**
	 * 특정 사용자의 숙소 예약 총 건수를 조회합니다.
	 * `vwAccomReservAtionDetails` 뷰를 사용하며, 검색 조건을 지원합니다.
	 *
	 * @param map 검색 조건(`search`, `column`, `word`)과 사용자 ID(`seq`)가 담긴 맵
	 * @return 조건에 맞는 총 숙소 예약 건수
	 */
	public int getAccomResrvationTotalCount(Map<String, String> map) {
		try {

			String where = "";

			if (map.get("search").equals("y")) {

				where = String.format("and %s like '%%%s%%'", map.get("column"), map.get("word"));

			}

			String sql = "select count(*) as cnt from vwAccomReservAtionDetails where user_id = ?" + where;

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
	 * 특정 사용자의 숙소 예약 목록을 페이징 처리하여 조회합니다.
	 * `vwAccomReservAtionDetails` 뷰를 사용하며, '취소됨'(status_id=3) 상태를 제외하고 조회합니다.
	 *
	 * @param map 페이징 정보(`begin`, `end`), 검색 조건(`search`, `column`, `word`)이 담긴 맵
	 * @return 조회된 숙소 예약 목록 (List<AccomReservationDTO>)
	 */
	public List<AccomReservationDTO> totalAccomList(Map<String, String> map) {
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
				sql = String.format("select * from (select a.*, rownum as rnum FROM ( SELECT * FROM vwAccomReservAtionDetails ORDER BY reservation_id DESC ) a WHERE status_id != 3 %s) where rnum between %s and %s"
								, where
								, map.get("begin")
								, map.get("end"));
		
			
			stat = conn.createStatement();
			rs = stat.executeQuery(sql);
			
			List<AccomReservationDTO> list = new ArrayList<AccomReservationDTO>();
			
			while (rs.next()) {
				
				AccomReservationDTO dto = new AccomReservationDTO();
				
				dto.setSeq(rs.getString("reservation_id"));
				dto.setUseq(rs.getString("user_id"));
				dto.setRoomtotalprice(rs.getString("room_total_price"));
				dto.setAccomseq(rs.getString("accom_reservation_id"));
				dto.setRoomname(rs.getString("room_name"));
				dto.setPlacename(rs.getString("place_name"));
				dto.setPlaceaddress(rs.getString("place_address"));
				dto.setRoomtype(rs.getString("room_type"));
				dto.setAccomtype(rs.getString("accom_type"));
				dto.setGuestcount(rs.getString("guest_count"));
				dto.setCheckindate(rs.getString("checkin_date"));
				dto.setCheckoutdate(rs.getString("checkout_date"));
				
				System.out.println("test111");



				
				list.add(dto);		
			}	
			
			
			return list;
			
		} catch (Exception e) {
			e.printStackTrace();
		}
				
		
		return null;
	}

	/**
	 * 특정 숙소 예약 상세 정보를 조회합니다.
	 * `vwAccomReservAtionDetails` 뷰를 사용합니다.
	 * (참고: 메소드 이름이 'carGet'으로 되어 있으나 실제로는 숙소 정보를 조회합니다.)
	 *
	 * @param seq      마스터 예약 ID (reservation_id)
	 * @param accomseq 숙소 예약 ID (accom_reservation_id)
	 * @return 조회된 숙소 예약 상세 정보 (AccomReservationDTO), 없으면 null
	 */
	public AccomReservationDTO carGet(String seq, String accomseq) {
		try {
		String sql = "";
		sql = String.format("select * from vwAccomReservAtionDetails where reservation_id = ? and accom_reservation_id = ?");


	
		pstat = conn.prepareStatement(sql);
		pstat.setString(1, seq);
		pstat.setString(2, accomseq);

		rs = pstat.executeQuery();
	
	while (rs.next()) {
		
		AccomReservationDTO dto = new AccomReservationDTO();
		
		dto.setSeq(rs.getString("reservation_id"));
		dto.setUseq(rs.getString("user_id"));
		dto.setRoomtotalprice(rs.getString("room_total_price"));
		dto.setRoomname(rs.getString("room_name"));
		dto.setAccomseq(rs.getString("accom_reservation_id"));
		dto.setPlacename(rs.getString("place_name"));
		dto.setPlaceaddress(rs.getString("place_address"));
		dto.setRoomtype(rs.getString("room_type"));
		dto.setAccomtype(rs.getString("accom_type"));
		dto.setGuestcount(rs.getString("guest_count"));
		dto.setCheckindate(rs.getString("checkin_date"));
		dto.setCheckoutdate(rs.getString("checkout_date"));


		return dto;
		
	}	
	
	
	
	
	} catch (Exception e) {
		e.printStackTrace();
	}
			
	
	return null;
}
}