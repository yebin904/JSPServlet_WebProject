<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <%@ include file="/WEB-INF/views/inc/asset.jsp" %>
    <style>
        .board-container {
            max-width: 1100px;
            margin: 50px auto;
            padding: 0 30px;
        }
        .board-container h1 {
            text-align: center;
            font-size: 2.5rem;
            margin-bottom: 40px;
            font-weight: 700;
        }
        .board-table {
            width: 100%;
            border-collapse: collapse;
            border-top: 2px solid var(--text);
        }
        .board-table th, .board-table td {
            padding: 18px 10px;
            text-align: center;
            border-bottom: 1px solid var(--border);
        }
        .board-table th {
            background-color: #f8f9fa;
            font-weight: 500;
            color: var(--text-light);
        }
        .board-table td {
            color: var(--text);
        }
        .board-table .col-no { width: 8%; }
        .board-table .col-title { width: auto; text-align: left; }
        .board-table .col-writer { width: 12%; }
        .board-table .col-date { width: 15%; }
        .board-table .col-views { width: 10%; }
        
        .board-table .title a {
            text-decoration: none;
            color: var(--text);
            transition: color 0.2s;
        }
        .board-table .title a:hover {
            color: var(--primary);
            font-weight: 500;
        }
        .board-actions {
            margin-top: 30px;
            display: flex;
            justify-content: space-between;
            align-items: center;
        }
        .btn-write {
            padding: 12px 25px;
            background-color: var(--primary-dark);
            color: white;
            border: none;
            border-radius: 8px;
            text-decoration: none;
            font-weight: 500;
            cursor: pointer;
            transition: background-color 0.2s;
        }
        .btn-write:hover {
            background-color: var(--primary);
        }
        .pagination-container {
            display: flex;
            justify-content: center;
            margin-top: 40px;
        }
        .pagination-container a {
            color: var(--text-light);
            padding: 8px 12px;
            text-decoration: none;
            transition: background-color .3s;
            border: 1px solid #ddd;
            margin: 0 4px;
        }
        .pagination-container a[style*="color:tomato"] {
        	background-color: var(--primary);
        	color: white;
        	border-color: var(--primary);
        }
        .search-container {
            display: flex;
            justify-content: center;
            margin-bottom: 40px;
        }
        .search-container select, .search-container input {
            padding: 10px;
            border: 1px solid var(--border);
            border-radius: 8px;
            margin: 0 5px;
        }
        .search-container input[type="text"] {
            width: 300px;
        }
        .search-container input[type="submit"] {
            background-color: #f1f3f5;
            cursor: pointer;
        }
    </style>
</head>
<body>

    <%@ include file="/WEB-INF/views/inc/header.jsp" %>

    <main class="board-container">
        <h1>여행 후기</h1>
        
        <div class="search-container">
            <form method="GET" action="/trip/reviewboard/list.do">
                <select name="col">
                    <option value="review_board_title">제목</option>
                    <option value="review_board_content">내용</option>
                </select>
                <input type="text" name="word" placeholder="검색어를 입력하세요.">
                <input type="submit" value="검색">
            </form>
        </div>

        <table class="board-table">
            <thead>
                <tr>
                    <th class="col-no">No.</th>
                    <th class="col-title">게시글</th>
                    <th class="col-writer">글쓴이</th>
                    <th class="col-date">작성일</th>
                    <th class="col-views">조회수</th>
                </tr>
            </thead>
            <tbody>
                <c:if test="${empty list}">
                    <tr>
                        <td colspan="5">게시글이 없습니다.</td>
                    </tr>
                </c:if>
                <c:forEach items="${list}" var="dto">
                    <tr>
                        <td>${dto.review_post_id}</td>
                        <td class="col-title title">
                            <a href="/trip/reviewboard/detail.do?id=${dto.review_post_id}">${dto.review_board_title}</a>
                        </td>
                        <td>${dto.nickname}</td>
                        <td>
                            <fmt:formatDate value="${dto.review_board_regdate}" pattern="yyyy-MM-dd" />
                        </td>
                        <td>${dto.review_board_count}</td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
        
        <div class="board-actions">
            <div></div>
            <div class="pagination-container">
                ${pagebar}
            </div>
            <a href="/trip/reviewboard/write.do" class="btn-write">글쓰기</a>
        </div>
    </main>

</body>
</html>
