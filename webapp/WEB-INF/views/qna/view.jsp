<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">

    <%-- 1. list.jsp와 동일한 공통 asset 파일을 include 합니다. --%>
    <jsp:include page="/WEB-INF/views/inc/asset.jsp" />
    
    <%-- 2. view 페이지에 필요한 qna.css를 이어서 추가합니다. --%>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/asset/css/qna.css">

    <script>const contextPath = '${pageContext.request.contextPath}';</script>

    <title>QnA 상세 보기</title> <%-- 페이지 제목을 추가하면 좋습니다. --%>
</head>
<body>

    <%-- 3. 헤더는 body 태그 안으로 이동시킵니다. --%>
    <jsp:include page="/WEB-INF/views/inc/header.jsp" />

    <%-- 이 아래부터 <main> 태그를 포함한 페이지 내용이 이어집니다. --%>
<main class="qna-view container">
	<div class="qna-view-box">
		<!-- 게시글 제목 -->
		<h2 class="qna-view-title">${dto.question_board_title}</h2>

		<!-- 게시글 정보 -->
		<div class="qna-view-meta">
			<span>작성자: ${dto.nickname} (ID: ${dto.user_id})</span> <span>작성일:
				${dto.question_board_regdate}</span> <span>조회수:
				${dto.question_board_view_count}</span> <span>좋아요:
				${dto.question_board_like_count}</span> <span>스크랩:
				${dto.question_board_scrap_count}</span>
			<c:choose>
				<c:when test="${dto.question_board_answer_status eq 'y'}">
					<span class="badge-answer answered">완료</span>
				</c:when>
				<c:otherwise>
					<span class="badge-answer unanswered">미답변</span>
				</c:otherwise>
			</c:choose>
		</div>

		<!-- 게시글 내용 -->
		<div class="qna-view-content">
			<div class="qna-view-body">${dto.question_board_content}</div>
		</div>

		<!-- Font Awesome (아이콘용) -->
		<link rel="stylesheet"
			href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">

		<!-- 하단 버튼 그룹 -->
		<div class="qna-bottom-actions">

			<!-- 왼쪽: 좋아요 / 스크랩 -->
			<div class="qna-interaction">
				<button id="btnLike" class="btn-like ${liked ? 'active' : ''}"
					data-seq="${dto.question_board_id}">
					<i class="fa-solid fa-heart"></i> 좋아요
					${dto.question_board_like_count}
				</button>
				<button id="btnScrap" class="btn-scrap ${scrapped ? 'active' : ''}"
					data-seq="${dto.question_board_id}">
					<i class="fa-solid fa-bookmark"></i> 스크랩
					${dto.question_board_scrap_count}
				</button>
			</div>


			<!-- 수정/삭제/목록 -->
			<div class="qna-view-btns">
				<c:if test="${isOwner || isAdmin}">
					<!-- 수정 버튼 -->
					<form action="${pageContext.request.contextPath}/qna/edit.do"
						method="get" style="display: inline-block;">
						<input type="hidden" name="seq" value="${dto.question_board_id}" />
						<button type="submit" class="btn btn-outline-secondary">수정</button>
					</form>

					<!-- 삭제 버튼 -->
					<form action="${pageContext.request.contextPath}/qna/delete.do"
						method="post" style="display: inline-block;">
						<input type="hidden" name="seq" value="${dto.question_board_id}" />
						<button type="submit" class="btn btn-outline-secondary"
							onclick="return confirm('게시글을 삭제하시겠습니까?');">삭제</button>
					</form>
				</c:if>

				<!-- 목록 버튼 -->
				<button type="button" class="btn btn-primary-list"
					onclick="location.href='${pageContext.request.contextPath}/qna/list.do'">목록으로</button>
			</div>
		</div>

		<!-- 댓글 영역 -->
		<div class="qna-comments">
			<h3>
				댓글 (<span class="comment-count">${fn:length(comments)}</span>)
			</h3>

			<c:forEach var="c" items="${comments}">
				<div class="comment-item" data-id="${c.question_answer_id}">

					<!-- 댓글 상단 정보 -->
					<div class="comment-meta">
						<strong>${c.nickname}</strong> (ID: ${c.user_id}) <span>${c.question_answer_regdate}</span>
					</div>

					<!-- 댓글 내용 -->
					<p class="comment-text">${c.question_answer_content}</p>

					<!-- 수정 입력창 (처음엔 숨김) -->
					<div class="comment-edit-box" style="display: none;">
						<textarea class="edit-textarea">${c.question_answer_content}</textarea>
						<div class="edit-btns">
							<button type="button" class="btn-save">저장</button>
							<button type="button" class="btn-cancel">취소</button>
						</div>
					</div>

					<!-- 수정/삭제/신고 버튼 -->
					<div class="comment-actions">
						<c:if test="${c.user_id eq sessionScope.userId || isAdmin}">
							<button type="button" class="btn-edit">수정</button>
							<form
								action="${pageContext.request.contextPath}/qna/comment/delete.do"
								method="post" style="display: inline-block;">
								<input type="hidden" name="commentId"
									value="${c.question_answer_id}">
								<button type="submit" class="btn-delete"
									onclick="return confirm('이 댓글을 삭제하시겠습니까?');">삭제</button>
							</form>
						</c:if>

						<c:if test="${c.user_id ne sessionScope.userId && !isAdmin}">
							<form
								action="${pageContext.request.contextPath}/qna/comment/report.do"
								method="post" style="display: inline-block;">
								<input type="hidden" name="commentId"
									value="${c.question_answer_id}" />
								<button type="submit" class="btn-comment-report"
									onclick="return confirm('이 댓글을 신고하시겠습니까?');">신고</button>
							</form>
						</c:if>
					</div>

				</div>
			</c:forEach>
		</div>



		<!-- 댓글 작성 폼 -->
		<c:if test="${not empty sessionScope.userId}">
			<form method="post"
				action="${pageContext.request.contextPath}/qna/comment/AddComment"
				class="qna-add-comment">
				<input type="hidden" name="boardId" value="${dto.question_board_id}" />
				<textarea name="content" placeholder="댓글을 입력하세요..." required></textarea>
				<button type="submit" class="btn btn-submit-comment">등록</button>
			</form>
		</c:if>
	</div>

	
	<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
	<script>
$(function() {

    // 수정 버튼 클릭 → 수정모드 전환
    $(".btn-edit").click(function() {
        const comment = $(this).closest(".comment-item");
        comment.find(".comment-text").hide();
        comment.find(".comment-edit-box").show();
    });

    // 취소 버튼 → 원래대로
    $(".btn-cancel").click(function() {
        const comment = $(this).closest(".comment-item");
        comment.find(".comment-edit-box").hide();
        comment.find(".comment-text").show();
    });

    // 저장 버튼 → AJAX로 수정요청 (예시)
    $(".btn-save").click(function() {
        const comment = $(this).closest(".comment-item");
        const id = comment.data("id");
        const newContent = comment.find(".edit-textarea").val();

        $.ajax({
            url: "comment_edit_ok.do",
            type: "POST",
            data: { comment_id: id, comment_content: newContent },
            success: function(result) {
                // 수정 성공 시 즉시 반영
                comment.find(".comment-text").text(newContent).show();
                comment.find(".comment-edit-box").hide();
            },
            error: function() {
                alert("수정 중 오류가 발생했습니다.");
            }
        });
    });

});
</script>


</main>

<jsp:include page="/WEB-INF/views/inc/member_footer.jsp" />