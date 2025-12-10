package com.trip.board.find.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 동행찾기 게시판 댓글 정보를 담는 DTO 클래스
 * @author jsg
 * @version 1.0
 * @since 2025.10.24
 */
@Getter
@Setter
@ToString
public class findcommentDTO {

    // tblFindComment 테이블 컬럼
    /**
     * 댓글 ID
     */
    private int find_comment_id;
    /**
     * 게시물 ID
     */
    private int find_board_id;
    /**
     * 사용자 ID
     */
    private int user_id;
    /**
     * 댓글 내용
     */
    private String find_comment_content;
    /**
     * 댓글 등록일
     */
    private String find_comment_regdate;
    
    // 화면 표시용 추가 데이터
    /**
     * 댓글 작성자 닉네임
     */
    private String nickname; // 댓글 작성자 닉네임
}
