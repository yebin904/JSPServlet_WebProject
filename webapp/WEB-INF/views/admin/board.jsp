<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>통합 게시판 관리</title>
    <%@ include file="/WEB-INF/views/inc/admin_asset.jsp" %>
    <style>
        /* 필터 및 검색 UI를 위한 추가 스타일 */
        .board-controls {
            display: flex;
            justify-content: space-between;
            align-items: center;
            padding: 15px;
            background-color: #f8f9fa;
            border-radius: 8px;
            margin-bottom: 25px;
            border: 1px solid var(--border-color);
        }
        .filter-buttons a {
            text-decoration: none;
            padding: 8px 15px;
            margin-right: 10px;
            border: 1px solid var(--border-color);
            background-color: #fff;
            color: #333;
            border-radius: 20px;
            font-size: 0.9em;
            transition: all 0.2s;
        }
        .filter-buttons a:hover {
            background-color: #e9ecef;
        }
        .filter-buttons a.active { /* 선택된 필터 버튼 스타일 */
            background-color: var(--primary-color);
            color: white;
            border-color: var(--primary-color);
            font-weight: bold;
        }
        .search-form {
            display: flex;
            gap: 8px;
        }
        .search-form select,
        .search-form input[type="text"] {
            height: 40px;
            padding: 0 15px;
            border-radius: 6px;
            border: 1px solid var(--border-color);
        }
        .search-form button {
            height: 40px;
        }
    </style>
</head>
<body>
    <div id="admin-container">
        <%@ include file="/WEB-INF/views/inc/admin_header.jsp" %>
        
        <main id="admin-main">
            <h1>통합 게시판 관리</h1>

            <div class="board-controls">
                <div class="filter-buttons">
                    <%-- ▼▼▼ [핵심 수정] href의 boardType 값을 DB의 실제 값과 일치시켰습니다. ▼▼▼ --%>
                    <a href="/trip/admin/board.do?boardType=all" class="${boardType == 'all' ? 'active' : ''}">전체</a>
                    <a href="/trip/admin/board.do?boardType=동행찾기" class="${boardType == '동행찾기' ? 'active' : ''}">동행찾기</a>
                    <a href="/trip/admin/board.do?boardType=루트추천" class="${boardType == '루트추천' ? 'active' : ''}">루트 추천</a>
                    <a href="/trip/admin/board.do?boardType=공지사항" class="${boardType == '공지사항' ? 'active' : ''}">공지사항</a>
                    <a href="/trip/admin/board.do?boardType=핫딜" class="${boardType == '핫딜' ? 'active' : ''}">핫딜</a>
                    <a href="/trip/admin/board.do?boardType=후기" class="${boardType == '후기' ? 'active' : ''}">후기</a>
                    <a href="/trip/admin/board.do?boardType=질문" class="${boardType == '질문' ? 'active' : ''}">질문</a>
                </div>
                
                <form method="GET" action="/trip/admin/board.do" class="search-form">
                    <input type="hidden" name="boardType" value="${boardType}">
                    
                    <select name="searchType">
                        <option value="title" ${searchType == 'title' ? 'selected' : ''}>제목</option>
                        <option value="nickname" ${searchType == 'nickname' ? 'selected' : ''}>작성자</option>
                    </select>
                    <input type="text" name="searchKeyword" placeholder="검색어를 입력하세요" value="${searchKeyword}">
                    <button type="submit" class="btn primary">검색</button>
                </form>
            </div>
            
            <table class="admin-table">
                <thead>
                    <tr>
                        <th style="width:5%;">번호</th>
                        <th style="width:10%;">게시판</th>
                        <th style="width:40%;">제목</th>
                        <th style="width:10%;">작성자</th>
                        <th style="width:15%;">작성일</th>
                        <th style="width:5%;">추천</th>
                        <th style="width:5%;">조회수</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach items="${list}" var="dto">
                        <tr>
                            <td>${dto.seq}</td>
                            <td>${dto.boardType}</td>
                            <td>
                                <a href="${pageContext.request.contextPath}/${dto.targetType}/view.do?seq=${dto.seq}" target="_blank">
                                    ${dto.title}
                                    <c:if test="${dto.commentCount > 0}">
                                        <span class="comment-count">(${dto.commentCount})</span>
                                    </c:if>
                                </a>
                            </td>
                            <td>${dto.nickname}</td>
                            <td>
                                <fmt:formatDate value="${dto.regdate}" pattern="yyyy-MM-dd HH:mm"/>
                            </td>
                            <td>${dto.likeCount}</td>
                            <td>${dto.viewCount}</td>
                        </tr>
                    </c:forEach>
                    <c:if test="${empty list}">
                        <tr>
                            <td colspan="7" style="text-align: center;">게시물이 없습니다.</td>
                        </tr>
                    </c:if>
                </tbody>
            </table>
        </main>
    </div>
</body>
</html>