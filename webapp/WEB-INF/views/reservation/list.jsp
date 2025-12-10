<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8">
<title>내 예약 목록</title>
<link rel="stylesheet" href="${pageContext.request.contextPath}/asset/css/reservation.css">
</head>
<body>
<div class="container">
  <h2>내 예약 목록</h2>

  <c:choose>
    <c:when test="${empty reservationList}">
      <div class="empty">예약 내역이 없습니다.</div>
    </c:when>
    <c:otherwise>
      <table>
        <thead>
          <tr>
            <th>예약번호</th>
            <th>숙소명</th>
            <th>차량명</th>
            <th>체크인</th>
            <th>체크아웃</th>
            <th>금액</th>
            <th>상태</th>
          </tr>
        </thead>
        <tbody>
          <c:forEach var="r" items="${reservationList}">
            <tr onclick="location.href='${pageContext.request.contextPath}/reservation/view.do?seq=${r.reservation_id}'">
              <td>${r.reservation_id}</td>
              <td>${r.room_name != null ? r.room_name : '-'}</td>
              <td>${r.car_name != null ? r.car_name : '-'}</td>
              <td><fmt:formatDate value="${r.checkin_date}" pattern="yyyy-MM-dd"/></td>
              <td><fmt:formatDate value="${r.checkout_date}" pattern="yyyy-MM-dd"/></td>
              <td>₩<fmt:formatNumber value="${r.reservation_price}" pattern="#,###"/></td>
              <td class="status ${r.status_name}">${r.status_name}</td>
            </tr>
          </c:forEach>
        </tbody>
      </table>
    </c:otherwise>
  </c:choose>
</div>
<form action="${pageContext.request.contextPath}/main.do" method="get">
        <button class="btn btn-secondary">메인으로 돌아가기</button>
      </form>
</body>
</html>
