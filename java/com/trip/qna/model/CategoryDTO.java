package com.trip.qna.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Q&A 카테고리 정보를 담는 DTO 클래스
 * @author jsg
 * @version 1.0
 * @since 2025.10.24
 */
@Getter
@Setter
@ToString
public class CategoryDTO {

	/**
	 * Q&A 카테고리 ID
	 */
	private String question_category_id;
	/**
	 * Q&A 카테고리 이름
	 */
	private String question_category_name;
	
}
