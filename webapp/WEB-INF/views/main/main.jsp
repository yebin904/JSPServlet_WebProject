<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page import="java.util.List, java.util.ArrayList, com.trip.admin.model.statDTO" %>
<%
    // 서블릿에서 넘어올 임시 데이터
    List<statDTO> rankingList = new ArrayList<>();
    statDTO r1 = new statDTO(); r1.setCategory("홍대/연남동"); r1.setCount(2); rankingList.add(r1);
    statDTO r2 = new statDTO(); r2.setCategory("부산 해리단길"); r2.setCount(-1); rankingList.add(r2);
    statDTO r3 = new statDTO(); r3.setCategory("성수동 팝업스토어"); r3.setCount(-1); rankingList.add(r3);
    statDTO r4 = new statDTO(); r4.setCategory("양양 서피비치"); r4.setCount(0); rankingList.add(r4);
    statDTO r5 = new statDTO(); r5.setCategory("경주 황리단길"); r5.setCount(0); rankingList.add(r5);
    request.setAttribute("rankingList", rankingList);
    
    // 통계 데이터 (임시)
    request.setAttribute("totalReviews", 103);
%>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Trip.com</title>
    <%@ include file="/WEB-INF/views/inc/asset.jsp" %>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.2/css/all.min.css"/>
    <link rel="stylesheet" href="<c:url value='/asset/css/mainpage.css' />">
