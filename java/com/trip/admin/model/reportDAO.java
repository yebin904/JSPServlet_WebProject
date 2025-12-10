package com.trip.admin.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.test.util.DBUtil;

/**
 * 신고 관련 데이터베이스 작업을 처리하는 DAO 클래스
 * @author jsg
 * @version 1.0
 * @since 2025.10.24
 */
public class reportDAO {

    private Connection conn;

    /**
     * reportDAO 객체를 생성하고 데이터베이스 연결을 초기화합니다.
     */
    public reportDAO() {
    	DBUtil dbUtil = new DBUtil();
        this.conn = dbUtil.open();
    }
    
    /**
     * '대기 중'인 신고 목록을 가져오는 메소드
     * @return 'PENDING' 상태의 신고 목록
     */
    public List<reportDTO> getPendingReports() {
        return getReportsByStatus("PENDING");
    }

    /**
     * '처리 완료'된 신고 목록을 가져오는 메소드
     * @return 'PROCESSED' 상태의 신고 목록
     */
    public List<reportDTO> getProcessedReports() {
        return getReportsByStatus("PROCESSED");
    }

    /**
     * 특정 상태의 신고 목록을 조회합니다.
     * @param statusType 조회할 신고 상태 (예: "PENDING", "PROCESSED")
     * @return 해당 상태의 신고 목록
     */
    private List<reportDTO> getReportsByStatus(String statusType) {
        List<reportDTO> list = new ArrayList<>();
        String sql = "";
        
        try {
            String baseSql = "SELECT "
                         + "    r.report_id, r.report_target_id, r.report_reason_type, r.report_status, r.report_regdate, "
                         + "    r.report_target_type, "
                         + "    u_reporter.nickname AS reporter_nickname, "
                         + "    u_reported.nickname AS reported_user_nickname, "
                         + "    CASE r.report_target_type "
                         + "        WHEN 'findboard' THEN (SELECT f.find_board_title FROM tblFindBoard f WHERE f.find_board_id = r.report_target_id) "
                         + "        WHEN 'question' THEN (SELECT q.question_board_title FROM tblQuestionBoard q WHERE q.question_board_id = r.report_target_id) "
                         + "        WHEN 'review' THEN (SELECT re.review_board_title FROM tblReviewBoard re WHERE re.review_post_id = r.report_target_id) "
                         + "        WHEN 'hotdeal' THEN (SELECT h.hotdeal_title FROM tblHotDealPost h WHERE h.hotdeal_id = r.report_target_id) "
                         + "        ELSE '삭제되거나 알 수 없는 게시글' "
                         + "    END as post_title "
                         + "FROM tblReports r "
                         + "    INNER JOIN tblUser u_reported ON r.reported_user_id = u_reported.user_id "
                         + "    INNER JOIN tblUser u_reporter ON r.user_id = u_reporter.user_id ";

            if ("PENDING".equals(statusType)) {
                sql = baseSql + "WHERE r.report_status = 'PENDING' ORDER BY r.report_regdate DESC";
            } else { 
                sql = baseSql + "WHERE r.report_status <> 'PENDING' ORDER BY r.report_regdate DESC";
            }

            PreparedStatement pstat = conn.prepareStatement(sql);
            ResultSet rs = pstat.executeQuery();

            while (rs.next()) {
                reportDTO dto = new reportDTO();
                dto.setReportId(rs.getInt("report_id"));
                dto.setReportTargetId(rs.getInt("report_target_id"));
                dto.setReportReasonType(rs.getString("report_reason_type"));
                dto.setReportStatus(rs.getString("report_status"));
                dto.setReportRegdate(rs.getDate("report_regdate"));
                dto.setReporterNickname(rs.getString("reporter_nickname"));
                dto.setReportedUserNickname(rs.getString("reported_user_nickname"));
                dto.setPostTitle(rs.getString("post_title"));
                dto.setReportTargetType(rs.getString("report_target_type"));
                list.add(dto);
            }
            rs.close();
            pstat.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * 신고 상태를 업데이트합니다.
     * @param reportId 신고 ID
     * @param status 변경할 상태 (예: "APPROVED", "REJECTED")
     */
    public void updateReportStatus(int reportId, String status) {
        try {
            String sql = "UPDATE tblReports SET report_status = ? WHERE report_id = ?";
            PreparedStatement pstat = conn.prepareStatement(sql);
            pstat.setString(1, status);
            pstat.setInt(2, reportId);
            pstat.executeUpdate();
            pstat.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 특정 게시글을 숨김 처리합니다.
     * @param targetType 게시글 유형 (예: "question", "review")
     * @param postId 게시글 ID
     */
    public void hidePost(String targetType, int postId) {
        String tableName = "";
        String columnId = "";
        String statusColumnName = "";

        if ("question".equalsIgnoreCase(targetType)) {
            tableName = "tblQuestionBoard"; 
            columnId = "question_board_id";
            statusColumnName = "post_status";
        } else if ("review".equalsIgnoreCase(targetType)) {
            tableName = "tblReviewBoard"; 
            columnId = "review_post_id";
            statusColumnName = "post_status";
        } else if ("hotdeal".equalsIgnoreCase(targetType)) {
            tableName = "tblHotDealPost"; 
            columnId = "hotdeal_id";
            statusColumnName = "post_status";
        } else if ("findboard".equalsIgnoreCase(targetType)) {
            tableName = "tblFindBoard";
            columnId = "find_board_id";
            statusColumnName = "find_board_report_status";
        } else {
            return;
        }

        try {
            String sql = String.format("UPDATE %s SET %s = 'N' WHERE %s = ?", tableName, statusColumnName, columnId);
            PreparedStatement pstat = conn.prepareStatement(sql);
            pstat.setInt(1, postId);
            pstat.executeUpdate();
            pstat.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * 새로운 신고를 접수합니다.
     * @param targetType 신고 대상 유형
     * @param targetId 신고 대상 ID
     * @param reporterId 신고자 ID
     * @param reportedUserId 신고당한 사용자 ID
     * @param reason 신고 사유
     * @return 성공 시 1, 실패 시 0
     */
    public int addReport(String targetType, int targetId, int reporterId, int reportedUserId, String reason) {
        
        int result = 0;

        try {
            String sql = "INSERT INTO tblReports (report_id, report_target_type, report_target_id, user_id, reported_user_id, report_reason_type) " +
                         "VALUES (seq_reports.NEXTVAL, ?, ?, ?, ?, ?)";
            
            PreparedStatement pstat = conn.prepareStatement(sql);
            pstat.setString(1, targetType);
            pstat.setInt(2, targetId);
            pstat.setInt(3, reporterId);
            pstat.setInt(4, reportedUserId);
            pstat.setString(5, reason);
            
            result = pstat.executeUpdate();

            conn.commit();
            
            pstat.close();

        } catch (Exception e) {
            e.printStackTrace();
            
            try {
                conn.rollback();
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return result;
    }

}