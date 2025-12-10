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
		<h1>숙소 예약</h1>
		
		<table id="view" class="borad-table">
			<tr>
				<th>예약번호</th>
				<td>${dto.seq}</td>
			</tr>
			<tr>
				<th>숙소 유형</th>
				<td>${dto.accomtype}</td>
			</tr>
			<tr>
				<th>주소</th>
				<td>${dto.placeaddress}</td>
			</tr>
			<tr>
				<th>장소명</th>
				<td>${dto.placename}</td>
			</tr>
			<tr>
				<th>객실명</th>
				<td>${dto.roomname}</td>
			</tr>
			<tr>
				<th>숙박 인원</th>
				<td>${dto.guestcount}</td>
			</tr>
			<tr>
				<th>체크인</th>
				<td>${dto.checkindate}</td>
			</tr>
			<tr>
				<th>체크아웃</th>
				<td>${dto.checkoutdate}</td>
			</tr>
			<tr>
				<th>총비용</th>
				<td>${dto.roomtotalprice}</td>
			</tr>

		</table>
		
		<div>
			<button type="button" class="back" onclick="location.href='/trip/user/accomreservation.do';">돌아가기</button>
						<button type="button" class="back" id="btnCancel" onclick="cancel(${dto.accomseq});">예약취소하기</button>
</div>

	<script>
function cancel(accomseq) {
    
    // 1. 사용자에게 취소 여부를 물어보는 확인 창을 띄웁니다.
    if (confirm("숙소 예약을 취소하시겠습니까?")) {
        
        // '확인'을 눌렀을 경우에만 ajax 요청을 보냅니다.
        $.ajax({
            type: 'POST',
            url: '/trip/user/accomcancel.do',
            data: { accomseq: accomseq },
            success: function(result) {
            	// 2. 서버에서 성공적으로 취소 처리가 완료되면 실행됩니다.
                alert('예약이 성공적으로 취소되었습니다.'); // 사용자에게 완료 피드백
                
                // 3. '돌아가기' 버튼과 동일한 경로로 페이지를 이동시킵니다.
                location.href = '/trip/user/accomreservation.do'; 
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























