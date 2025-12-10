package com.trip.reviewboard.model;

import java.util.Date;
import lombok.Data;

/**
 * 리뷰 게시판 정보를 담는 DTO 클래스
 * @author jsg
 * @version 1.0
 * @since 2025.10.24
 */
@Data
public class ReviewBoardDTO {
    /**
     * 리뷰 게시물 식별자
     */
    private int review_post_id;
    /**
     * 사용자 식별자
     */
    private int user_id;
    /**
     * 리뷰 게시판 제목
     */
    private String review_board_title;
    /**
     * 리뷰 게시판 내용
     */
    private String review_board_content;
    /**
     * 리뷰 게시판 조회수
     */
    private int review_board_count;
    /**
     * 리뷰 게시판 스크랩 수
     */
    private int review_board_scrap_count;
    /**
     * 리뷰 게시판 신고 상태
     */
    private String review_board_report_status;
    /**
     * 리뷰 게시판 신고 수
     */
    private int review_board_report_count;
    /**
     * 리뷰 게시판 등록일
     */
    private Date review_board_regdate;
    /**
     * 리뷰 게시판 수정일
     */
    private Date review_board_update;

    /**
     * 사용자 닉네임
     */
    private String nickname; 
}