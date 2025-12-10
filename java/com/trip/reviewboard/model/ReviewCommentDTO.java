package com.trip.reviewboard.model;

import java.util.Date;

import lombok.Data;

/**
 * 리뷰 게시판 댓글 정보를 담는 DTO 클래스
 * @author jsg
 * @version 1.0
 * @since 2025.10.24
 */
@Data
public class ReviewCommentDTO {
    /**
     * 리뷰 댓글 식별자
     */
    private int review_comment_id;
    /**
     * 리뷰 게시물 식별자
     */
    private int review_post_id;
    /**
     * 사용자 식별자
     */
    private int user_id;
    /**
     * 리뷰 댓글 내용
     */
    private String review_comment_content;
    /**
     * 리뷰 댓글 등록일
     */
    private Date review_comment_regdate;
    /**
     * 리뷰 댓글 신고 수
     */
    private int review_comment_report_count;
    /**
     * 리뷰 댓글 상태
     */
    private String review_comment_status;
    
    /**
     * 사용자 닉네임
     */
    private String nickname;
}