 <%@page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
   
<!DOCTYPE html>
<html lang="ko">
<head>
	<meta charset="UTF-8">
	<%@ include file="/WEB-INF/views/inc/asset.jsp" %>
				<link rel="stylesheet" href="/trip/asset/css/liststyle.css">
	
</head>
<body>
	<!-- list.jsp -->
	<%@ include file="/WEB-INF/views/inc/header.jsp" %>
	
	
	<div id="main" >
		<h1>여행 용품 게시판 
			<c:if test="${map.search == 'n'}">
			<small>목록</small>
			</c:if>
			
			<c:if test="${map.search == 'y'}">
			<small>검색</small>
			</c:if>
		</h1>
		
		<c:if test="${map.search == 'y'}">
		<div id="labelSearch">
			'${map.word}'(으)로 검색한 결과 ${map.totalCount}건이 있습니다.			
		</div>
		</c:if>
		
		
		
		<table id="list" class="borad-table">
			<tr>
				<th>번호</th>
				<th>제목</th>
				<th>이름</th>
				<th>날짜</th>
				<th>읽음</th>
			</tr>
			<c:if test="${list.size() == 0}">
			<tr>
				<td colspan="5">게시물이 없습니다.</td>
			</tr>
			</c:if>
			<c:forEach items="${list}" var="dto">
			

			
			
				<td>
				<!-- 글번호 -->
				${dto.seq}	
					
				</td>
				<td>
				<!-- 글제목 -->
						<a href="/trip/board/view.do?seq=${dto.seq}&column=${map.column}&word=${map.word}">
						<c:if test="${dto.img != null }">
						<img src="/trip/asset/place/${dto.img}" id="imgPlace">
						</c:if>
						<c:if test="${dto.img == null }">
						<img src="/trip/asset/place/default.png" id="imgPlace">
						</c:if>
						[${dto.status}]${dto.subject}</a>

					
					<!-- 댓글 개수 -->
					<c:if test="${dto.commentCount > 0}">
					<span class="commentCount">
						<span class="material-symbols-outlined">chat</span>
						${dto.commentCount}
					</span>
					</c:if>
					
					
					
				</td>
				<td>${dto.name}</td>
				<td>
					
					<fmt:formatDate value="${regdate}" pattern="yyyy-MM-dd" /> 
					
					${dto.regdate}	
				</td>
				<td>${dto.readcount}</td>
			</tr>
			</c:forEach>
		</table>
		
		<!-- 검색 -->
		<form id="searchForm" method="GET" action="/trip/board/list.do">
			<select name="column">
				<option value="hotdeal_title">제목</option>
				<option value="hotdeal_content">내용</option>
				<option value="hotdeal_name">이름</option>
			</select>
			<input type="text" name="word" class="long" required>
			<input type="submit" value="검색하기">	
		</form>		
		
		<!-- 페이지바 -->
		<div id="pagebar">${pagebar}</div>
		
		<div>
			<c:if test="${not empty id}">
			<button type="button" class="add primary" onclick="location.href='/trip/board/add.do';">쓰기</button>
			</c:if>
		</div>
		
	</div>
	
	<script>
	
		<c:if test="${map.search == 'y'}">
		$('select[name=column]').val('${map.column}');
		$('input[name=word]').val('${map.word}');
		</c:if>
	
	</script>
		
</body>
</html>
