<%@page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="ko">
<head>
	<meta charset="UTF-8">
	<%@ include file="/WEB-INF/views/inc/asset.jsp" %>	
		<link rel="stylesheet" href="/trip/asset/css/registerstyle.css">
	
	
	<style>
		/* 라디오 버튼과 라벨 사이의 간격을 위한 간단한 스타일 */
		#main table td label + label {
			margin-left: 10px;
		}
	</style>
</head>
<body>
	<%@ include file="/WEB-INF/views/inc/header.jsp" %>
	
	<div id="main">
		<h1>회원 <small>가입하기</small></h1>
		
		<form method="POST" action="/trip/user/register.do" enctype="multipart/form-data">
		<table class="borad-title">
			<tr>
				<th>아이디</th>
				<td><input type="text" name="id" id="id" required class="short"></td>
			</tr>
			<tr>
				<th>암호</th>
				<td><input type="password" name="pw" id="pw" required class="short"></td>
			</tr>
			<tr>
				<th>이름</th>
				<td><input type="text" name="name" id="name" required class="short"></td>
			</tr>
			<tr>
				<th>주민등록번호</th>
				<td>
				<div>
					<input type="text" name="ssn" id="ssn" required class="long">
				</div>
				</td>
			</tr>
			<tr>
				<th>이메일</th>
				<td>
					<div>
						<input type="email" name="email" id="email" required class="long">
						<input type="button" value="인증 메일 보내기" id="btnMail">
					</div>
					<div style="margin-top: 10px;">
						<input type="text" id="validNumber" class="short" disabled maxlength="5">
						<input type="button" value="입력하기" id="btnValid" disabled>
						<span id="remainTime" style="display: none;">05:00</span>
					</div>
				</td>
			</tr>
			
			<tr>
				<th>전화번호</th>
				<td><input type="text" name="phoneNumber" id="phoneNumber" class="long"></td>
			</tr>
			<tr>
				<th>닉네임</th>
			<td><input type="text" name="nickName" id="nickName" class="short"></td>			
			</tr>
			
			<!-- <tr>
				<th>주소</th>
				<td><input type="text" name="address" id="address" class="long"></td>
			</tr> -->
			
			<tr>
    <th>주소</th>
    <td>
        <input type="text" name="address" id="address" placeholder="주소 검색 버튼을 눌러주세요." readonly class="long">
        <button type="button" id="btn-address-search" class="btn-primary">주소 검색</button>
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
				<td><input type="number" name="height" id="height" class="short" min="0" placeholder="숫자만 입력">cm</td>
			</tr>
			
			<tr>
				<th>몸무게</th>
				<td><input type="number" name="weight" id="weight" class="short" min="0" placeholder="숫자만 입력">kg</td>
			</tr>

			<tr>
				<th>건강목표</th>
				<td><input type="text" name="healthGoals" id="healthGoals" class="long"></td>
			</tr>
		</table>
		
		<div>
			<button type="button" class="btn" onclick="location.href='/trip/main.do';">돌아가기</button>
			<button type="submit" class="btn">가입하기</button>
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
    
    
    
    
	
	let timer = 0;

	$('#btnMail').click(() => {
		
		if ($('#email').val().trim() != '') {
			
			$.ajax({
				type: 'POST',
				url: '/trip/user/sendmail.do',
				data: {
					email: $('#email').val().trim()
				},
				dataType: 'json',
				success: function(result) {
					
					if (result.result > 0) {
						
						//alert('성공');
						$('#validNumber').prop('disabled', false);
						$('#btnValid').prop('disabled', false);
						$('#remainTime').show();
						
						//타이머 동작
						const remainTime = new Date();
						remainTime.setMinutes(0);
						remainTime.setSeconds(300);
						
						timer = setInterval(() => {
							
							remainTime.setSeconds(remainTime.getSeconds() - 1);
							$('#remainTime').text(
								String(remainTime.getMinutes()).padStart(2, '0')
								+ ':'
								+ String(remainTime.getSeconds()).padStart(2, '0')
							);
							
							if ($('#remainTime').text() == '00:00') {
								
								
								//인증 시간 만료
								$.ajax({
									type: 'POST',
									url: '/trip/user/delmail.do',
									dataType: 'json',
									success: function(result) {
																					
										if (result.result > 0) {
											
											$('#validNumber').val('');
											$('#btnValid').prop('disabled', true);
											$('#validNumber').prop('disabled', true);
											$('#remainTime').hide();
											
											clearInterval(timer);
											timer = 0;
											
										}
										
									},
									error: function(a,b,c) {
										console.log(a,b,c);
									}
								});
								
							}
					
						}, 1000);
						
					} else {
						alert('인증 메일 발송에 실패했습니다.');
					}
					
				},
				error: function(a,b,c) {
					console.log(a,b,c);
				}
			});
			
		} else {
			alert('이메일을 입력하세요.');
		}		
		
	});
	
	$('#btnValid').click(() => {
		
		$.ajax({
			type: 'POST',
			url: '/trip/user/validmail.do',
			data: {
				validNumber: $('#validNumber').val()
			},
			dataType: 'json',
			success: function(result) {
				
				if (result.result > 0) {
					
					 alert('인증에 성공했습니다.');
		                isValid = true;
		                
		                clearInterval(timer);
		                $('#remainTime').hide();
		                $('#validNumber').prop('disabled', true); 
		                $('#btnValid').prop('disabled', true);
					
				} else {
					alert('인증 번호가 틀립니다.');
				}
				
			},
			error: function(a,b,c) {
				console.log(a,b,c);
			}
		});
		
	});
	
	let isValid = false;
	
	$('form').submit(() => {
		
		if (!isValid) {
			alert('이메일 인증을 진행하세요.');
			event.preventDefault();
			return false;
		}
		
	});

</script>
</body>
</html>