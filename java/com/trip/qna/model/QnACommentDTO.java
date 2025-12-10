package com.trip.qna.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Q&A 댓글 정보를 담는 DTO 클래스
 * @author jsg
 * @version 1.0
 * @since 2025.10.24
 */
@Getter
@Setter
@ToString
public class QnACommentDTO {
	
	/**
	 * 질문 댓글 번호
	 */
	private String question_answer_id;
	/**
	 * 회원 번호 (FK)
	 */
	private String user_id;
	/**
	 * 회원 닉네임 (tblUser에서 조인)
	 */
	private String nickname;
	/**
	 * 질문 게시판 번호 (FK)
	 */
	private String question_board_id;
	/**
	 * 댓글 내용
	 */
	private String question_answer_content;
	/**
	 * 댓글 작성일
	 */
	private String question_answer_regdate;
	/**
	 * 댓글 누적 신고 횟수
	 */
	private String question_answer_report_count;
	/**
	 * 댓글 상태: 정상(y), 숨김처리(n)
	 */
	private String question_answer_status;

}
