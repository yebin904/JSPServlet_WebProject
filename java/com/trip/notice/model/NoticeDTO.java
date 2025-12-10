package com.trip.notice.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 공지사항 정보를 담는 DTO(Data Transfer Object) 클래스.
 * tblNoticePost 테이블의 데이터를 전송하는 데 사용됩니다.
 * * @author jsg
 * @version 1.0
 * @since 2025.10.24
 */
@Getter
@Setter
@ToString
public class NoticeDTO {
	
	/** 공지사항 게시물 ID (PK) */
	private String notice_post_id;
	
	/** 작성한 관리자 ID (FK, tblAdmin 참조) */
	private String admin_id;
	
	/** 공지사항 제목 */
	private String notice_header;
	
	/** 공지사항 내용 */
	private String notice_content;
	
	/** 공지사항 조회수 */
	private String notice_view_count;
	
	/** 공지사항 등록 날짜 */
	private String notice_regdate;
	

	//DB에는 없지만, 화면 출력을 위해 필요한 멤버 변수
	
	/** 행 번호 (Rownum) - 페이징 처리를 위해 사용 */
	private String rnum; 
	
	/** 새 글 여부 ('y' 또는 null) - 목록에서 'N' 아이콘 표시를 위해 사용 */
	private String isnew;

}
