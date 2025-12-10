package com.trip.board.model;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 게시물 정보를 담는 DTO 클래스
 * (주로 핫딜 게시판(tblHotDealPost) 및 마이페이지 통합 목록용으로 사용)
 * @author jsg
 * @version 1.0
 * @since 2025.10.24
 */
@Getter
@Setter
@ToString
public class BoardDTO {
	
	/**
	 * 게시물 고유 ID (hotdeal_id 또는 마이페이지 뷰의 bseq)
	 */
	private String seq;
	/**
	 * 게시물 제목
	 */
	private String subject;
	/**
	 * 게시물 내용
	 */
	private String content;
	/**
	 * 작성자 ID (user_id)
	 */
	private String id;
	/**
	 * 게시물 작성일
	 */
	private String regdate;
	/**
	 * 조회수
	 */
	private String readcount;
	/**
	 * 카테고리명 (e.g., "숙박", "교통")
	 */
	private String category;
	/**
	 * 핫딜 상태 (e.g., "진행중", "종료")
	 */
	private String status;
	/**
	 * 핫딜 상품명
	 */
	private String itemName;
	/**
	 * 핫딜 상품 가격
	 */
	private String price;
	/**
	 * 핫딜 상품 구매 URL
	 */
	private String url;
	/**
	 * 사용자 ID (신고 등 특정 기능에서 사용)
	 */
	private String useq;
	/**
	 * 게시판명 (마이페이지 통합 뷰용)
	 */
	private String boradTitle;
	/**
	 * 게시판 코드 (마이페이지 통합 뷰용)
	 */
	private String boradCode;
	/**
	 * 댓글 내용 (마이페이지 '내 댓글' 뷰용)
	 */
	private String commentcontent;
	
	/**
	 * 작성자 닉네임 (조인 쿼리 결과)
	 */
	private String name;			//작성자
	//private double isnew; 			//최신글?
	/**
	 * 댓글 수 (조인 쿼리 결과)
	 */
	private String commentCount;	//댓글 수
	/**
	 * 대표 이미지 URL
	 */
	private String img; 			//이미지
	
	/*
	 * private List<String> hashtag; //해시태그 private String secret; //비밀글 private
	 * String notice; //공지글
	 * * private String scrapbook; //즐겨찾기
	 */
	
}
