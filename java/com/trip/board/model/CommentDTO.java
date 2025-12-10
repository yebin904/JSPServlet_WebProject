package com.trip.board.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * (핫딜) 댓글 정보를 담는 DTO 클래스
 * @author jsg
 * @version 1.0
 * @since 2025.10.24
 */
@Getter
@Setter
@ToString
public class CommentDTO {
	/**
	 * 댓글 고유 ID (PK) (hotdeal_comment_id)
	 */
	private String seq;
	/**
	 * 댓글 내용
	 */
	private String content;
	/**
	 * 작성자 로그인 ID (username)
	 */
	private String id;
	/**
	 * 댓글 작성일
	 */
	private String regdate;
	/**
	 * 원본 게시물 ID (hotdeal_id)
	 */
	private String bseq;
	
	/**
	 * 작성자 닉네임 (조인 쿼리 결과)
	 */
	private String name;
}
