<%@page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>     
<!DOCTYPE html>
<html lang="ko">
<head>
	<meta charset="UTF-8">
	<%@ include file="/WEB-INF/views/inc/asset.jsp" %>
	<link rel="stylesheet" href="/trip/asset/css/reservationviewstyle.css">
	
</head>
<body>
	<!-- view.jsp -->
	<%@ include file="/WEB-INF/views/inc/header.jsp" %>
	<nav class="board-sub-header">
    <div class="sub-header-inner">
        <a href="/trip/user/carreservation.do">렌트카 예약</a>
        <a href="/trip/user/accomreservation.do">숙소 예약</a>
    </div>
</nav>
	
	<div id="main">
		<h1>차량 예약</h1>
		
		<table id="view" class="borad-table">
			<tr>
				<th>예약번호</th>
				<td>${dto.seq}</td>
			</tr>
			<tr>
				<th>차종</th>
				<td>${dto.cartype}</td>
			</tr>
			<tr>
				<th>차량 모델명</th>
				<td>${dto.carname}</td>
			</tr>
			<tr>
				<th>유종</th>
				<td>${dto.carfueltype}</td>
			</tr>
			<tr>
				<th>차량 대여일</th>
				<td>${dto.pickupdate}</td>
			</tr>
			<tr>
				<th>차량 반납일</th>
				<td>${dto.dropoffdate}</td>
			</tr>
			<tr>
				<th>차량 픽업 장소</th>
				<td>${dto.pickuplocation}</td>
			</tr>
			<tr>
				<th>차량 반납 장소</th>
				<td>${dto.dropofflocation}</td>
			</tr>
			<tr>
				<th>총비용</th>
				<td>${dto.cartotalprice}</td>
			</tr>
			<tr>
				<th>요청사항</th>
				<td>${dto.carnotes}</td>
			</tr>

		</table>
		
		<div>
		<button type="button" class="back" onclick="location.href='/trip/user/carreservation.do';">돌아가기</button>
						<button type="button" class="back" id="btnCancel" onclick="cancel(${dto.carseq});">예약취소하기</button>
</div>

	<script>
function cancel(carseq) {
    
    // 1. 사용자에게 취소 여부를 물어보는 확인 창을 띄웁니다.
    if (confirm("랜트카 예약을 취소하시겠습니까?")) {
        
        // '확인'을 눌렀을 경우에만 ajax 요청을 보냅니다.
        $.ajax({
            type: 'POST',
            url: '/trip/user/carcancel.do',
            data: { carseq: carseq },
            success: function(result) {
                // 2. 서버에서 성공적으로 취소 처리가 완료되면 실행됩니다.
                alert('예약이 성공적으로 취소되었습니다.'); // 사용자에게 완료 피드백
                
                // 3. '돌아가기' 버튼과 동일한 경로로 페이지를 이동시킵니다.
                location.href = '/trip/user/carreservation.do'; 
            },
            error: function(a, b, c) {
                console.log(a, b, c);
                alert('요청 처리 중 오류가 발생했습니다.'); // 실패 시 피드백
            }
        });
    }
    // '취소'를 누르면 아무 동작도 하지 않습니다.
}
</script>
		
</body>
</html>























