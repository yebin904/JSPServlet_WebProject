package com.trip.admin.model;

import java.util.Date;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 통합 게시판 정보를 담는 DTO 클래스
 * @author jsg
 * @version 1.0
 * @since 2025.10.24
 */
@Getter
@Setter
@ToString
public class integratedBoardDTO {
    /**
     * 게시물 일련번호
     */
    private int seq;
    /**
     * 게시판 종류
     */
    private String boardType;
    /**
     * 게시물 제목
     */
    private String title;
    /**
     * 작성자 닉네임
     */
    private String nickname;
    /**
     * 등록일
     */
    private Date regdate;
    /**
     * 조회수
     */
    private int viewCount;
    
    /**
     * 대상 타입 (예: 리뷰, Q&A 등)
     */
    private String targetType; 
    /**
     * 댓글 수
     */
    private int commentCount;
    /**
     * 좋아요 수
     */
    private int likeCount;


}