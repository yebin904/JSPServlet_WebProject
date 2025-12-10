<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>     
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>여행루트 만들기</title>
    
    <%-- asset.jsp를 포함하여 notice.css, 폰트, 아이콘 등을 모두 불러옵니다. --%>
    <%@ include file="/WEB-INF/views/inc/asset.jsp" %>
    
    <%-- 이 페이지 전용 스타일인 route.css를 추가로 링크합니다. --%>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/asset/css/route.css">
</head>
<body>
    <c:set var="activeMenu" value="route" scope="request" />
    <%@ include file="/WEB-INF/views/inc/header.jsp" %>

    <%-- main 태그 구조를 더 간단하고 명확하게 변경 --%>
    <main class="route-choice-container">
        <h2>어떤 방식으로 여행 루트를 만드시겠어요?</h2>
        <div class="choice-buttons">
             <a href="${pageContext.request.contextPath}/ai/plan.do" class="choice-button">AI 추천 루트</a>
            <a href="#" class="choice-button">내가 직접 여행 루트 만들기</a>
        </div>
    </main>
    
    <script src="${pageContext.request.contextPath}/asset/js/main.js"></script>
</body>
</html>
