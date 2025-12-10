<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<header>
    <div class="container">
        <div class="header-top">
            <div class="logo"><a href="/trip/findboard/list.do">LOGO</a></div>
            <div class="user-menu">
                <c:if test="${empty sessionScope.userId}">
                    <a href="/trip/member/login.do">로그인</a>
                    <span>/</span>
                    <a href="#!">회원가입</a>
                </c:if>
                <c:if test="${not empty sessionScope.userId}">
                    <span>${sessionScope.nickname}님</span>
                    <a href="/trip/member/logout.do">로그아웃</a>
                </c:if>
            </div>
        </div>
        <nav class="main-nav">
            <a href="#!">여행정보</a>
            <a href="#!">AI 맞춤 루트</a>
            <a href="/trip/findboard/list.do">게시판</a>
            <a href="#!">더보기</a>
        </nav>
        <div class="sub-nav">
            <span>여행 루트 추천 게시판</span> |
            <span>여행 후기 게시판</span> |
            <span>Q&A 게시판</span> |
            <span>동행 찾기 게시판</span>
        </div>
    </div>
</header>