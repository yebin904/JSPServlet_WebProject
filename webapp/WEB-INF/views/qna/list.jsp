<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    
    <%-- 공통 CSS/JS 파일인 asset.jsp를 먼저 포함합니다. --%>
    <jsp:include page="/WEB-INF/views/inc/asset.jsp" />
    
    <%-- QnA 게시판 전용 CSS를 이어서 포함합니다. --%>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/asset/css/qna.css">
</head>
<body>

    <%-- 헤더 --%>
    <jsp:include page="/WEB-INF/views/inc/header.jsp" />

    <%-- 메인 콘텐츠 --%>
    <main class="qna-list container">

        <h2 class="qna-list-title">QnA 게시판</h2>

        <form method="GET" action="/trip/qna/list.do" class="qna-search-form">
            
            <div class="category-box">
                <select name="category" id="categorySelect" onchange="this.form.submit()">
                    <option value="">전체 카테고리</option>
                    <c:forEach var="c" items="${categoryList}">
                        <option value="${c.question_category_id}" ${param.category == c.question_category_id ? "selected" : ""}>
                            ${c.question_category_name}
                        </option>
                    </c:forEach>
                </select>
            </div>

            <div class="search-group">
                <select name="col">
                    <option value="question_board_title" ${param.col == 'question_board_title' ? "selected" : ""}>제목</option>
                    <option value="question_board_content" ${param.col == 'question_board_content' ? "selected" : ""}>내용</option>
                    <option value="user_id" ${param.col == 'user_id' ? "selected" : ""}>작성자</option>
                </select>
                <input type="text" name="word" placeholder="검색어를 입력하세요" value="${param.word != null ? param.word : ''}" />
                <button type="submit" class="btn btn-primary">검색</button>
            </div>

            <div class="write-btn">
                <button type="button" class="btn btn-success" onclick="location.href='/trip/qna/add.do'">질문 등록</button>
            </div>
        </form>

        <table class="qna-table">
            <colgroup>
                <col style="width: 50%;">
                <col style="width: 15%;">
                <col style="width: 15%;">
                <col style="width: 20%;">
            </colgroup>
            <thead>
                <tr>
                    <th>제목</th>
                    <th>작성자</th>
                    <th>작성일</th>
                    <th>조회수 / 좋아요 / 스크랩</th>
                </tr>
            </thead>
            <tbody>
                <c:if test="${empty list}">
                    <tr>
                        <td colspan="4" class="empty">등록된 문의가 없습니다.</td>
                    </tr>
                </c:if>

                <c:forEach var="dto" items="${list}">
                    <tr>
                        <td class="title">
                            <a href="view.do?seq=${dto.question_board_id}">
                                <span style="color=#2e7d32;">[${dto.question_category_name}]</span> ${dto.display_title}
                            </a>
                            <span class="badge-answer ${dto.question_board_answer_status eq 'y' ? 'answered' : 'unanswered'}">
                                <c:choose>
                                    <c:when test="${dto.question_board_answer_status eq 'y'}">완료</c:when>
                                    <c:otherwise>미답변</c:otherwise>
                                </c:choose>
                            </span>
                        </td>
                        <td>${dto.nickname}</td>
                        <td>${dto.display_regdate}</td>
                        <td class="info">
                            조회수: ${dto.question_board_view_count} | 좋아요: ${dto.question_board_like_count} | 스크랩: ${dto.question_board_scrap_count}
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>

        <div id="pagebar">${pagebar}</div>

    </main>

    <%-- 푸터 --%>
    <jsp:include page="/WEB-INF/views/inc/member_footer.jsp" />

</body>
</html>
