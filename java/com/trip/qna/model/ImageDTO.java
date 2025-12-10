package com.trip.qna.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Q&A 이미지 정보를 담는 DTO 클래스
 * @author jsg
 * @version 1.0
 * @since 2025.10.24
 */
@Getter
@Setter
@ToString
public class ImageDTO {
	
	/**
	 * Q&A 이미지 ID
	 */
	private String question_image_id;
	/**
	 * Q&A 게시물 ID
	 */
	private String question_board_id;
	/**
	 * Q&A 이미지 URL
	 */
	private String question_image_image_url;
	/**
	 * Q&A 이미지 순서
	 */
	private String question_image_image_sequence;
	

}
