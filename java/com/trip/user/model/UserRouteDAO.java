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
 * 사용자 경로 관련 데이터베이스 작업을 처리하는 DAO 클래스
 * @author jsg
 * @version 1.0
 * @since 2025.10.24
 */
public class UserRouteDAO {
	private DBUtil util;
	private Connection conn;
	private Statement stat;
	private PreparedStatement pstat;
	private ResultSet rs;

	/**
     * 기본 생성자입니다.
     * DBUtil 유틸리티 클래스를 통해 데이터베이스 연결을 초기화합니다.
     */
	public UserRouteDAO() {
		try {
			util = new DBUtil();
			conn = util.open();
			stat = conn.createStatement();
		} catch (Exception e) {
			e.printStackTrace();
		}
}

	/**
	 * 특정 사용자의 경로 총 건수를 조회합니다.
	 * 검색 조건을 지원합니다.
	 *
	 * @param map 검색 조건(`search`, `column`, `word`)과 사용자 ID(`seq`)가 담긴 맵
	 * @return 조건에 맞는 총 경로 건수
	 */
	public int getUserRouteTotalCount(Map<String, String> map) {
		try {

			String where = "";

			if (map.get("search").equals("y")) {

				where = String.format("and %s like '%%%s%%'", map.get("column"), map.get("word"));

			}

			String sql = "select count(*) as cnt from tblUserRoute where user_id = ?" + where;

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
	 * 특정 사용자의 경로 목록을 페이징 처리하여 조회합니다.
	 * 검색 조건을 지원합니다.
	 *
	 * @param map 페이징 정보(`begin`, `end`), 검색 조건(`search`, `column`, `word`)이 담긴 맵
	 * @return 조회된 사용자 경로 목록 (List<UserRouteDTO>)
	 */
	public List<UserRouteDTO> UserRouteList(Map<String, String> map) {
try {
			
			//목록보기 > select * from vwBoard
			//검색하기 > select * from vwBoard where 조건
			
			String where = "";
			
			if (map.get("search").equals("y")) {
				
				//where = "where 조건";
				//where subject like '%검색어%'
				//where content like '%검색어%'
				//where name like '%검색어%'

				where = String.format("where %s like '%%%s%%'"
										, map.get("column")
										, map.get("word"));

			}
			
			String sql = "";
				sql = String.format("select * from (select a.*, rownum as rnum FROM ( SELECT * FROM tblUserRoute ORDER BY user_route_id DESC ) a %s) where rnum between %s and %s"
								, where
								, map.get("begin")
								, map.get("end"));
		
				System.out.println("DEBUG SQL: " + sql);

			stat = conn.createStatement();
			rs = stat.executeQuery(sql);
			
			System.out.println(sql);

			List<UserRouteDTO> list = new ArrayList<UserRouteDTO>();
			
			while (rs.next()) {
				
				UserRouteDTO dto = new UserRouteDTO();
				
				System.out.println("Query executed successfully. Checking results...");
				dto.setSeq(rs.getString("user_route_id"));
				dto.setUseq(rs.getString("user_id"));
				dto.setUserroutetitle(rs.getString("user_route_title"));
				dto.setUserroutedays(rs.getString("user_route_days"));
				dto.setUserroutestartdate(rs.getString("user_route_startdate"));
				dto.setUserrouteenddate(rs.getString("user_route_enddate"));
		
				System.out.println("테스트");

				System.out.println(dto);

				list.add(dto);		
			}	
			
			System.out.println(list);
			return list;
			
		} catch (Exception e) {
			e.printStackTrace();
		}
				
		
		return null;
	}
}