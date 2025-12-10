package com.trip.board.model;

import java.sql.*;
import java.util.*;
import com.test.util.DBUtil;

/**
 * 경로 게시물 관련 데이터베이스 작업을 처리하는 DAO 클래스
 * @author jsg
 * @version 1.0
 * @since 2025.10.24
 */
public class RoutePostDAO {

    /**
     * 데이터베이스 연결 객체
     */
    private Connection conn;
    /**
     * SQL 실행 객체 (동적 쿼리용)
     */
    private PreparedStatement pstat;
    /**
     * 쿼리 결과 집합 객체
     */
    private ResultSet rs;

    /**
     * RoutePostDAO 생성자.
     * DB 유틸리티를 사용하여 데이터베이스 연결을 엽니다.
     */
    public RoutePostDAO() {
    	try {
            // ✅ 인스턴스 생성 후 open() 호출
            DBUtil util = new DBUtil();
            conn = util.open();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 새로운 경로 게시물을 데이터베이스에 등록합니다.
     * 등록 시 생성된 routepost_id를 DTO에 다시 설정합니다.
     *
     * @param dto 등록할 게시물 정보가 담긴 DTO
     * @return DB에 삽입된 행의 수 (성공 시 1, 실패 시 0)
     */
    // ✅ 게시글 등록
    public int insert(RoutePostDTO dto) {
        try {
            String sql = "INSERT INTO tblRoutePost ("
                    + "routepost_id, user_id, routepost_title, routepost_content, "
                    + "routepost_satisfaction, routepost_view_count, routepost_report_count, "
                    + "routepost_status, routepost_regdate, routepost_update"
                    + ") VALUES (seqRoutePost.nextVal, ?, ?, ?, ?, 0, 0, ?, SYSDATE, SYSDATE)";
            pstat = conn.prepareStatement(sql, new String[] {"routepost_id"});
            pstat.setInt(1, 1);
            pstat.setString(2, dto.getRoutepostTitle());
            pstat.setString(3, dto.getRoutepostContent());
            pstat.setDouble(4, dto.getRoutepostSatisfaction());
            pstat.setString(5, dto.getRoutepostStatus());
            int result = pstat.executeUpdate();

            rs = pstat.getGeneratedKeys();
            if (rs.next()) dto.setRoutepostId(rs.getInt(1));
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
    
    /**
     * 전체 경로 게시물 목록을 조회합니다.
     * 작성자 닉네임(tblUser)과 좋아요 수(tblRoutePostLikes)를 조인하여 함께 반환합니다.
     *
     * @return 경로 게시물 DTO 리스트 (List<RoutePostDTO>)
     */
    public List<RoutePostDTO> getList() {
        List<RoutePostDTO> list = new ArrayList<>();

        try {
            String sql = """
                SELECT 
                    r.routepost_id,
                    r.routepost_title,
                    r.user_id,
                    u.nickname,
                    r.routepost_regdate,
                    r.routepost_view_count,
                    r.routepost_satisfaction,
                    NVL(l.like_count, 0) AS like_count
                FROM tblRoutePost r
                JOIN tblUser u ON r.user_id = u.user_id
                LEFT JOIN (
                    SELECT routepost_id, COUNT(*) AS like_count
                    FROM tblRoutePostLikes
                    GROUP BY routepost_id
                ) l ON r.routepost_id = l.routepost_id
                ORDER BY r.routepost_id DESC
            """;

            pstat = conn.prepareStatement(sql);
            rs = pstat.executeQuery();

            while (rs.next()) {
                RoutePostDTO dto = new RoutePostDTO();
                dto.setRoutepostId(rs.getInt("routepost_id"));
                dto.setRoutepostTitle(rs.getString("routepost_title"));
                dto.setUserId(rs.getInt("user_id"));
                dto.setNickname(rs.getString("nickname"));
                dto.setRoutepostRegdate(rs.getString("routepost_regdate"));
                dto.setRoutepostViewCount(rs.getInt("routepost_view_count"));
                dto.setRoutepostSatisfaction(rs.getDouble("routepost_satisfaction"));
                dto.setLikeCount(rs.getInt("like_count")); // ✅ 새 필드
                list.add(dto);
            }

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("❌ 게시글 목록 조회 중 오류 발생");
        }

        return list;
    }

    
    /**
     * 특정 ID의 경로 게시물 기본 정보를 조회합니다.
     *
     * @param postId 조회할 게시물의 ID
     * @return 조회된 게시물 DTO (게시물이 없을 경우 null)
     */
    public RoutePostDTO get(int postId) {
        RoutePostDTO dto = null;
        try {
            String sql = "SELECT r.*, u.nickname "
                       + "FROM tblRoutePost r "
                       + "JOIN tblUser u ON r.user_id = u.user_id "
                       + "WHERE r.routepost_id = ?";

            pstat = conn.prepareStatement(sql);
            pstat.setInt(1, postId);
            rs = pstat.executeQuery();

            if (rs.next()) {
                dto = new RoutePostDTO();
                dto.setRoutepostId(rs.getInt("routepost_id"));
                dto.setUserId(rs.getInt("user_id"));
                dto.setNickname(rs.getString("nickname"));
                dto.setRoutepostTitle(rs.getString("routepost_title"));
                dto.setRoutepostContent(rs.getString("routepost_content"));
                dto.setRoutepostRegdate(rs.getString("routepost_regdate"));
                dto.setRoutepostViewCount(rs.getInt("routepost_view_count"));
                dto.setRoutepostSatisfaction(rs.getDouble("routepost_satisfaction"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return dto;
    }
    
    
    /**
     * 기존 경로 게시물의 내용을 수정합니다. (제목, 내용, 만족도)
     *
     * @param dto 수정할 내용이 담긴 DTO (routepostId 필수 포함)
     * @return DB에 수정된 행의 수 (성공 시 1, 실패 시 0)
     */
 // 게시글 수정
    public int update(RoutePostDTO dto) {
        int result = 0;
        try {
            String sql = "UPDATE tblRoutePost "
                       + "SET routepost_title = ?, "
                       + "routepost_content = ?, "
                       + "routepost_satisfaction = ?, "
                       + "routepost_update = SYSDATE "
                       + "WHERE routepost_id = ?";

            pstat = conn.prepareStatement(sql);
            pstat.setString(1, dto.getRoutepostTitle());
            pstat.setString(2, dto.getRoutepostContent());
            pstat.setDouble(3, dto.getRoutepostSatisfaction());
            pstat.setInt(4, dto.getRoutepostId());

            result = pstat.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 특정 ID의 경로 게시물을 삭제합니다.
     * 트랜잭션을 사용하여 관련된 댓글, 이미지, 좋아요, 스크랩 데이터를 먼저 삭제하고
     * 부모 게시물을 삭제합니다.
     *
     * @param id 삭제할 게시물의 ID
     * @return 부모 게시물 삭제 성공 시 1, 실패 시 0
     */
    public int delete(int id) {
        int result = 0;
        PreparedStatement ps = null;

        try {
            conn.setAutoCommit(false); // ✅ 트랜잭션 시작

            // 댓글 삭제
            ps = conn.prepareStatement("DELETE FROM tblRoutePostComment WHERE routepost_id = ?");
            ps.setInt(1, id);
            ps.executeUpdate();
            ps.close();

            // 이미지 삭제
            ps = conn.prepareStatement("DELETE FROM tblRoutePostImage WHERE routepost_id = ?");
            ps.setInt(1, id);
            ps.executeUpdate();
            ps.close();

            // 추천 삭제
            ps = conn.prepareStatement("DELETE FROM tblRoutePostLikes WHERE routepost_id = ?");
            ps.setInt(1, id);
            ps.executeUpdate();
            ps.close();

            // 스크랩 삭제
            ps = conn.prepareStatement("DELETE FROM tblRoutePostScrap WHERE routepost_id = ?");
            ps.setInt(1, id);
            ps.executeUpdate();
            ps.close();

            // 부모글 삭제
            ps = conn.prepareStatement("DELETE FROM tblRoutePost WHERE routepost_id = ?");
            ps.setInt(1, id);
            result = ps.executeUpdate();
            ps.close();

            conn.commit(); // ✅ 커밋

        } catch (Exception e) {
            e.printStackTrace();
            try { if (conn != null) conn.rollback(); } catch (Exception ex) { ex.printStackTrace(); }
        } finally {
            try { if (ps != null) ps.close(); } catch (Exception e) {}
        }

        return result;
    }





    /**
     * 게시물의 조회수를 1 증가시킵니다.
     *
     * @param id 조회수를 증가시킬 게시물의 ID (String 타입)
     */
    // ✅ 조회수 증가
    public void increaseViewCount(String id) {
        try {
            String sql = "UPDATE tblRoutePost SET routepost_view_count = routepost_view_count + 1 WHERE routepost_id = ?";
            pstat = conn.prepareStatement(sql);
            pstat.setString(1, id);
            pstat.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 게시물 상세 정보를 조회합니다. (게시물 내용 + 작성자 닉네임 + 첨부 이미지 목록)
     *
     * @param id 조회할 게시물의 ID (String 타입)
     * @return 상세 정보가 포함된 DTO (이미지 리스트 포함, 없을 경우 null)
     */
    // ✅ 상세보기 (작성자 + 이미지)
    public RoutePostDTO getDetail(String id) {
        RoutePostDTO dto = null;
        try {
            String sql = "SELECT r.*, u.nickname FROM tblRoutePost r "
                       + "JOIN tblUser u ON r.user_id = u.user_id "
                       + "WHERE r.routepost_id = ?";
            pstat = conn.prepareStatement(sql);
            pstat.setString(1, id);
            rs = pstat.executeQuery();

            if (rs.next()) {
                dto = new RoutePostDTO();
                dto.setRoutepostId(rs.getInt("routepost_id"));
                dto.setUserId(rs.getInt("user_id"));
                dto.setRoutepostTitle(rs.getString("routepost_title"));
                dto.setRoutepostContent(rs.getString("routepost_content"));
                dto.setNickname(rs.getString("nickname"));
                dto.setRoutepostViewCount(rs.getInt("routepost_view_count"));
                dto.setRoutepostSatisfaction(rs.getDouble("routepost_satisfaction"));
                dto.setRoutepostRegdate(rs.getString("routepost_regdate"));
                dto.setImages(getImages(rs.getInt("routepost_id")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dto;
    }

    

    /**
     * 경로 게시물에 이미지 정보를 추가합니다.
     *
     * @param postId   이미지를 추가할 게시물 ID
     * @param seq      게시물 내 이미지 순서
     * @param fileName 저장된 이미지 파일명 또는 URL
     */
    // ✅ 이미지 추가
    public void insertImage(int postId, int seq, String fileName) {
        try {
            String sql = "INSERT INTO tblRoutePostImage VALUES (seqRoutePostImage.nextVal, ?, ?, ?)";
            pstat = conn.prepareStatement(sql);
            pstat.setInt(1, postId);
            pstat.setInt(2, seq);
            pstat.setString(3, fileName);
            pstat.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ✅ 추천/스크랩 관련
    
    /**
     * 사용자의 게시물 '좋아요' 상태를 토글(추가/삭제)합니다.
     * 이미 '좋아요' 상태이면 삭제하고, 아니면 추가합니다.
     *
     * @param userId 사용자 ID
     * @param postId 게시물 ID
     * @return '좋아요'가 추가되었으면 true, 삭제되었으면 false
     */
    public boolean toggleLike(int userId, int postId) {
        try {
            String checkSql = "SELECT COUNT(*) FROM tblRoutePostLikes WHERE user_id=? AND routepost_id=?";
            pstat = conn.prepareStatement(checkSql);
            pstat.setInt(1, userId);
            pstat.setInt(2, postId);
            rs = pstat.executeQuery();
            rs.next();
            boolean alreadyLiked = rs.getInt(1) > 0;
            rs.close();
            pstat.close();

            if (alreadyLiked) {
                String del = "DELETE FROM tblRoutePostLikes WHERE user_id=? AND routepost_id=?";
                pstat = conn.prepareStatement(del);
                pstat.setInt(1, userId);
                pstat.setInt(2, postId);
                pstat.executeUpdate();
                return false; // 취소됨
            } else {
                String ins = "INSERT INTO tblRoutePostLikes (user_id, routepost_id) VALUES (?, ?)";
                pstat = conn.prepareStatement(ins);
                pstat.setInt(1, userId);
                pstat.setInt(2, postId);
                pstat.executeUpdate();
                return true; // 좋아요됨
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 사용자의 게시물 '스크랩' 상태를 토글(추가/삭제)합니다.
     * 이미 '스크랩' 상태이면 삭제하고, 아니면 추가합니다.
     *
     * @param userId 사용자 ID
     * @param postId 게시물 ID
     * @return '스크랩'이 추가되었으면 true, 삭제되었으면 false
     */
    // ✅ 스크랩 토글
    public boolean toggleScrap(int userId, int postId) {
        try {
            String checkSql = "SELECT COUNT(*) FROM tblRoutePostScrap WHERE routepost_user_id=? AND routepost_id=?";
            pstat = conn.prepareStatement(checkSql);
            pstat.setInt(1, userId);
            pstat.setInt(2, postId);
            rs = pstat.executeQuery();
            rs.next();
            boolean alreadyScrapped = rs.getInt(1) > 0;
            rs.close();
            pstat.close();

            if (alreadyScrapped) {
                String del = "DELETE FROM tblRoutePostScrap WHERE routepost_user_id=? AND routepost_id=?";
                pstat = conn.prepareStatement(del);
                pstat.setInt(1, userId);
                pstat.setInt(2, postId);
                pstat.executeUpdate();
                return false; // 스크랩 취소
            } else {
                String ins = "INSERT INTO tblRoutePostScrap (routepost_scrap_id, routepost_user_id, routepost_id) VALUES (seqRoutePostScrap.NEXTVAL, ?, ?)";
                pstat = conn.prepareStatement(ins);
                pstat.setInt(1, userId);
                pstat.setInt(2, postId);
                pstat.executeUpdate();
                return true; // 스크랩 성공
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 사용자가 해당 게시물에 '좋아요'를 눌렀는지 확인합니다. (페이지 로드 시 UI 표시용)
     *
     * @param userId 사용자 ID
     * @param postId 게시물 ID
     * @return '좋아요'를 눌렀으면 true, 아니면 false
     */
    // ✅ 상태조회용 (페이지 진입 시 버튼 상태 유지)
    public boolean checkLike(int userId, int postId) {
        try {
            String sql = "SELECT COUNT(*) FROM tblRoutePostLikes WHERE user_id=? AND routepost_id=?";
            pstat = conn.prepareStatement(sql);
            pstat.setInt(1, userId);
            pstat.setInt(2, postId);
            rs = pstat.executeQuery();
            rs.next();
            return rs.getInt(1) > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 사용자가 해당 게시물을 '스크랩'했는지 확인합니다. (페이지 로드 시 UI 표시용)
     *
     * @param userId 사용자 ID
     * @param postId 게시물 ID
     * @return '스크랩'을 했으면 true, 아니면 false
     */
    public boolean checkScrap(int userId, int postId) {
        try {
            String sql = "SELECT COUNT(*) FROM tblRoutePostScrap WHERE routepost_user_id=? AND routepost_id=?";
            pstat = conn.prepareStatement(sql);
            pstat.setInt(1, userId);
            pstat.setInt(2, postId);
            rs = pstat.executeQuery();
            rs.next();
            return rs.getInt(1) > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }



    
    /**
     * 특정 게시물에 첨부된 모든 이미지 목록을 순서대로 조회합니다.
     *
     * @param postId 게시물 ID
     * @return 이미지 DTO 리스트 (List<RoutePostImageDTO>)
     */
    public List<RoutePostImageDTO> getImages(int postId) {
        List<RoutePostImageDTO> list = new ArrayList<>();
        try {
            String sql = "SELECT * FROM tblRoutePostImage WHERE routepost_id = ? ORDER BY routepost_image_seq";
            pstat = conn.prepareStatement(sql);
            pstat.setInt(1, postId);
            rs = pstat.executeQuery();

            while (rs.next()) {
                RoutePostImageDTO img = new RoutePostImageDTO();
                img.setRoutepostImageId(rs.getInt("routepost_image_id"));
                img.setRoutepostId(rs.getInt("routepost_id"));
                img.setRoutepostImageSeq(rs.getInt("routepost_image_seq"));
                img.setRoutepostImageUrl(rs.getString("routepost_image_url"));
                list.add(img);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    
    /**
     * 특정 게시물의 모든 이미지 정보를 DB에서 삭제합니다.
     * (게시물 수정 시 기존 이미지 전체 삭제 후 재삽입하는 로직에 사용)
     *
     * @param postId 이미지를 삭제할 게시물 ID
     */
    // 특정 게시글 이미지 전체 삭제
    public void deleteImages(int postId) {
        try {
            String sql = "DELETE FROM tblRoutePostImage WHERE routepost_id = ?";
            pstat = conn.prepareStatement(sql);
            pstat.setInt(1, postId);
            pstat.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * DAO 사용이 끝난 후 DB 연결 리소스(Connection, PreparedStatement)를 닫습니다.
     */
    // ✅ 종료
    public void close() {
        try {
            if (pstat != null) pstat.close();
            if (conn != null) conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * 게시물 ID를 이용하여 작성자의 ID (user_id)를 조회합니다.
     *
     * @param postId 조회할 게시물 ID
     * @return 작성자 ID (String), 조회 실패 시 null
     */
    public String getWriterId(int postId) {
        String writerId = null;
        try {
            String sql = "SELECT user_id FROM TBLROUTEPOST WHERE routepost_id = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, postId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                writerId = rs.getString("user_id");
            }
            rs.close();
            ps.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return writerId;
    }
    
    /**
     * 게시물 ID를 이용하여 작성자의 ID (user_id)를 조회합니다. (int 반환)
     *
     * @param postId 조회할 게시물 ID
     * @return 작성자 ID (int), 조회 실패 시 -1
     */
 // ✅ 게시글 작성자(user_id) 조회 메서드 추가
    public int getPostOwnerId(int postId) {
        int ownerId = -1;

        try {
            String sql = "SELECT user_id FROM tblRoutePost WHERE routepost_id = ?";
            pstat = conn.prepareStatement(sql);
            pstat.setInt(1, postId);
            rs = pstat.executeQuery();

            if (rs.next()) {
                ownerId = rs.getInt("user_id");
            }

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("❌ getPostOwnerId() 오류: " + e.getMessage());
        }

        return ownerId;
    }


}
