<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<jsp:include page="/WEB-INF/views/inc/member_asset.jsp" /> 
<link rel="stylesheet" href="${pageContext.request.contextPath}/asset/css/main.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/asset/css/qna.css"> 
<jsp:include page="/WEB-INF/views/inc/header.jsp" />

<main class="qna-edit container">
    <div class="qna-edit-box">
        <h2>댓글 수정</h2>

        <form action="${pageContext.request.contextPath}/trip/qna/comment/EditComment" method="post" class="qna-edit-form">
            <input type="hidden" name="commentId" value="${comment.question_answer_id}" />
            <input type="hidden" name="boardId" value="${comment.question_board_id}" />

            <textarea name="content" required placeholder="댓글 내용을 입력하세요..." rows="5" class="form-control">${comment.question_answer_content}</textarea>

            <div class="qna-edit-actions" style="margin-top:10px;">
                <button type="submit" class="btn btn-primary">수정 완료</button>
                <button type="button" class="btn btn-secondary" 
                        onclick="location.href='${pageContext.request.contextPath}/trip/qna/view.do?seq=${comment.question_board_id}'">취소</button>
            </div>
        </form>
    </div>
</main>

<jsp:include page="/WEB-INF/views/inc/member_footer.jsp" />
