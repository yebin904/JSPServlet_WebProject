<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>동행 찾기</title>
<%@ include file="/WEB-INF/views/inc/asset.jsp" %>
<%-- ✅ [추가] 외부 CSS 파일을 연결합니다. --%>
<link rel="stylesheet" href="/trip/asset/css/findboard.css">
<style>
    /* ✅ [수정] 테이블 행 높이 및 추천수 줄바꿈 방지 CSS */
    .notice-table th {
        white-space: nowrap; /* 헤더 텍스트 줄바꿈 방지 */
    }
    .notice-table tbody tr td {
        padding: 0.5rem 0.75rem; /* 상하 여백(padding)을 줄여 높이를 낮춤 */
        vertical-align: middle;
    }
</style>
</head>
<body>
    <%@ include file="/WEB-INF/views/inc/header.jsp" %>
    
    <main class="container">
        <div class="notice-title">
            <h2>동행 찾기</h2>
        </div>

        <table class="notice-table">
            <thead>
                <tr>
                    <th style="width:8%;">No.</th>
                    <th style="width:52%;" class="title">제목</th>
                    <th style="width:15%;">글쓴이</th>
                    <th style="width:10%;">작성일</th>
                    <th style="width:7%;">추천수</th>
                    <th style="width:8%;">조회수</th>
                </tr>
            </thead>
            <tbody>
                <c:if test="${empty list}">
                    <tr><td colspan="6">게시글이 없습니다.</td></tr>
                </c:if>
                <c:forEach items="${list}" var="dto">
                    <tr>
                        <td>${dto.find_board_id}</td>
                        <td class="title">
                            <a href="/trip/findboard/view.do?seq=${dto.find_board_id}&page=${map.page}&searchType=${map.searchType}&searchKeyword=${map.searchKeyword}">${dto.find_board_title}</a>
                            <c:if test="${dto.commentCount > 0}">
                                <span class="comment-count">[${dto.commentCount}]</span>
                            </c:if>
                        </td>
                        <td>${dto.nickname}</td>
                        <td>
                            <fmt:parseDate value="${dto.find_board_regdate}" pattern="yyyy-MM-dd HH:mm:ss" var="parsedDateTime" type="both" />
                            <fmt:formatDate value="${parsedDateTime}" pattern="yyyy-MM-dd" />
                        </td>
                        <td>${dto.likeCount}</td>
                        <td>${dto.find_board_view_count}</td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>

        <div class="notice-footer">
            <div class="search-box">
                <form method="GET" action="/trip/findboard/list.do">
                    <select name="searchType">
                        <option value="title_content" ${map.searchType == 'title_content' ? 'selected' : ''}>제목+내용</option>
                        <option value="nickname" ${map.searchType == 'nickname' ? 'selected' : ''}>작성자</option>
                    </select>
                    <input type="text" name="searchKeyword" placeholder="검색어를 입력하세요" value="${map.searchKeyword}">
                    <button type="submit">검색</button>
                </form>
            </div>
            
            <div class="pagination">
                ${paging}
            </div>

            <div class="table-options">
                <button type="button" class="btn btn-primary" onclick="location.href='/trip/findboard/add.do';">글쓰기</button>
            </div>
        </div>
    </main>
</body>
</html>

