<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>     
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html lang="ko">
<head>
	<meta charset="UTF-8">
	<meta name="viewport" content="width=device-width, initial-scale=1.0">
	<title>공지사항</title>
	<%@ include file="/WEB-INF/views/inc/asset.jsp" %>
	
	<link rel="stylesheet" href="${pageContext.request.contextPath}/asset/css/notice.css">
</head>
<body>
	<%@ include file="/WEB-INF/views/inc/header.jsp" %>
	
	<main>
		<div class="notice-container">
		
			<div class="notice-title">
				<h2>공지사항</h2>
			</div>
			
			<table class="notice-table">
				<thead>
					<tr>
						<th>No.</th>
						<th>게시글</th>
						<th>글쓴이</th>
						<th>작성일</th>
						<th>조회수</th>
					</tr>
				</thead>
				
				<tbody>
					<c:if test="${list.size() == 0}">
					<tr>
						<td colspan="5">게시물이 없습니다.</td>
					</tr>
					</c:if>
					
					<c:forEach items="${list}" var="dto">
					<tr>
						<td>${dto.rnum}</td>
						<td class="title">
							<a href="/trip/notice/view.do?seq=${dto.notice_post_id}">${dto.notice_header}</a>
							<c:if test="${dto.isnew < 1}">
								<span class="isnew">new</span>
							</c:if>
						</td>
						<td>관리자</td>
						<td>${dto.notice_regdate}</td>
                        <td>${dto.notice_view_count}</td>
					</tr>
					</c:forEach>
				</tbody>
			</table>
			
			<div class="notice-footer">
				<div class="search-box">
					<form method="GET" action="/trip/notice/list.do">
						<input type="text" name="search" placeholder="Search">
						<button type="submit">검색</button>
					</form>
				</div>
				
				<div class="pagination">
    				${pagebar}
				</div>
			
				<div class="table-options">
					<%-- 관리자일 경우에만 글쓰기 버튼 표시 --%>
					<c:if test="${not empty id && lv == '2'}">
						<button type="button" class="btn btn-primary"
							onclick="location.href='${pageContext.request.contextPath}/trip/notice/add.do';">글쓰기</button>
					</c:if>
				</div>
			</div>
		</div>
	</main>

	<%-- ★★★ JavaScript 파일 연결 (body 태그 닫기 직전) ★★★ --%>
    <script src="${pageContext.request.contextPath}/asset/js/main.js"></script>
</body>
</html>