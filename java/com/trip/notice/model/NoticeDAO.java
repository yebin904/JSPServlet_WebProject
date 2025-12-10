package com.trip.notice.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Map;

import com.test.util.DBUtil;

/**
 * 공지사항 관련 데이터베이스 작업을 처리하는 DAO 클래스
 * @author jsg
 * @version 1.0
 * @since 2025.10.24
 */
public class NoticeDAO {

	private DBUtil util;
	private Connection conn;
	private Statement stat;
	private PreparedStatement pstat;
	private ResultSet rs;

	/**
	 * NoticeDAO의 생성자입니다.
	 * 데이터베이스 연결을 초기화하고 Statement 객체를 생성합니다.
	 * DB 연결 중 오류 발생 시 스택 트레이스를 출력합니다.
	 */
	public NoticeDAO() {
		try {
			util = new DBUtil();
			conn = util.open();
			stat = conn.createStatement();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 공지사항 목록을 조회합니다. 검색 조건과 페이징 정보를 사용하여 데이터베이스에서 공지사항 목록을 가져옵니다.
	 * @param map 검색 조건(column, word) 및 페이징 정보(begin, end)를 담고 있는 Map 객체
	 * @return 조회된 NoticeDTO 객체들의 ArrayList. 오류 발생 시 null 반환.
	 */
	public ArrayList<NoticeDTO> getList(Map<String, String> map) {
		ArrayList<NoticeDTO> list = new ArrayList<NoticeDTO>();
		try {
			
			String where = "";
			
			//검색어 O
			if ("y".equals(map.get("search"))) {
				where = String.format("where %s like '%%' || ? || '%%'", map.get("column"));
			}

			String sql = String.format(
					"select * from (select a.*, rownum as rnum from (select * from tblNoticePost %s order by notice_regdate desc) a) where rnum between ? and ?"
					, where
			);

			pstat = conn.prepareStatement(sql);
			
			int index = 1;
			
			if ("y".equals(map.get("search"))) {
				pstat.setString(index++, map.get("word"));
			}
			pstat.setString(index++, map.get("begin"));
			pstat.setString(index++, map.get("end"));

			rs = pstat.executeQuery();
			
			while (rs.next()) {
				NoticeDTO dto = new NoticeDTO();
				
				dto.setRnum(rs.getString("rnum"));
				dto.setNotice_post_id(rs.getString("notice_post_id"));
				dto.setAdmin_id(rs.getString("admin_id"));
				dto.setNotice_header(rs.getString("notice_header"));
				dto.setNotice_view_count(rs.getString("notice_view_count"));
				dto.setNotice_regdate(rs.getString("notice_regdate"));
				
				list.add(dto);	
			}
			
			return list;

		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * 검색 조건에 해당하는 전체 공지사항의 개수를 조회합니다.
	 * @param map 검색 조건(column, word)을 담고 있는 Map 객체
	 * @return 검색 조건에 맞는 공지사항의 총 개수. 오류 발생 시 0 반환.
	 */
	public int getTotalCount(Map<String, String> map) {
		
		try {
			String where = "";
			
			if ("y".equals(map.get("search"))) {
				where = String.format("where %s like '%%' || ? || '%%'", map.get("column"));
			}
			
			String sql = "select count(*) as cnt from tblNoticePost" + where;
			
			pstat = conn.prepareStatement(sql);
			
			if("y".equals(map.get("search"))) {
				pstat.setString(1, map.get("word"));
			}
			
			rs = pstat.executeQuery();
			
			if (rs.next()) {

				return rs.getInt("cnt");				
			}	
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return 0;
	}

	
}