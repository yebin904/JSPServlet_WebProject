package com.trip.board.model;

import java.util.List;

import lombok.Data;

/**
 * 경로 게시물 정보를 담는 DTO 클래스
 * @author jsg
 * @version 1.0
 * @since 2025.10.24
 */
@Data
public class RoutePostDTO {
    
    /**
     * 경로 게시물 고유 ID (PK)
     */
    private int routepostId;
    /**
     * 작성자 ID (FK)
     */
    private int userId;
    /**
     * 게시물 제목
     */
    private String routepostTitle;
    /**
     * 게시물 내용
     */
    private String routepostContent;
    /**
     * 만족도 (별점 등)
     */
    private double routepostSatisfaction;
    /**
     * 게시물 상태 (e.g., "published", "draft")
     */
    private String routepostStatus;
    /**
     * 조회수
     */
    private int routepostViewCount;
    /**
     * 신고 횟수
     */
    private int routepostReportCount;
    /**
     * 좋아요 수 (조인 쿼리 결과)
     */
    private int LikeCount;
    /**
     * 작성자 닉네임 (조인 쿼리 결과)
     */
    private String nickname;
    /**
     * 게시물 등록일
     */
    private String routepostRegdate;
    
    /**
     * 게시물에 첨부된 이미지 목록
     */
    private List<RoutePostImageDTO> images;

    
    
   
}
