package com.trip;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 검색 조건의 기준이 되는 클래스
 *
 */
@Getter
@Setter
@ToString
public class BaseSearchCriteria {

	private String keyword; // 검색어
    private int page = 1;      // 현재 페이지 번호 (기본값 1)
    private String sort;    // 정렬 기준
	
}
