package com.trip.board.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 좋아요 정보를 담는 DTO 클래스
 * (핫딜 게시판 좋아요/스크랩 기능에서 사용)
 * @author jsg
 * @version 1.0
 * @since 2025.10.24
 */
@Getter
@Setter
@ToString
public class LikeDTO {
	/**
	 * 작업을 수행하는 사용자 ID (user_id)
	 */
	private String seq;
	/**
	 * 대상 게시물 ID (hotdeal_id)
	 */
	private String bseq;

}
