<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8">
<title>예약 확인</title>
<link rel="stylesheet" href="${pageContext.request.contextPath}/asset/css/reservation.css">
</head>

<body>
<div class="confirm-container">
  <h2 class="confirm-title">예약 확인</h2>

  <!-- 숙소 정보 -->
  <div class="confirm-section">
    <h3>숙소 정보</h3>
    <c:choose>
      <c:when test="${room != null}">
        <div class="confirm-info">
          <p><strong>숙소명:</strong> ${room.place_name} ${room.room_name}</p>
          <p><strong>요금:</strong> ₩<fmt:formatNumber value="${room.price_per_night}" pattern="#,###"/> / 박</p>
        </div>
        
        <div class="confirm-image-box">
          	<img src="${pageContext.request.contextPath}/asset/img/room/${room.room_image_url}"
               alt="${room.room_name}"
               onerror="this.src='${pageContext.request.contextPath}/asset/img/room/default-room.jpg'">
        </div>
        
      </c:when>
      <c:otherwise>
        <div class="detail"><p>숙소 정보를 불러올 수 없습니다.</p></div>
      </c:otherwise>
    </c:choose>
  </div>

  <!-- 차량 정보 -->
  <div class="confirm-section">
    <h3>차량 정보</h3>
    <c:choose>
      <c:when test="${car != null}">
        <div class="confirm-info">
          <p><strong>차량명:</strong> ${car.car_name}</p>
          <p><strong>차종:</strong> ${car.car_type}</p>
          <p><strong>연료:</strong> ${car.car_fuel_type}</p>
          <p><strong>요금:</strong> ₩<fmt:formatNumber value="${car.car_price_per_day}" pattern="#,###"/> / 일</p>
          <div class="confirm-image-box">
          	<img src="${pageContext.request.contextPath}/asset/img/car/${car.car_image_url}"
               alt="${car.car_name}"
               onerror="this.src='${pageContext.request.contextPath}/asset/img/car/default-car.jpg'">
          </div>
        </div>
        
      </c:when>
      <c:otherwise>
        <div class="detail"><p>차량을 선택하지 않았습니다.</p></div>
      </c:otherwise>
    </c:choose>
  </div>

  <!-- 일정 -->
    <h3 style="color:#517D6F;">이용 일정</h3>
    <div class="schedule-box">
      <p><strong>여행 시작:</strong> ${start_date != null ? start_date : '정보 없음'}</p>
      <p><strong>여행 종료:</strong> ${end_date != null ? end_date : '정보 없음'}</p>
      <p><strong>인원수:</strong> ${people != null ? people : '0'}명</p>
    </div>


  <!-- 합계 -->
  <p class="total-price">총 예상 금액: <span id="totalPrice">계산 중...</span></p>
  

  <form method="post" action="${pageContext.request.contextPath}/reservation/insert.do">
    <input type="hidden" name="room_id" value="${room != null ? room.room_id : ''}">
    <input type="hidden" name="car_id" value="${car != null ? car.car_id : ''}">
    <input type="hidden" name="start_date" value="${start_date}">
    <input type="hidden" name="end_date" value="${end_date}">
    <input type="hidden" name="user_route_id" value="${user_route_id}">
    <input type="hidden" id="total_price" name="total_price" value="0">
    <button type="submit" class="confirm-btn">예약 확정하기</button>
</form>
</div>

<script>
function getDateDiff(start, end) {
  const s = new Date(start);
  const e = new Date(end);
  if (isNaN(s) || isNaN(e)) return 1; // 날짜가 비정상일 경우 1박 처리
  const diff = e - s;
  return Math.max(1, Math.ceil(diff / (1000 * 60 * 60 * 24)));
}

window.addEventListener('DOMContentLoaded', () => {
  const start = "${start_date}";
  const end = "${end_date}";
  const pricePerNight = Number("${room != null ? room.price_per_night : 0}") || 0;
  const carPricePerDay = Number("${car != null ? car.car_price_per_day : 0}") || 0;

  // NaN 방지용
  const validStart = start && start !== 'null' && start !== '' ? start : "2025-10-20";
  const validEnd = end && end !== 'null' && end !== '' ? end : "2025-10-23";

  const days = getDateDiff(validStart, validEnd);
  const total = (pricePerNight + carPricePerDay) * days;

  document.getElementById("totalPrice").innerText = "₩" + total.toLocaleString();
  document.getElementById("total_price").value = total;
});
</script>
</body>
</html>
