package com.trip.qna.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import com.test.util.DBUtil;

/**
 * Q&A 댓글 관련 데이터베이스 작업을 처리하는 DAO 클래스
 * @author jsg
 * @version 1.0
 * @since 2025.10.24
 */
public class QnACommentDAO {
    /**
     * 데이터베이스 연결을 위한 Connection 객체입니다.
     */
    private Connection conn;

    /**
     * QnACommentDAO의 생성자입니다.
     * 데이터베이스 연결을 초기화합니다.
     */
    public QnACommentDAO() {
        conn = new DBUtil().open();
    }

    /**
     * 데이터베이스 연결을 닫습니다.
     */
    public void close() {
        try { if (conn != null && !conn.isClosed()) conn.close(); } catch (Exception e) { e.printStackTrace(); }
    }

    // 댓글 목록
    /**
     * 특정 게시물에 속한 모든 댓글 목록을 조회합니다.
     * @param boardId 댓글을 조회할 게시물의 ID
     * @return 댓글 정보를 담은 QnACommentDTO 객체들의 리스트
     */
    public List<QnACommentDTO> list(String boardId) {
        List<QnACommentDTO> list = new ArrayList<>();
        String sql = "SELECT a.question_answer_id, a.user_id, u.nickname, a.question_board_id, "
                   + "a.question_answer_content, a.question_answer_regdate, a.question_answer_report_count, "
                   + "a.question_answer_status FROM tblQuestionAnswer a "
                   + "JOIN tblUser u ON a.user_id = u.user_id "
                   + "WHERE a.question_board_id=? AND a.question_answer_status='y' "
                   + "ORDER BY a.question_answer_regdate ASC";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, boardId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    QnACommentDTO dto = new QnACommentDTO();
                    dto.setQuestion_answer_id(rs.getString("question_answer_id"));
                    dto.setUser_id(rs.getString("user_id"));
                    dto.setNickname(rs.getString("nickname"));
                    dto.setQuestion_board_id(rs.getString("question_board_id"));
                    dto.setQuestion_answer_content(rs.getString("question_answer_content"));
                    dto.setQuestion_answer_regdate(rs.getString("question_answer_regdate"));
                    dto.setQuestion_answer_report_count(rs.getString("question_answer_report_count"));
                    dto.setQuestion_answer_status(rs.getString("question_answer_status"));
                    list.add(dto);
                }
            }
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }

    // 댓글 등록
    /**
     * 새로운 댓글을 데이터베이스에 추가합니다.
     * @param boardId 댓글이 속할 게시물의 ID
     * @param userId 댓글을 작성한 사용자의 ID
     * @param content 댓글 내용
     * @return 댓글 추가 성공 여부 (true: 성공, false: 실패)
     */
    public boolean addComment(String boardId, String userId, String content) {
        String sql = "INSERT INTO tblQuestionAnswer(question_board_id, user_id, question_answer_content, "
                   + "question_answer_status, question_answer_report_count, question_answer_regdate) "
                   + "VALUES (?, ?, ?, 'y', 0, SYSDATE)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, boardId);
            ps.setString(2, userId);
            ps.setString(3, content);
            return ps.executeUpdate() > 0;
        } catch (Exception e) { e.printStackTrace(); return false; }
    }

    // 댓글 가져오기
    /**
     * 댓글 ID를 사용하여 댓글 정보를 조회합니다.
     * @param commentId 조회할 댓글의 ID
     * @return 댓글 정보를 담은 QnACommentDTO 객체, 없으면 null 반환
     */
    public QnACommentDTO getCommentById(String commentId) {
        QnACommentDTO dto = null;
        String sql = "SELECT * FROM tblQuestionAnswer WHERE question_answer_id=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, commentId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    dto = new QnACommentDTO();
                    dto.setQuestion_answer_id(rs.getString("question_answer_id"));
                    dto.setQuestion_board_id(rs.getString("question_board_id"));
                    dto.setUser_id(rs.getString("user_id"));
                    dto.setQuestion_answer_content(rs.getString("question_answer_content"));
                    dto.setQuestion_answer_status(rs.getString("question_answer_status"));
                }
            }
        } catch (Exception e) { e.printStackTrace(); }
        return dto;
    }

    // 댓글 수정
    /**
     * 댓글 내용을 수정합니다. 관리자이거나 댓글 작성자 본인일 경우에만 수정 가능합니다.
     * @param commentId 수정할 댓글의 ID
     * @param userId 댓글 작성자의 ID (관리자가 아닐 경우 본인 확인용)
     * @param content 수정할 댓글 내용
     * @param isAdmin 현재 사용자가 관리자인지 여부
     * @return 댓글 수정 성공 여부 (true: 성공, false: 실패)
     */
    public boolean editComment(String commentId, String userId, String content, boolean isAdmin) {
        String sql = isAdmin
            ? "UPDATE tblQuestionAnswer SET question_answer_content=? WHERE question_answer_id=?"
            : "UPDATE tblQuestionAnswer SET question_answer_content=? WHERE question_answer_id=? AND user_id=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, content);
            ps.setString(2, commentId);
            if (!isAdmin) ps.setString(3, userId);
            return ps.executeUpdate() > 0;
        } catch (Exception e) { e.printStackTrace(); return false; }
    }

    // 댓글 삭제
    /**
     * 댓글을 삭제(상태 변경)합니다. 관리자이거나 댓글 작성자 본인일 경우에만 삭제 가능합니다.
     * @param commentId 삭제할 댓글의 ID
     * @param userId 댓글 작성자의 ID (관리자가 아닐 경우 본인 확인용)
     * @param isAdmin 현재 사용자가 관리자인지 여부
     * @return 댓글 삭제 성공 여부 (true: 성공, false: 실패)
     */
    public boolean deleteComment(String commentId, String userId, boolean isAdmin) {
        String sql = isAdmin
            ? "UPDATE tblQuestionAnswer SET question_answer_status='n' WHERE question_answer_id=?"
            : "UPDATE tblQuestionAnswer SET question_answer_status='n' WHERE question_answer_id=? AND user_id=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, commentId);
            if (!isAdmin) ps.setString(2, userId);
            return ps.executeUpdate() > 0;
        } catch (Exception e) { e.printStackTrace(); return false; }
    }

    // 댓글 신고
    /**
     * 댓글을 신고합니다. 해당 댓글의 신고 횟수를 1 증가시킵니다.
     * @param commentId 신고할 댓글의 ID
     * @return 신고 처리 성공 여부 (true: 성공, false: 실패)
     */
    public boolean reportComment(String commentId) {
        String sql = "UPDATE tblQuestionAnswer SET question_answer_report_count=question_answer_report_count+1 WHERE question_answer_id=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, commentId);
            return ps.executeUpdate() > 0;
        } catch (Exception e) { e.printStackTrace(); return false; }
    }
}
