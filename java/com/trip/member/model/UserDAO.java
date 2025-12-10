package com.trip.member.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.test.util.DBUtil;

/**
 * 사용자(User) 관련 데이터베이스 작업을 처리하는 DAO(Data Access Object) 클래스.
 * tblUser 테이블에 대한 CRUD(Create, Read, Update, Delete) 작업을 수행합니다.
 */
public class UserDAO {

	private DBUtil util;
	private Connection conn;
	private Statement stat;
	private PreparedStatement pstat;
	private ResultSet rs;

	/**
	 * UserDAO 생성자입니다.
	 * DBUtil을 초기화하고 데이터베이스 연결(Connection) 및 Statement를 생성합니다.
	 */
	public UserDAO() {
		try {
			util = new DBUtil();
			conn = util.open();
			stat = conn.createStatement();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 사용자의 아이디와 비밀번호를 검증하여 로그인을 처리합니다.
	 * * @param dto 로그인 시도하는 사용자의 username과 password가 담긴 UserDTO 객체
	 * @return 로그인 성공 시, 사용자의 기본 정보(ID, 이름, 닉네임)가 담긴 UserDTO. 실패 시 null.
	 */
	public UserDTO login(UserDTO dto) {
		try {
			String sql = "select * from tblUser where username = ? and password = ?";
			
			pstat = conn.prepareStatement(sql);
			pstat.setString(1, dto.getUsername());
			pstat.setString(2, dto.getPassword());
			
			rs = pstat.executeQuery();
			
			if (rs.next()) {
				UserDTO result = new UserDTO();
				
				result.setUserId(rs.getLong("user_id"));
				result.setUsername(rs.getString("username"));
				result.setNickName(rs.getString("nickname"));
				result.setRealName(rs.getString("real_name"));
				return result;
			}	
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}

	/**
	 * 사용자 고유 번호(ID)를 기준으로 사용자의 상세 정보를 조회합니다.
	 * * @param seq 조회할 사용자의 고유 번호 (user_id)
	 * @return 해당 사용자의 상세 정보가 담긴 UserDTO. 사용자가 없을 경우 null.
	 */
	public UserDTO userInfo(String seq) {
		try {
			String sql = "select * from tblUser where user_id = ?";
			
			pstat = conn.prepareStatement(sql);
			pstat.setString(1, seq);
			
			rs = pstat.executeQuery();
			
			if (rs.next()) {
				UserDTO result = new UserDTO();
				
				result.setUserId(rs.getLong("user_id"));
				result.setUsername(rs.getString("username"));
				result.setNickName(rs.getString("nickname"));
				result.setRealName(rs.getString("real_name"));
				result.setEmail(rs.getString("email"));
				result.setAddress(rs.getString("address"));
				result.setGender(rs.getString("gender"));
				result.setHeight(rs.getString("height"));
				result.setWeight(rs.getString("weight"));
				result.setHealthGoals(rs.getString("health_goals"));
				result.setPhoneNumber(rs.getString("phone_number"));
				return result;
			}	
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}

	/**
	 * 사용자의 정보를 수정합니다.
	 * * @param dto 수정할 사용자 정보가 담긴 UserDTO 객체 (user_id 포함)
	 * @return DB에 적용된 행(row)의 수. 성공 시 1, 실패 시 0.
	 */
	public int userEdit(UserDTO dto) {
		try {

			String sql = "update tblUser set real_name = ?, phone_number = ?, username = ?, password = ?, email = ?, nickname = ?, address = ?, gender = ?, height = ?, weight = ?, health_goals =? where user_id = ?";

			pstat = conn.prepareStatement(sql);
			pstat.setString(1, dto.getRealName());
			pstat.setString(2, dto.getPhoneNumber());
			pstat.setString(3, dto.getUsername());
			pstat.setString(4, dto.getPassword());
			pstat.setString(5, dto.getEmail());
			pstat.setString(6, dto.getNickName());
			pstat.setString(7, dto.getAddress());
			pstat.setString(8, dto.getGender());
			pstat.setString(9, dto.getHeight());
			pstat.setString(10, dto.getWeight());
			pstat.setString(11, dto.getHealthGoals());
			pstat.setLong(12, dto.getUserId());

			return pstat.executeUpdate();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return 0;
	}

	/**
	 * 새로운 사용자를 등록(회원가입)합니다.
	 * user_id는 시퀀스(seqUser)를 통해 자동 생성되며, user_status_id는 1(활성)로 고정됩니다.
	 * * @param dto 회원가입할 사용자의 정보가 담긴 UserDTO 객체
	 * @return DB에 적용된 행(row)의 수. 성공 시 1, 실패 시 0.
	 */
	public int register(UserDTO dto) {
		try {

			String sql = "insert into tblUser (user_id, user_status_id, real_name, ssn, phone_number, username, password, email, nickname, address, regdate, deleted_at, gender, height, weight, health_goals) values (seqUser.nextVal, 1, ?, ?, ?, ?, ?, ?, ?, ?, sysdate, null, ?,  ?, ?, ?)";

			pstat = conn.prepareStatement(sql);
			pstat.setString(1, dto.getRealName());
			pstat.setString(2, dto.getSsn());
			pstat.setString(3, dto.getPhoneNumber());
			pstat.setString(4, dto.getUsername());
			pstat.setString(5, dto.getPassword());
			pstat.setString(6, dto.getEmail());
			pstat.setString(7, dto.getNickName());
			pstat.setString(8, dto.getAddress());
			pstat.setString(9, dto.getGender());
			pstat.setString(10, dto.getHeight());
			pstat.setString(11, dto.getWeight());
			pstat.setString(12, dto.getHealthGoals());
			return pstat.executeUpdate();

		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return 0;
	}


	/**
	 * 회원 탈퇴 전, 사용자의 ID와 비밀번호가 일치하는지 확인합니다.
	 * * @param dto 확인할 사용자의 user_id와 password가 담긴 UserDTO 객체
	 * @return 일치하는 사용자가 있으면 1, 없으면 0.
	 */
	public int  userDelCheck(UserDTO dto) {
		try {
			
			String sql = "select * from tblUser where user_id = ? and password = ?";
			
			pstat = conn.prepareStatement(sql);
			pstat.setLong(1, dto.getUserId());
			pstat.setString(2, dto.getPassword());
		
			
			rs = pstat.executeQuery();
			
			
			if (rs.next()) {
				return 1;
			}	
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return 0;
	}

	/**
	 * 사용자 정보를 논리적으로 삭제(탈퇴) 처리합니다.
	 * 실제 데이터를 삭제하는 대신 user_status_id를 2(탈퇴/비활성)로 업데이트합니다.
	 * * @param dto 탈퇴할 사용자의 user_id와 password가 담긴 UserDTO 객체
	 * @return DB에 적용된 행(row)의 수. 성공 시 1, 실패 시 0.
	 */
	public int userDel(UserDTO dto) {
		try {

			String sql = "update tblUser set user_status_id = 2 where user_id = ? and password = ?";

			pstat = conn.prepareStatement(sql);
			pstat.setLong(1, dto.getUserId());
			pstat.setString(2, dto.getPassword());

			return pstat.executeUpdate();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return 0;
		
	}

	/**
	 * 사용자의 현재 상태(활성, 정지, 탈퇴 등)를 확인합니다.
	 * * @param dto 확인할 사용자의 user_id가 담긴 UserDTO 객체
	 * @return 사용자의 user_status_id (ex: 1=활성, 2=탈퇴). 사용자가 없으면 0.
	 */
	public int loginCheck(UserDTO dto) {
		try {
			
			String sql = "select user_status_id from tblUser where user_id = ?";
			
			pstat = conn.prepareStatement(sql);
			pstat.setLong(1, dto.getUserId());
			
			rs = pstat.executeQuery();
			
			if (rs.next()) {
				return rs.getInt("user_status_id");
			}	
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return 0;
		
	}

	/**
	 * 회원가입 시 이메일 중복 여부를 확인합니다.
	 * * @param email 중복 확인할 이메일 주소
	 * @return 해당 이메일이 이미 존재하면 1, 존재하지 않으면 0.
	 */
	public int emailCheck(String email) {
		try {
			
			String sql = "select * from tblUser where email = ?";
			
			pstat = conn.prepareStatement(sql);
			pstat.setString(1, email);
			
			rs = pstat.executeQuery();
			
			if (rs.next()) {
				return 1;
			}	
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return 0;
	}

	/**
	 * 회원가입 시 아이디 중복 여부를 확인합니다.
	 * * @param id 중복 확인할 아이디 (username)
	 * @return 해당 아이디가 이미 존재하면 1, 존재하지 않으면 0.
	 */
	public int idCheck(String id) {
		try {
			
			String sql = "select * from tblUser where username = ?";
			
			pstat = conn.prepareStatement(sql);
			pstat.setString(1, id);
			
			rs = pstat.executeQuery();
			
			if (rs.next()) {
				return 1;
			}	
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return 0;
	}

	/**
	 * 아이디 찾기 기능. 이메일과 실명을 기준으로 사용자의 아이디(username)를 조회합니다.
	 * * @param email 사용자 이메일
	 * @param name 사용자 실명 (real_name)
	 * @return 일치하는 사용자의 username. 없으면 null.
	 */
	public String idSelect(String email, String name) {
		try {
			
			String sql = "select username from tblUser where email = ? and real_name = ?";
			
			pstat = conn.prepareStatement(sql);
			pstat.setString(1, email);
			pstat.setString(2, name);
			
			rs = pstat.executeQuery();
			
			if (rs.next()) {
				return rs.getString("username");
			}	
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}

	/**
	 * 비밀번호 찾기(재설정) 전, 이메일과 아이디가 일치하는 사용자가 있는지 확인합니다.
	 * * @param email 사용자 이메일
	 * @param id 사용자 아이디 (username)
	 * @return 일치하는 사용자가 있으면 해당 username, 없으면 null.
	 */
	public String PwSelect(String email, String id) {
		try {
			String sql = "select username from tblUser where email = ? and username = ?";
			
			pstat = conn.prepareStatement(sql);
			pstat.setString(1, email);
			pstat.setString(2, id);
			
			rs = pstat.executeQuery();
			
			if (rs.next()) {
				return rs.getString("username");
			}	
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}

	/**
	 * 비밀번호 재설정 기능.
	 * 이메일과 아이디가 일치하는 사용자의 비밀번호를 임시 비밀번호(validNumber)로 업데이트합니다.
	 * * @param email 사용자 이메일
	 * @param id 사용자 아이디 (username)
	 * @param validNumber 새로 설정할 임시 비밀번호
	 */
	public void PwUpdate(String email, String id, String validNumber) {
		try {

			String sql = "update tblUser set password = ? where TRIM(email) = ? and TRIM(username) = ?";

			pstat = conn.prepareStatement(sql);
			pstat.setString(1, validNumber);
			pstat.setString(2, email);
			pstat.setString(3, id);

			pstat.executeUpdate();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	// =======================================
	// ✅ [추가] 관리자 기능 (userDAO에서 병합)
	// =======================================

	/**
	 * [관리자] 모든 사용자 목록을 검색 조건과 상태에 따라 조회합니다.
	 * * @param searchType 검색 기준 (컬럼명: real_name 또는 email)
	 * @param keyword 검색어 (이름 또는 이메일에 포함될 문자열)
	 * @param status 사용자 상태 ID (tblUserStatus의 user_status_id. 예: 1=활성, 2=정지)
	 * @return 검색 조건에 맞는 사용자 목록 (List&lt;UserDTO&gt;)
	 */
	public List<UserDTO> getAllUsers(String searchType, String keyword, String status) {
	    List<UserDTO> list = new ArrayList<>();
	    PreparedStatement pstat = null;
	    ResultSet rs = null;

	    try {
	        StringBuilder sql = new StringBuilder(
	            "SELECT u.user_id, u.nickname, u.real_name, u.email, u.regdate, s.status " +
	            "FROM tblUser u INNER JOIN tblUserStatus s ON u.user_status_id = s.user_status_id " +
	            "WHERE 1=1 "
	        );

	        List<Object> params = new ArrayList<>();

	        // 🔍 검색 조건 추가
	        if (keyword != null && !keyword.trim().isEmpty()) {
	            if ("real_name".equals(searchType)) {
	                sql.append("AND u.real_name LIKE ? ");
	                params.add("%" + keyword + "%");
	            } else if ("email".equals(searchType)) {
	                sql.append("AND u.email LIKE ? ");
	                params.add("%" + keyword + "%");
	            }
	        }

	        // 🔍 상태 필터 추가
	        if (status != null && !status.isEmpty()) {
	            sql.append("AND u.user_status_id = ? ");
	            params.add(Integer.parseInt(status));
	        }

	        sql.append("ORDER BY u.regdate DESC");

	        pstat = conn.prepareStatement(sql.toString());
	        for (int i = 0; i < params.size(); i++) {
	            pstat.setObject(i + 1, params.get(i));
	        }

	        rs = pstat.executeQuery();

	        while (rs.next()) {
	            UserDTO dto = new UserDTO();
	            dto.setUserId(rs.getLong("user_id"));
	            dto.setNickName(rs.getString("nickname"));
	            dto.setRealName(rs.getString("real_name"));
	            dto.setEmail(rs.getString("email"));
	            dto.setRegdate(rs.getDate("regdate"));
	            dto.setStatus(rs.getString("status")); // UserStatus 테이블의 'status' 문자열
	            list.add(dto);
	        }

	    } catch (Exception e) {
	        e.printStackTrace();
	    } finally {
	        try {
	            if (rs != null) rs.close();
	            if (pstat != null) pstat.close();
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	    }
	    return list;
	}

	/**
	 * [관리자] 특정 사용자를 정지시킵니다.
	 * 이 메서드는 두 가지 작업을 수행합니다:
	 * 1. tblMemSuspended 테이블에 정지 기록을 추가합니다.
	 * 2. tblUser 테이블의 user_status_id를 2(정지 상태)로 업데이트합니다.
	 * * @param userId 정지시킬 사용자의 ID (user_id)
	 * @param adminId 정지 조치를 수행하는 관리자의 ID (admin_id)
	 * @param reason 정지 사유
	 * @param duration 정지 기간 (일 단위)
	 */
	public void suspendUser(int userId, int adminId, String reason, int duration) {
	    PreparedStatement pstat1 = null;
	    PreparedStatement pstat2 = null;

	    try {
	        // 1️⃣ 정지 기록 INSERT
	        String sql1 = "INSERT INTO tblMemSuspended (memsuspended_id, user_id, admin_id, suspended_reason, suspended_startdate, suspended_enddate) " +
	                      "VALUES (seq_mem_suspended.NEXTVAL, ?, ?, ?, SYSDATE, SYSDATE + ?)";
	        pstat1 = conn.prepareStatement(sql1);
	        pstat1.setInt(1, userId);
	        pstat1.setInt(2, adminId);
	        pstat1.setString(3, reason);
	        pstat1.setInt(4, duration);
	        pstat1.executeUpdate();

	        // 2️⃣ 회원 상태를 ‘정지(2)’로 변경
	        String sql2 = "UPDATE tblUser SET user_status_id = 2 WHERE user_id = ?";
	        pstat2 = conn.prepareStatement(sql2);
	        pstat2.setInt(1, userId);
	        pstat2.executeUpdate();

	    } catch (Exception e) {
	        e.printStackTrace();
	    } finally {
	        try {
	            if (pstat1 != null) pstat1.close();
	            if (pstat2 != null) pstat2.close();
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	    }
	}

}
