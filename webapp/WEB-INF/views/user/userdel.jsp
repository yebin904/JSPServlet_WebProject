<%@page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>     
<!DOCTYPE html>
<html lang="ko">
<head>
	<meta charset="UTF-8">
	<%@ include file="/WEB-INF/views/inc/asset.jsp" %>
	<link rel="stylesheet" href="/trip/asset/css/userdelstyle.css">
</head>
<body>
	<!-- del.jsp -->
	<%@ include file="/WEB-INF/views/inc/header.jsp" %>
	
	<form method="POST" action="/trip/user/userdel.do">
	<div id="main">
		<h1>탈퇴하기</h1>
		
		<div>
        <label for="pw">비밀번호 입력</label>
        <input type="password" name="pw" id="pw" required>
    </div>
    <div>
        <label for="name">탈퇴사유</label>
        <textarea name="name" id="name" required></textarea>
    </div>
    
    <div class="button-group">
        <button type="button" class="back" onclick="location.href='/trip/user/userinfo.do';">돌아가기</button>
        <button type="submit" class="del primary">삭제하기</button>
    </div>
    <input type="hidden" name="seq" value="${seq}">
	</div>
	</form>
		
</body>
</html>