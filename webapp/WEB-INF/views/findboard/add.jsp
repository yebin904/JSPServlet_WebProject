<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>글쓰기</title>
<%@ include file="/WEB-INF/views/inc/asset.jsp" %>
</head>
<body>
    <%@ include file="/WEB-INF/views/inc/header.jsp" %>

    <main class="container">
        <div class="notice-title"><h2>동행 찾기 - 글쓰기</h2></div>
        
        <%-- 이 부분은 notice.css에 스타일이 없어 카드 디자인이 적용되지 않을 수 있습니다 --%>
        <div class="card-container">
            <form method="POST" action="/trip/findboard/add.do">
                <div class="form-group">
                    <label for="title">제목</label>
                    <input type="text" id="title" name="title" required placeholder="제목을 입력하세요" class="form-control">
                </div>
                <div class="form-group">
                    <label for="content">내용</label>
                    <textarea id="content" name="content" required placeholder="내용을 입력하세요" class="form-control" style="height: 300px;"></textarea>
                </div>
                <div class="btn-container" style="text-align: right; margin-top: 20px;">
                    <button type="button" class="btn" onclick="location.href='/trip/findboard/list.do';">목록</button>
                    <button type="submit" class="btn btn-primary">등록하기</button>
                </div>
            </form>
        </div>
    </main>
</body>
</html>