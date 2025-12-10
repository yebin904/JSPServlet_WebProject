<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>글 수정</title>
<%@ include file="/WEB-INF/views/inc/asset.jsp" %>
</head>
<body>
    <%@ include file="/WEB-INF/views/inc/header.jsp" %>

    <main class="container">
        <div class="notice-title"><h2>동행 찾기 - 글 수정</h2></div>
        
        <%-- 이 부분은 notice.css에 스타일이 없어 카드 디자인이 적용되지 않을 수 있습니다 --%>
        <div class="card-container">
            <form method="POST" action="/trip/findboard/edit.do">
                <input type="hidden" name="seq" value="${dto.find_board_id}">
                <div class="form-group">
                    <label for="title">제목</label>
                    <input type="text" id="title" name="title" required value="${dto.find_board_title}" class="form-control">
                </div>
                <div class="form-group">
                    <label for="content">내용</label>
                    <textarea id="content" name="content" required class="form-control" style="height: 300px;">${dto.find_board_content}</textarea>
                </div>
                <div class="btn-container" style="text-align: right; margin-top: 20px;">
                    <button type="button" class="btn" onclick="history.back();">취소</button>
                    <button type="submit" class="btn btn-primary">수정하기</button>
                </div>
            </form>
        </div>
    </main>
</body>
</html>