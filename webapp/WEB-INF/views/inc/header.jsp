<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>  

<header id="main-header">
    <div class="header-inner">
        <%-- 왼쪽: 로고 --%>
        <div class="header-column">
            <div class="logo">
                <a href="${pageContext.request.contextPath}/main.do">LOGO</a>
            </div>
        </div>
        
        <%-- 중앙: 내비게이션 메뉴 --%>
        <nav class="nav-menu">
            <div class="nav-item has-dropdown">
                <a href="#">여행정보</a>
                <div class="sub-menu">
                    <a href="${pageContext.request.contextPath}/allplace/map.do">관광지 지도</a>
                    <a href="${pageContext.request.contextPath}/info/trend/trend.do">여행트렌드</a>
                    <a href="#">여행지 뉴스</a>
                    <a href="#">날씨/공기질</a>
                    <a href="#">시기별 축제/행사</a>
                </div>
            </div>
            <a href="${pageContext.request.contextPath}/route/mainroute.do" class="${activeMenu == 'route' ? 'active' : ''}">여행루트</a>
            
            <div class="nav-item has-dropdown">
                <a href="#">게시판</a>
                <div class="sub-menu">
                    <a href="${pageContext.request.contextPath}/routepost/list.do">여행 루트 추천 게시판</a>
                    <a href="${pageContext.request.contextPath}/reviewboard/list.do">여행 후기 게시판</a>
                    <a href="${pageContext.request.contextPath}/qna/list.do">Q&A</a>
                    <a href="${pageContext.request.contextPath}/findboard/list.do">동행 찾기 게시판</a>
                    <a href="${pageContext.request.contextPath}/board/list.do">여행 용품 게시판</a>
                </div>
            </div>
            
            <a href="${pageContext.request.contextPath}/list.do">공지사항</a>
        </nav>
        
        <%-- 오른쪽: 검색 및 사용자 메뉴 --%>
        <div class="header-column header-right">
            <form class="search-bar" action="${pageContext.request.contextPath}/search.do" method="get">
                <input type="text" name="query" placeholder="검색어 입력" autocomplete="off">
                <%-- main.css 와 Font Awesome 아이콘에 맞게 클래스 수정 --%>
                <button type="submit" aria-label="검색"><i class="fa-solid fa-magnifying-glass"></i></button>
            </form>
    
            <%-- main.css 클래스에 맞춰 로그인/로그아웃 상태 UI 수정 --%>
            <div class="user-info">
                <c:choose>
                    <%-- 로그인 상태가 아닐 때 --%>
                    <c:when test="${empty user}">
                        <a href="${pageContext.request.contextPath}/user/login.do" class="btn-login">로그인</a>
                        <a href="${pageContext.request.contextPath}/user/register.do" class="btn-login">회원가입</a>
                    </c:when>
                    <%-- 로그인 상태일 때 --%>
                    <c:otherwise>
                        <a href="${pageContext.request.contextPath}/user/mypage.do" class="btn-logout">마이페이지</a>
                        <a href="${pageContext.request.contextPath}/user/logout.do" class="btn-logout">로그아웃</a>
                    </c:otherwise>
                </c:choose>
            </div>

            <%-- 모바일 햄버거 버튼 --%>
            <button id="hamburger-btn" class="hamburger-btn" aria-label="메뉴 열기">
                <i class="fa-solid fa-bars"></i>
            </button>
        </div>
    </div>
</header>

<%-- 모바일 메뉴 패널 --%>
<div id="mobile-menu" class="mobile-menu-panel">
    <div class="menu-header">
        <div class="logo">
             <a href="${pageContext.request.contextPath}/main.do">LOGO</a>
        </div>
        <button id="close-menu-btn" class="close-btn" aria-label="메뉴 닫기">
            <i class="fa-solid fa-times"></i>
        </button>
    </div>

    <%-- ✅ [수정] 모바일 내비게이션 링크에 드롭다운 구조 추가 --%>
    <nav class="mobile-nav-links">
        <%-- 여행정보 드롭다운 --%>
        <div class="mobile-nav-item has-dropdown">
            <a href="#" class="dropdown-toggle">여행정보 <i class="fa-solid fa-chevron-down dropdown-arrow"></i></a>
            <div class="mobile-sub-menu">
                <a href="${pageContext.request.contextPath}/allplace/map.do">관광지 지도</a>
                <a href="${pageContext.request.contextPath}/info/trend/trend.do">여행트렌드</a>
                <a href="#">여행지 뉴스</a>
                <a href="#">날씨/공기질</a>
                <a href="#">시기별 축제/행사</a>
            </div>
        </div>
        <%-- 여행루트 (단일 링크) --%>
        <a href="${pageContext.request.contextPath}/route/mainroute.do" class="mobile-nav-item">여행루트</a>
        <%-- 게시판 드롭다운 --%>
        <div class="mobile-nav-item has-dropdown">
            <a href="#" class="dropdown-toggle">게시판 <i class="fa-solid fa-chevron-down dropdown-arrow"></i></a>
            <div class="mobile-sub-menu">
                <a href="${pageContext.request.contextPath}/routepost/list.do">여행 루트 추천</a>
                <a href="${pageContext.request.contextPath}/reviewboard/list.do">여행 후기</a>
                <a href="${pageContext.request.contextPath}/qna/list.do">Q&A</a>
                <a href="${pageContext.request.contextPath}/findboard/list.do">동행 찾기</a>
                <a href="${pageContext.request.contextPath}/board/list.do">여행 용품</a>
            </div>
        </div>
        <%-- 공지사항 (단일 링크) --%>
        <a href="${pageContext.request.contextPath}/list.do" class="mobile-nav-item">공지사항</a>
    </nav>
    <div class="menu-footer">
        <a href="#" class="icon-link" aria-label="검색"><i class="fa-solid fa-magnifying-glass"></i></a>
        <c:choose>
            <c:when test="${not empty user}">
                <a href="${pageContext.request.contextPath}/user/mypage.do" class="icon-link profile-link" aria-label="내 프로필"><i class="fa-solid fa-user"></i></a>
            </c:when>
            <c:otherwise>
                <a href="${pageContext.request.contextPath}/user/login.do" class="login-link">로그인</a>
            </c:otherwise>
        </c:choose>
    </div>
</div>