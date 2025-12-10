package com.trip.admin.model;

import java.util.Date;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 정지된 사용자 정보를 담는 DTO 클래스
 * @author jsg
 * @version 1.0
 * @since 2025.10.24
 */
@Getter
@Setter
@ToString
public class suspendedUserDTO {
    /**
     * 사용자 ID
     */
    private int userId;
    /**
     * 회원 정지 ID
     */
    private int memsuspendedId;
    /**
     * 사용자 닉네임
     */
    private String nickname;
    /**
     * 정지 사유
     */
    private String suspendedReason;
    /**
     * 정지 시작일
     */
    private Date suspendedStartDate;
    /**
     * 정지 종료일
     */
    private Date suspendedEndDate;
}