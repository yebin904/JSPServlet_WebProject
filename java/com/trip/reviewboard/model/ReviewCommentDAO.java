package com.trip.reviewboard.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.test.util.DBUtil;

/**
 * 리뷰 게시판 댓글 데이터베이스 액세스 객체입니다.
 * @author jsg
 * @version 1.0
 * @since 2025.10.24
 */
public class ReviewCommentDAO {

    private Connection conn;

    /**
     * 기본 생성자입니다. 데이터베이스 연결을 초기화합니다.
     */
    public ReviewCommentDAO() {
        try {
            conn = new DBUtil().open();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 데이터베이스 연결을 닫습니다.
     */
    public void close() {
        try {
            if (conn != null && !conn.isClosed()) conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 게시물에 대한 댓글 목록을 가져옵니다.
     * @param postId 게시물 ID
     * @return 댓글 목록
     */
    public List<ReviewCommentDTO> list(String postId) {
        List<ReviewCommentDTO> list = new ArrayList<>();
        String sql = "SELECT c.*, u.nickname FROM tblReviewComment c INNER JOIN tblUser u ON c.user_id = u.user_id WHERE c.review_post_id = ? AND c.review_comment_status = 'y' ORDER BY c.review_comment_id ASC";
        
        try (PreparedStatement pstat = conn.prepareStatement(sql)) {
            pstat.setString(1, postId);
            try (ResultSet rs = pstat.executeQuery()) {
                while (rs.next()) {
                    ReviewCommentDTO dto = new ReviewCommentDTO();
                    dto.setReview_comment_id(rs.getInt("review_comment_id"));
                    dto.setReview_post_id(rs.getInt("review_post_id"));
                    dto.setUser_id(rs.getInt("user_id"));
                    dto.setReview_comment_content(rs.getString("review_comment_content"));
                    dto.setReview_comment_regdate(rs.getDate("review_comment_regdate"));
                    dto.setNickname(rs.getString("nickname"));
                    list.add(dto);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * 새 댓글을 추가합니다.
     * @param dto 추가할 댓글 정보
     * @return 성공 시 1, 실패 시 0
     */
    public int add(ReviewCommentDTO dto) {
        String sql = "INSERT INTO tblReviewComment (review_comment_id, review_post_id, user_id, review_comment_content) VALUES (seqReviewComment.nextVal, ?, ?, ?)";
        try (PreparedStatement pstat = conn.prepareStatement(sql)) {
            pstat.setInt(1, dto.getReview_post_id());
            pstat.setInt(2, dto.getUser_id());
            pstat.setString(3, dto.getReview_comment_content());
            int result = pstat.executeUpdate();
            if (result > 0) {
                updateCommentCount(String.valueOf(dto.getReview_post_id()));
            }
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 댓글을 삭제합니다.
     * @param commentId 삭제할 댓글 ID
     * @param postId 게시물 ID
     * @return 성공 시 1, 실패 시 0
     */
    public int delete(String commentId, String postId) {
        String sql = "UPDATE tblReviewComment SET review_comment_status = 'n' WHERE review_comment_id = ?";
        try (PreparedStatement pstat = conn.prepareStatement(sql)) {
            pstat.setString(1, commentId);
            int result = pstat.executeUpdate();
            if (result > 0) {
                updateCommentCount(postId);
            }
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 게시물의 댓글 수를 업데이트합니다.
     * @param postId 게시물 ID
     */
    private void updateCommentCount(String postId) {
        String sql = "UPDATE tblReviewBoard SET review_board_comment_count = (SELECT COUNT(*) FROM tblReviewComment WHERE review_post_id = ? AND review_comment_status = 'y') WHERE review_post_id = ?";
        try (PreparedStatement pstat = conn.prepareStatement(sql)) {
            pstat.setString(1, postId);
            pstat.setString(2, postId);
            pstat.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}