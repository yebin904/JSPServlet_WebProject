package com.trip.board.find.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 동행찾기 게시판 게시물 정보를 담는 DTO 클래스
 * @author jsg
 * @version 1.0
 * @since 2025.10.24
 */
@Getter
@Setter
@ToString
public class findboardDTO {

    // tblFindBoard 테이블 컬럼
    /**
     * 게시물 ID
     */
    private int find_board_id;
    /**
     * 사용자 ID
     */
    private int user_id;
    /**
     * 게시물 제목
     */
    private String find_board_title;
    /**
     * 게시물 내용
     */
    private String find_board_content;
    /**
     * 게시물 조회수
     */
    private int find_board_view_count;
    /**
     * 게시물 신고 수
     */
    private int find_board_report_count;
    /**
     * 게시물 신고 상태
     */
    private String find_board_report_status;
    /**
     * 게시물 등록일
     */
    private String find_board_regdate;
    /**
     * 게시물 수정일
     */
    private String find_board_update;

    // 화면 표시용 추가 데이터
    /**
     * 작성자 닉네임
     */
    private String nickname;
    /**
     * 댓글 수
     */
    private int commentCount;
    /**
     * 총 추천수
     */
    private int likeCount;
    /**
     * 현재 로그인한 사용자의 추천 여부
     */
    private boolean liked;
    
    /**
     * 총 스크랩 수
     */
    private int scrapCount;
    /**
     * 현재 로그인한 사용자의 스크랩 여부
     */
    private boolean scrapped;
    /**
     * 행 번호
     */
    private int rownum;
}
