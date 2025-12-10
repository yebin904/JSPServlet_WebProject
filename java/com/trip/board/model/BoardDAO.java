package com.trip.board.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.test.util.DBUtil;

/**
 * (핫딜) 게시판 관련 데이터베이스 작업을 처리하는 DAO 클래스
 * 마이페이지의 통합 활동 내역 조회 기능도 포함합니다.
 * @author jsg
 * @version 1.0
 * @since 2025.10.24
 */
public class BoardDAO {

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
     * BoardDAO 생성자.
     * DB 유틸리티를 사용하여 데이터베이스 연결을 엽니다.
     */
	public BoardDAO() {
		try {
			util = new DBUtil();
			conn = util.open();
			stat = conn.createStatement();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
     * 핫딜 게시판(tblHotDealPost)의 전체 게시물 수 또는 검색 결과 수를 조회합니다.
     *
     * @param map 검색 조건(search, column, word)이 담긴 Map
     * @return 게시물 수
     */
	public int getBoardTotalCount(Map<String, String> map) {
		// 총 게시물 수 및 검색결과별 게시물수
		// queryParamTokenReturn
		try {

			String where = "";

			if (map.get("search").equals("y")) {

				where = String.format("where %s like '%%%s%%'", map.get("column"), map.get("word"));

			}

			String sql = "select count(*) as cnt from tblHotDealPost " + where;

			pstat = conn.prepareStatement(sql);

			rs = pstat.executeQuery();

			if (rs.next()) {

				return rs.getInt("cnt");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return 0;
	}

	/**
     * 사용자의 전체 활동(vwUserAllActivities) 뷰의 전체 레코드 수 또는 검색 결과 수를 조회합니다.
     * (마이페이지에서 사용될 수 있음)
     *
     * @param map 검색 조건(search, column, word)이 담긴 Map
     * @return 레코드 수
     */
	public int getTotalCount(Map<String, String> map) {
		// 총 게시물 수 및 검색결과별 게시물수
		// queryParamTokenReturn
		try {

			String where = "";

			if (map.get("search").equals("y")) {

				where = String.format("where %s like '%%%s%%'", map.get("column"), map.get("word"));

			}

			String sql = "select count(*) as cnt from vwUserAllActivities " + where;

			pstat = conn.prepareStatement(sql);

			rs = pstat.executeQuery();

			if (rs.next()) {

				return rs.getInt("cnt");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return 0;
	}
	
	/**
     * 핫딜 게시판(tblHotDealPost)의 목록을 페이징 처리하여 조회합니다. (검색 기능 포함)
     * SQL Injection을 방지하기 위해 컬럼명은 화이트리스트 방식으로 검증하고, 값은 PreparedStatement를 사용합니다.
     *
     * @param map 페이징(begin, end) 및 검색(search, column, word) 정보가 담긴 Map
     * @return 핫딜 게시물 DTO 리스트 (List<BoardDTO>)
     */
	public List<BoardDTO> list(Map<String, String> map) {
	    List<BoardDTO> list = new ArrayList<>();
	    String sql = "";
	    try {
	        if (map.get("search").equals("y")) {
	            // SQL Injection 방지를 위해 허용할 컬럼 목록을 미리 정합니다.
	            String column = map.get("column");
	            if (!column.equals("hotdeal_title") && !column.equals("hotdeal_content")) {
	                column = "hotdeal_title"; // 허용되지 않은 값이면 기본값으로 변경
	            }

	            // 변수는 ? 로 대체하여 SQL Injection을 원천 차단합니다.
	            sql = String.format("SELECT b.* FROM (SELECT a.*, ROWNUM AS rnum FROM (SELECT p.*, u.nickname, s.hotdeal_status AS status, i.hotdeal_image_url AS img FROM tblHotDealPost p LEFT JOIN tblUser u ON p.user_id = u.user_id LEFT JOIN tblHotDealStatus s ON p.hotdeal_status_id = s.hotdeal_status_id LEFT JOIN tblHotDealImage i ON p.hotdeal_id = i.hotdeal_id WHERE %s LIKE ? ORDER BY p.hotdeal_id DESC) a) b WHERE rnum BETWEEN ? AND ?", column);

	            pstat = conn.prepareStatement(sql);
	            pstat.setString(1, "%" + map.get("word") + "%");
	            pstat.setString(2, map.get("begin"));
	            pstat.setString(3, map.get("end"));

	        } else {
	            // 검색이 아닐 때의 쿼리
	            sql = "SELECT b.* FROM (SELECT a.*, ROWNUM AS rnum FROM (SELECT p.*, u.nickname, s.hotdeal_status AS status, i.hotdeal_image_url AS img FROM tblHotDealPost p LEFT JOIN tblUser u ON p.user_id = u.user_id LEFT JOIN tblHotDealStatus s ON p.hotdeal_status_id = s.hotdeal_status_id LEFT JOIN tblHotDealImage i ON p.hotdeal_id = i.hotdeal_id ORDER BY p.hotdeal_id DESC) a) b WHERE rnum BETWEEN ? AND ?";
	            
	            pstat = conn.prepareStatement(sql);
	            pstat.setString(1, map.get("begin"));
	            pstat.setString(2, map.get("end"));
	        }

	        rs = pstat.executeQuery();

	        while (rs.next()) {
	            BoardDTO dto = new BoardDTO();
	            // ... (기존의 rs.getString ... 코드는 동일)
							dto.setSeq(rs.getString("hotdeal_id"));
							dto.setSubject(rs.getString("hotdeal_title"));
							dto.setId(rs.getString("user_id"));
							dto.setReadcount(rs.getString("hotdeal_view_count"));
							dto.setRegdate(rs.getString("hotdeal_regdate"));
							dto.setName(rs.getString("nickname"));
							dto.setStatus(rs.getString("status"));
							dto.setImg(rs.getString("img"));
	            list.add(dto);
	        }
	        return list;

	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    return null; // 에러 발생 시 null 반환
	}

	/*
	 * public List<BoardDTO> list(Map<String, String> map) { //##목록 출력
	 * //queryNoParamListReturn try {
	 * * //목록보기 > select * from vwBoard //검색하기 > select * from vwBoard where 조건
	 * * String where = "";
	 * * if (map.get("search").equals("y")) {
	 * * //where = "where 조건"; //where subject like '%검색어%' //where content like
	 * '%검색어%' //where name like '%검색어%'
	 * * where = String.format("where %s like '%%%s%%'" , map.get("column") ,
	 * map.get("word"));
	 * * }
	 * * String sql = "";
	 * * //if (map.get("tag") == null) { //태그부분 해당없으므로 제외 sql = String.
	 * format("SELECT b.*, (SELECT nickname FROM tblUser WHERE user_id = b.user_id) AS nickname, (SELECT hotdeal_status FROM tblHotDealStatus WHERE hotdeal_status_id = b.hotdeal_status_id) AS status, (SELECT hotdeal_image_url FROM tblHotDealImage WHERE hotdeal_id = b.hotdeal_id) AS img FROM ( SELECT a.*, ROWNUM AS rnum FROM ( SELECT * FROM tblHotDealPost ORDER BY hotdeal_id DESC ) a %s ) b WHERE rnum BETWEEN %s AND %s"
	 * , where , map.get("begin") , map.get("end"));
	 * * } else { sql = String.
	 * format("select * from (select a.*, rownum as rnum from tblHotDealPost a ) b inner join tblTagging t on b.seq = t.bseq inner join tblHashtag h on h.seq = t.hseq where rnum between %s and %s and h.hashtag = '%s'"
	 * , map.get("begin") , map.get("end") , map.get("tag")); }
	 * * * stat = conn.createStatement(); rs = stat.executeQuery(sql);
	 * * List<BoardDTO> list = new ArrayList<BoardDTO>();
	 * * while (rs.next()) {
	 * * BoardDTO dto = new BoardDTO();
	 * * dto.setSeq(rs.getString("hotdeal_id"));
	 * dto.setSubject(rs.getString("hotdeal_title"));
	 * dto.setId(rs.getString("user_id"));
	 * dto.setReadcount(rs.getString("hotdeal_view_count"));
	 * dto.setRegdate(rs.getString("hotdeal_regdate"));
	 * * dto.setName(rs.getString("nickname")); dto.setStatus(rs.getString("status"));
	 * dto.setImg(rs.getString("img"));
	 * * //dto.setIsnew(rs.getDouble("isnew"));
	 * //dto.setCommentCount(rs.getString("commentCount"));
	 * * //dto.setSecret(rs.getString("secret"));
	 * //dto.setNotice(rs.getString("notice"));
	 * * list.add(dto); }
	 * * * //##공지사항이므로 배제 sql = "select * from vwNotice"; stat = conn.createStatement();
	 * rs = stat.executeQuery(sql);
	 * * while (rs.next()) {
	 * * BoardDTO dto = new BoardDTO();
	 * * dto.setSeq(rs.getString("seq")); dto.setSubject(rs.getString("subject"));
	 * dto.setId(rs.getString("id")); dto.setReadcount(rs.getString("readcount"));
	 * dto.setRegdate(rs.getString("regdate"));
	 * * dto.setName(rs.getString("name")); dto.setIsnew(rs.getDouble("isnew"));
	 * dto.setCommentCount(rs.getString("commentCount"));
	 * * dto.setSecret(rs.getString("secret")); dto.setNotice(rs.getString("notice"));
	 * * list.add(0, dto); }
	 * * * return list;
	 * * } catch (Exception e) { e.printStackTrace(); }
	 * * * return null; }
	 */

	/**
     * 핫딜 게시물의 조회수를 1 증가시킵니다.
     *
     * @param seq 조회수를 증가시킬 핫딜 게시물 ID (hotdeal_id)
     */
	public void updateReadcount(String seq) {
		// queryParamNoReturn
		try {

			String sql = "update tblHotDealPost set hotdeal_view_count = hotdeal_view_count + 1 where hotdeal_id = ?";

			pstat = conn.prepareStatement(sql);
			pstat.setString(1, seq);

			pstat.executeUpdate();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
     * 핫딜 게시물의 댓글을 추가로 로드합니다. (더보기 페이징 기능)
     *
     * @param map 게시물 ID(bseq) 및 페이징 시작 번호(begin)가 담긴 Map
     * @return 댓글 DTO 리스트 (List<CommentDTO>)
     */
	public List<CommentDTO> moreComment(Map<String, String> map) {
		// ##댓글 불러오기
		// queryParamListReturn
		try {

			String sql = "select * from (select a.*, rownum as rnum from (select tblHotDealComment.*, (select nickname from tblUser where user_id = tblHotDealComment.user_id) as name, (select username from tblUser where user_id = tblHotDealComment.user_id) as login from tblHotDealComment where hotdeal_id = ? order by hotdeal_id desc) a ) where rnum between ? and ? + 4";

			pstat = conn.prepareStatement(sql);
			pstat.setString(1, map.get("bseq"));
			pstat.setString(2, map.get("begin"));
			pstat.setString(3, map.get("begin"));

			rs = pstat.executeQuery();

			ArrayList<CommentDTO> list = new ArrayList<CommentDTO>();

			while (rs.next()) {

				CommentDTO dto = new CommentDTO();

				dto.setSeq(rs.getString("hotdeal_comment_id"));
				dto.setContent(rs.getString("hotdeal_comment_content"));
				dto.setId(rs.getString("login"));
				dto.setName(rs.getString("name"));
				dto.setRegdate(rs.getString("hotdeal_comment_regdate"));
				dto.setBseq(rs.getString("hotdeal_id"));

				list.add(dto);
			}

			return list;

		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
     * 특정 핫딜 게시물의 상세 정보를 조회합니다.
     *
     * @param seq 조회할 핫딜 게시물 ID (hotdeal_id)
     * @return 핫딜 게시물 DTO (게시물이 없을 경우 null)
     */
	public BoardDTO get(String seq) {
		// queryParamDTOReturn
		try {

			String sql = "select tblHotDealPost.*, (select nickname from tblUser where user_id = tblHotDealPost.user_id) as name, (SELECT hotdeal_image_url FROM tblHotDealImage WHERE hotdeal_id = tblHotDealPost.hotdeal_id AND ROWNUM = 1) as img, (select hotdeal_category from tblHotDealcategory where hotdeal_category_id = tblHotDealPost.hotdeal_category_id) as category,(select hotdeal_status from tblHotDealStatus where hotdeal_status_id = tblHotDealPost.hotdeal_status_id) as status from tblHotDealPost where hotdeal_id = ?";

			pstat = conn.prepareStatement(sql);
			pstat.setString(1, seq);

			rs = pstat.executeQuery();

			if (rs.next()) {

				BoardDTO dto = new BoardDTO();

				dto.setSeq(rs.getString("hotdeal_id"));
				dto.setSubject(rs.getString("hotdeal_title"));
				dto.setId(rs.getString("user_id"));
				// dto.setReadcount(rs.getString("readcount"));
				dto.setRegdate(rs.getString("hotdeal_regdate"));
				dto.setContent(rs.getString("hotdeal_content"));
				dto.setImg(rs.getString("img"));

				dto.setName(rs.getString("name"));
				dto.setCategory(rs.getString("category"));
				dto.setStatus(rs.getString("status"));
				dto.setItemName(rs.getString("hotdeal_name"));
				dto.setPrice(rs.getString("hotdeal_price"));
				dto.setUrl(rs.getString("hotdeal_url"));

				// dto.setAttach(rs.getString("attach"));

				// dto.setSecret(rs.getString("secret"));

				// dto.setScrapbook(rs.getString("scrapbook"));

				// 해시태그 추가
				/*
				 * sql = "select h.hashtag\r\n" + "from tblBoard b \r\n" +
				 * "    inner join tblTagging t\r\n" + "        on b.seq = t.bseq\r\n" +
				 * "            inner join tblHashtag h\r\n" +
				 * "                on h.seq = t.hseq\r\n" + "    where b.seq = ?";
				 * * pstat = conn.prepareStatement(sql); pstat.setString(1, seq); rs =
				 * pstat.executeQuery();
				 */

				// List<String> tlist = new ArrayList<String>();

				/*
				 * while (rs.next()) { tlist.add(rs.getString("hashtag")); }
				 */
				// dto.setHashtag(tlist);

				return dto;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
     * 핫딜 게시물의 최근 댓글 5개를 조회합니다. (상세보기 초기 로드용)
     *
     * @param bseq 핫딜 게시물 ID (hotdeal_id)
     * @return 댓글 DTO 리스트 (List<CommentDTO>)
     */
	public List<CommentDTO> listComment(String bseq) {
		// queryParamListReturn
		try {

			String sql = "select * from (select vw_hotdeal_comment.*, (select nickname from tblUser where user_id = vw_hotdeal_comment.user_id) as name from vw_hotdeal_comment where hotdeal_id = ? order by hotdeal_comment_id desc) where rownum <= 5";

			pstat = conn.prepareStatement(sql);
			pstat.setString(1, bseq);

			rs = pstat.executeQuery();

			ArrayList<CommentDTO> list = new ArrayList<CommentDTO>();

			while (rs.next()) {

				CommentDTO dto = new CommentDTO();

				dto.setSeq(rs.getString("hotdeal_comment_id"));
				dto.setContent(rs.getString("content"));
				dto.setId(rs.getString("user_id"));
				dto.setName(rs.getString("name"));
				dto.setRegdate(rs.getString("regdate"));
				dto.setBseq(rs.getString("hotdeal_id"));

				list.add(dto);
			}

			return list;

		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
     * 새로운 핫딜 게시물을 등록합니다.
     *
     * @param dto 등록할 핫딜 게시물 정보가 담긴 DTO
     * @return DB에 삽입된 행의 수 (성공 시 1, 실패 시 0)
     */
	public int add(BoardDTO dto) {
		System.out.println("test2");
		System.out.println(dto);

		// queryParamNoReturn
		try {

			String sql = "insert into tblHotDealPost (hotdeal_id, user_id, hotdeal_category_id, hotdeal_status_id, hotdeal_title, hotdeal_content, hotdeal_name, hotdeal_price, hotdeal_url, hotdeal_view_count, hotdeal_report_count, hotdeal_report_status, hotdeal_regdate) values (seqHotDealPost.nextVal, ?, ?, ?, ?, ?,? ,? ,?, 0, 0,  'y', sysdate)";

			// 현재 장소 > 특정 데이터 없어?
			// 1. 이 장소에서 특정 데이터를 가져올 수 있는지? > 스스로
			// 2. 현재 객체를 호출한쪽에서 특정 데이터를 전달해줄 수 있는지? > 전달

			// 로그인한 유저의 아이디 > 세션
			
			System.out.println(dto);

			pstat = conn.prepareStatement(sql);
			pstat.setInt(1, Integer.parseInt(dto.getSeq()));
			pstat.setInt(2, Integer.parseInt(dto.getCategory()));
			pstat.setInt(3, Integer.parseInt(dto.getStatus()));
			pstat.setString(4, dto.getSubject());
			pstat.setString(5, dto.getContent());
			pstat.setString(6, dto.getItemName());
			pstat.setString(7, dto.getPrice());
			pstat.setString(8, dto.getUrl());

			return pstat.executeUpdate();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return 0;
	}

	/**
     * [비권장] 현재 `addimg`에서만 사용. 가장 최근에 삽입된 핫딜 게시물의 ID를 조회합니다.
     *
     * @param dto (사용되지 않음)
     * @return 가장 큰 hotdeal_id (String)
     */
	public String recent(BoardDTO dto) {
		// queryNoParamTokenReturn
		try {

			String sql = "select max(hotdeal_id) as hotdeal_id from tblHotDealPost";

			stat = conn.createStatement();

			rs = stat.executeQuery(sql);

			if (rs.next()) {
				return rs.getString("hotdeal_id");
			}

		} catch (Exception e) {
			System.out.println("BoardDAO.recent");
			e.printStackTrace();
		}

		return null;
	}

	/**
     * 핫딜 게시물에 이미지를 추가합니다. (기본 순서 1로 삽입)
     *
     * @param dto    이미지 URL(img)이 포함된 DTO
     * @param recent 이미지를 추가할 핫딜 게시물 ID (hotdeal_id)
     * @return DB에 삽입된 행의 수
     */
	public int addimg(BoardDTO dto, String recent) {
		try {

			String sql = "insert into tblHotDealImage (hotdeal_image_id, hotdeal_id, hotdeal_image_url, hotdeal_image_seq) values (seqHotDealImage.nextVal, ?, ?, 1)";

			System.out.println(recent + "확인용");
			pstat = conn.prepareStatement(sql);
			pstat.setString(1, recent);
			pstat.setString(2, dto.getImg());

			return pstat.executeUpdate();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return 0;
	}

	/**
     * 가장 최근(MAX)의 핫딜 게시물 ID를 조회합니다. (게시물 추가 직후 ID 확인용)
     *
     * @return 가장 큰 hotdeal_id (String)
     */
	public String getBseq() {
		// queryNoParamTokenReturn
		try {

			String sql = "select max(hotdeal_id) as hotdeal_id from tblHotDealPost";

			stat = conn.createStatement();

			rs = stat.executeQuery(sql);

			if (rs.next()) {
				return rs.getString("hotdeal_id");
			}

		} catch (Exception e) {
			System.out.println("BoardDAO.getBseq");
			e.printStackTrace();
		}

		return null;
	}

	/**
     * 특정 핫딜 게시물을 삭제합니다.
     * 트랜잭션을 사용하여 관련된 댓글, 좋아요, 이미지 데이터를 먼저 삭제하고
     * 부모 게시물을 삭제합니다.
     *
     * @param seq 삭제할 핫딜 게시물 ID (hotdeal_id)
     * @return 삭제된 총 행의 수
     */
	public int del(String seq) {

	    // 사용할 PreparedStatement들을 try문 외부에서 선언
	    PreparedStatement pstat1 = null;
	    PreparedStatement pstat2 = null;
	    PreparedStatement pstat3 = null;
	    PreparedStatement pstat4 = null;
	    PreparedStatement pstat5 = null;
	    int result = 0; // 총 삭제된 행의 수를 저장할 변수

	    try {
	        // 1. 트랜잭션 시작: 자동 커밋 기능 비활성화
	        conn.setAutoCommit(false);



	        // 3. 두 번째 DELETE 실행 (연관된 데이터 테이블 1)
	        //    (실제 프로젝트의 테이블과 컬럼명으로 수정하세요)
	        String sql2 = "delete from tblHotDealComment where hotdeal_id = ?";
	        pstat2 = conn.prepareStatement(sql2);
	        pstat2.setString(1, seq);
	        result += pstat2.executeUpdate();

	        // 4. 세 번째 DELETE 실행 (연관된 데이터 테이블 2)
	        //    (실제 프로젝트의 테이블과 컬럼명으로 수정하세요)
	        String sql3 = "delete from tblHotDealLikes where hotdeal_id = ?";
	        pstat3 = conn.prepareStatement(sql3);
	        pstat3.setString(1, seq);
	        result += pstat3.executeUpdate();
	        
	        // 4. 세 번째 DELETE 실행 (연관된 데이터 테이블 2)
	        //    (실제 프로젝트의 테이블과 컬럼명으로 수정하세요)
	        String sql4 = "delete from tblHotDealImage where hotdeal_id = ?";
	        pstat4 = conn.prepareStatement(sql4);
	        pstat4.setString(1, seq);
	        result += pstat4.executeUpdate();
	        
	        
	        // 4. 세 번째 DELETE 실행 (연관된 데이터 테이블 2)
	        //    (실제 프로젝트의 테이블과 컬럼명으로 수정하세요)
	        String sql5 = "delete from tblHotDealImage where hotdeal_id = ?";
	        pstat5 = conn.prepareStatement(sql5);
	        pstat5.setString(1, seq);
	        result += pstat5.executeUpdate();

	        
	        // 2. 첫 번째 DELETE 실행
	        String sql1 = "delete from tblHotDealPost where hotdeal_id = ?";
	        pstat1 = conn.prepareStatement(sql1);
	        pstat1.setString(1, seq);
	        result += pstat1.executeUpdate();


	        // 5. 모든 DELETE 작업이 성공하면 DB에 최종 반영 (커밋)
	        conn.commit();

	    } catch (Exception e) {
	        e.printStackTrace();
	        try {
	            // 6. 작업 중 하나라도 실패하면 모든 변경사항을 취소 (롤백)
	            conn.rollback();
	        } catch (Exception e2) {
	            e2.printStackTrace();
	        }
	    } finally {
	        try {
	            // 7. 사용한 리소스를 정리하고, AutoCommit 설정을 원상 복구
	            if (pstat1 != null) pstat1.close();
	            if (pstat2 != null) pstat2.close();
	            if (pstat3 != null) pstat3.close();
	            if (pstat4 != null) pstat4.close();
	            if (pstat5 != null) pstat5.close();
	            conn.setAutoCommit(true);
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    }

	    return result; // 3개의 delete문으로 삭제된 총 행의 수 반환
	}

	/**
     * 핫딜 게시물의 제목과 내용을 수정합니다.
     *
     * @param dto 수정할 내용(subject, content)과 게시물 ID(seq)가 담긴 DTO
     * @return DB에 수정된 행의 수
     */
	public int edit(BoardDTO dto) {
		// queryParamNoReturn
		try {

			String sql = "update tblHotDealPost set hotdeal_title = ?, hotdeal_content = ? where hotdeal_id = ?";

			pstat = conn.prepareStatement(sql);
			pstat.setString(1, dto.getSubject());
			pstat.setString(2, dto.getContent());
			pstat.setString(3, dto.getSeq()); // ???

			return pstat.executeUpdate();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return 0;
	}

	/**
     * 핫딜 게시물에 새 댓글을 추가합니다.
     *
     * @param dto 댓글 내용(content), 작성자ID(seq), 게시물ID(bseq)가 담긴 DTO
     * @return DB에 삽입된 행의 수
     */
	public int addComment(CommentDTO dto) {
		// queryParamNoReturn
		try {

			String sql = "insert into tblHotDealComment (hotdeal_comment_id, user_id, hotdeal_id, hotdeal_comment_content, hotdeal_comment_regdate, hotdeal_comment_report_count, hotdeal_comment_status) values (seqHotDealComment.nextVal, ?, ?, ?, sysdate, 0, 'y')";

			pstat = conn.prepareStatement(sql);
			pstat.setString(1, dto.getSeq());
			pstat.setString(2, dto.getBseq());
			pstat.setString(3, dto.getContent());

			return pstat.executeUpdate();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return 0;
	}

	/**
     * 가장 최근에 작성된(MAX ID) 댓글 1개를 조회합니다. (댓글 추가 직후 AJAX 콜백용)
     *
     * @return 최근 댓글 DTO (없을 경우 null)
     */
	public CommentDTO getComment() {
		// queryNoParamDTOReturn
		try {

			String sql = "select tblHotDealComment.*,(select nickname from tblUser where user_id = tblHotDealComment.user_id) as name from tblHotDealComment where hotdeal_comment_id = (select max(hotdeal_comment_id) from tblHotDealComment)";

			stat = conn.createStatement();
			rs = stat.executeQuery(sql);

			if (rs.next()) {

				CommentDTO dto = new CommentDTO();

				dto.setSeq(rs.getString("hotdeal_comment_id"));
				dto.setContent(rs.getString("hotdeal_comment_content"));
				dto.setId(rs.getString("user_id"));
				dto.setName(rs.getString("name"));
				dto.setRegdate(rs.getString("hotdeal_comment_regdate"));
				dto.setBseq(rs.getString("hotdeal_id"));

				return dto;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
     * 댓글 내용을 수정합니다.
     *
     * @param dto 수정할 내용(content)과 댓글 ID(seq)가 담긴 DTO
     * @return DB에 수정된 행의 수
     */
	public int editComment(CommentDTO dto) {
		// queryParamNoReturn
		try {

			String sql = "update tblHotDealComment set hotdeal_comment_content = ? where hotdeal_comment_id = ?";

			pstat = conn.prepareStatement(sql);
			pstat.setString(1, dto.getContent());
			pstat.setString(2, dto.getSeq());

			return pstat.executeUpdate();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return 0;
	}

	/**
     * 댓글을 삭제합니다.
     *
     * @param seq 삭제할 댓글 ID (hotdeal_comment_id)
     * @return DB에 삭제된 행의 수
     */
	public int delComment(String seq) {
		// queryParamNoReturn
		try {

			String sql = "delete from tblHotDealComment where hotdeal_comment_id = ?";

			pstat = conn.prepareStatement(sql);
			pstat.setString(1, seq);

			return pstat.executeUpdate();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return 0;
	}

	/**
     * 핫딜 게시물의 신고 횟수를 1 증가시킵니다.
     * (동일 사용자가 중복 신고하는 것을 방지하는 로직은 없어 보임)
     *
     * @param dto 게시물 ID(seq)와 사용자 ID(useq)가 담긴 DTO
     */
	public void addReport(BoardDTO dto) {
		// queryParamNoReturn
				try {

					String sql = "update tblHotDealPost set hotdeal_report_count = hotdeal_report_count + 1  where hotdeal_id = ? and user_id = ?";

					pstat = conn.prepareStatement(sql);
					pstat.setString(1, dto.getSeq());
					pstat.setString(2, dto.getUseq());

					pstat.executeUpdate();

				} catch (Exception e) {
					e.printStackTrace();
				}

		
	}

	/**
     * [마이페이지] 특정 사용자의 전체 '게시물' 활동 내역(vwUserAllActivities) 수를 조회합니다. (검색 포함)
     *
     * @param map 사용자 ID(seq) 및 검색 조건(search, column, word)이 담긴 Map
     * @return 활동 내역 수
     */
	public int getAllBoardTotalCount(Map<String, String> map) {
		try {

			String where = "";

			if (map.get("search").equals("y")) {

				where = String.format("and %s like '%%%s%%'", map.get("column"), map.get("word"));

			}

			String sql = "select count(*) as cnt from vwUserAllActivities where user_id = ?" + where;

			pstat = conn.prepareStatement(sql);
			pstat.setString(1, map.get("seq"));
			
			rs = pstat.executeQuery();

			if (rs.next()) {

				return rs.getInt("cnt");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return 0;
	}

	/**
     * [마이페이지] 특정 사용자의 전체 '게시물' 활동 목록을 페이징하여 조회합니다. (검색 포함)
     *
     * @param map 사용자 ID(seq), 페이징(begin, end), 검색 조건이 담긴 Map
     * @return 활동 내역 DTO 리스트 (List<BoardDTO>)
     */
	public List<BoardDTO> totalBoardList(Map<String, String> map) {
		//queryNoParamListReturn
				try {
					
					//목록보기 > select * from vwBoard
					//검색하기 > select * from vwBoard where 조건
					
					String where = "";
					
					if (map.get("search").equals("y")) {
						
						//where = "where 조건";
						//where subject like '%검색어%'
						//where content like '%검색어%'
						//where name like '%검색어%'

						where = String.format("and  %s like '%%%s%%'"
								,	map.get("seq")				
								, map.get("column")
												, map.get("word"));

					}
					
					String sql = "";
					sql = String.format("select * from (select a.*, rownum as rnum from (select * from vwUserAllActivities where user_id = %s %s order by regdate desc) a) where rnum between %s and %s"		
							,	map.get("seq")	
					                    , where
					                    , map.get("begin")
					                    , map.get("end"));
				
					
					stat = conn.createStatement();
					rs = stat.executeQuery(sql);
					
					List<BoardDTO> list = new ArrayList<BoardDTO>();
					
					while (rs.next()) {
						
						BoardDTO dto = new BoardDTO();

						dto.setSeq(rs.getString("bseq"));
						dto.setBoradTitle(rs.getString("activity_type"));
						dto.setBoradCode(rs.getString("activity_code"));
						dto.setSubject(rs.getString("title"));
						dto.setRegdate(rs.getString("regdate"));
						
						list.add(dto);		
					}	
					
					
					return list;
					
				} catch (Exception e) {
					e.printStackTrace();
				}
						
				
				return null;
	}

	/**
     * [마이페이지] 특정 사용자의 전체 '댓글' 활동 내역(vwUserAllComments) 수를 조회합니다. (검색 포함)
     *
     * @param map 사용자 ID(seq) 및 검색 조건(search, column, word)이 담긴 Map
     * @return 댓글 수
     */
	public int getAllCommentTotalCount(Map<String, String> map) {
		try {

			String where = "";

			if (map.get("search").equals("y")) {

				where = String.format("and %s like '%%%s%%'", map.get("column"), map.get("word"));

			}

			String sql = "select count(*) as cnt from vwUserAllComments where user_id = ?" + where;

			pstat = conn.prepareStatement(sql);
			pstat.setString(1, map.get("seq"));
			
			rs = pstat.executeQuery();

			if (rs.next()) {

				return rs.getInt("cnt");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return 0;
	}

	/**
     * [마이페이지] 특정 사용자의 전체 '댓글' 활동 목록을 페이징하여 조회합니다. (검색 포함)
     *
     * @param map 사용자 ID(seq), 페이징(begin, end), 검색 조건이 담긴 Map
     * @return 댓글 활동 DTO 리스트 (List<BoardDTO>)
     */
	public List<BoardDTO> totalCommentList(Map<String, String> map) {
		//queryNoParamListReturn
		try {
			
			//목록보기 > select * from vwBoard
			//검색하기 > select * from vwBoard where 조건
			
			String where = "";
			
			if (map.get("search").equals("y")) {
				
				//where = "where 조건";
				//where subject like '%검색어%'
				//where content like '%검색어%'
				//where name like '%검색어%'

				where = String.format("and %s like '%%%s%%'"
										, map.get("column")
										, map.get("word"));

			}
			
			String sql = "";
			sql = String.format("select * from (select a.*, rownum as rnum from (select * from vwUserAllComments where user_id = %s %s order by regdate desc) a) where rnum between %s and %s",
					
					map.get("seq"),	
			                    where,
			                    map.get("begin"),
			                    map.get("end"));
		
			
			stat = conn.createStatement();
			rs = stat.executeQuery(sql);
			
			List<BoardDTO> list = new ArrayList<BoardDTO>();
			
			while (rs.next()) {
				
				BoardDTO dto = new BoardDTO();
				
				dto.setSeq(rs.getString("parent_post_id"));
				dto.setBoradTitle(rs.getString("comment_type"));
				dto.setBoradCode(rs.getString("activity_code"));
				dto.setSubject(rs.getString("parent_post_title"));
				dto.setRegdate(rs.getString("regdate"));
				dto.setCommentcontent(rs.getString("content"));
				
				list.add(dto);		
			}	
			
			
			return list;
			
		} catch (Exception e) {
			e.printStackTrace();
		}
				
		
		return null;
	}

	/**
     * [마이페이지] 특정 사용자의 전체 '좋아요' 내역(vwUserAllLikes) 수를 조회합니다. (검색 포함)
     *
     * @param map 사용자 ID(seq) 및 검색 조건(search, column, word)이 담긴 Map
     * @return 좋아요 수
     */
	public int getAllLikeTotalCount(Map<String, String> map) {
		try {

			String where = "";

			if (map.get("search").equals("y")) {

				where = String.format("and %s like '%%%s%%'", map.get("column"), map.get("word"));

			}

			String sql = "select count(*) as cnt from vwUserAllLikes where user_id = ?" + where;

			pstat = conn.prepareStatement(sql);
			pstat.setString(1, map.get("seq"));
			
			rs = pstat.executeQuery();

			if (rs.next()) {

				return rs.getInt("cnt");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return 0;
	}

	/**
     * [마이페이지] 특정 사용자의 전체 '좋아요' 목록을 페이징하여 조회합니다. (검색 포함)
     *
     * @param map 사용자 ID(seq), 페이징(begin, end), 검색 조건이 담긴 Map
     * @return 좋아요 DTO 리스트 (List<BoardDTO>)
     */
	public List<BoardDTO> totalLikeList(Map<String, String> map) {
		try {
			
			//목록보기 > select * from vwBoard
			//검색하기 > select * from vwBoard where 조건
			
			String where = "";
			
			if (map.get("search").equals("y")) {
				
				//where = "where 조건";
				//where subject like '%검색어%'
				//where content like '%검색어%'
				//where name like '%검색어%'

				where = String.format("where %s like '%%%s%%'"
										, map.get("column")
										, map.get("word"));

			}
			
			String sql = "";
				sql = String.format("select * from (select a.*, rownum as rnum from vwUserAllLikes a where user_id = %s %s order by regdate desc) where rnum between %s and %s"
						, map.get("seq")
								, where
								, map.get("begin")
								, map.get("end"));
		
			
			stat = conn.createStatement();
			rs = stat.executeQuery(sql);
			
			List<BoardDTO> list = new ArrayList<BoardDTO>();
			
			while (rs.next()) {
				
				BoardDTO dto = new BoardDTO();
				
				dto.setSeq(rs.getString("post_id"));
				dto.setBoradTitle(rs.getString("board_name"));
				dto.setBoradCode(rs.getString("board_type"));
				dto.setSubject(rs.getString("post_title"));
				dto.setRegdate(rs.getString("regdate"));
				
				list.add(dto);		
			}	
			
			
			return list;
			
		} catch (Exception e) {
			e.printStackTrace();
		}
				
		
		return null;
	}

	/**
     * [마이페이지] 특정 사용자의 전체 '스크랩' 내역(vwAllScraps) 수를 조회합니다. (검색 포함)
     *
     * @param map 사용자 ID(seq) 및 검색 조건(search, column, word)이 담긴 Map
     * @return 스크랩 수
     */
	public int getAllScrapTotalCount(Map<String, String> map) {
		try {

			String where = "";

			if (map.get("search").equals("y")) {

				where = String.format("and %s like '%%%s%%'", map.get("column"), map.get("word"));

			}

			String sql = "select count(*) as cnt from vwAllScraps where user_id = ?" + where;

			pstat = conn.prepareStatement(sql);
			pstat.setString(1, map.get("seq"));
			
			rs = pstat.executeQuery();

			if (rs.next()) {

				return rs.getInt("cnt");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return 0;
	}

	/**
     * [마이페이지] 특정 사용자의 전체 '스크랩' 목록을 페이징하여 조회합니다. (검색 포함)
     *
     * @param map 사용자 ID(seq), 페이징(begin, end), 검색 조건이 담긴 Map
     * @return 스크랩 DTO 리스트 (List<BoardDTO>)
     */
	public List<BoardDTO> totalScrapList(Map<String, String> map) {
try {
			
			//목록보기 > select * from vwBoard
			//검색하기 > select * from vwBoard where 조건
			
			String where = "";
			
			if (map.get("search").equals("y")) {
				
				//where = "where 조건";
				//where subject like '%검색어%'
				//where content like '%검색어%'
				//where name like '%검색어%'

				where = String.format("and %s like '%%%s%%'"
										, map.get("column")
										, map.get("word"));

			}
			
			String sql = "";
				sql = String.format("select * from (select a.*, rownum as rnum from vwAllScraps a where user_id = %s %s ORDER BY a.post_regdate DESC) where rnum between %s and %s"
						, map.get("seq")	
						, where
								, map.get("begin")
								, map.get("end"));
		
			
			stat = conn.createStatement();
			rs = stat.executeQuery(sql);
			
			List<BoardDTO> list = new ArrayList<BoardDTO>();
			
			while (rs.next()) {
				
				BoardDTO dto = new BoardDTO();
				
				dto.setSeq(rs.getString("content_id"));
				dto.setBoradTitle(rs.getString("board_name"));
				dto.setBoradCode(rs.getString("content_type"));
				dto.setSubject(rs.getString("post_title"));
				dto.setRegdate(rs.getString("post_regdate"));
				
				list.add(dto);		
			}	
			
			
			return list;
			
		} catch (Exception e) {
			e.printStackTrace();
		}
				
		
		return null;
	}

	

}
