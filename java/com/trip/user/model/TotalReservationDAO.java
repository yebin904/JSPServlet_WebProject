package com.trip.user.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import com.test.util.DBUtil;

/**
 * 사용자 전체 예약 관련 데이터베이스 작업을 처리하는 DAO 클래스
 * (현재 구체적인 기능은 구현되지 않았습니다.)
 * @author jsg
 * @version 1.0
 * @since 2025.10.24
 */
public class TotalReservationDAO {
	private DBUtil util;
	private Connection conn;
	private Statement stat;
	private PreparedStatement pstat;
	private ResultSet rs;

	/**
     * 기본 생성자입니다.
     * DBUtil 유틸리티 클래스를 통해 데이터베이스 연결을 초기화합니다.
     */
	public TotalReservationDAO() {
		try {
			util = new DBUtil();
			conn = util.open();
			stat = conn.createStatement();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}