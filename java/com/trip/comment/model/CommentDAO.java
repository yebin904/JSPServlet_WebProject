package com.trip.comment.model;

import java.sql.*;
import java.util.*;
import com.test.util.DBUtil;

/**
 * 댓글(tblRoutePostComment) 관련 데이터베이스 작업을 처리하는 DAO(Data Access Object) 클래스.
 *
 * @author jsg
 * @version 1.0
 * @since 2025.10.24
 */
public class CommentDAO {

    private Connection conn;
    private PreparedStatement pstat;
    private ResultSet rs;

    /**
     * CommentDAO 생성자입니다.
     * DBUtil을 통해 데이터베이스 연결(Connection)을 엽니다.
     */
    public CommentDAO() {
        try {
            DBUtil util = new DBUtil();
            conn = util.open();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 새로운 댓글을 데이터베이스에 등록합니다.
     *
     * @param userId  댓글 작성자의 ID
     * @param postId  원본 게시글의 ID
     * @param content 댓글 내용
     * @return DB에 삽입된 행(row)의 수 (성공 시 1, 실패 시 0)
     */
    public int addComment(int userId, int postId, String content) {
        try {
            // comment_status: 'Y' (활성 상태)로 기본값 삽입
            String sql = """
                INSERT INTO tblRoutePostComment
                (routepost_comment_id, user_id, routepost_id, routepost_content, routepost_regdate, routepost_comment_report_count, comment_status)
                VALUES (seqRoutePostComment.NEXTVAL, ?, ?, ?, SYSDATE, 0, 'Y')
            """;

            pstat = conn.prepareStatement(sql);
            pstat.setInt(1, userId);
            pstat.setInt(2, postId);
            pstat.setString(3, content);
            return pstat.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0; // 예외 발생 시 0 반환
    }

    /**
     * 특정 게시글의 댓글 목록을 페이지네이션하여 조회합니다.
     * 작성자의 닉네임을 포함하기 위해 tblUser와 INNER JOIN을 수행합니다.
     *
     * @param postId 조회할 원본 게시글 ID
     * @param begin  페이지네이션 시작 row 번호
     * @param end    페이지네이션 끝 row 번호
     * @return 해당 페이지의 댓글 목록 (List&lt;CommentDTO&gt;)
     */
    public List<CommentDTO> getCommentPage(int postId, int begin, int end) {
        List<CommentDTO> list = new ArrayList<>();

        try {
            // ROWNUMBER()를 이용한 서버 사이드 페이지네이션
            // 활성 상태(comment_status = 'Y')인 댓글만 조회
            String sql = """
                SELECT * FROM (
                    SELECT c.routepost_comment_id,
                           c.user_id,
                           c.routepost_id,
                           c.routepost_content,
                           TO_CHAR(c.routepost_regdate, 'YYYY-MM-DD HH24:MI') AS routepost_regdate,
                           c.routepost_comment_report_count,
                           c.comment_status,
                           u.nickname,
                           ROW_NUMBER() OVER (ORDER BY c.routepost_comment_id DESC) AS rnum
                    FROM tblRoutePostComment c
                    INNER JOIN tblUser u ON c.user_id = u.user_id
                    WHERE c.routepost_id = ? AND c.comment_status = 'Y'
                )
                WHERE rnum BETWEEN ? AND ?
            """;

            pstat = conn.prepareStatement(sql);
            pstat.setInt(1, postId);
            pstat.setInt(2, begin);
            pstat.setInt(3, end);
            rs = pstat.executeQuery();

            while (rs.next()) {
                CommentDTO dto = new CommentDTO();
                dto.setRoutepostCommentId(rs.getInt("routepost_comment_id"));
                dto.setUserId(rs.getInt("user_id"));
                dto.setRoutepostId(rs.getInt("routepost_id"));
                dto.setRoutepostContent(rs.getString("routepost_content"));
                dto.setRoutepostRegdate(rs.getString("routepost_regdate")); // 포맷팅된 날짜
                dto.setRoutepostCommentReportCount(rs.getInt("routepost_comment_report_count"));
                dto.setCommentStatus(rs.getString("comment_status"));
                dto.setNickname(rs.getString("nickname")); // JOIN된 닉네임
                list.add(dto);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list; // 조회된 목록 반환 (없으면 빈 리스트)
    }

    /**
     * 특정 게시글의 활성(Y) 댓글 총 개수를 조회합니다.
     *
     * @param postId 원본 게시글 ID
     * @return 댓글 총 개수
     */
    public int getTotalCount(int postId) {
        try {
            String sql = "SELECT COUNT(*) AS cnt FROM tblRoutePostComment WHERE routepost_id = ? AND comment_status = 'Y'";
            pstat = conn.prepareStatement(sql);
            pstat.setInt(1, postId);
            rs = pstat.executeQuery();

            if (rs.next()) {
                return rs.getInt("cnt");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0; // 예외 또는 결과 없음 시 0 반환
    }

    /**
     * 댓글을 논리적으로 삭제합니다. (comment_status를 'N'으로 변경)
     *
     * @param commentId 삭제할 댓글 ID
     * @param userId    삭제를 시도하는 사용자 ID (본인 확인용)
     * @return DB에 업데이트된 행(row)의 수 (성공 시 1, 실패 시 0)
     */
    public int deleteComment(int commentId, int userId) {
        try {
            // 본인 댓글만 삭제하도록 WHERE 조건에 userId 포함
            String sql = """
                UPDATE tblRoutePostComment
                SET comment_status = 'N'
                WHERE routepost_comment_id = ? AND user_id = ?
            """;
            pstat = conn.prepareStatement(sql);
            pstat.setInt(1, commentId);
            pstat.setInt(2, userId);
            return pstat.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 댓글 내용을 수정합니다.
     * 수정 시, 작성일(routepost_regdate)을 현재 시간(SYSDATE)으로 업데이트합니다.
     *
     * @param commentId 수정할 댓글 ID
     * @param content   새로운 댓글 내용
     * @return DB에 업데이트된 행(row)의 수 (성공 시 1, 실패 시 0)
     */
    public int editComment(int commentId, String content) {
        try {
            String sql = """
                UPDATE tblRoutePostComment
                SET routepost_content = ?, routepost_regdate = SYSDATE
                WHERE routepost_comment_id = ?
            """;
            pstat = conn.prepareStatement(sql);
            pstat.setString(1, content);
            pstat.setInt(2, commentId);
            return pstat.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * DAO 사용이 끝난 후 데이터베이스 자원(Connection, PreparedStatement, ResultSet)을 해제합니다.
     */
    public void close() {
        try {
            if (rs != null) rs.close();
            if (pstat != null) pstat.close();
            if (conn != null) conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * 댓글 ID를 기준으로 해당 댓글의 작성자(user_id)를 조회합니다.
     * (댓글 수정/삭제 시 본인 확인용)
     *
     * @param commentId 조회할 댓글 ID
     * @return 댓글 작성자의 user_id (조회 실패 시 -1 반환)
     */
    public int getCommentOwnerId(int commentId) {
        int ownerId = -1; // 기본값 -1 (찾지 못함)
        try {
            String sql = "SELECT user_id FROM tblRoutePostComment WHERE routepost_comment_id = ?";
            pstat = conn.prepareStatement(sql);
            pstat.setInt(1, commentId);
            rs = pstat.executeQuery();
            if (rs.next()) {
                ownerId = rs.getInt("user_id");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ownerId;
    }
    
    /**
     * 댓글을 데이터베이스에서 물리적으로 삭제합니다. (DELETE)
     * (RouteCommDelete 서블릿에서 사용)
     *
     * @param commentId 삭제할 댓글 ID
     * @return DB에서 삭제된 행(row)의 수 (성공 시 1, 실패 시 0)
     */
    public int deleteComment(int commentId) {
        int result = 0;
        try {
            String sql = "DELETE FROM tblRoutePostComment WHERE routepost_comment_id = ?";
            pstat = conn.prepareStatement(sql);
            pstat.setInt(1, commentId);
            result = pstat.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }


}
