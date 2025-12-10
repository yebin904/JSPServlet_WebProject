<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>${place.place_name} - 상세 정보</title>
    <%@ include file="/WEB-INF/views/inc/asset.jsp" %>
    <style>
        body {
            padding-top: 70px; /* 헤더 높이 */
        }
        .detail-container {
            max-width: 900px;
            margin: 40px auto;
            padding: 0 20px;
        }
        .main-image {
            width: 100%;
            height: 450px;
            object-fit: cover;
            border-radius: 12px;
            margin-bottom: 30px;
        }
        .place-title {
            font-size: 2.5rem;
            font-weight: bold;
            margin-bottom: 10px;
        }
        .place-description {
            font-size: 1.2rem;
            color: #555;
            margin-bottom: 20px;
        }
        .place-address {
            font-size: 1rem;
            color: #888;
            margin-bottom: 40px;
        }
        .section-title {
            font-size: 1.8rem;
            font-weight: bold;
            margin-top: 60px;
            margin-bottom: 20px;
            border-bottom: 2px solid var(--primary-dark);
            padding-bottom: 10px;
        }
        
        /* 상세 정보 그리드 */
        .details-grid {
            display: grid;
            grid-template-columns: 1fr;
            gap: 25px;
            margin-top: 20px;
            line-height: 1.7;
        }
        .detail-item {
            display: flex;
        }
        .detail-item-title {
            font-weight: bold;
            color: #333;
            width: 100px;
            flex-shrink: 0;
        }
        .detail-item-content {
            color: #666;
            white-space: pre-wrap; /* 줄바꿈 적용 */
        }

        #map {
            width: 100%;
            height: 400px;
            border-radius: 12px;
        }
        .related-places-container {
            display: grid;
            grid-template-columns: repeat(auto-fill, minmax(200px, 1fr));
            gap: 20px;
            margin-top: 20px;
        }
        .related-card {
            border: 1px solid var(--border);
            border-radius: 12px;
            overflow: hidden;
            text-decoration: none;
            color: inherit;
            transition: transform 0.2s, box-shadow 0.2s;
        }
        .related-card:hover {
            transform: translateY(-5px);
            box-shadow: 0 4px 15px rgba(0,0,0,0.1);
        }
        .related-card img {
            width: 100%;
            height: 150px;
            object-fit: cover;
        }
        .related-card-title {
            padding: 15px;
            font-weight: bold;
            white-space: nowrap;
            overflow: hidden;
            text-overflow: ellipsis;
        }
    </style>
</head>
<body>
    <%@ include file="/WEB-INF/views/inc/header.jsp" %>

    <div class="detail-container">
        
        <!-- 기본 장소 정보 -->
        <c:if test="${not empty place}">
            <img src="${place.place_main_image_url}" alt="${place.place_name}" class="main-image" onerror="this.src='https://placehold.co/900x450/E9ECEF/495057?text=No+Image'">
            
            <h1 class="place-title">${place.place_name}</h1>
            <p class="place-description">${place.place_description}</p>
            <p class="place-address">${place.place_address}</p>
        </c:if>

        <!-- 관광지 상세 정보 -->
        <c:if test="${not empty touristSpot}">
            <h2 class="section-title">상세 정보</h2>
            <div class="details-grid">
                <c:if test="${not empty touristSpot.spot_overinfo}">
                    <div class="detail-item">
                        <span class="detail-item-title">개요</span>
                        <span class="detail-item-content">${touristSpot.spot_overinfo}</span>
                    </div>
                </c:if>
                <c:if test="${not empty touristSpot.opening_hours}">
                    <div class="detail-item">
                        <span class="detail-item-title">운영시간</span>
                        <span class="detail-item-content">${touristSpot.opening_hours}</span>
                    </div>
                </c:if>
                <c:if test="${not empty touristSpot.rest_day}">
                    <div class="detail-item">
                        <span class="detail-item-title">쉬는날</span>
                        <span class="detail-item-content">${touristSpot.rest_day}</span>
                    </div>
                </c:if>
                <c:if test="${not empty touristSpot.parking_info}">
                    <div class="detail-item">
                        <span class="detail-item-title">주차</span>
                        <span class="detail-item-content">${touristSpot.parking_info}</span>
                    </div>
                </c:if>
                <c:if test="${not empty touristSpot.contact_info}">
                    <div class="detail-item">
                        <span class="detail-item-title">문의</span>
                        <span class="detail-item-content">${touristSpot.contact_info}</span>
                    </div>
                </c:if>
                <c:if test="${not empty touristSpot.admission_fee}">
                    <div class="detail-item">
                        <span class="detail-item-title">이용요금</span>
                        <span class="detail-item-content">${touristSpot.admission_fee}</span>
                    </div>
                </c:if>
            </div>
        </c:if>

        <!-- 지도 -->
        <h2 class="section-title">위치 정보</h2>
        <div id="map"></div>

        <!-- 연관 관광지 -->
        <c:if test="${not empty relatedPlaces}">
            <h2 class="section-title">이곳과 비슷한 추천 장소</h2>
            <div class="related-places-container">
                <c:forEach items="${relatedPlaces}" var="related">
                    <a href="/trip/allplace/detail.do?place_id=${related.place_id}" class="related-card">
                        <img src="${related.place_main_image_url}" alt="${related.place_name}" onerror="this.src='https://placehold.co/200x150/E9ECEF/495057?text=No+Image'">
                        <div class="related-card-title">${related.place_name}</div>
                    </a>
                </c:forEach>
            </div>
        </c:if>

    </div>

    <script type="text/javascript" src="//dapi.kakao.com/v2/maps/sdk.js?appkey=09d09e9035bb509e8f002c6fab6b12ac"></script>
    <script>
        // Controller로부터 받은 장소 데이터가 있을 경우에만 지도 생성
        <c:if test="${not empty place}">
            document.addEventListener("DOMContentLoaded", function() {
                var mapContainer = document.getElementById('map'), // 지도를 표시할 div 
                    mapOption = {
                        center: new kakao.maps.LatLng(${place.place_lat}, ${place.place_lng}), // 지도의 중심좌표
                        level: 4 // 지도의 확대 레벨
                    };

                // 지도를 생성합니다    
                var map = new kakao.maps.Map(mapContainer, mapOption); 

                // 마커를 생성합니다
                var marker = new kakao.maps.Marker({
                    position: new kakao.maps.LatLng(${place.place_lat}, ${place.place_lng}),
                    title: '${place.place_name}'
                });

                // 마커가 지도 위에 표시되도록 설정합니다
                marker.setMap(map);
            });
        </c:if>
    </script>
</body>
</html>
