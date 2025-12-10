<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="/WEB-INF/views/inc/member_asset.jsp" />
<link rel="stylesheet" href="${pageContext.request.contextPath}/asset/css/qna.css">
<jsp:include page="/WEB-INF/views/inc/header.jsp" />

<main class="qna-write container">
    <h2 class="qna-list-title">질문 등록</h2>

    <form method="post" action="${pageContext.request.contextPath}/qna/add.do" class="qna-form">
        <div class="form-group">
            <label>카테고리</label>
            <select name="categoryId" required>
                <option value="">선택하세요</option>
                <c:forEach var="c" items="${categories}">
                    <option value="${c.question_category_id}">${c.question_category_name}</option>
                </c:forEach>
            </select>
        </div>

        <div class="form-group">
            <label>제목</label>
            <input type="text" name="title" maxlength="200" required />
        </div>

        <div class="form-group">
            <label>내용</label>
            <textarea name="content" rows="10" maxlength="4000" required></textarea>
        </div>

        <div class="btn-area">
            <button type="submit" class="btn btn-primary">등록</button>
            <button type="button" class="btn btn-secondary" onclick="history.back();">취소</button>
        </div>
    </form>
</main>

<jsp:include page="/WEB-INF/views/inc/member_footer.jsp" />
