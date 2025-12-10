<%@page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="ko">
<head>
	<meta charset="UTF-8">
	<%@ include file="/WEB-INF/views/inc/asset.jsp" %>
	<link rel="stylesheet" href="/trip/asset/css/usereditstyle.css">
	
	</style>
</head>
<body>
	<%@ include file="/WEB-INF/views/inc/header.jsp" %>
	<div id="main">
		<h1>개인정보 <small>수정하기</small></h1>
		
		<form method="POST" action="/trip/user/useredit.do">
		<table>
			<tr>
				<th>아이디</th>
				<td><input type="text" name="id" id="id" required class="short" value="${dto.id}"></td>
			</tr>
			<tr>
				<th>암호</th>
				<td><input type="password" name="pw" id="pw" required class="short"></td>
			</tr>
			<tr>
				<th>이름</th>
				<td><input type="text" name="name" id="name" required class="short" value="${dto.name}"></td>
			</tr>
			<tr>
				<th>이메일</th>
				<td>
				<div>
					<input type="email" name="email" id="email" required class="long" value="${dto.email}">
				</div>
				</td>
			</tr>
			
			<tr>
				<th>전화번호</th>
				<td><input type="text" name="phoneNumber" id="phoneNumber" class="long" value="${dto.phoneNumber}"></td>
			</tr>
			<tr>
				<th>닉네임</th>
			<td><input type="text" name="nickName" id="nickName" class="short" value="${dto.nickName}"></td>			
			</tr>
			
			<tr>
    <th>주소</th>
    <td>
        <input type="text" name="address" id="address" placeholder="주소 검색 버튼을 눌러주세요." readonly class="long">
        <button type="button" id="btn-address-search" class="btn">주소 검색</button>
    </td>
</tr>
			
			<tr>
				<th>성별</th>
				<td>
					<label><input type="radio" name="gender" value="m" required> 남자</label>
					<label><input type="radio" name="gender" value="f"> 여자</label>
				</td>
			</tr>


			<tr>
				<th>키</th>
				<td><input type="number" name="height" id="height" class="short" min="0" placeholder="숫자만 입력" value="${dto.height}">cm</td>
			</tr>
			
			<tr>
				<th>몸무게</th>
				<td><input type="number" name="weight" id="weight" class="short" min="0" placeholder="숫자만 입력" value="${dto.weight}">kg</td>
			</tr>

			<tr>
				<th>건강목표</th>
				<td><input type="text" name="healthGoals" id="healthGoals" class="long" value="${dto.healthGoals}"></td>
			</tr>
		</table>
		<div>
			<button type="button" class="back" onclick="location.href='/trip/user/userinfo.do';">돌아가기</button>
			<button type="submit" class="edit primary">수정하기</button>
		</div>
		</form>
	</div>
	
	
	
	<script src="//t1.daumcdn.net/mapjsapi/bundle/postcode/prod/postcode.v2.js"></script>

<script>
    // '주소 검색' 버튼 클릭 이벤트
    document.getElementById('btn-address-search').addEventListener('click', function() {
        new daum.Postcode({
            oncomplete: function(data) {
                let addr = ''; // 주소 변수

                // 사용자가 도로명 주소를 선택했을 경우
                if (data.userSelectedType === 'R') {
                    addr = data.roadAddress;
                } else { // 사용자가 지번 주소를 선택했을 경우
                    addr = data.jibunAddress;
                }

                // 검색된 주소를 '주소' input에 넣기
                document.getElementById("address").value = addr;
            }
        }).open();
    });
</script>
</body>
</html>