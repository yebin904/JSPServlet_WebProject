<%@page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>     
<!DOCTYPE html>
<html lang="ko">
<head>
	<meta charset="UTF-8">
	<%@ include file="/WEB-INF/views/inc/asset.jsp" %>
</head>
<body>
	<!-- del.jsp -->
	<%@ include file="/WEB-INF/views/inc/header.jsp" %>
	
	
	<div id="main">
		<h1>게시판 <small>삭제하기</small></h1>
		
		<form method="POST" action="/trip/board/del.do">

		<div>
			<button type="button" class="back" onclick="location.href='/trip/board/view.do?seq=${seq}';">돌아가기</button>
			<button type="submit" class="del primary">삭제하기</button>
		</div>
		<input type="hidden" name="seq" value="${seq}">
		</form>
	</div>
		
</body>
</html>























