package com.trip.qna.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Q&A 게시물 정보를 담는 DTO 클래스
 * @author jsg
 * @version 1.0
 * @since 2025.10.24
 */
@Getter
@Setter
@ToString
public class QnADTO {
	
	/**
	 * 질문 게시판 번호
	 */
	private String question_board_id;
	/**
	 * 회원 번호
	 */
	private String user_id;
	/**
	 * 회원 닉네임 (JSP에서 사용)
	 */
	private String nickname;
    /**
     * 게시물 제목
     */
    private String question_board_title;    
    /**
     * 게시물 내용
     */
    private String question_board_content;
    /**
     * 조회수
     */
    private String question_board_view_count;
    /**
     * 좋아요 수
     */
    private String question_board_like_count;
    /**
     * 스크랩 수
     */
    private String question_board_scrap_count;
    /**
     * 댓글 수
     */
    private String question_board_comment_count;
    /**
     * 답변 상태 (y, n)
     */
    private String question_board_answer_status;
    /**
     * 게시글 상태 (y, n)
     */
    private String question_board_status;
    /**
     * 작성일
     */
    private String question_board_regdate;
    /**
     * 수정일
     */
    private String question_board_update;
    /**
     * 누적 신고 횟수
     */
    private String question_board_report_count;
    /**
     * 카테고리 번호
     */
    private String question_category_id;
    /**
     * 카테고리 이름 (JSP에서 표시용)
     */
    private String question_category_name;
    
    /**
     * 목록 페이징용 rownum
     */
    private String rownum;
    /**
     * 화면에 표시될 제목 (길이 제한 적용)
     */
    private String display_title;
    /**
     * 화면에 표시될 작성일 (길이 제한 적용)
     */
    private String display_regdate;
}
