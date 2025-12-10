<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>여행 후기 - ${dto.review_board_title}</title>
<%@ include file="/WEB-INF/views/inc/asset.jsp" %>
<style>
    .post-container {
        max-width: 1000px;
        margin: 40px auto;
        padding: 40px;
        background-color: #fff;
        border-radius: 20px;
        box-shadow: 0 8px 30px rgba(0,0,0,0.06);
    }
    .post-header {
        border-bottom: 1px solid var(--border);
        padding-bottom: 20px;
        margin-bottom: 30px;
    }
    .post-header h1 {
        font-size: 2.2rem;
        font-weight: 700;
        margin: 0 0 15px 0;
    }
    .post-meta {
        display: flex;
        align-items: center;
        font-size: 0.9rem;
        color: var(--text-light);
    }
    .post-meta .author {
        font-weight: 500;
        color: var(--text);
    }
    .post-meta span:not(:last-child)::after {
        content: '|';
        margin: 0 10px;
    }
    .post-content {
        min-height: 300px;
        line-height: 1.8;
        font-size: 1.05rem;
    }
    .button-container {
        display: flex;
        justify-content: space-between;
        margin-top: 40px;
        padding-top: 20px;
        border-top: 1px solid var(--border);
    }
    .btn {
        padding: 10px 25px;
        border: none;
        border-radius: 8px;
        font-size: 1rem;
        font-weight: 500;
        cursor: pointer;
        text-decoration: none;
        text-align: center;
        transition: all 0.2s;
    }
    .btn-primary {
        background-color: var(--primary);
        color: white;
    }
    .btn-primary:hover {
        background-color: var(--primary-dark);
    }
    .btn-secondary {
        background-color: #f1f3f5;
        color: var(--text);
    }
    .btn-secondary:hover {
        background-color: #e9ecef;
    }
    .btn-danger {
    	background-color: #ff6b6b;
    	color: white;
    }
    .action-buttons {
        display: flex;
        justify-content: center;
        gap: 20px;
        margin-top: 30px;
    }
    .action-buttons .btn {
        font-size: 1.1rem;
        padding: 12px 30px;
    }
    .btn.liked {
        background-color: var(--primary);
        color: white;
    }
    .comment-container {
        margin-top: 50px;
    }
    .comment-container h3 {
        font-size: 1.5rem;
        margin-bottom: 20px;
        border-bottom: 1px solid var(--border);
        padding-bottom: 10px;
    }
    .comment-form textarea {
        width: 100%;
        padding: 10px;
        border: 1px solid #ddd;
        border-radius: 8px;
        resize: vertical;
        min-height: 80px;
        margin-bottom: 10px;
    }
    .comment-form button {
        float: right;
    }
    .comment-list {
        margin-top: 30px;
    }
    .comment-item {
        border-bottom: 1px dashed #eee;
        padding: 15px 0;
    }
    .comment-item:last-child {
        border-bottom: none;
    }
    .comment-meta {
        font-size: 0.85rem;
        color: var(--text-light);
        margin-bottom: 5px;
    }
    .comment-meta .author {
        font-weight: 500;
        color: var(--text);
    }
    .comment-content {
        font-size: 0.95rem;
        line-height: 1.6;
    }
    .comment-actions {
        text-align: right;
        margin-top: 5px;
    }
    .comment-actions button {
        background: none;
        border: none;
        color: #dc3545;
        cursor: pointer;
        font-size: 0.8rem;
    }
