package com.trip.admin.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.test.util.DBUtil;

/**
 * 통계 관련 데이터베이스 작업을 처리하는 DAO 클래스
 * @author jsg
 * @version 1.0
 * @since 2025.10.24
 */
public class statDAO {

    private Connection conn;

    /**
     * statDAO 객체를 생성하고 데이터베이스 연결을 초기화합니다.
     */
    public statDAO() { 
    	DBUtil dbUtil = new DBUtil();
        this.conn = dbUtil.open();
    }

    /**
     * 연도별 가입자 통계를 조회합니다.
     * @return 연도별 가입자 수를 담은 statDTO 객체 리스트
     */
    public List<statDTO> getYearlySignUps() {
        List<statDTO> list = new ArrayList<>();
        try {
            String sql = "SELECT TO_CHAR(user_regdate, 'YYYY') AS category, COUNT(*) AS count " +
                         "FROM tblUser GROUP BY TO_CHAR(user_regdate, 'YYYY') ORDER BY category ASC";
            PreparedStatement pstat = conn.prepareStatement(sql);
            ResultSet rs = pstat.executeQuery();
            while (rs.next()) {
                statDTO dto = new statDTO();
                dto.setCategory(rs.getString("category"));
                dto.setCount(rs.getInt("count"));
                list.add(dto);
            }
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }
    
    /**
     * 연도별 탈퇴자 통계를 조회합니다.
     * @return 연도별 탈퇴자 수를 담은 statDTO 객체 리스트
     */
    public List<statDTO> getYearlyWithdrawals() {
        List<statDTO> list = new ArrayList<>();
        try {
            String sql = "SELECT TO_CHAR(user_withdrawal_date, 'YYYY') AS category, COUNT(*) AS count " +
                         "FROM tblUser WHERE user_status = 'N' " +
                         "GROUP BY TO_CHAR(user_withdrawal_date, 'YYYY') ORDER BY category ASC";
            PreparedStatement pstat = conn.prepareStatement(sql);
            ResultSet rs = pstat.executeQuery();
            while (rs.next()) {
                statDTO dto = new statDTO();
                dto.setCategory(rs.getString("category"));
                dto.setCount(rs.getInt("count"));
                list.add(dto);
            }
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }

    /**
     * 회원 성별 통계를 조회합니다.
     * @return 성별 회원 수를 담은 statDTO 객체 리스트
     */
    public List<statDTO> getMemberGenderStats() {
        List<statDTO> list = new ArrayList<>();
        try {
            String sql = "SELECT " +
                         "    CASE " +
                         "        WHEN user_gender = 'M' THEN '남자' " +
                         "        WHEN user_gender = 'F' THEN '여자' " +
                         "        ELSE '기타' " +
                         "    END AS category, " +
                         "    COUNT(*) AS count " +
                         "FROM tblUser " +
                         "GROUP BY user_gender";
                         
            PreparedStatement pstat = conn.prepareStatement(sql);
            ResultSet rs = pstat.executeQuery();
            while (rs.next()) {
                statDTO dto = new statDTO();
                dto.setCategory(rs.getString("category"));
                dto.setCount(rs.getInt("count"));
                list.add(dto);
            }
        } catch (Exception e) { 
            e.printStackTrace(); 
        }
        return list;
    }

    /**
     * 전체 회원 수를 조회합니다.
     * @return 활동 중인 전체 회원 수
     */
    public int getTotalUserCount() {
        int count = 0;
        try {
            String sql = "SELECT COUNT(*) AS cnt FROM tblUser WHERE user_status_id = 1";
            PreparedStatement pstat = conn.prepareStatement(sql);
            ResultSet rs = pstat.executeQuery();
            if (rs.next()) {
                count = rs.getInt("cnt");
            }
        } catch (Exception e) { e.printStackTrace(); }
        return count;
    }

    /**
     * 모든 게시판의 총 게시글 수를 조회합니다.
     * @return 모든 게시판의 총 게시글 수
     */
    public int getTotalPostCount() {
        int count = 0;
        try {
            String sql = "SELECT COUNT(*) AS cnt FROM VWINTEGRATEDBOARD";
            PreparedStatement pstat = conn.prepareStatement(sql);
            ResultSet rs = pstat.executeQuery();
            if (rs.next()) {
                count = rs.getInt("cnt");
            }
        } catch (Exception e) { e.printStackTrace(); }
        return count;
    }
    
    /**
     * 여행 후기 게시글 수를 조회합니다.
     * @return 여행 후기 게시글 수
     */
    public int getTotalReviewCount() {
        int count = 0;
        try {
            String sql = "SELECT COUNT(*) AS cnt FROM tblReviewBoard WHERE post_status = 'Y'";
            PreparedStatement pstat = conn.prepareStatement(sql);
            ResultSet rs = pstat.executeQuery();
            if (rs.next()) {
                count = rs.getInt("cnt");
            }
        } catch (Exception e) { e.printStackTrace(); }
        return count;
    }

    /**
     * 동행찾기 게시글 수를 조회합니다.
     * @return 동행찾기 게시글 수
     */
    public int getTotalFindBoardCount() {
        int count = 0;
        try {
            String sql = "SELECT COUNT(*) AS cnt FROM tblFindBoard WHERE find_board_report_status = 'N'";
            PreparedStatement pstat = conn.prepareStatement(sql);
            ResultSet rs = pstat.executeQuery();
            if (rs.next()) {
                count = rs.getInt("cnt");
            }
        } catch (Exception e) { e.printStackTrace(); }
        return count;
    }
}