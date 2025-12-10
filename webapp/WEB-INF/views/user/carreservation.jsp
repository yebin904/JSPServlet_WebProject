<%@page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>     
<!DOCTYPE html>
<html lang="ko">
<head>
	<meta charset="UTF-8">
	<%@ include file="/WEB-INF/views/inc/asset.jsp" %>
			<link rel="stylesheet" href="/trip/asset/css/myactivitiesstyle.css">
	
</head>
<body>
	
	<%@ include file="/WEB-INF/views/inc/header.jsp" %>
	
	<nav class="board-sub-header">
    <div class="sub-header-inner">
        <a href="/trip/user/carreservation.do">렌트카 예약</a>
        <a href="/trip/user/accomreservation.do">숙소 예약</a>
    </div>
</nav>
	
	<div id="main" >
		<h1>차량 예약</h1>
		
		<c:if test="${map.search == 'y'}">
		<div id="labelSearch">
			'${map.word}'(으)로 검색한 결과 ${map.totalCount}건이 있습니다.			
		</div>
		</c:if>
		
		<%-- 
		<div id="pagebar">
			<span>페이지: </span>
			<input type="number" class="short" id="page" value="${map.nowPage}" min="1" max="${map.totalPage}">
			<input type="button" value="이동하기" onclick="location.href='/toy/board/list.do?page=' + $('#page').val();">
		</div> 
		--%>
		
		
		<%-- 
		<div id="pagebar">
			<select onchange="location.href='/toy/board/list.do?page=' + $(this).val() + '&column=${map.column}&word=${map.word}';">
				<c:forEach begin="1" end="${map.totalPage}" var="i">
				<option value="${i}" <c:if test="${map.nowPage == i}">selected</c:if>>${i}페이지</option>
				</c:forEach>
			</select>
		</div>  
		--%>
		
		
		<table id="list" class="borad-table">
			<tr>
				<th>예약번호</th>
				<th>차종</th>
				<th>차량모델</th>
				<th>차량대여일</th>
				<th>차량반납일</th>
			</tr>
			<c:if test="${list.size() == 0}">
			<tr>
				<td colspan="5">게시물이 없습니다.</td>
			</tr>
			</c:if>
			<c:forEach items="${list}" var="dto">
			
			<%-- 
			//##공지사항 부분 제외
			<c:if test="${dto.notice == '0'}">
			<tr>
			</c:if>
			
			<c:if test="${dto.notice == '1' }">
			<tr style="background-color: rgba(255,0,0,.03);">
			</c:if> --%>
			 <%-- onclick="location.href='/trip/board/${dto.boradCode}.do?seq=${dto.seq}&column=${map.column}&word=${map.word}'" --%>
			<tr onclick="location.href='/trip/user/carreservationview.do?seq=${dto.seq}&carseq=${dto.carseq}'">
				<td>
				${dto.seq}	
					
				</td>
				<td>
				<!-- 글제목 -->
						<%-- <a href="/trip/board/${dto.boradCode}.do?seq=${dto.seq}&column=${map.column}&word=${map.word}">
						${dto.cartype}</a> --%>

					${dto.cartype}
					<%-- <!-- 댓글 개수 -->
					<c:if test="${dto.commentCount > 0}">
					<span class="commentCount">
						<span class="material-symbols-outlined">chat</span>
						${dto.commentCount}
					</span>
					</c:if> --%>
					
					<%-- <!-- 최신글 표시 -->
					<c:if test="${dto.isnew < 1}">
						<span class="isnew">new</span>
					</c:if> --%>
					
				</td>
				<td>
					<%-- 
					<fmt:parseDate value="${dto.regdate}" var="regdate" pattern="yyyy-MM-dd HH:mm:ss"></fmt:parseDate>
					<fmt:formatDate value="${regdate}" pattern="yyyy-MM-dd" /> 
					--%>
					${dto.carname}	
				</td>
				<td>
					${dto.pickupdate}	
				</td>
				<td>
					${dto.dropoffdate}	
				</td>
			</tr>
			</c:forEach>
		</table>
		
		<!-- 검색 -->
		<!-- <form id="searchForm" method="GET" action="/trip/user/boardactivities.do">
			<select name="column">
				<option value="hotdeal_title">제목</option>
				<option value="hotdeal_content">내용</option>
				<option value="hotdeal_name">이름</option>
			</select>
			<input type="text" name="word" class="long" required>
			<input type="submit" value="검색하기">	
		</form>		 -->
		
		<!-- 페이지바 -->
		<div id="pagebar">${pagebar}</div>
		
	
		
	</div>
	
	<script>
	
		<c:if test="${map.search == 'y'}">
		$('select[name=column]').val('${map.column}');
		$('input[name=word]').val('${map.word}');
		</c:if>
	
	</script>
		
</body>
</html>

			
			
		
