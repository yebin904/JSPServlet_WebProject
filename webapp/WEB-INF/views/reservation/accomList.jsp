<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8">
<title>숙소 선택</title>
<link rel="stylesheet" href="${pageContext.request.contextPath}/asset/css/reservation.css">
</head>

<body>

<div class="accom-container">

  <!-- ✅ 좌측 필터 -->
  <form class="accom-sidebar" method="get" action="${pageContext.request.contextPath}/reservation/accomList.do">
    <input type="hidden" name="region" value="${region}" />

    <div class="accom-filter">
      <h4>숙소 유형</h4>
      <div class="filter-options">
        <c:forEach var="type" items="${fn:split('호텔,모텔,펜션,리조트,게스트하우스', ',')}">
          <label>
            <input type="checkbox" name="accom_type" value="${type}"
              <c:if test="${paramValues.accom_type != null && fn:contains(fn:join(paramValues.accom_type, ','), type)}">checked</c:if>>
            ${type}
          </label>
        </c:forEach>
      </div>
    </div>

    <div class="accom-filter">
      <h4>객실 유형</h4>
      <div class="filter-options">
        <c:forEach var="rType" items="${fn:split('스탠다드,더블룸,트윈룸,스위트룸', ',')}">
          <label>
            <input type="checkbox" name="room_type" value="${rType}"
              <c:if test="${paramValues.room_type != null && fn:contains(fn:join(paramValues.room_type, ','), rType)}">checked</c:if>>
            ${rType}
          </label>
        </c:forEach>
      </div>
    </div>

    <div class="accom-filter">
      <h4>수용 인원</h4>
      <div class="filter-options">
        <c:forEach var="cap" items="${fn:split('2,4,6,8', ',')}">
          <label>
            <input type="checkbox" name="capacity" value="${cap}"
              <c:if test="${paramValues.capacity != null && fn:contains(fn:join(paramValues.capacity, ','), cap)}">checked</c:if>>
            ${cap}인
          </label>
        </c:forEach>
      </div>
    </div>

    <div class="filter-actions">
      <button type="submit" class="reset-btn">필터 적용</button>
      <button type="button" class="reset-btn" onclick="resetFilters()">필터 초기화 ↻</button>
    </div>
  </form>

  <!-- ✅ 숙소 목록 -->
<div class="accom-content">
  <div class="page-title">
    <h2>숙소 선택 <span>(${region})</span></h2>
  </div>

    <c:choose>
      <c:when test="${empty list}">
        <div class="no-result">
          <p>조건에 맞는 숙소가 없습니다.</p>
        </div>
      </c:when>

      <c:otherwise>
        <div class="room-list">
          <c:forEach var="room" items="${list}">
            <div class="accom-card">
              <div class="accom-thumb">
                <img src="${pageContext.request.contextPath}/asset/img/room/${room.room_image_url}"
                     alt="${room.room_name}"
                     onerror="this.src='${pageContext.request.contextPath}/asset/img/room/default-room.jpg'">
              </div>

              <div class="accom-info">
                <h3>${room.place_name}</h3>
                <p>${room.room_name} (${room.accom_type} · ${room.room_type})</p>
                <p>최대 ${room.capacity}인 · 
                   <span class="accom-status ${room.room_status == 'y' ? 'available' : 'unavailable'}">
                     ${room.room_status == 'y' ? '예약 가능' : '예약 마감'}
                   </span>
                </p>
                <p class="price">₩<fmt:formatNumber value="${room.price_per_night}" pattern="#,###"/> /박</p>

                <form method="get" action="${pageContext.request.contextPath}/reservation/carList.do">
                  <input type="hidden" name="room_id" value="${room.room_id}">
                  <input type="hidden" name="region" value="${region}">
                  <input type="hidden" name="start_date" value="${start_date}">
                  <input type="hidden" name="end_date" value="${end_date}">
                  <input type="hidden" name="people" value="${people}">
                  <button type="submit" class="accom-btn">예약하기</button>
                </form>
              </div>
            </div>
          </c:forEach>
        </div>
      </c:otherwise>
    </c:choose>
  </div>
</div>

<script>
function resetFilters() {
  document.querySelectorAll('.accom-sidebar input[type=checkbox]').forEach(cb => cb.checked = false);
}
</script>

</body>
</html>
