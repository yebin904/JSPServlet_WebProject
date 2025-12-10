package com.trip.qna.model;

import java.sql.*;
import java.util.*;
import com.test.util.DBUtil;

/**
 * Q&A 카테고리 관련 데이터베이스 작업을 처리하는 DAO 클래스
 * @author jsg
 * @version 1.0
 * @since 2025.10.24
 */
public class CategoryDAO {
    /**
     * 데이터베이스 연결을 위한 Connection 객체입니다.
     */
    private Connection conn;
    /**
     * SQL 쿼리 실행을 위한 PreparedStatement 객체입니다.
     */
    private PreparedStatement pstat;
    /**
     * SQL 쿼리 결과를 저장하는 ResultSet 객체입니다.
     */
    private ResultSet rs;

    /**
     * CategoryDAO의 생성자입니다.
     * 데이터베이스 연결을 초기화합니다.
     */
    public CategoryDAO() {
        conn = new DBUtil().open();
    }

    /**
     * 모든 Q&A 카테고리 목록을 조회합니다.
     * @return 카테고리 정보를 담은 CategoryDTO 객체들의 리스트
     */
    public List<CategoryDTO> list() {
        List<CategoryDTO> result = new ArrayList<>();
        try {
            String sql = "SELECT question_category_id, question_category_name FROM tblQuestionCategory ORDER BY question_category_id";
            pstat = conn.prepareStatement(sql);
            rs = pstat.executeQuery();
            while (rs.next()) {
                CategoryDTO dto = new CategoryDTO();
                dto.setQuestion_category_id(rs.getString("question_category_id"));
                dto.setQuestion_category_name(rs.getString("question_category_name"));
                result.add(dto);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 데이터베이스 연결을 닫습니다.
     */
    public void close() {
        try { conn.close(); } catch (Exception e) { e.printStackTrace(); }
    }
}
