<%@page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="ko">
<head>
	<meta charset="UTF-8">
	<%@ include file="/WEB-INF/views/inc/asset.jsp" %>
	<link rel="stylesheet" href="/trip/asset/css/userinfostyle.css">
	
	</style>
</head>
<body>
	<%@ include file="/WEB-INF/views/inc/header.jsp" %>
	<div id="main">
		<h1>회원정보</h1>
		
		<table class="vertical">
			<tr>
				<th>아이디</th>
				<td>${dto.id}</td>
				
			</tr>
				<tr>
				<th>이름</th>
				<td>${dto.name}</td>
			</tr>
				<tr>
				<th>전화번호</th>
				<td>${dto.phoneNumber}</td>
			</tr>
				<tr>
				<th>닉네임</th>
				<td>${dto.nickName}</td>
		
				</tr>
			
				<tr>
				<th>이메일</th>
				<td>${dto.email}</td>
			</tr>
				<tr>
				<th>주소</th>
				<td>${dto.address}</td>
			</tr>
				<tr>
				<th>성별</th>
				<td>${dto.gender}</td>
			</tr>
				<tr>
				<th>키</th>
				<td>${dto.height}</td>
			</tr>
				<tr>
				<th>몸무게</th>
				<td>${dto.weight}</td>
			</tr>
				<tr>
				<th>건강목표</th>
				<td>${dto.healthGoals}</td>
			</tr>

		</table>
	
		<div>
			<button type="button" class="back" onclick="location.href='/trip/board/list.do';">돌아가기</button>
<button type="button" onclick="location.href='/trip/user/useredit.do';" class="edit primary">수정하기</button>

<button type="button" onclick="location.href='/trip/user/userdel.do';" class="edit primary">회원탈퇴</button>

</div>
	</div>
	
	
	<script 
	src="https://code.jquery.com/jquery-3.7.1.js"></script>
	<script src="https://bit.ly/4cMuheh"></script>
	<script>
		
	</script>
</body>
</html>