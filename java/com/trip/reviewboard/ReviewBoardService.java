package com.trip.reviewboard;

import java.util.List;
import java.util.Map;

import com.trip.reviewboard.model.ReviewBoardDAO;
import com.trip.reviewboard.model.ReviewBoardDTO;
import com.trip.reviewboard.model.ReviewCommentDAO;
import com.trip.reviewboard.model.ReviewCommentDTO;

/**
 * 리뷰 게시판 관련 비즈니스 로직을 처리하는 서비스 클래스
 * @author jsg
 * @version 1.0
 * @since 2025.10.24
 */
public class ReviewBoardService {

    private ReviewBoardDAO dao;

    /**
     * ReviewBoardService 객체를 생성하고 ReviewBoardDAO를 초기화합니다.
     */
    public ReviewBoardService() {
        this.dao = new ReviewBoardDAO();
    }

    /**
     * 페이징 및 검색 조건을 적용하여 게시물 목록을 조회합니다.
     * @param map 검색 조건과 페이징 정보를 담은 Map
     * @return 게시물 목록
     */
    public List<ReviewBoardDTO> list(Map<String, String> map) {
        return dao.list(map);
    }

    /**
     * 검색 조건에 맞는 총 게시물 수를 조회합니다.
     * @param map 검색 조건을 담은 Map
     * @return 총 게시물 수
     */
    public int getTotalCount(Map<String, String> map) {
        return dao.getTotalCount(map);
    }

    /**
     * 새로운 게시물을 추가합니다.
     * @param dto 추가할 게시물 정보를 담은 DTO
     * @return 실행 결과 (1: 성공, 0: 실패)
     */
    public int addPost(ReviewBoardDTO dto) {
        return dao.addPost(dto);
    }

    /**
     * 특정 게시물을 조회하고 조회수를 1 증가시킵니다.
     * @param review_post_id 조회할 게시물 ID
     * @return 게시물 정보를 담은 DTO
     */
    public ReviewBoardDTO getPost(int review_post_id) {
        dao.updateViewCount(review_post_id);
        return dao.getPost(review_post_id);
    }

    /**
     * 특정 게시물을 삭제합니다.
     * @param review_post_id 삭제할 게시물 ID
     * @return 실행 결과 (1: 성공, 0: 실패)
     */
    public int deletePost(String review_post_id) {
        return dao.del(review_post_id);
    }

    /**
     * 게시물에 대한 좋아요 상태를 추가하거나 삭제합니다.
     * @param postId 게시물 ID
     * @param userId 사용자 ID
     * @return 좋아요 추가 시 true, 삭제 시 false
     */
    public boolean toggleLike(String postId, String userId) {
        return dao.toggleLike(postId, userId);
    }

    /**
     * 게시물에 대한 스크랩 상태를 추가하거나 삭제합니다.
     * @param postId 게시물 ID
     * @param userId 사용자 ID
     * @return 스크랩 추가 시 true, 삭제 시 false
     */
    public boolean toggleScrap(String postId, String userId) {
        return dao.toggleScrap(postId, userId);
    }
    
    /**
     * 사용자가 특정 게시물에 좋아요를 눌렀는지 확인합니다.
     * @param postId 게시물 ID
     * @param userId 사용자 ID
     * @return 좋아요를 눌렀으면 true, 아니면 false
     */
    public boolean isLiked(String postId, String userId) {
    	return dao.isLiked(postId, userId);
    }
    
    /**
     * 사용자가 특정 게시물을 스크랩했는지 확인합니다.
     * @param postId 게시물 ID
     * @param userId 사용자 ID
     * @return 스크랩했으면 true, 아니면 false
     */
    public boolean isScrapped(String postId, String userId) {
    	return dao.isScrapped(postId, userId);
    }
    
    /**
     * 특정 게시물의 댓글 목록을 조회합니다.
     * @param postId 게시물 ID
     * @return 댓글 목록
     */
    public List<ReviewCommentDTO> listComment(String postId) {
        ReviewCommentDAO commentDAO = new ReviewCommentDAO();
        List<ReviewCommentDTO> list = commentDAO.list(postId);
        commentDAO.close();
        return list;
    }

    /**
     * 새로운 댓글을 추가합니다.
     * @param dto 추가할 댓글 정보를 담은 DTO
     * @return 실행 결과 (1: 성공, 0: 실패)
     */
    public int addComment(ReviewCommentDTO dto) {
        ReviewCommentDAO commentDAO = new ReviewCommentDAO();
        int result = commentDAO.add(dto);
        commentDAO.close();
        return result;
    }

    /**
     * 특정 댓글을 삭제합니다.
     * @param commentId 삭제할 댓글 ID
     * @param postId 게시물 ID
     * @return 실행 결과 (1: 성공, 0: 실패)
     */
    public int deleteComment(String commentId, String postId) {
        ReviewCommentDAO commentDAO = new ReviewCommentDAO();
        int result = commentDAO.delete(commentId, postId);
        commentDAO.close();
        return result;
    }
    
    /**
     * DAO 리소스를 닫습니다.
     */
    public void close() {
        dao.close();
    }
}