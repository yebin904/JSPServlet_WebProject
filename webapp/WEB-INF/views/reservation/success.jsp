<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8">
<title>예약 완료</title>
<link rel="stylesheet" href="${pageContext.request.contextPath}/asset/css/reservation.css">
</head>
<body>
  <div class="container">
    <h2>예약이 완료되었습니다 🎉</h2>

    <div class="info-box">
      <p>예약번호: <strong>${reservationId}</strong></p>
      <p>숙소명:
		  <c:choose>
		    <c:when test="${not empty placeName}">
		      <strong>${placeName}</strong>
		    </c:when>
		    <c:otherwise>
		      <strong>${roomName}</strong>
		    </c:otherwise>
		  </c:choose>
		</p>
      <p>차량명: <strong>${carName}</strong></p>
      <p>여행 기간: <strong>${startDate} ~ ${endDate}</strong></p>
      <p>인원수: <strong>${people}</strong></p>
      <p>총 결제 금액: <strong>₩<fmt:formatNumber value="${totalPrice}" pattern="#,###"/></strong></p>
    </div>

    <p style="margin-top: 25px; color: #555;">
      숙소와 차량 예약이 정상적으로 처리되었습니다.<br>
      차량을 선택하지 않았다면 숙소 예약만 진행됩니다.
    </p>

    <div class="btn-box">
      <form action="${pageContext.request.contextPath}/reservation/list.do" method="get">
        <button class="btn">내 예약 목록 보기</button>
      </form>
      <form action="${pageContext.request.contextPath}/main.do" method="get">
        <button class="btn btn-secondary">메인으로 돌아가기</button>
      </form>
    </div>
  </div>
</body>
</html>
