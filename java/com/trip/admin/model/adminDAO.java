package com.trip.admin.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.test.util.DBUtil;

/**
 * 관리자 관련 데이터베이스 작업을 처리하는 DAO 클래스
 * @author jsg
 * @version 1.0
 * @since 2025.10.24
 */
public class adminDAO {

    private Connection conn;

    /**
     * adminDAO 객체를 생성하고 데이터베이스 연결을 초기화합니다.
     */
    public adminDAO() { 
    	DBUtil dbUtil = new DBUtil();
        this.conn = dbUtil.open(); }

    /**
     * 관리자 로그인을 처리합니다.
     * @param dto 로그인 정보를 담은 adminDTO 객체
     * @return 로그인 성공 시 관리자 정보를 담은 adminDTO 객체, 실패 시 null
     */
    public adminDTO login(adminDTO dto) {
        try {
            String sql = "SELECT * FROM tblAdmin WHERE admin_name = ? AND admin_password = ?";
            PreparedStatement pstat = conn.prepareStatement(sql);
            pstat.setString(1, dto.getAdminName());
            pstat.setString(2, dto.getAdminPassword());
            ResultSet rs = pstat.executeQuery();

            if (rs.next()) {
                adminDTO result = new adminDTO();
                result.setAdminId(rs.getInt("admin_id"));
                result.setAdminName(rs.getString("admin_name"));
                return result;
            }
        } catch (Exception e) { e.printStackTrace(); }
        return null;
    }

    /**
     * 특정 관리자의 정보를 조회합니다.
     * @param adminId 조회할 관리자 ID
     * @return 관리자 정보를 담은 adminDTO 객체, 실패 시 null
     */
    public adminDTO getAdminInfo(int adminId) {
        try {
            String sql = "SELECT * FROM tblAdmin WHERE admin_id = ?";
            PreparedStatement pstat = conn.prepareStatement(sql);
            pstat.setInt(1, adminId);
            ResultSet rs = pstat.executeQuery();

            if (rs.next()) {
                adminDTO result = new adminDTO();
                result.setAdminId(rs.getInt("admin_id"));
                result.setAdminRealName(rs.getString("admin_real_name"));
                result.setAdminName(rs.getString("admin_name"));
                result.setAdminEmail(rs.getString("admin_email"));
                result.setAdminRegdate(rs.getDate("admin_regdate"));
                return result;
            }
        } catch (Exception e) { e.printStackTrace(); }
        return null;
    }
}
