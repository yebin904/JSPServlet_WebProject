package com.trip.qna.model;

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
 * Q&A 게시판 관련 데이터베이스 작업을 처리하는 DAO 클래스
 * @author jsg
 * @version 1.0
 * @since 2025.10.24
 */
public class QnADAO {

	/**
	 * 데이터베이스 연결을 위한 Connection 객체입니다.
	 */
	private Connection conn;
	/**
	 * SQL 문을 실행하기 위한 Statement 객체입니다.
	 */
	private Statement stat;
	/**
	 * SQL 쿼리 실행을 위한 PreparedStatement 객체입니다.
	 */
	private PreparedStatement pstat;
	/**
	 * SQL 쿼리 결과를 저장하는 ResultSet 객체입니다.
	 */
	private ResultSet rs;

	/**
	 * QnADAO의 생성자입니다.
	 * 데이터베이스 연결 및 Statement 객체를 초기화합니다.
	 */
	public QnADAO() {

		try {
			conn = new DBUtil().open();
			stat = conn.createStatement();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 게시글 목록 불러오기(검색 + 페이징)
	/**
	 * 검색 및 페이징 조건을 적용하여 Q&A 게시물 목록을 조회합니다.
	 * @param map 검색 조건(col, word, category) 및 페이징 정보(begin, end)를 담은 Map 객체
	 * @return QnADTO 객체들의 리스트
	 */
	public List<QnADTO> list(Map<String, String> map) {
		List<QnADTO> list = new ArrayList<>();

		try {
			String where = "";
			String col = map.get("col");
			String word = map.get("word");
			String category = map.get("category");

			if ("y".equals(map.get("search"))) {
				List<String> allowedColumns = List.of("question_board_title", "question_board_content", "user_id");
				if (col != null && !col.isEmpty() && word != null && !word.trim().isEmpty()) {
					if ("user_id".equals(col)) {
						where = "WHERE u.nickname LIKE ?";
					} else if (allowedColumns.contains(col)) {
						where = "WHERE b." + col + " LIKE ?";
					}
				}

				// 카테고리 필터링
				if (category != null && !category.isEmpty()) {
					if (where.isEmpty())
						where = "WHERE b.question_category_id = ?";
					else
						where += " AND b.question_category_id = ?";
				}
			} else if (category != null && !category.isEmpty()) {
				where = "WHERE b.question_category_id = ?";
			}

			String sql = "SELECT * FROM ( "
			        + "SELECT ROWNUM AS rnum, a.* FROM ( "
			        + "SELECT b.question_board_id, "
			        + "       b.question_board_title, "
			        + "       b.user_id AS board_user_id, "
			        + "       u.nickname, "
			        + "       b.question_board_regdate, "
			        + "       b.question_board_answer_status, "
			        + "       b.question_category_id, "
			        + "       c.question_category_name, " // ✅ 카테고리 이름 추가
			        + "       b.question_board_like_count, "
			        + "       b.question_board_scrap_count, "
			        + "       b.question_board_view_count, "
			        + "       b.question_board_comment_count "
			        + "FROM tblQuestionBoard b "
			        + "LEFT JOIN tblUser u ON b.user_id = u.user_id "
			        + "LEFT JOIN tblQuestionCategory c ON b.question_category_id = c.question_category_id " // ✅ 조인 추가
			        + (where.isEmpty() ? "" : " " + where)
			        + " ORDER BY b.question_board_id DESC "
			        + ") a ) WHERE rnum BETWEEN ? AND ?";


			PreparedStatement pstat = conn.prepareStatement(sql);
			int index = 1;

			if ("y".equals(map.get("search"))) {
				// 1. 검색어 조건: word가 있으면 ? 1개 추가
				if (col != null && !col.isEmpty() && word != null && !word.trim().isEmpty()) {
					pstat.setString(index++, "%" + word + "%"); // index 1 증가
				}
				// 2. 카테고리 조건: category가 있으면 ? 1개 추가
				if (category != null && !category.isEmpty()) {
					pstat.setString(index++, category); // index 1 증가
				}
			} else if (category != null && !category.isEmpty()) {
				// 3. 카테고리만 있는 경우: ? 1개 추가
				pstat.setString(index++, category); // index 1 증가
			}

			// 4. 페이징 조건: ? 2개 (begin, end)
			pstat.setString(index++, map.get("begin")); // index 1 증가(필수)
			pstat.setString(index++, map.get("end")); // index 1 증가(필수)

			rs = pstat.executeQuery();

			while (rs.next()) {
				QnADTO dto = new QnADTO();
				dto.setQuestion_board_id(rs.getString("question_board_id"));
				dto.setQuestion_board_title(rs.getString("question_board_title"));
				dto.setUser_id(rs.getString("board_user_id"));
				dto.setNickname(rs.getString("nickname"));
				dto.setQuestion_board_regdate(rs.getString("question_board_regdate"));
				dto.setQuestion_category_id(rs.getString("question_category_id"));
				dto.setQuestion_category_name(rs.getString("question_category_name"));
				dto.setQuestion_board_like_count(rs.getString("question_board_like_count"));
				dto.setQuestion_board_scrap_count(rs.getString("question_board_scrap_count"));
				dto.setQuestion_board_view_count(rs.getString("question_board_view_count"));
				dto.setQuestion_board_comment_count(rs.getString("question_board_comment_count"));

				int commentCnt = Integer.parseInt(dto.getQuestion_board_comment_count());
				dto.setQuestion_board_answer_status(commentCnt > 0 ? "y" : "n");

				String title = dto.getQuestion_board_title();
				dto.setDisplay_title(title.length() > 30 ? title.substring(0, 30) + "..." : title);

				String regdate = dto.getQuestion_board_regdate();
				dto.setDisplay_regdate(regdate.length() > 10 ? regdate.substring(0, 10) : regdate);

				list.add(dto);
			}

			

		} catch (Exception e) {
			e.printStackTrace();
		}

		return list;
	}

	// 카테고리 전체 조회
	/**
	 * 모든 Q&A 카테고리 목록을 조회합니다.
	 * @return 카테고리 정보를 담은 CategoryDTO 객체들의 ArrayList
	 */
	public ArrayList<CategoryDTO> getCategoryList() {
		ArrayList<CategoryDTO> list = new ArrayList<>();
		try {
			String sql = "SELECT question_category_id, question_category_name FROM tblQuestionCategory ORDER BY question_category_id";
			pstat = conn.prepareStatement(sql);
			rs = pstat.executeQuery();
			while (rs.next()) {
				CategoryDTO dto = new CategoryDTO();
				dto.setQuestion_category_id(rs.getString("question_category_id"));
				dto.setQuestion_category_name(rs.getString("question_category_name"));
				list.add(dto);
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	// 게시글 총 개수
	/**
	 * 검색 조건에 해당하는 Q&A 게시물의 총 개수를 조회합니다.
	 * @param map 검색 조건(col, word, category)을 담은 Map 객체
	 * @return 검색 조건에 맞는 게시물의 총 개수
	 */
	public int getTotalCount(Map<String, String> map) {
		int result = 0;

		try {
			String where = "";
			String col = map.get("col");
			String word = map.get("word");
			String category = map.get("category");

			if ("y".equals(map.get("search"))) {
				List<String> allowedColumns = List.of("question_board_title", "question_board_content", "user_id");
				if (col != null && !col.isEmpty() && word != null && !word.trim().isEmpty()) {
					if ("user_id".equals(col)) {
						where = "WHERE u.nickname LIKE '%" + word + "%'";
					} else if (allowedColumns.contains(col)) {
						where = "WHERE " + col + " LIKE '%" + word + "%'";
					}
				}
				if (category != null && !category.isEmpty()) {
					if (where.isEmpty())
						where = "WHERE question_category_id = " + category;
					else
						where += " AND question_category_id = " + category;
				}
			} else if (category != null && !category.isEmpty()) {
				where = "WHERE question_category_id = " + category;
			}

			String sql = "SELECT COUNT(*) AS cnt FROM tblQuestionBoard b "
					+ "LEFT JOIN tblUser u ON b.user_id = u.user_id " + where;

			rs = stat.executeQuery(sql);

			if (rs.next()) {
				result = rs.getInt("cnt");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;
	}

	/**
	 * 데이터베이스 관련 객체(ResultSet, PreparedStatement, Statement, Connection)를 닫습니다.
	 */
	public void close() {
		try {
			// rs, pstat, stat, conn 순서대로 닫기
			if (rs != null && !rs.isClosed())
				rs.close();
			if (pstat != null && !pstat.isClosed())
				pstat.close();
			if (stat != null && !stat.isClosed())
				stat.close();
			if (conn != null && !conn.isClosed())
				conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 게시글 상세 보기
	/**
	 * 특정 Q&A 게시물의 상세 정보를 조회합니다.
	 * @param question_board_id 조회할 게시물의 ID
	 * @return 게시물 정보를 담은 QnADTO 객체
	 */
	public QnADTO view(String question_board_id) {
		QnADTO dto = new QnADTO();
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			conn = new DBUtil().open();
			String sql = "SELECT b.question_board_id, b.user_id, u.nickname, b.question_board_title, "
					+ "b.question_board_content, b.question_board_view_count, "
					+ "b.question_board_like_count, b.question_board_scrap_count, "
					+ "b.question_board_comment_count, b.question_board_answer_status, "
					+ "b.question_board_status, b.question_board_regdate, b.question_board_update, "
					+ "b.question_board_report_count, b.question_category_id, c.question_category_name "
					+ "FROM tblQuestionBoard b " + "JOIN tblUser u ON b.user_id = u.user_id "
					+ "LEFT JOIN tblQuestionCategory c ON b.question_category_id = c.question_category_id "
					+ "WHERE b.question_board_id = ?";

			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, question_board_id);
			rs = pstmt.executeQuery();

			if (rs.next()) {
				dto.setQuestion_board_id(rs.getString("question_board_id"));
				dto.setUser_id(rs.getString("user_id"));
				dto.setNickname(rs.getString("nickname"));
				dto.setQuestion_board_title(rs.getString("question_board_title"));
				dto.setQuestion_board_content(rs.getString("question_board_content"));
				dto.setQuestion_board_view_count(rs.getString("question_board_view_count"));
				dto.setQuestion_board_like_count(rs.getString("question_board_like_count"));
				dto.setQuestion_board_scrap_count(rs.getString("question_board_scrap_count"));
				dto.setQuestion_board_comment_count(rs.getString("question_board_comment_count"));
				dto.setQuestion_board_answer_status(rs.getString("question_board_answer_status"));
				dto.setQuestion_board_status(rs.getString("question_board_status"));
				dto.setQuestion_board_regdate(rs.getString("question_board_regdate"));
				dto.setQuestion_board_update(rs.getString("question_board_update"));
				dto.setQuestion_board_report_count(rs.getString("question_board_report_count"));
				dto.setQuestion_category_id(rs.getString("question_category_id"));
				dto.setQuestion_category_name(rs.getString("question_category_name"));
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null)
					rs.close();
			} catch (Exception e) {
			}
			try {
				if (pstmt != null)
					pstmt.close();
			} catch (Exception e) {
			}
			try {
				if (conn != null)
					conn.close();
			} catch (Exception e) {
			}
		}

		return dto;
	}

	// 게시글 작성
	/**
	 * 새로운 Q&A 게시물을 데이터베이스에 추가합니다.
	 * @param dto 추가할 게시물 정보를 담은 QnADTO 객체
	 * @return 추가된 행의 수 (1: 성공, 0: 실패)
	 */
	public int add(QnADTO dto) {
	    int result = 0;

	    try {
	        String sql = "INSERT INTO tblQuestionBoard "
	                + "(question_board_id, user_id, question_category_id, question_board_title, question_board_content) "
	                + "VALUES (seqQuestionBoard.NEXTVAL, ?, ?, ?, ?)";

	        pstat = conn.prepareStatement(sql);
	        pstat.setString(1, dto.getUser_id());
	        pstat.setString(2, dto.getQuestion_category_id());
	        pstat.setString(3, dto.getQuestion_board_title());
	        pstat.setString(4, dto.getQuestion_board_content());

	        result = pstat.executeUpdate();

	    } catch (Exception e) {
	        e.printStackTrace();
	    }

	    return result;
	}

	// 게시글 수정
	/**
	 * Q&A 게시물 정보를 수정합니다.
	 * @param dto 수정할 게시물 정보를 담은 QnADTO 객체
	 * @return 수정된 행의 수 (1: 성공, 0: 실패)
	 */
	public int edit(QnADTO dto) {
	    int result = 0;
	    try {
	        String sql = "UPDATE tblQuestionBoard "
	                   + "SET question_board_title = ?, "
	                   + "    question_board_content = ?, "
	                   + "    question_category_id = ?, "
	                   + "    question_board_update = SYSDATE "
	                   + "WHERE question_board_id = ?";

	        pstat = conn.prepareStatement(sql);
	        pstat.setString(1, dto.getQuestion_board_title());
	        pstat.setString(2, dto.getQuestion_board_content());
	        pstat.setString(3, dto.getQuestion_category_id());
	        pstat.setString(4, dto.getQuestion_board_id());

	        result = pstat.executeUpdate();
	    } catch (Exception e) {
	        e.printStackTrace();
	    }

	    return result;
	}

// 게시글 삭제
	/**
	 * Q&A 게시물을 삭제합니다.
	 * @param seq 삭제할 게시물의 ID
	 * @return 삭제된 행의 수 (1: 성공, 0: 실패)
	 */
	public int del(String seq) {
	    String sql = "DELETE FROM tblQuestionBoard WHERE question_board_id=?";
	    try (PreparedStatement ps = conn.prepareStatement(sql)) {
	        ps.setString(1, seq);  // seq 그대로 문자열로 넣음
	        int result = ps.executeUpdate();
	        conn.commit();
	        return result;
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return 0;
	}
	
	// 답변 상태 변경
	/**
	 * Q&A 게시물의 답변 상태를 업데이트합니다.
	 * @param id 게시물 ID
	 * @param status 변경할 답변 상태 (예: 'y' 또는 'n')
	 * @return 업데이트된 행의 수 (1: 성공, 0: 실패)
	 */
	public int updateAnswerStatus(int id, String status) {
		String sql = "UPDATE tblQuestionBoard SET question_board_answer_status=? WHERE question_board_id=?";
		try (PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setString(1, status);
			ps.setInt(2, id);
			return ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}

	// 조회수 증가
	/**
	 * Q&A 게시물의 조회수를 1 증가시킵니다.
	 * @param id 조회수를 증가시킬 게시물의 ID
	 */
	public void updateReadCount(String id) {
		try {
			String sql = "UPDATE tblQuestionBoard SET question_board_view_count = question_board_view_count + 1 WHERE question_board_id = ?";
			pstat = conn.prepareStatement(sql);
			pstat.setString(1, id);
			pstat.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 게시글 등록
	/**
	 * 새로운 Q&A 게시물을 데이터베이스에 삽입합니다.
	 * @param dto 삽입할 게시물 정보를 담은 QnADTO 객체
	 * @return 삽입된 행의 수 (1: 성공, 0: 실패)
	 */
	public int insert(QnADTO dto) {
	    int result = 0;
	    try {
	        String sql = "INSERT INTO tblQuestionBoard "
	                   + "(question_board_id, user_id, question_board_title, question_board_content, "
	                   + "question_category_id, question_board_regdate, question_board_status) "
	                   + "VALUES (seqQuestionBoard.NEXTVAL, ?, ?, ?, ?, SYSDATE, 'y')";
	        
	        pstat = conn.prepareStatement(sql);
	        pstat.setString(1, dto.getUser_id());
	        pstat.setString(2, dto.getQuestion_board_title());
	        pstat.setString(3, dto.getQuestion_board_content());
	        pstat.setString(4, dto.getQuestion_category_id());
	        
	        result = pstat.executeUpdate();
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    return result;
	}

	// 좋아요 토글
	/**
	 * Q&A 게시물의 좋아요 상태를 토글합니다. (좋아요 추가 또는 취소)
	 * @param boardId 좋아요를 토글할 게시물의 ID
	 * @param userId 좋아요를 누른 사용자의 ID
	 * @return 좋아요 상태 (true: 좋아요, false: 좋아요 취소)
	 */
	public boolean toggleLike(String boardId, String userId) {
	    boolean alreadyLiked = isLiked(boardId, userId);

	    try {
	        if (alreadyLiked) {
	            // 좋아요 취소
	            String sql = "DELETE FROM tblQuestionLike WHERE question_board_id=? AND user_id=?";
	            PreparedStatement ps = conn.prepareStatement(sql);
	            ps.setString(1, boardId);
	            ps.setString(2, userId);
	            ps.executeUpdate();

	            sql = "UPDATE tblQuestionBoard SET question_board_like_count = question_board_like_count - 1 WHERE question_board_id=?";
	            ps = conn.prepareStatement(sql);
	            ps.setString(1, boardId);
	            ps.executeUpdate();

	            return false;
	        } else {
	            // 좋아요 추가
	            String sql = "INSERT INTO tblQuestionLike VALUES(seqQuestionLike.NEXTVAL, ?, ?)";
	            PreparedStatement ps = conn.prepareStatement(sql);
	            ps.setString(1, boardId);
	            ps.setString(2, userId);
	            ps.executeUpdate();

	            sql = "UPDATE tblQuestionBoard SET question_board_like_count = question_board_like_count + 1 WHERE question_board_id=?";
	            ps = conn.prepareStatement(sql);
	            ps.setString(1, boardId);
	            ps.executeUpdate();

	            return true;
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    return alreadyLiked;
	}

	/**
	 * 특정 사용자가 특정 게시물에 좋아요를 눌렀는지 확인합니다.
	 * @param boardId 게시물 ID
	 * @param userId 사용자 ID
	 * @return 좋아요 여부 (true: 좋아요 누름, false: 좋아요 누르지 않음)
	 */
	public boolean isLiked(String boardId, String userId) {
	    try {
	        String sql = "SELECT COUNT(*) FROM tblQuestionLike WHERE question_board_id=? AND user_id=?";
	        PreparedStatement ps = conn.prepareStatement(sql);
	        ps.setString(1, boardId);
	        ps.setString(2, userId);
	        ResultSet rs = ps.executeQuery();
	        if (rs.next()) return rs.getInt(1) > 0;
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    return false;
	}

	/**
	 * 특정 게시물의 좋아요 수를 조회합니다.
	 * @param boardId 게시물 ID
	 * @return 게시물의 좋아요 수
	 */
	public int getLikeCount(String boardId) {
	    try {
	        String sql = "SELECT question_board_like_count FROM tblQuestionBoard WHERE question_board_id=?";
	        PreparedStatement ps = conn.prepareStatement(sql);
	        ps.setString(1, boardId);
	        ResultSet rs = ps.executeQuery();
	        if (rs.next()) return rs.getInt(1);
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    return 0;
	}
	
	// 스크랩 토글
	/**
	 * Q&A 게시물의 스크랩 상태를 토글합니다. (스크랩 추가 또는 취소)
	 * @param boardId 스크랩을 토글할 게시물의 ID
	 * @param userId 스크랩을 한 사용자의 ID
	 * @return 스크랩 상태 (true: 스크랩됨, false: 스크랩 취소됨)
	 */
	public boolean toggleScrap(String boardId, String userId) {
	    boolean alreadyScrapped = isScrapped(boardId, userId);

	    try {
	        if (alreadyScrapped) {
	            String sql = "DELETE FROM tblQuestionScrap WHERE question_board_id=? AND user_id=?";
	            PreparedStatement ps = conn.prepareStatement(sql);
	            ps.setString(1, boardId);
	            ps.setString(2, userId);
	            ps.executeUpdate();

	            sql = "UPDATE tblQuestionBoard SET question_board_scrap_count = question_board_scrap_count - 1 WHERE question_board_id=?";
	            ps = conn.prepareStatement(sql);
	            ps.setString(1, boardId);
	            ps.executeUpdate();

	            return false;
	        } else {
	            String sql = "INSERT INTO tblQuestionScrap VALUES(seqQuestionScrap.NEXTVAL, ?, ?)";
	            PreparedStatement ps = conn.prepareStatement(sql);
	            ps.setString(1, boardId);
	            ps.setString(2, userId);
	            ps.executeUpdate();

	            sql = "UPDATE tblQuestionBoard SET question_board_scrap_count = question_board_scrap_count + 1 WHERE question_board_id=?";
	            ps = conn.prepareStatement(sql);
	            ps.setString(1, boardId);
	            ps.executeUpdate();

	            return true;
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    return alreadyScrapped;
	}

	/**
	 * 특정 사용자가 특정 게시물을 스크랩했는지 확인합니다.
	 * @param boardId 게시물 ID
	 * @param userId 사용자 ID
	 * @return 스크랩 여부 (true: 스크랩함, false: 스크랩하지 않음)
	 */
	public boolean isScrapped(String boardId, String userId) {
	    try {
	        String sql = "SELECT COUNT(*) FROM tblQuestionScrap WHERE question_board_id=? AND user_id=?";
	        PreparedStatement ps = conn.prepareStatement(sql);
	        ps.setString(1, boardId);
	        ps.setString(2, userId);
	        ResultSet rs = ps.executeQuery();
	        if (rs.next()) return rs.getInt(1) > 0;
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    return false;
	}

	/**
	 * 특정 게시물의 스크랩 수를 조회합니다.
	 * @param boardId 게시물 ID
	 * @return 게시물의 스크랩 수
	 */
	public int getScrapCount(String boardId) {
	    try {
	        String sql = "SELECT question_board_scrap_count FROM tblQuestionBoard WHERE question_board_id=?";
	        PreparedStatement ps = conn.prepareStatement(sql);
	        ps.setString(1, boardId);
	        ResultSet rs = ps.executeQuery();
	        if (rs.next()) return rs.getInt(1);
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    return 0;
	}



}
