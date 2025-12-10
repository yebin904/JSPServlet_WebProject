package com.trip.admin.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 통계 데이터를 담는 DTO 클래스
 * @author jsg
 * @version 1.0
 * @since 2025.10.24
 */
@Getter
@Setter
@ToString
public class statDTO {
    /**
     * 통계 카테고리 (예: 연도, 성별)
     */
    private String category;
    /**
     * 해당 카테고리의 개수
     */
    private int count;
}