package com.trip.admin.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.test.util.DBUtil;

/**
 * 게시판 관련 데이터베이스 작업을 처리하는 DAO 클래스
 * @author jsg
 * @version 1.0
 * @since 2025.10.24
 */
public class boardDAO {

    private Connection conn;

    /**
     * boardDAO 객체를 생성하고 데이터베이스 연결을 초기화합니다.
     */
    public boardDAO() { 
    	DBUtil dbUtil = new DBUtil();
    	this.conn = dbUtil.open(); }

    /**
     * 필터와 검색 기능을 포함하여 통합 게시판 목록을 조회합니다.
     * @param boardType 필터링할 게시판 종류 ("all" 또는 특정 게시판 타입)
     * @param searchType 검색할 컬럼 ("title", "nickname")
     * @param searchKeyword 검색어
     * @return 필터링 및 검색 조건에 맞는 게시글 목록
     */
    public List<integratedBoardDTO> list(String boardType, String searchType, String searchKeyword) {
        List<integratedBoardDTO> list = new ArrayList<>();
        try {
            String sql = "SELECT * FROM VWINTEGRATEDBOARD WHERE 1=1";

            if (boardType != null && !boardType.equals("all")) {
                sql += " AND boardType = ?";
            }

            if (searchKeyword != null && !searchKeyword.trim().isEmpty()) {
                if ("title".equals(searchType)) {
                    sql += " AND title LIKE ?";
                } else if ("nickname".equals(searchType)) {
                    sql += " AND nickname LIKE ?";
                }
            }
            
            sql += " ORDER BY regdate DESC";
            
            PreparedStatement pstat = conn.prepareStatement(sql);
            
            int parameterIndex = 1;

            if (boardType != null && !boardType.equals("all")) {
                pstat.setString(parameterIndex++, boardType);
            }

            if (searchKeyword != null && !searchKeyword.trim().isEmpty()) {
                pstat.setString(parameterIndex, "%" + searchKeyword + "%");
            }

            ResultSet rs = pstat.executeQuery();
            
            while (rs.next()) {
                integratedBoardDTO dto = new integratedBoardDTO();
                dto.setSeq(rs.getInt("SEQ"));
                dto.setBoardType(rs.getString("BOARDTYPE"));
                dto.setTitle(rs.getString("TITLE"));
                dto.setNickname(rs.getString("NICKNAME"));
                dto.setRegdate(rs.getDate("REGDATE"));
                dto.setViewCount(rs.getInt("VIEWCOUNT"));
                dto.setTargetType(rs.getString("TARGET_TYPE"));
                dto.setCommentCount(rs.getInt("COMMENTCOUNT"));
                dto.setLikeCount(rs.getInt("LIKECOUNT"));

                list.add(dto);
            }
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }
}