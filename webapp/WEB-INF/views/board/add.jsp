<%@page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>     
<!DOCTYPE html>
<html lang="ko">
<head>
	<meta charset="UTF-8">
	<%@ include file="/WEB-INF/views/inc/asset.jsp" %>
	<link rel="stylesheet" href="/trip/asset/css/tagify.css">
		<link rel="stylesheet" href="/trip/asset/css/addstyle.css">
	
	<script src="/trip/asset/js/tagify.js"></script>
</head>
<body>
	<!-- add.jsp -->
	<%@ include file="/WEB-INF/views/inc/header.jsp" %>
	<div id="main">
		<h1>게시판 <small>쓰기</small></h1>
		
		<form method="POST" action="/trip/board/add.do" enctype="multipart/form-data">
    <table>
        <tr>
            <th>제목</th>
            <td><input type="text" name="subject" id="subject" required class="full"></td>
        </tr>
        <tr>
            <th>판매상태</th>
            <td>
                <select name="status">
                    <option value="1">판매예정</option>
                    <option value="2">진행중</option>
                    <option value="3">판매종료</option>
                </select>
            </td>
        </tr>
        <tr>
            <th>카테고리</th>
            <td>
                <select name="category">
                    <option value="1">잡화</option>
                    <option value="2">전자기기·액세서리</option>
                    <option value="3">세면·뷰티</option>
                    <option value="4">수납·안전용품</option>
                    <option value="5">e쿠폰·입장권</option>
                    <option value="6">아웃도어·캠핑</option>
                </select>
            </td>
        </tr>
        <tr>
            <th>내용</th>
            <td><textarea name="content" id="content" required class="full"></textarea></td>
        </tr>
        <tr>
            <th>이미지</th>
            <td><input type="file" name="img" class="full" accept="image/*"></td>
        </tr>
        <tr>
            <th>핫딜아이템 이름</th>
            <td><input type="text" name="itemname" id="itemname" required class="full"></td>
        </tr>
        <tr>
            <th>가격</th>
            <td><input type="number" name="price" id="price" required class="full" oninput="this.value = this.value.replace(/[^0-9]/g, '');"></td>
        </tr>
        <tr>
            <th>링크</th>
            <td><input type="text" name="url" id="url" required class="full"></td>
        </tr>
    </table>
    <div>
        <button type="button" class="back" onclick="location.href='/trip/board/list.do';">돌아가기</button>
        <button type="submit" class="add primary">쓰기</button>
    </div>
</form>


</body>
</html>























