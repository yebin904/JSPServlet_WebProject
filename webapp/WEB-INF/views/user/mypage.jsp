<%@page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>     
<!DOCTYPE html>
<html lang="ko">
<head>
	<meta charset="UTF-8">
	<%@ include file="/WEB-INF/views/inc/asset.jsp" %>
	<link rel="stylesheet" href="/trip/asset/css/mypagestyle.css">
	
</head>
<body>
	
	<%@ include file="/WEB-INF/views/inc/header.jsp" %>
	
	
	<div id="main">
		<h1>마이페이지</h1>
		
		<table id="view">
			<tr>
				<th></th>
				<td><a href="/trip/user/userinfo.do">회원정보</a></td>
			</tr>
			
			<tr>
				<th></th>
				<td><a href="/trip/user/boardactivities.do">활동내역</a></td>
			</tr>
						
			<tr>
				<th></th>
				<td><a href="/trip/user/userroute.do">내 여행루트</a></td>
			</tr>
			
			<tr>
				<th></th>
				<td><a href="/trip/user/carreservation.do">예약</a></td>
			</tr>

			
			
		</table>
		
		
	

	</div>


	
		
</body>
</html>























