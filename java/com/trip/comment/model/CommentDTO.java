package com.trip.comment.model;

import lombok.Data;

/**
 * 댓글 정보를 담는 DTO(Data Transfer Object) 클래스.
 * tblRoutePostComment 테이블의 데이터를 표현하며,
 * 목록 조회 시 사용자 닉네임(nickname)을 추가로 담습니다.
 *
 * @author jsg
 * @version 1.0
 * @since 2025.10.24
 */
@Data
public class CommentDTO {

    /** 댓글 고유 ID (PK) */
    private int routepostCommentId;
    
    /** 작성자 ID (FK: tblUser) */
    private int userId;
    
    /** 원본 경로게시글 ID (FK: tblRoutePost) */
    private int routepostId;
    
    /** 댓글 내용 */
    private String routepostContent;
    
    /** 작성일 (YYYY-MM-DD HH24:MI 형식) */
    private String routepostRegdate;
    
    /** 댓글 신고 횟수 */
    private int routepostCommentReportCount;
    
    /** 댓글 상태 (Y: 활성, N: 삭제) */
    private String commentStatus;
    
    /** 작성자 닉네임 (tblUser와 JOIN 시 사용) */
    private String nickname; // JOIN용

    
}
