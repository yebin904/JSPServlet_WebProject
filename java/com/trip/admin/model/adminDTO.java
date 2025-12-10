package com.trip.admin.model;

import java.util.Date;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 관리자 정보를 담는 DTO 클래스
 * @author jsg
 * @version 1.0
 * @since 2025.10.24
 */
@Getter
@Setter
@ToString
public class adminDTO {
    /**
     * 관리자 ID
     */
    private int adminId;
    /**
     * 관리자 실명
     */
    private String adminRealName;
    /**
     * 관리자 주민등록번호
     */
    private String adminSsn;
    /**
     * 관리자 전화번호
     */
    private String adminPhoneNumber;
    /**
     * 관리자 계정명 (로그인 ID)
     */
    private String adminName;
    /**
     * 관리자 비밀번호
     */
    private String adminPassword;
    /**
     * 관리자 이메일 주소
     */
    private String adminEmail;
    /**
     * 관리자 주소
     */
    private String adminAddress;
    /**
     * 관리자 등록일
     */
    private Date adminRegdate;
}
