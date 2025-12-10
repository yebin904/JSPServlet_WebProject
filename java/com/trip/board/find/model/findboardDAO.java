package com.trip.board.find.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.test.util.DBUtil;

/**
 * 동행찾기 게시판 관련 데이터베이스 작업을 처리하는 DAO 클래스
 * @author jsg
 * @version 1.0
 * @since 2025.10.24
 */
public class findboardDAO {

    private Connection conn;

    /**
     * findboardDAO 객체를 생성하고 데이터베이스 연결을 초기화합니다.
     */
    public findboardDAO() {
    	DBUtil dbUtil = new DBUtil();
        this.conn = dbUtil.open();
    }

    /**
     * ✅ [수정] 동행찾기 게시글 목록을 '페이징 처리'하여 조회하는 메소드
     * @param map 페이징(start, end) 및 검색(searchType, searchKeyword) 정보가 담긴 HashMap
     * @return 페이징 처리된 게시글 목록
     */
    public List<findboardDTO> getList(HashMap<String, String> map) {
        List<findboardDTO> list = new ArrayList<>();
        try {
            String where = ""; // 검색 조건을 저장할 변수

            // 검색어가 있는 경우 WHERE 절을 동적으로 생성
            if (map.get("searchKeyword") != null && !map.get("searchKeyword").isEmpty()) {
                if ("title_content".equals(map.get("searchType"))) {
                    where = "AND (fb.find_board_title LIKE '%" + map.get("searchKeyword") + "%' OR fb.find_board_content LIKE '%" + map.get("searchKeyword") + "%')";
                } else if ("nickname".equals(map.get("searchType"))) {
                    where = "AND u.nickname LIKE '%" + map.get("searchKeyword") + "%'";
                }
            }

            // ROWNUM을 사용한 페이징 쿼리
            String sql = String.format("SELECT * FROM "
                       + "    (SELECT a.*, ROWNUM as rnum FROM "
                       + "        (SELECT fb.*, u.nickname, "
                       + "            (SELECT COUNT(*) FROM tblFindComment WHERE find_board_id = fb.find_board_id) as commentCount, "
                       + "            (SELECT COUNT(*) FROM tblFindLikes WHERE find_board_id = fb.find_board_id) as likeCount "
                       + "        FROM tblFindBoard fb "
                       + "            INNER JOIN tblUser u ON fb.user_id = u.user_id "
                       + "        WHERE (fb.find_board_report_status IS NULL OR fb.find_board_report_status <> 'N') %s" // 검색 조건이 들어갈 위치
                       + "        ORDER BY fb.find_board_id DESC) a) "
                       + "WHERE rnum BETWEEN ? AND ?", where);
            
            PreparedStatement pstat = conn.prepareStatement(sql);
            pstat.setString(1, map.get("start"));
            pstat.setString(2, map.get("end"));
            
            ResultSet rs = pstat.executeQuery();
            
            while (rs.next()) {
                findboardDTO dto = new findboardDTO();
                dto.setFind_board_id(rs.getInt("find_board_id"));
                dto.setFind_board_title(rs.getString("find_board_title"));
                dto.setNickname(rs.getString("nickname"));
                dto.setFind_board_regdate(rs.getString("find_board_regdate"));
                dto.setFind_board_view_count(rs.getInt("find_board_view_count"));
                dto.setCommentCount(rs.getInt("commentCount"));
                dto.setLikeCount(rs.getInt("likeCount"));
                dto.setRownum(rs.getInt("rnum")); // ✅ [추가] 행 번호(rownum)도 DTO에 추가
                list.add(dto);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * ✅ [추가] 검색 조건에 맞는 총 게시글 수를 반환하는 메소드
     * @param map 검색(searchType, searchKeyword) 정보가 담긴 HashMap
     * @return 총 게시글 수
     */
    public int getTotalCount(HashMap<String, String> map) {
        int count = 0;
        try {
            String where = "";

            if (map.get("searchKeyword") != null && !map.get("searchKeyword").isEmpty()) {
                if ("title_content".equals(map.get("searchType"))) {
                    where = "AND (fb.find_board_title LIKE '%" + map.get("searchKeyword") + "%' OR fb.find_board_content LIKE '%" + map.get("searchKeyword") + "%')";
                } else if ("nickname".equals(map.get("searchType"))) {
                    where = "AND u.nickname LIKE '%" + map.get("searchKeyword") + "%'";
                }
            }

            String sql = String.format("SELECT COUNT(*) as cnt FROM tblFindBoard fb "
                       + "INNER JOIN tblUser u ON fb.user_id = u.user_id "
                       + "WHERE (fb.find_board_report_status IS NULL OR fb.find_board_report_status <> 'N') %s", where);

            PreparedStatement pstat = conn.prepareStatement(sql);
            ResultSet rs = pstat.executeQuery();

            if (rs.next()) {
                count = rs.getInt("cnt");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return count;
    }
    
    // =================================================================================
    // 아래의 다른 메소드들은 수정할 필요가 없습니다. (기존 코드 그대로 사용)
    // =================================================================================

    /**
     * 동행찾기 게시글을 DB에 추가하는 메소드
     * @param dto 추가할 게시글 정보를 담은 findboardDTO 객체
     * @return 실행 결과 (1: 성공, 0: 실패)
     */
    public int addPost(findboardDTO dto) {
        try {
            String sql = "INSERT INTO tblFindBoard (find_board_id, user_id, find_board_title, find_board_content, find_board_report_status) "
                       + "VALUES (seq_find_board.NEXTVAL, ?, ?, ?, 'Y')";
            
            PreparedStatement pstat = conn.prepareStatement(sql);
            pstat.setInt(1, dto.getUser_id());
            pstat.setString(2, dto.getFind_board_title());
            pstat.setString(3, dto.getFind_board_content());
            
            return pstat.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
    
    
    /**
     * 게시글 조회수를 1 증가시키는 메소드
     * @param boardSeq 조회수를 증가시킬 게시글 ID
     */
    public void updateViewCount(int boardSeq) {
        try {
            String sql = "UPDATE tblFindBoard SET find_board_view_count = find_board_view_count + 1 WHERE find_board_id = ?";
            PreparedStatement pstat = conn.prepareStatement(sql);
            pstat.setInt(1, boardSeq);
            pstat.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 특정 게시글의 상세 정보를 조회하는 메소드
     * @param boardSeq 조회할 게시글 ID
     * @return 게시글 정보를 담은 findboardDTO 객체
     */
    public findboardDTO getPost(int boardSeq) {
        findboardDTO dto = null;
        try {
            String sql = "SELECT fb.*, u.nickname "
                       + "FROM tblFindBoard fb "
                       + "    INNER JOIN tblUser u ON fb.user_id = u.user_id "
                       + "WHERE fb.find_board_id = ?";
            
            PreparedStatement pstat = conn.prepareStatement(sql);
            pstat.setInt(1, boardSeq);
            ResultSet rs = pstat.executeQuery();
            
            if (rs.next()) {
                dto = new findboardDTO();
                dto.setFind_board_id(rs.getInt("find_board_id"));
                dto.setUser_id(rs.getInt("user_id"));
                dto.setFind_board_title(rs.getString("find_board_title"));
                dto.setFind_board_content(rs.getString("find_board_content"));
                dto.setNickname(rs.getString("nickname"));
                dto.setFind_board_regdate(rs.getString("find_board_regdate"));
                dto.setFind_board_view_count(rs.getInt("find_board_view_count"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dto;
    }

    /**
     * 게시글을 수정하는 메소드
     * @param dto 수정할 게시글 정보를 담은 findboardDTO 객체
     * @return 실행 결과 (1: 성공, 0: 실패)
     */
    public int updatePost(findboardDTO dto) {
        try {
            String sql = "UPDATE tblFindBoard SET find_board_title = ?, find_board_content = ?, find_board_update = SYSDATE WHERE find_board_id = ?";
            PreparedStatement pstat = conn.prepareStatement(sql);
            pstat.setString(1, dto.getFind_board_title());
            pstat.setString(2, dto.getFind_board_content());
            pstat.setInt(3, dto.getFind_board_id());
            
            return pstat.executeUpdate();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 게시글을 삭제하는 메소드
     * @param boardSeq 삭제할 게시글 ID
     * @return 실행 결과 (1: 성공, 0: 실패)
     */
    public int deletePost(int boardSeq) {
        try {
            String sql = "DELETE FROM tblFindBoard WHERE find_board_id = ?";
            PreparedStatement pstat = conn.prepareStatement(sql);
            pstat.setInt(1, boardSeq);
            
            return pstat.executeUpdate();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 댓글을 DB에 추가하는 메소드
     * @param dto 추가할 댓글 정보를 담은 findcommentDTO 객체
     * @return 실행 결과 (1: 성공, 0: 실패)
     */
    public int addComment(findcommentDTO dto) {
        try {
            String sql = "INSERT INTO tblFindComment (find_comment_id, find_board_id, user_id, find_comment_content) "
                       + "VALUES (seq_find_comment.NEXTVAL, ?, ?, ?)";
            PreparedStatement pstat = conn.prepareStatement(sql);
            pstat.setInt(1, dto.getFind_board_id());
            pstat.setInt(2, dto.getUser_id());
            pstat.setString(3, dto.getFind_comment_content());
            
            return pstat.executeUpdate();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 특정 게시글의 댓글 목록을 조회하는 메소드
     * @param boardSeq 게시글 ID
     * @return 댓글 목록
     */
    public List<findcommentDTO> getCommentList(int boardSeq) {
        List<findcommentDTO> list = new ArrayList<>();
        try {
            String sql = "SELECT fc.*, u.nickname "
                       + "FROM tblFindComment fc "
                       + "    INNER JOIN tblUser u ON fc.user_id = u.user_id "
                       + "WHERE fc.find_board_id = ? "
                       + "ORDER BY fc.find_comment_id ASC";
            
            PreparedStatement pstat = conn.prepareStatement(sql);
            pstat.setInt(1, boardSeq);
            ResultSet rs = pstat.executeQuery();
            
            while (rs.next()) {
                findcommentDTO dto = new findcommentDTO();
                dto.setFind_comment_id(rs.getInt("find_comment_id"));
                dto.setFind_board_id(rs.getInt("find_board_id"));
                dto.setUser_id(rs.getInt("user_id"));
                dto.setNickname(rs.getString("nickname"));
                dto.setFind_comment_content(rs.getString("find_comment_content"));
                dto.setFind_comment_regdate(rs.getString("find_comment_regdate"));
                list.add(dto);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * 특정 게시글의 총 추천(좋아요) 수를 가져오는 메소드
     * @param boardSeq 게시글 ID
     * @return 추천(좋아요) 수
     */
    public int getLikeCount(int boardSeq) {
        try {
            String sql = "SELECT COUNT(*) as cnt FROM tblFindLikes WHERE find_board_id = ?";
            PreparedStatement pstat = conn.prepareStatement(sql);
            pstat.setInt(1, boardSeq);
            ResultSet rs = pstat.executeQuery();
            if (rs.next()) {
                return rs.getInt("cnt");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 특정 사용자가 특정 게시글을 추천했는지 확인하는 메소드
     * @param boardSeq 게시글 ID
     * @param userId 사용자 ID
     * @return 추천 여부 (true: 추천함, false: 추천 안 함)
     */
    public boolean checkLike(int boardSeq, int userId) {
        try {
            String sql = "SELECT COUNT(*) as cnt FROM tblFindLikes WHERE find_board_id = ? AND user_id = ?";
            PreparedStatement pstat = conn.prepareStatement(sql);
            pstat.setInt(1, boardSeq);
            pstat.setInt(2, userId);
            ResultSet rs = pstat.executeQuery();
            if (rs.next()) {
                return rs.getInt("cnt") > 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 추천(좋아요)을 DB에 추가하는 메소드
     * @param boardSeq 게시글 ID
     * @param userId 사용자 ID
     */
    public void addLike(int boardSeq, int userId) {
        try {
            String sql = "INSERT INTO tblFindLikes (find_board_id, user_id) VALUES (?, ?)";
            PreparedStatement pstat = conn.prepareStatement(sql);
            pstat.setInt(1, boardSeq);
            pstat.setInt(2, userId);
            pstat.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 추천(좋아요)을 DB에서 삭제하는 메소드
     * @param boardSeq 게시글 ID
     * @param userId 사용자 ID
     */
    public void removeLike(int boardSeq, int userId) {
        try {
            String sql = "DELETE FROM tblFindLikes WHERE find_board_id = ? AND user_id = ?";
            PreparedStatement pstat = conn.prepareStatement(sql);
            pstat.setInt(1, boardSeq);
            pstat.setInt(2, userId);
            pstat.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 게시글 신고 정보를 DB에 추가하고, 해당 게시글 상태를 'PENDING'으로 변경하는 메소드
     * @param reportTargetId 신고 대상 게시글 ID
     * @param reporterId 신고자 ID
     * @param reportedUserId 신고당한 사용자 ID
     * @param reason 신고 사유
     * @return 실행 결과 (1: 성공, 0: 실패)
     */
    public int addReport(int reportTargetId, int reporterId, int reportedUserId, String reason) {
        try {
            // 1. tblReports 테이블에 신고 내역을 INSERT
            String sqlReport = "INSERT INTO tblReports (report_id, report_target_type, report_target_id, user_id, reported_user_id, report_reason_type) "
                             + "VALUES (seq_reports.NEXTVAL, ?, ?, ?, ?, ?)";
            
            PreparedStatement pstatReport = conn.prepareStatement(sqlReport);
            pstatReport.setString(1, "findboard");
            pstatReport.setInt(2, reportTargetId);
            pstatReport.setInt(3, reporterId);
            pstatReport.setInt(4, reportedUserId);
            pstatReport.setString(5, reason);
            pstatReport.executeUpdate();
            pstatReport.close();

            // 2. tblFindBoard 테이블의 상태를 'PENDING'으로 UPDATE
            String sqlUpdateStatus = "UPDATE tblFindBoard SET find_board_report_status = 'PENDING' WHERE find_board_id = ?";
            
            PreparedStatement pstatUpdate = conn.prepareStatement(sqlUpdateStatus);
            pstatUpdate.setInt(1, reportTargetId);
            
            return pstatUpdate.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 특정 게시글의 스크랩 수를 가져오는 메소드
     * @param boardSeq 게시글 ID
     * @return 스크랩 수
     */
    public int getScrapCount(int boardSeq) {
        try {
            String sql = "SELECT COUNT(*) as cnt FROM tblFindScrap WHERE find_board_id = ?";
            PreparedStatement pstat = conn.prepareStatement(sql);
            pstat.setInt(1, boardSeq);
            ResultSet rs = pstat.executeQuery();
            if (rs.next()) {
                return rs.getInt("cnt");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
    /**
     * 특정 사용자가 특정 게시글을 스크랩했는지 확인하는 메소드
     * @param boardSeq 게시글 ID
     * @param userId 사용자 ID
     * @return 스크랩 여부 (true: 스크랩함, false: 스크랩 안 함)
     */
    public boolean checkScrap(int boardSeq, int userId) {
        try {
            String sql = "SELECT COUNT(*) as cnt FROM tblFindScrap WHERE find_board_id = ? AND user_id = ?";
            PreparedStatement pstat = conn.prepareStatement(sql);
            pstat.setInt(1, boardSeq);
            pstat.setInt(2, userId);
            ResultSet rs = pstat.executeQuery();
            if (rs.next()) {
                return rs.getInt("cnt") > 0; // 1개 이상이면 스크랩함
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    /**
     * 스크랩을 DB에 추가하는 메소드 (seq_find_scrap 사용)
     * @param boardSeq 게시글 ID
     * @param userId 사용자 ID
     */
    public void addScrap(int boardSeq, int userId) {
        try {
            String sql = "INSERT INTO tblFindScrap (find_scrap_id, find_board_id, user_id) VALUES (seq_find_scrap.NEXTVAL, ?, ?)";
            PreparedStatement pstat = conn.prepareStatement(sql);
            pstat.setInt(1, boardSeq);
            pstat.setInt(2, userId);
            pstat.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 스크랩을 DB에서 삭제하는 메소드 (취소)
     * @param boardSeq 게시글 ID
     * @param userId 사용자 ID
     */
    public void removeScrap(int boardSeq, int userId) {
        try {
            // 해당 게시글과 사용자 ID가 모두 일치하는 행을 삭제
            String sql = "DELETE FROM tblFindScrap WHERE find_board_id = ? AND user_id = ?";
            PreparedStatement pstat = conn.prepareStatement(sql);
            pstat.setInt(1, boardSeq);
            pstat.setInt(2, userId);
            pstat.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * 특정 댓글의 작성자 user_id를 반환하는 메소드 (수정/삭제 권한 확인용)
     * @param commentId 댓글 ID
     * @return 댓글 작성자의 user_id, 없거나 오류 발생 시 0
     */
    public int getCommentAuthor(int commentId) {
        try {
            String sql = "SELECT user_id FROM tblFindComment WHERE find_comment_id = ?";
            PreparedStatement pstat = conn.prepareStatement(sql);
            pstat.setInt(1, commentId);
            ResultSet rs = pstat.executeQuery();
            if (rs.next()) {
                return rs.getInt("user_id");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0; // 작성자가 없거나 오류 발생 시 0 반환
    }

    /**
     * 댓글 내용을 수정하는 메소드
     * @param dto 수정할 댓글 정보를 담은 findcommentDTO 객체
     * @return 실행 결과 (1: 성공, 0: 실패)
     */
    public int updateComment(findcommentDTO dto) {
        try {
            String sql = "UPDATE tblFindComment SET find_comment_content = ? WHERE find_comment_id = ?";
            PreparedStatement pstat = conn.prepareStatement(sql);
            pstat.setString(1, dto.getFind_comment_content());
            pstat.setInt(2, dto.getFind_comment_id());
            return pstat.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 댓글을 삭제하는 메소드
     * @param commentId 삭제할 댓글 ID
     * @return 실행 결과 (1: 성공, 0: 실패)
     */
    public int deleteComment(int commentId) {
        try {
            String sql = "DELETE FROM tblFindComment WHERE find_comment_id = ?";
            PreparedStatement pstat = conn.prepareStatement(sql);
            pstat.setInt(1, commentId);
            return pstat.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
}
