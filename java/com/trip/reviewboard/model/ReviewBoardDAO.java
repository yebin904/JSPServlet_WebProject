package com.trip.reviewboard.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.test.util.DBUtil;

/**
 * 리뷰 게시판 데이터베이스 작업을 처리하는 DAO 클래스
 * @author jsg
 * @version 1.0
 * @since 2025.10.24
 */
public class ReviewBoardDAO {

    private Connection conn;
    private Statement stat;
    private PreparedStatement pstat;
    private ResultSet rs;

    /**
     * ReviewBoardDAO 객체를 생성하고 데이터베이스 연결을 초기화합니다.
     */
    public ReviewBoardDAO() {
        try {
            conn = new DBUtil().open();
            stat = conn.createStatement();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 데이터베이스 관련 리소스를 닫습니다.
     */
    public void close() {
        try {
            if (rs != null) rs.close();
            if (pstat != null) pstat.close();
            if (stat != null) stat.close();
            if (conn != null) conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 페이징 및 검색 기능을 포함한 게시글 목록을 조회합니다.
     * @param map 검색 조건(col, word)과 페이징 정보(begin, end)를 담은 Map
     * @return ReviewBoardDTO 객체 리스트
     */
    public List<ReviewBoardDTO> list(Map<String, String> map) {
        List<ReviewBoardDTO> list = new ArrayList<>();
        try {
            
            String where = "";
            if ("y".equals(map.get("search"))) {
                where = "WHERE " + map.get("col") + " LIKE ?";
            }

            String sql = "SELECT * FROM (SELECT a.*, ROWNUM AS rnum FROM ("
            		   + "SELECT rb.*, u.nickname FROM tblReviewBoard rb INNER JOIN tblUser u ON rb.user_id = u.user_id "
                       + where
                       + " ORDER BY rb.review_post_id DESC) a) WHERE rnum BETWEEN ? AND ?";

            pstat = conn.prepareStatement(sql);
            
            int index = 1;
            if ("y".equals(map.get("search"))) {
                pstat.setString(index++, "%" + map.get("word") + "%");
            }
            pstat.setString(index++, map.get("begin"));
            pstat.setString(index++, map.get("end"));
            
            rs = pstat.executeQuery();

            while (rs.next()) {
                ReviewBoardDTO dto = new ReviewBoardDTO();
                dto.setReview_post_id(rs.getInt("review_post_id"));
                dto.setReview_board_title(rs.getString("review_board_title"));
                dto.setNickname(rs.getString("nickname"));
                dto.setReview_board_regdate(rs.getDate("review_board_regdate"));
                dto.setReview_board_count(rs.getInt("review_board_count"));
                list.add(dto);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * 검색 조건에 맞는 총 게시물 수를 조회합니다.
     * @param map 검색 조건(col, word)을 담은 Map
     * @return 총 게시물 수
     */
    public int getTotalCount(Map<String, String> map) {
        int total = 0;
        try {
            String where = "";
            if ("y".equals(map.get("search"))) {
                where = "WHERE " + map.get("col") + " LIKE ?";
            }
            String sql = "SELECT COUNT(*) AS cnt FROM tblReviewBoard " + where;
            
            pstat = conn.prepareStatement(sql);

            if ("y".equals(map.get("search"))) {
                pstat.setString(1, "%" + map.get("word") + "%");
            }

            rs = pstat.executeQuery();
            if (rs.next()) {
                total = rs.getInt("cnt");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return total;
    }
    
    /**
     * 새로운 게시물을 추가합니다.
     * @param dto 추가할 게시물 정보를 담은 ReviewBoardDTO 객체
     * @return 실행 결과 (1: 성공, 0: 실패)
     */
    public int addPost(ReviewBoardDTO dto) {
        String sql = "INSERT INTO tblReviewBoard (review_post_id, user_id, review_board_title, review_board_content) VALUES (seqReviewBoard.nextval, ?, ?, ?)";
        try {
            pstat = conn.prepareStatement(sql);
            pstat.setInt(1, dto.getUser_id());
            pstat.setString(2, dto.getReview_board_title());
            pstat.setString(3, dto.getReview_board_content());
            return pstat.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 게시물의 조회수를 1 증가시킵니다.
     * @param review_post_id 조회수를 증가시킬 게시물 ID
     */
    public void updateViewCount(int review_post_id) {
        String sql = "UPDATE tblReviewBoard SET review_board_count = review_board_count + 1 WHERE review_post_id = ?";
        try {
            pstat = conn.prepareStatement(sql);
            pstat.setInt(1, review_post_id);
            pstat.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 특정 게시물의 상세 정보를 조회합니다.
     * @param review_post_id 조회할 게시물 ID
     * @return 게시물 정보를 담은 ReviewBoardDTO 객체
     */
    public ReviewBoardDTO getPost(int review_post_id) {
        String sql = "SELECT rb.*, u.nickname FROM tblReviewBoard rb INNER JOIN tblUser u ON rb.user_id = u.user_id WHERE rb.review_post_id = ?";
        try {
            pstat = conn.prepareStatement(sql);
            pstat.setInt(1, review_post_id);
            rs = pstat.executeQuery();
            if (rs.next()) {
                ReviewBoardDTO dto = new ReviewBoardDTO();
                dto.setReview_post_id(rs.getInt("review_post_id"));
                dto.setUser_id(rs.getInt("user_id"));
                dto.setReview_board_title(rs.getString("review_board_title"));
                dto.setReview_board_content(rs.getString("review_board_content"));
                dto.setReview_board_count(rs.getInt("review_board_count"));
                dto.setReview_board_regdate(rs.getDate("review_board_regdate"));
                dto.setNickname(rs.getString("nickname"));
                return dto;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 게시물에 대한 좋아요 상태를 추가하거나 삭제합니다.
     * @param postId 게시물 ID
     * @param userId 사용자 ID
     * @return 좋아요 추가 시 true, 삭제 시 false
     */
    public boolean toggleLike(String postId, String userId) {
        boolean alreadyLiked = isLiked(postId, userId);
        try {
            String sql;
            if (alreadyLiked) {
                sql = "DELETE FROM tblReviewLike WHERE review_post_id=? AND user_id=?";
            } else {
                sql = "INSERT INTO tblReviewLike (review_post_id, user_id) VALUES (?, ?)";
            }
            pstat = conn.prepareStatement(sql);
            pstat.setString(1, postId);
            pstat.setString(2, userId);
            pstat.executeUpdate();
            
            updateLikeCount(postId);

            return !alreadyLiked;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 게시물에 대한 스크랩 상태를 추가하거나 삭제합니다.
     * @param postId 게시물 ID
     * @param userId 사용자 ID
     * @return 스크랩 추가 시 true, 삭제 시 false
     */
    public boolean toggleScrap(String postId, String userId) {
        boolean alreadyScrapped = isScrapped(postId, userId);
        try {
            String sql;
            if (alreadyScrapped) {
                sql = "DELETE FROM tblReviewScrap WHERE review_post_id=? AND user_id=?";
            } else {
                sql = "INSERT INTO tblReviewScrap (review_scrap_id, review_post_id, user_id) VALUES (seqReviewScrap.nextVal, ?, ?)";
            }
            pstat = conn.prepareStatement(sql);
            pstat.setString(1, postId);
            pstat.setString(2, userId);
            pstat.executeUpdate();

            updateScrapCount(postId);

            return !alreadyScrapped;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    
    /**
     * 사용자가 특정 게시물에 좋아요를 눌렀는지 확인합니다.
     * @param postId 게시물 ID
     * @param userId 사용자 ID
     * @return 좋아요를 눌렀으면 true, 아니면 false
     */
    public boolean isLiked(String postId, String userId) {
        try {
            String sql = "SELECT COUNT(*) FROM tblReviewLike WHERE review_post_id=? AND user_id=?";
            pstat = conn.prepareStatement(sql);
            pstat.setString(1, postId);
            pstat.setString(2, userId);
            rs = pstat.executeQuery();
            if (rs.next()) return rs.getInt(1) > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 사용자가 특정 게시물을 스크랩했는지 확인합니다.
     * @param postId 게시물 ID
     * @param userId 사용자 ID
     * @return 스크랩했으면 true, 아니면 false
     */
    public boolean isScrapped(String postId, String userId) {
        try {
            String sql = "SELECT COUNT(*) FROM tblReviewScrap WHERE review_post_id=? AND user_id=?";
            pstat = conn.prepareStatement(sql);
            pstat.setString(1, postId);
            pstat.setString(2, userId);
            rs = pstat.executeQuery();
            if (rs.next()) return rs.getInt(1) > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 게시물의 좋아요 수를 업데이트합니다.
     * @param postId 게시물 ID
     */
    private void updateLikeCount(String postId) {
        try {
            String sql = "UPDATE tblReviewBoard SET review_board_like_count = (SELECT COUNT(*) FROM tblReviewLike WHERE review_post_id = ?) WHERE review_post_id = ?";
            pstat = conn.prepareStatement(sql);
            pstat.setString(1, postId);
            pstat.setString(2, postId);
            pstat.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 게시물의 스크랩 수를 업데이트합니다.
     * @param postId 게시물 ID
     */
    private void updateScrapCount(String postId) {
        try {
            String sql = "UPDATE tblReviewBoard SET review_board_scrap_count = (SELECT COUNT(*) FROM tblReviewScrap WHERE review_post_id = ?) WHERE review_post_id = ?";
            pstat = conn.prepareStatement(sql);
            pstat.setString(1, postId);
            pstat.setString(2, postId);
            pstat.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * 게시물과 관련된 모든 데이터(댓글, 좋아요, 스크랩, 이미지)를 삭제합니다.
     * @param review_post_id 삭제할 게시물 ID
     * @return 실행 결과 (1: 성공, 0: 실패)
     */
    public int del(String review_post_id) {
        int result = 0;
        try {
            conn.setAutoCommit(false);

            String sql = "DELETE FROM tblReviewComment WHERE review_post_id = ?";
            pstat = conn.prepareStatement(sql);
            pstat.setString(1, review_post_id);
            pstat.executeUpdate();

            sql = "DELETE FROM tblReviewLike WHERE review_post_id = ?";
            pstat = conn.prepareStatement(sql);
            pstat.setString(1, review_post_id);
            pstat.executeUpdate();

            sql = "DELETE FROM tblReviewScrap WHERE review_post_id = ?";
            pstat = conn.prepareStatement(sql);
            pstat.setString(1, review_post_id);
            pstat.executeUpdate();
            
            sql = "DELETE FROM tblReviewImage WHERE review_post_id = ?";
            pstat = conn.prepareStatement(sql);
			pstat.setString(1, review_post_id);
            pstat.executeUpdate();

            sql = "DELETE FROM tblReviewBoard WHERE review_post_id = ?";
            pstat = conn.prepareStatement(sql);
            pstat.setString(1, review_post_id);
            result = pstat.executeUpdate();

            conn.commit();

        } catch (Exception e) {
            e.printStackTrace();
            try {
                conn.rollback();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        }
        return result;
    }
}