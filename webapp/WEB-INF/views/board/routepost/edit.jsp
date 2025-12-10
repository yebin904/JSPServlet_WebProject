<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
<meta charset="UTF-8">
<title>게시글 수정</title>
<%@ include file="/WEB-INF/views/inc/asset.jsp" %>
<link rel="stylesheet" href="${pageContext.request.contextPath}/asset/css/routepost.css">
</head>
<body>
<%@ include file="/WEB-INF/views/inc/header.jsp" %>
<div class="container">
    <h2>게시글 수정 ✏️</h2>

    <form method="post" action="${pageContext.request.contextPath}/routepost/edit.do?id=${dto.routepostId}" enctype="multipart/form-data">

        <label>제목</label>
        <input type="text" name="title" value="${dto.routepostTitle}" required>
        
        <!-- 루트 선택 -->
        <label for="route_id">루트 선택</label>
        <select id="route_id" name="route_id" value="${dto.routepostTitle}" required>
            <option value="">-- 여행 루트를 선택하세요 --</option>
            <option value="1">서울 → 강릉 1박 2일 루트</option>
            <option value="2">부산 해운대 당일치기 루트</option>
            <option value="3">제주도 2박 3일 루트</option>
        </select>

        <label>내용</label>
        <textarea name="content" required>${dto.routepostContent}</textarea>

        <label>만족도 (0.0 ~ 5.0)</label>
        <input type="number" name="satisfaction" step="0.1" min="0" max="5" value="${dto.routepostSatisfaction}" required>

        <label>현재 등록된 이미지</label>
			<c:choose>
			    <c:when test="${not empty imageList}">
			        <div class="image-preview">
			            <c:forEach var="img" items="${imageList}">
			                <img src="${pageContext.request.contextPath}/uploads/routepost/${img.routepostImageUrl}" alt="기존 이미지">
			            </c:forEach>
			        </div>
			    </c:when>
			    <c:otherwise>
			        <p>등록된 이미지가 없습니다.</p>
			    </c:otherwise>
			</c:choose>


        <label>이미지 변경 (여러 장 선택 가능)</label>
        <input type="file" name="images" multiple accept="image/*">

        <div class="btn-area">
            <button type="submit" class="btn btn-submit">수정 완료</button>
            <button type="button" class="btn btn-cancel" onclick="location.href='${pageContext.request.contextPath}/routepost/view.do?id=${dto.routepostId}'">취소</button>
        </div>
    </form>
</div>

</body>
</html>