</head>
<body>

    <%@ include file="/WEB-INF/views/inc/header.jsp" %>
    
    <main>
        <div class="main-container">
            <div class="main-left-content">
                <section class="main-banner">
                    <div id="mainBannerCarousel" class="carousel slide" data-ride="carousel">
                        <ol class="carousel-indicators">
                            <li data-target="#mainBannerCarousel" data-slide-to="0" class="active"></li>
                            <li data-target="#mainBannerCarousel" data-slide-to="1"></li>
                            <li data-target="#mainBannerCarousel" data-slide-to="2"></li>
                            <li data-target="#mainBannerCarousel" data-slide-to="3"></li>
                        </ol>
                        <%-- ▼▼▼ [수정] 모든 인라인 스타일을 제거하고 CSS 클래스로 제어 ▼▼▼ --%>
                        <div class="carousel-inner">
                            <div class="carousel-item active">
                                <img src="<c:url value='/asset/images/banner1.jpg' />" class="d-block w-100" alt="배너 1">
                                <div class="carousel-caption d-none d-md-block text-left">
                                    <h2>지금 바로 떠나는 제주 여행</h2>
                                    <p>AI추천 루트를 이용해서 여행 계획을 세워보세요!</p>
                                </div>
                            </div>
                            <div class="carousel-item">
                                <img src="<c:url value='/asset/images/banner2.png' />" class="d-block w-100" alt="배너 2">
                                <div class="carousel-caption d-none d-md-block text-left">
                                    <h2>친구와 함께, 잊지 못할 추억</h2>
                                    <p>#동행찾기 #우정여행 #맛집탐방</p>
                                </div>
                            </div>
                            <div class="carousel-item">
                                <img src="<c:url value='/asset/images/banner3.jpg' />" class="d-block w-100" alt="배너 3">
                                <div class="carousel-caption d-none d-md-block text-left">
                                    <h2>도심 속 완벽한 휴식</h2>
                                    <p>지친 일상 속 휴식을 즐기세요!</p>
                                </div>
                            </div>
                            <div class="carousel-item">
                                <img src="<c:url value='/asset/images/banner4.png' />" class="d-block w-100" alt="배너 4">
                                <div class="carousel-caption d-none d-md-block text-left">
                                    <h2>여행 속 헬스케어</h2>
                                    <p>여행과 다이어트 둘 다 가능합니다. #헬스케어 #다이어트</p>
                                </div>
                            </div>
                        </div>
                        <a class="carousel-control-prev" href="#mainBannerCarousel" role="button" data-slide="prev">
                            <span class="carousel-control-prev-icon" aria-hidden="true"></span>
                            <span class="sr-only">Previous</span>
                        </a>
                        <a class="carousel-control-next" href="#mainBannerCarousel" role="button" data-slide="next">
                            <span class="carousel-control-next-icon" aria-hidden="true"></span>
                            <span class="sr-only">Next</span>
                        </a>
                    </div>
                </section>

                <c:if test="${not empty weatherList}">
                    <section class="weather-section">
                        <h2 class="weather-header">주요 여행지 날씨</h2>
                        <div class="weather-grid">
                            <c:forEach items="${weatherList}" var="weather">
                                <div class="weather-card">
                                    <div class="city">${weather.city}</div>
                                    <div class="icon">
                                        <c:choose>
                                            <c:when test="${weather.precipitationForm == '1' or weather.precipitationForm == '4'}"><i class="fa-solid fa-cloud-showers-heavy"></i></c:when>
                                            <c:when test="${weather.precipitationForm == '2' or weather.precipitationForm == '3'}"><i class="fa-solid fa-snowflake"></i></c:when>
                                            <c:otherwise>
                                                <c:choose>
                                                    <c:when test="${weather.skyCondition == '1'}"><i class="fa-solid fa-sun"></i></c:when>
                                                    <c:when test="${weather.skyCondition == '3'}"><i class="fa-solid fa-cloud-sun"></i></c:when>
                                                    <c:when test="${weather.skyCondition == '4'}"><i class="fa-solid fa-cloud"></i></c:when>
                                                    <c:otherwise><i class="fa-solid fa-temperature-half"></i></c:otherwise>
                                                </c:choose>
                                            </c:otherwise>
                                        </c:choose>
                                    </div>
                                    <div class="temp">${weather.temperature}<span>°C</span></div>
                                </div>
                            </c:forEach>
                        </div>
                    </section>
                </c:if>
            </div>

            <div class="main-right-sidebar">
                <div class="ranking-widget">
                    <div class="ranking-header">
                        <h3>인기 여행지 TOP 5</h3>
                        <span class="date"><fmt:formatDate value="<%= new java.util.Date() %>" pattern="MM.dd"/> 기준</span>
                    </div>
                    <ul class="ranking-list">
                        <c:forEach items="${rankingList}" var="item" varStatus="status">
                            <li class="ranking-item">
                                <span class="rank">${status.count}</span>
                                <span class="name">${item.category}</span>
                                <span class="change ${item.count > 0 ? 'up' : (item.count < 0 ? 'down' : 'none')}">
                                    <c:if test="${item.count != 0}">
                                        <i class="fa-solid ${item.count > 0 ? 'fa-caret-up' : 'fa-caret-down'}"></i>
                                        ${item.count > 0 ? item.count : -item.count}
                                    </c:if>
                                    <c:if test="${item.count == 0}">-</c:if>
                                </span>
                            </li>
                        </c:forEach>
                    </ul>
                </div>
                
                <section class="main-stats">
                    <div class="stats-grid">
                        <div class="stat-item">
                            <div class="icon"><i class="fa-solid fa-wand-magic-sparkles"></i></div>
                            <div class="stat-rating">
                                <span class="stars">
                                    <i class="fa-solid fa-star"></i><i class="fa-solid fa-star"></i><i class="fa-solid fa-star"></i><i class="fa-solid fa-star"></i><i class="fa-solid fa-star-half-stroke"></i>
                                </span>
                                <span class="score">4.8</span>
                            </div>
                            <span class="stat-label">AI 추천루트 만족도</span>
                        </div>
                        <div class="stat-item">
                            <div class="icon"><i class="fa-solid fa-comments"></i></div>
                            <span class="stat-value"><fmt:formatNumber value="${totalReviews}" pattern="#,###"/></span>
                            <span class="stat-label">여행 후기</span>
                        </div>
                    </div>
                </section>
            </div>
        </div>
    </main>
    
</body>
</html>