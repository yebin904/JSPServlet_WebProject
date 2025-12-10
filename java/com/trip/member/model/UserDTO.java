package com.trip.member.model;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 사용자 정보(Data Transfer Object) 클래스.
 * tblUser 테이블의 컬럼 데이터를 전송하는 데 사용됩니다.
 */
@Getter
@Setter
@ToString
public class UserDTO {
	
    /** 회원 고유 번호 (PK) */
	private long userId;          // 회원번호 (user_id)
    
    /** 회원 상태 번호 (FK, tblUserStatus 참조) */
    private int userStatusId;     // 회원상태번호 (user_status_id)
    
    /** 회원 실명 */
    private String realName;      // 회원이름 (real_name)
    
    /** 회원 주민등록번호 */
    private String ssn;           // 회원주민번호 (ssn)
    
    /** 회원 전화번호 */
    private String phoneNumber;   // 회원전화번호 (phone_number)
    
    /** 회원 로그인 아이디 */
    private String username;      // 회원아이디 (username)
    
    /** 회원 로그인 비밀번호 */
    private String password;      // 회원비밀번호 (password)
    
    /** 회원 이메일 */
    private String email;         // 회원이메일 (email)
    
    /** 회원 닉네임 */
    private String nickName;      // 회원닉네임 (nickname)
    
    /** 회원 주소 */
    private String address;       // 회원주소 (address)
    
    /** 회원 가입 날짜 */
    private Date regdate;         // 회원가입날짜 (regdate)
    
    /** 회원 탈퇴 날짜 (탈퇴 처리 시 기록) */
    private Date deletedAt;       // 회원탈퇴날짜 (deleted_at)
    
    /** 회원 성별 */
    private String gender;        // 회원성별 (gender)
    
    /** 회원 키 */
    private String height;        // 키 (height)
    
    /** 회원 몸무게 */
    private String weight;        // 몸무게 (weight)
    
    /** 건강 목표 */
    private String healthGoals;   // 건강 목표 (health_goals
    
    /** 회원 상태 (tblUserStatus의 status 값) - 관리자용 */
    private String status;
    
    // ----- 하위 호환성을 위한 필드 -----
    
    /** 호환성 필드 (username 대체) */
    private String id;
    
    /** 호환성 필드 (password 대체) */
    private String pw;
    
    /** 호환성 필드 (userId 대체) */
    private String seq;
    
    /** 호환성 필드 (realName 또는 nickName 대체) */
    private String name;

}
