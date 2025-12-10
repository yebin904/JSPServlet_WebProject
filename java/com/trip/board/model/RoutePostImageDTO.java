package com.trip.board.model;

import lombok.Data;

/**
 * 경로 게시물 이미지 정보를 담는 DTO 클래스
 * @author jsg
 * @version 1.0
 * @since 2025.10.24
 */
@Data
public class RoutePostImageDTO {

    /**
     * 경로 게시물 이미지 고유 ID (PK)
     */
    private int routepostImageId;
    /**
     * 이미지가 속한 경로 게시물 ID (FK)
     */
    private int routepostId;
    /**
     * 게시물 내 이미지 순서
     */
    private int routepostImageSeq;
    /**
     * 이미지 파일 경로 또는 URL
     */
    private String routepostImageUrl;

}
