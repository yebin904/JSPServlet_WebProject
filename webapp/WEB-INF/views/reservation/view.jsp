<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8">
<title>예약 상세보기</title>
<link rel="stylesheet" href="${pageContext.request.contextPath}/asset/css/reservation.css">
</head>
<body>
<div class="container">
  <h2>예약 상세보기</h2>

  <div class="section">
    <h3>기본 정보</h3>
    <div class="detail">
      <p><strong>예약번호:</strong> ${dto.reservation_id}</p>
      <p><strong>상태:</strong> ${dto.status_name}</p>
      <p><strong>예약일:</strong> <fmt:formatDate value="${dto.reservation_regdate}" pattern="yyyy-MM-dd HH:mm:ss"/></p>
    </div>
  </div>

  <div class="section">
    <h3>숙소 정보</h3>
    <div class="detail">
      <p><strong>숙소명:</strong> ${dto.accom_name}</p>
      <p><strong>객실명:</strong> ${dto.room_name}</p>
      <p><strong>숙박 기간:</strong>
        <fmt:formatDate value="${dto.reservation_start_date}" pattern="yyyy-MM-dd"/>
        ~
        <fmt:formatDate value="${dto.reservation_end_date}" pattern="yyyy-MM-dd"/>
      </p>
      <p><strong>숙소 요금:</strong> ₩<fmt:formatNumber value="${dto.room_total_price}" pattern="#,###"/></p>
    </div>
    <div class="image-box">
      <img src="${dto.room_image_url}" alt="숙소 이미지">
    </div>
  </div>
  
  <div class="section">
  <h3>숙소 위치</h3>
  <div id="map" style="width:100%;height:400px;border-radius:10px;"></div>
</div>

<script type="text/javascript" src="//dapi.kakao.com/v2/maps/sdk.js?appkey=0e065a782a80bd99c2c88184e66bff5a"></script>
<script>
window.addEventListener('DOMContentLoaded', () => {
  const lat = ${dto.place_lat};
  const lng = ${dto.place_lng};
  
  const mapContainer = document.getElementById('map');
  const mapOption = { center: new kakao.maps.LatLng(lat, lng), level: 3 };

  const map = new kakao.maps.Map(mapContainer, mapOption);
  
  const marker = new kakao.maps.Marker({
    position: new kakao.maps.LatLng(lat, lng),
    map: map
  });

  const iwContent = `<div style="padding:6px 10px;">${dto.accom_name}</div>`;
  const infowindow = new kakao.maps.InfoWindow({
    content: iwContent
  });
  infowindow.open(map, marker);
});
</script>
  

  <div class="section">
    <h3>차량 정보</h3>
    <c:choose>
      <c:when test="${dto.car_name != null}">
        <div class="detail">
          <p><strong>차량명:</strong> ${dto.car_name}</p>
          <p><strong>차종:</strong> ${dto.car_type}</p>
          <p><strong>연료:</strong> ${dto.car_fuel_type}</p>
          <p><strong>차량 요금:</strong> ₩<fmt:formatNumber value="${dto.car_total_price}" pattern="#,###"/></p>
        </div>
        <div class="image-box">
          <img src="${dto.car_image_url}" alt="차량 이미지">
        </div>
      </c:when>
      <c:otherwise>
        <div class="detail"><p>차량을 선택하지 않았습니다.</p></div>
      </c:otherwise>
    </c:choose>
  </div>

  <div class="section">
    <h3>총 결제 금액</h3>
    <p style="font-size:18px; font-weight:bold;">₩<fmt:formatNumber value="${dto.reservation_price}" pattern="#,###"/></p>
  </div>

  <button class="btn" onclick="location.href='${pageContext.request.contextPath}/reservation/list.do'">목록으로</button>
</div>
</body>
</html>
