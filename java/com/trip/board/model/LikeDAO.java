package com.trip.board.model;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import com.test.util.DBUtil;

/**
 * 핫딜 게시판의 좋아요 및 스크랩 관련 데이터베이스 작업을 처리하는 DAO 클래스
 * @author jsg
 * @version 1.0
 * @since 2025.10.24
 */
public class LikeDAO {

	private DBUtil util;
	/**
     * 데이터베이스 연결 객체
     */
	private Connection conn;
	/**
     * SQL 실행 객체 (정적 쿼리용)
     */
	private Statement stat;
	/**
     * SQL 실행 객체 (동적 쿼리용)
     */
	private PreparedStatement pstat;
	/**
     * 쿼리 결과 집합 객체
     */
	private ResultSet rs;

	/**
     * LikeDAO 생성자.
     * DB 유틸리티를 사용하여 데이터베이스 연결을 엽니다.
     */
	public LikeDAO() {
		try {
			util = new DBUtil();
			conn = util.open();
			stat = conn.createStatement();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
     * 사용자가 특정 핫딜 게시물에 '좋아요'를 눌렀는지 확인합니다.
     *
     * @param seq  사용자 ID (user_id)
     * @param bseq 핫딜 게시물 ID (hotdeal_id)
     * @return '좋아요'를 눌렀으면 1 이상(count), 아니면 0
     */
	public int likeCheck(String seq, String bseq) {
		try {

			String sql = "select count(*) from tblHotDealLikes where user_id = ? and hotdeal_id = ?";

			pstat = conn.prepareStatement(sql);
			pstat.setString(1, seq);
			pstat.setString(2, bseq);

			 rs = pstat.executeQuery();
			 
			 if (rs.next()) {

		            return rs.getInt(1); 
		        }

		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return 0;
		
	}

	/**
     * 핫딜 게시물의 '좋아요'를 취소합니다. (DB에서 삭제)
     *
     * @param seq  사용자 ID (user_id)
     * @param bseq 핫딜 게시물 ID (hotdeal_id)
     */
	public void likeDel(String seq, String bseq) {
		try {

			String sql = "delete from tblHotDealLikes where user_id = ? and hotdeal_id = ?";

			pstat = conn.prepareStatement(sql);
			pstat.setString(1, seq);
			pstat.setString(2, bseq);


			pstat.executeUpdate();

		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
	}

	/**
     * 핫딜 게시물에 '좋아요'를 추가합니다.
     *
     * @param seq  사용자 ID (user_id)
     * @param bseq 핫딜 게시물 ID (hotdeal_id)
     */
	public void likeAdd(String seq, String bseq) {
		try {

			String sql = "insert into tblHotDealLikes (user_id, hotdeal_id) values (?,?)";

			pstat = conn.prepareStatement(sql);
			pstat.setString(1, seq);
			pstat.setString(2, bseq);


			pstat.executeUpdate();

		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	/**
     * 사용자가 특정 핫딜 게시물을 '스크랩'했는지 확인합니다.
     *
     * @param seq  사용자 ID (user_id)
     * @param bseq 핫딜 게시물 ID (hotdeal_id)
     * @return '스크랩'을 했으면 1 이상(count), 아니면 0
     */
	public int scrapCheck(String seq, String bseq) {
		try {

			String sql = "select count(*) from tblHotDealScrap where user_id = ? and hotdeal_id = ?";

			pstat = conn.prepareStatement(sql);
			pstat.setString(1, seq);
			pstat.setString(2, bseq);

			 rs = pstat.executeQuery();
			 
			 if (rs.next()) {

		            return rs.getInt(1); 
		        }

		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return 0;
	}

	/**
     * 핫딜 게시물 '스크랩'을 취소합니다. (DB에서 삭제)
     *
     * @param seq  사용자 ID (user_id)
     * @param bseq 핫딜 게시물 ID (hotdeal_id)
     */
	public void scrapDel(String seq, String bseq) {
		try {

			String sql = "delete from tblHotDealScrap where user_id = ? and hotdeal_id = ?";

			pstat = conn.prepareStatement(sql);
			pstat.setString(1, seq);
			pstat.setString(2, bseq);


			pstat.executeUpdate();

		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	/**
     * 핫딜 게시물을 '스크랩'합니다. (DB에 추가)
     *
     * @param seq  사용자 ID (user_id)
     * @param bseq 핫딜 게시물 ID (hotdeal_id)
     */
	public void scrapAdd(String seq, String bseq) {
		try {

			String sql = "insert into tblHotDealScrap (hotdeal_scrap_id, user_id, hotdeal_id) values (seqHotDealScrap.nextVal,?,?)";

			pstat = conn.prepareStatement(sql);
			pstat.setString(1, seq);
			pstat.setString(2, bseq);


			pstat.executeUpdate();

		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}


	
	

	
}
