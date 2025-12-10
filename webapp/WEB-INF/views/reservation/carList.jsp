<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8">
<title>차량 선택</title>
<link rel="stylesheet" href="${pageContext.request.contextPath}/asset/css/reservation.css">
</head>

<body>

<div class="car-container">

  <!-- ✅ 왼쪽 사이드바 -->
  <div class="car-sidebar">

    <!-- 🚗 필터 폼 -->
    <form method="get" action="${pageContext.request.contextPath}/reservation/carList.do">
      <input type="hidden" name="region" value="${region}" />

      <div class="filter-section">
        <h4>차종</h4>
        <div class="filter-options">
          <c:forEach var="type" items="${fn:split('세단,SUV,승합차,경차,수입차', ',')}">
            <label>
              <input type="checkbox" name="car_type" value="${type}"
                <c:if test="${paramValues.car_type != null && fn:contains(fn:join(paramValues.car_type, ','), type)}">checked</c:if>>
              ${type}
            </label>
          </c:forEach>
        </div>
      </div>

      <div class="filter-section">
        <h4>인승</h4>
        <div class="filter-options">
          <c:forEach var="seat" items="${fn:split('4,5,7,9', ',')}">
            <label>
              <input type="checkbox" name="car_seats" value="${seat}"
                <c:if test="${paramValues.car_seats != null && fn:contains(fn:join(paramValues.car_seats, ','), seat)}">checked</c:if>>
              ${seat}인승
            </label>
          </c:forEach>
        </div>
      </div>

      <div class="filter-section">
        <h4>연료</h4>
        <div class="filter-options">
          <c:forEach var="fuel" items="${fn:split('휘발유,디젤,LPG,전기', ',')}">
            <label>
              <input type="checkbox" name="car_fuel_type" value="${fuel}"
                <c:if test="${paramValues.car_fuel_type != null && fn:contains(fn:join(paramValues.car_fuel_type, ','), fuel)}">checked</c:if>>
              ${fuel}
            </label>
          </c:forEach>
        </div>
      </div>

      <div class="filter-actions">
        <button type="submit" class="reset-btn">필터 적용</button>
        <button type="button" class="reset-btn" onclick="resetFilters()">필터 초기화 ↻</button>
      </div>
    </form>

    <!-- 🚙 차량 선택 안 함 폼 -->
    <form action="${pageContext.request.contextPath}/reservation/confirm.do" method="get" class="no-car-form">
      <input type="hidden" name="region" value="${region}">
      <input type="hidden" name="room_id" value="${room_id}">
      <input type="hidden" name="car_id" value="0">
      <input type="hidden" name="start_date" value="${start_date}">
      <input type="hidden" name="end_date" value="${end_date}">
      <input type="hidden" name="people" value="${people}">
      <button type="submit" class="no-car-btn">🚗 차량 선택 안 함</button>
    </form>

  </div>

  <!-- ✅ 오른쪽 차량 목록 -->
  <div class="car-content">
    <div class="page-title">
      <h2>차량 선택 <span>(${region})</span></h2>
    </div>

    <c:choose>
      <c:when test="${empty list}">
        <div class="no-result">
          <p>조건에 맞는 차량이 없습니다.</p>
        </div>
      </c:when>

      <c:otherwise>
        <c:forEach var="car" items="${list}">
          <div class="car-card">
            <div class="car-thumb">
              <img src="${pageContext.request.contextPath}/asset/img/car/${car.car_image_url}"
                   alt="${car.car_name}"
                   onerror="this.src='${pageContext.request.contextPath}/asset/img/car/default-car.jpg'">
            </div>

            <div class="car-info">
              <h3>${car.car_name}</h3>
              <p>${car.car_type} · ${car.car_fuel_type} · ${car.car_seats}인승</p>
              <p class="price">₩<fmt:formatNumber value="${car.car_price_per_day}" pattern="#,###"/> /일</p>
            </div>

            <div class="car-actions">
              <form action="${pageContext.request.contextPath}/reservation/confirm.do" method="get">
                <input type="hidden" name="car_id" value="${car.car_id}">
                <input type="hidden" name="room_id" value="${room_id}">
                <input type="hidden" name="region" value="${region}">
                <input type="hidden" name="start_date" value="${start_date}">
                <input type="hidden" name="end_date" value="${end_date}">
                <input type="hidden" name="people" value="${people}">
                <button type="submit" class="reserve-btn">선택하기</button>
              </form>
            </div>
          </div>
        </c:forEach>
      </c:otherwise>
    </c:choose>
  </div>

</div>

<script>
function resetFilters() {
  document.querySelectorAll('.car-sidebar input[type=checkbox]').forEach(cb => cb.checked = false);
}
</script>

</body>
</html>
