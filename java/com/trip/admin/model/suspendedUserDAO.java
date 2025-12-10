package com.trip.admin.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.test.util.DBUtil;

/**
 * 정지된 사용자 관련 데이터베이스 작업을 처리하는 DAO 클래스
 * @author jsg
 * @version 1.0
 * @since 2025.10.24
 */
public class suspendedUserDAO {

    private Connection conn;

    /**
     * suspendedUserDAO 객체를 생성하고 데이터베이스 연결을 초기화합니다.
     */
    public suspendedUserDAO() {
    	DBUtil dbUtil = new DBUtil();
        this.conn = dbUtil.open();
    }

    /**
     * 정지된 모든 사용자의 목록을 가져오는 메소드입니다.
     * tblMemSuspended와 tblUser 테이블을 JOIN하여 필요한 정보를 조회합니다.
     * @return suspendedUserDTO 객체를 담은 List
     */
    public List<suspendedUserDTO> getSuspendedUserList() {
        
        List<suspendedUserDTO> list = new ArrayList<>();
        
        try {
            String sql = "SELECT "
                       + "    s.memsuspended_id, "
                       + "    u.user_id, "
                       + "    u.nickname, "
                       + "    s.suspended_reason, "
                       + "    s.suspended_startdate, "
                       + "    s.suspended_enddate "
                       + "FROM tblMemSuspended s "
                       + "    INNER JOIN tblUser u ON s.user_id = u.user_id "
                       + "ORDER BY s.suspended_startdate DESC";

            PreparedStatement pstat = conn.prepareStatement(sql);
            ResultSet rs = pstat.executeQuery();

            while (rs.next()) {
                suspendedUserDTO dto = new suspendedUserDTO();
                
                dto.setMemsuspendedId(rs.getInt("memsuspended_id"));
                dto.setUserId(rs.getInt("user_id"));
                dto.setNickname(rs.getString("nickname"));
                dto.setSuspendedReason(rs.getString("suspended_reason"));
                dto.setSuspendedStartDate(rs.getDate("suspended_startdate"));
                dto.setSuspendedEndDate(rs.getDate("suspended_enddate"));
                
                list.add(dto);
            }
            rs.close();
            pstat.close();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return list;
    }

    /**
     * 특정 사용자를 정지 상태에서 복구하는 메소드입니다.
     * @param userId 복구할 사용자의 ID
     */
    public void restoreUser(int userId) {
        try {
            String sqlDelete = "DELETE FROM tblMemSuspended WHERE user_id = ?";
            PreparedStatement pstatDelete = conn.prepareStatement(sqlDelete);
            pstatDelete.setInt(1, userId);
            pstatDelete.executeUpdate();
            pstatDelete.close();

            String sqlUpdate = "UPDATE tblUser SET user_status_id = 1 WHERE user_id = ?";
            PreparedStatement pstatUpdate = conn.prepareStatement(sqlUpdate);
            pstatUpdate.setInt(1, userId);
            pstatUpdate.executeUpdate();
            pstatUpdate.close();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}