<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>동행 찾기 - ${dto.find_board_title}</title>
<%@ include file="/WEB-INF/views/inc/asset.jsp" %>

<%-- 이 파일은 view.jsp 전용 스타일이므로 그대로 둬야 합니다. --%>
<link rel="stylesheet" href="${pageContext.request.contextPath}/asset/css/findboard.css">

</head>
<body>
    <%@ include file="/WEB-INF/views/inc/header.jsp" %>

    <main class="container">
        <%-- ▼▼▼ [수정] 클래스 이름 변경 ▼▼▼ --%>
        <div class="notice-title">
            <h2>동행 찾기</h2>
        </div>

        <%-- ▼▼▼ [핵심 수정] card-container를 notice.css에 있는 notice-table로 변경 ▼▼▼ --%>
        <div class="notice-table" style="padding: 30px;">
            <div class="post-header">
                <h1>${dto.find_board_title}</h1>
                <div class="post-meta">
                    <span><strong>글쓴이:</strong> ${dto.nickname}</span>
                    <span><strong>작성일:</strong> ${dto.find_board_regdate} | <strong>조회수:</strong> ${dto.find_board_view_count}</span>
                </div>
            </div>
            <div class="post-content">
                <pre>${dto.find_board_content}</pre>
            </div>
          
            <div class="post-actions">
                <button type="button" class="btn-like ${dto.liked ? 'liked' : ''}" onclick="location.href='/trip/findboard/like.do?seq=${dto.find_board_id}';">
                    <span class="icon">👍</span>
                    <span class="text">추천</span>
                    <span class="count">${dto.likeCount}</span>
                </button>
                
                <c:if test="${not empty sessionScope.userId}">
                    <button type="button" class="btn-like btn-scrap ${dto.scrapped ? 'scrapped' : ''}" onclick="location.href='/trip/findboard/scrap.do?seq=${dto.find_board_id}';">
                        <span class="icon">🔖</span>
                        <span class="text">스크랩</span>
                        <span class="count">${dto.scrapCount}</span>
                    </button>
                </c:if>
                <c:if test="${empty sessionScope.userId}">
                    <span class="btn-like btn-scrap" style="cursor:default; opacity:0.7;">
                        <span class="icon">🔖</span> 스크랩: ${dto.scrapCount}
                    </span>
                </c:if>
            </div>
            
            <%-- ▼▼▼ [수정] 클래스 이름 변경 ▼▼▼ --%>
            <div class="table-options" style="margin-top: 20px;">
                <button type="button" class="btn" onclick="location.href='/trip/findboard/list.do';">목록</button>
                
                <c:if test="${sessionScope.userId != dto.user_id && not empty sessionScope.userId}">
                    <button type="button" class="btn danger" onclick="window.open('/trip/findboard/report.do?boardSeq=${dto.find_board_id}&reportedUserId=${dto.user_id}', 'reportPopup', 'width=400,height=350');">
                        신고하기
                    </button>
                </c:if>

                <c:if test="${sessionScope.userId == dto.user_id}">
                    <button type="button" class="btn" onclick="location.href='/trip/findboard/edit.do?seq=${dto.find_board_id}';">수정</button>
                    <button type="button" class="btn danger" onclick="if(confirm('정말 삭제하시겠습니까?')) { location.href='/trip/findboard/delete.do?seq=${dto.find_board_id}'; }">삭제</button>
                </c:if>
            </div>
        </div>
        
  <div class="comment-list">
    <c:if test="${empty commentList}"><div class="comment-item" style="justify-content: center;">등록된 댓글이 없습니다.</div></c:if>

    <c:forEach items="${commentList}" var="comment">
        <div class="comment-item" id="comment-${comment.find_comment_id}">
            <div class="comment-profile-icon">👤</div>
            <div class="comment-body">
                <div class="comment-author">
                    <strong>${comment.nickname}</strong>
                    <span>${comment.find_comment_regdate}</span>
                </div>

                <%-- 1. 댓글 내용 표시부 --%>
                <div class="comment-content-view">
                    <div class="comment-content">${comment.find_comment_content}</div>
                    
                    <c:if test="${sessionScope.userId == comment.user_id}">
                        <div class="comment-actions">
                            <%-- ▼▼▼ [수정] 수정 버튼은 JavaScript 함수만 호출 (페이지 이동 X) ▼▼▼ --%>
                            <a href="javascript:void(0);" onclick="showEditForm(${comment.find_comment_id})">수정</a>
                            
                            <%-- ▼▼▼ [수정] 삭제 링크의 href가 "deletecomment.do"인지 확인 ▼▼▼ --%>
                            <a href="/trip/findboard/deletecomment.do?commentId=${comment.find_comment_id}&boardSeq=${dto.find_board_id}"
                               onclick="return confirm('정말 삭제하시겠습니까?');">삭제</a>
                        </div>
                    </c:if>
                </div>

                <%-- 2. 댓글 수정 폼 --%>
                <div class="comment-content-edit" style="display: none;">
                    <%-- ▼▼▼ [수정] 수정 폼의 action이 "editcomment.do"인지 확인 ▼▼▼ --%>
                    <form method="POST" action="/trip/findboard/editcomment.do">
                        <input type="hidden" name="commentId" value="${comment.find_comment_id}">
                        <input type="hidden" name="boardSeq" value="${dto.find_board_id}">
                        <textarea name="content" class="edit-textarea">${comment.find_comment_content}</textarea>
                        <div class="edit-actions">
                            <button type="button" class="btn-cancel" onclick="hideEditForm(${comment.find_comment_id})">취소</button>
                            <button type="submit" class="btn-submit">수정 완료</button>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </c:forEach>
</div>

            <c:if test="${not empty sessionScope.userId}">
                <div class="comment-write">
                    <form method="POST" action="/trip/findboard/addcomment.do">
                        <input type="hidden" name="boardSeq" value="${dto.find_board_id}">
                        <textarea name="content" placeholder="따뜻한 댓글을 남겨주세요." required></textarea>
                        <div>
                            <button type="submit" class="btn primary">등록</button>
                        </div>
                    </form>
                </div>
            </c:if>
    </main>
    <script>
    function showEditForm(commentId) {
        document.querySelectorAll('.comment-content-edit').forEach(form => form.style.display = 'none');
        document.querySelectorAll('.comment-content-view').forEach(view => view.style.display = 'block');
        
        const commentDiv = document.querySelector('#comment-' + commentId);
        commentDiv.querySelector('.comment-content-view').style.display = 'none';
        commentDiv.querySelector('.comment-content-edit').style.display = 'block';
    }

    function hideEditForm(commentId) {
        const commentDiv = document.querySelector('#comment-' + commentId);
        commentDiv.querySelector('.comment-content-view').style.display = 'block';
        commentDiv.querySelector('.comment-content-edit').style.display = 'none';
    }
</script>
</body>
</html>