<%@page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="ko">
<head>
	<meta charset="UTF-8">
	<%@ include file="/WEB-INF/views/inc/asset.jsp" %>	
	
	<link rel="stylesheet" href="/trip/asset/css/loginstyle.css">
	
</head>
<body>
	
	<%@ include file="/WEB-INF/views/inc/header.jsp" %>	
	
	<div class="login-container">
		<h1>회원 <small>로그인</small></h1>
		
		<form method="POST" action="/trip/user/login.do">
			<div class="form-group">
				<label for="id">아이디</label>
				<input type="text" name="id" id="id" required>
			</div>
			<div class="form-group">
				<label for="pw">비밀번호</label>
				<input type="password" name="pw" id="pw" required>
			</div>
			<div class="button-group">
				<button type="submit" class="in primary">로그인</button>
								<button type="button" class="back" onclick="location.href='/trip/main.do';">돌아가기</button>
			</div>
		</form>
		
		<div class="find-links">
			<button type="button" onclick="location.href='/trip/user/idsearch.do';">아이디 찾기</button>
			<button type="button" onclick="location.href='/trip/user/pwsearch.do';">비밀번호 찾기</button>
		</div>
	</div>
	
	

	
</body>
</html>