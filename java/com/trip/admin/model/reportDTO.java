package com.trip.admin.model;

import java.util.Date;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 신고 정보를 담는 DTO 클래스
 * @author jsg
 * @version 1.0
 * @since 2025.10.24
 */
@Getter
@Setter
@ToString
public class reportDTO {
    
    /**
     * 신고 ID
     */
    private int reportId;
    /**
     * 신고 대상 유형 (예: "findboard", "question", "review", "hotdeal")
     */
    private String reportTargetType;
    /**
     * 신고 대상 ID
     */
    private int reportTargetId;
    /**
     * 신고자 ID
     */
    private int userId;
    /**
     * 신고된 사용자 ID
     */
    private int reportedUserId;
    /**
     * 신고 사유 유형
     */
    private String reportReasonType;
    /**
     * 신고 처리 상태 (예: "PENDING", "PROCESSED")
     */
    private String reportStatus;
    /**
     * 신고 등록일
     */
    private Date reportRegdate;
    
    /**
     * 신고자 닉네임
     */
    private String reporterNickname;
    /**
     * 신고된 사용자 닉네임
     */
    private String reportedUserNickname;
    /**
     * 신고된 게시글 제목
     */
    private String postTitle;
}