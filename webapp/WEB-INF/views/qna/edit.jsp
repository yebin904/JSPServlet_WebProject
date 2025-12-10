<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<jsp:include page="/WEB-INF/views/inc/member_asset.jsp" />
<link rel="stylesheet" href="${pageContext.request.contextPath}/asset/css/main.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/asset/css/qna.css">
<jsp:include page="/WEB-INF/views/inc/header.jsp" />

<main class="qna-edit container">
    <div class="qna-edit-box">
        <h2>게시글 수정</h2>

        <form action="${pageContext.request.contextPath}/qna/edit.do" method="post">
            <input type="hidden" name="seq" value="${dto.question_board_id}" />

            <div class="form-group">
                <label for="title">제목</label>
                <input type="text" id="title" name="title" value="${dto.question_board_title}" required>
            </div>

            <div class="form-group">
                <label for="category">카테고리</label>
                <select name="category" id="category" required>
                    <c:forEach var="cat" items="${categories}">
                        <option value="${cat.question_category_id}"
                                <c:if test="${cat.question_category_id eq dto.question_category_id}">selected</c:if>>
                            ${cat.question_category_name}
                        </option>
                    </c:forEach>
                </select>
            </div>

            <div class="form-group">
                <label for="content">내용</label>
                <textarea id="content" name="content" rows="10" required>${dto.question_board_content}</textarea>
            </div>

            <div class="form-actions">
                <button type="submit" class="btn btn-primary">수정 완료</button>
                <button type="button" class="btn btn-secondary" onclick="location.href='${pageContext.request.contextPath}/qna/view.do?seq=${dto.question_board_id}'">취소</button>
            </div>
        </form>
    </div>
</main>

<jsp:include page="/WEB-INF/views/inc/member_footer.jsp" />
