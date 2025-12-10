<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%--
    [admin_asset.jsp]
    - 모든 관리자 페이지(login.jsp 제외)에서 공통으로 사용하는 CSS와 JavaScript 파일을 관리합니다.
    - 경로가 틀어지는 것을 방지하기 위해 ${pageContext.request.contextPath}를 사용하여 절대 경로로 참조합니다.
--%>

<%-- 1. 공통 CSS 파일 --%>
<link rel="stylesheet" href="${pageContext.request.contextPath}/asset/css/admin.css">

<%-- 2. jQuery 라이브러리 (AJAX 등 편리한 기능 사용을 위해) --%>
<script src="https://code.jquery.com/jquery-3.7.1.js"></script>

<%-- 3. 구글 폰트 (관리자 페이지 디자인용) --%>
<link rel="preconnect" href="https://fonts.googleapis.com">
<link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
<link href="https://fonts.googleapis.com/css2?family=Poppins:wght@300;400;600&family=Noto+Sans+KR:wght@400;500&display=swap" rel="stylesheet">