</style>
</head>
<body>
    <%@ include file="/WEB-INF/views/inc/header.jsp" %>

    <main>
        <div class="post-container">
            <div class="post-header">
                <h1>${dto.review_board_title}</h1>
                <div class="post-meta">
                    <span class="author">${dto.nickname}</span>
                    <span><fmt:formatDate value="${dto.review_board_regdate}" pattern="yyyy-MM-dd HH:mm" /></span>
                    <span>조회수 ${dto.review_board_count}</span>
                </div>
            </div>

            <div class="post-content">
                <p style="white-space: pre-wrap;">${dto.review_board_content}</p>
            </div>

            <div class="action-buttons">
                <button id="like-btn" class="btn ${isLiked ? 'liked' : 'btn-secondary'}">좋아요</button>
                <button id="scrap-btn" class="btn ${isScrapped ? 'liked' : 'btn-secondary'}">스크랩</button>
            </div>

            <div class="button-container">
                <a href="${pageContext.request.contextPath}/reviewboard/list.do" class="btn btn-secondary">목록으로</a>
                <div>
                    <c:if test="${sessionScope.user.userId == dto.user_id}">
                        <a href="#" class="btn btn-secondary">수정</a>
                        <form action="/trip/reviewboard/delete.do" method="POST" style="display: inline;" onsubmit="return confirm('정말로 삭제하시겠습니까?');">
                            <input type="hidden" name="id" value="${dto.review_post_id}">
                            <button type="submit" class="btn btn-danger">삭제</button>
                        </form>
                    </c:if>
                </div>
            </div>
            
            <div class="comment-container">
                <h3>댓글</h3>
                <div class="comment-form">
                    <form id="comment-add-form">
                        <input type="hidden" name="postId" value="${dto.review_post_id}">
                        <textarea name="content" placeholder="댓글을 입력하세요." required></textarea>
                        <button type="submit" class="btn btn-primary">댓글 작성</button>
                        <div style="clear:both;"></div>
                    </form>
                </div>
                <div class="comment-list">
                    <c:if test="${empty comments}">
                        <p>아직 댓글이 없습니다.</p>
                    </c:if>
                    <c:forEach items="${comments}" var="comment">
                        <div class="comment-item">
                            <div class="comment-meta">
                                <span class="author">${comment.nickname}</span>
                                <span><fmt:formatDate value="${comment.review_comment_regdate}" pattern="yyyy-MM-dd HH:mm" /></span>
                            </div>
                            <div class="comment-content">${comment.review_comment_content}</div>
                            <c:if test="${sessionScope.user.userId == comment.user_id}">
                                <div class="comment-actions">
                                    <button class="btn-delete-comment" data-comment-id="${comment.review_comment_id}" data-post-id="${dto.review_post_id}">삭제</button>
                                </div>
                            </c:if>
                        </div>
                    </c:forEach>
                </div>
            </div>
        </div>
    </main>

    <script>
        const likeBtn = document.getElementById('like-btn');
        const scrapBtn = document.getElementById('scrap-btn');
        const postId = '${dto.review_post_id}';
        const userId = '${sessionScope.user.userId}';

        likeBtn.addEventListener('click', () => toggleAction('like'));
        scrapBtn.addEventListener('click', () => toggleAction('scrap'));

        function toggleAction(type) {
            if (!userId) {
                alert('로그인이 필요합니다.');
                return;
            }

            fetch(`/trip/reviewboard/${type}.do`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded',
                },
                body: `postId=${postId}`
            })
            .then(response => response.json())
            .then(data => {
                const btn = (type === 'like') ? likeBtn : scrapBtn;
                if (data.result) {
                    btn.classList.add('liked');
                    btn.classList.remove('btn-secondary');
                } else {
                    btn.classList.remove('liked');
                    btn.classList.add('btn-secondary');
                }
            })
            .catch(error => console.error('Error:', error));
        }

        // Comment Add Form Submission
        const commentAddForm = document.getElementById('comment-add-form');
        if (commentAddForm) {
            commentAddForm.addEventListener('submit', function(event) {
                event.preventDefault();
                if (!userId) {
                    alert('로그인이 필요합니다.');
                    return;
                }

                const formData = new FormData(this);
                const content = formData.get('content');

                fetch(`/trip/reviewboard/comment/add.do`, {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/x-www-form-urlencoded',
                    },
                    body: `postId=${postId}&content=${encodeURIComponent(content)}`
                })
                .then(response => response.json())
                .then(data => {
                    if (data.result > 0) {
                        alert('댓글이 작성되었습니다.');
                        location.reload(); // Reload to show new comment
                    } else {
                        alert('댓글 작성에 실패했습니다.');
                    }
                })
                .catch(error => console.error('Error:', error));
            });
        }

        // Comment Delete Buttons
        document.querySelectorAll('.btn-delete-comment').forEach(button => {
            button.addEventListener('click', function() {
                if (!confirm('정말로 댓글을 삭제하시겠습니까?')) {
                    return;
                }
                const commentId = this.dataset.commentId;
                const postId = this.dataset.postId;

                fetch(`/trip/reviewboard/comment/del.do`, {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/x-www-form-urlencoded',
                    },
                    body: `commentId=${commentId}&postId=${postId}`
                })
                .then(response => response.json())
                .then(data => {
                    if (data.result > 0) {
                        alert('댓글이 삭제되었습니다.');
                        location.reload(); // Reload to update comments
                    } else {
                        alert('댓글 삭제에 실패했습니다.');
                    }
                })
                .catch(error => console.error('Error:', error));
            });
        });
    </script>

</body>
</html>
