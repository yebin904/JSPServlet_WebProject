<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<%@ include file="/WEB-INF/views/inc/asset.jsp" %>
<style>
    /* 게시판 공통 스타일 */
    .board-container {
        max-width: 1000px;
        margin: 40px auto;
        padding: 40px;
        background-color: #fff;
        border-radius: 20px;
        box-shadow: 0 8px 30px rgba(0,0,0,0.06);
    }
    .board-title {
        font-size: 2rem;
        font-weight: 700;
        margin-bottom: 30px;
        text-align: center;
        color: var(--primary-dark);
    }

    /* 글쓰기 폼 스타일 */
    .write-form .form-group {
        margin-bottom: 20px;
    }
    .write-form label {
        display: block;
        margin-bottom: 8px;
        font-weight: 500;
    }
    .write-form input[type="text"],
    .write-form textarea,
    .write-form input[type="file"] {
        width: 100%;
        padding: 12px;
        border: 1px solid var(--border);
        border-radius: 8px;
        box-sizing: border-box;
        font-size: 1rem;
        font-family: 'Noto Sans KR', sans-serif;
    }
    .write-form input[type="text"]:focus,
    .write-form textarea:focus,
    .write-form input[type="file"]:focus {
        outline: none;
        border-color: var(--primary);
        box-shadow: 0 0 0 3px rgba(108, 154, 139, 0.1);
    }
    .write-form textarea {
        height: 300px;
        resize: vertical;
    }
    .button-container {
        display: flex;
        justify-content: flex-end;
        gap: 10px;
        margin-top: 30px;
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
</style>
</head>
<body>
    <%@ include file="/WEB-INF/views/inc/header.jsp" %>

    <main>
        <div class="board-container">
            <h1 class="board-title">여행 후기 작성</h1>

            <form method="POST" action="${pageContext.request.contextPath}/reviewboard/write.do" class="write-form" enctype="multipart/form-data">
                <div class="form-group">
                    <label for="title">제목</label>
                    <input type="text" id="title" name="title" required placeholder="제목을 입력하세요">
                </div>
                <div class="form-group">
                    <label for="content">내용</label>
                    <textarea id="content" name="content" required placeholder="내용을 입력하세요"></textarea>
                </div>
                <div class="form-group">
                    <label for="images">사진 첨부</label>
                    <input type="file" id="images" name="images" multiple accept="image/*">
                </div>

                <div class="button-container">
                    <a href="${pageContext.request.contextPath}/reviewboard/list.do" class="btn btn-secondary">목록으로</a>
                    <button type="submit" class="btn btn-primary">작성 완료</button>
                </div>
            </form>
        </div>
    </main>

</body>
</html>

