<%@page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="ko">
<head>
	<meta charset="UTF-8">
	<%@ include file="/WEB-INF/views/inc/asset.jsp" %>	
	
	<style>
		/* 라디오 버튼과 라벨 사이의 간격을 위한 간단한 스타일 */
		#main table td label + label {
			margin-left: 10px;
		}
	</style>
</head>
<body>
	<%@ include file="/WEB-INF/views/inc/header.jsp" %>
			<link rel="stylesheet" href="/trip/asset/css/pwsearchstyle.css">
	
	
	<div id="main">
		<h1>비밀번호 찾기</h1>
		
		<form method="POST" action="/trip/user/idsearch.do">
		<table class="borad-title">
			
			<tr>
				<th>아이디</th>
				<td><input type="text" name="id" id="id" required class="short"></td>
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
			
			
		</table>
		
		<div>
			<button type="button" class="btn" onclick="location.href='/trip/main.do';">돌아가기</button>
			<button type="button" class="btn" id="btnPwSearch">비밀번호 찾기</button>

		</div>
	</form>
	</div>
	
	

<script>

    
    
    
	
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
	
	$('#btnPwSearch').click(() => {

	    // 1. 이메일 인증을 완료했는지 먼저 확인합니다.
	    if (!isValid) {
	        alert('이메일 인증을 진행하세요.');
	        return; // AJAX 요청을 보내지 않고 함수를 종료합니다.
	    }

	    // 2. 서버로 아이디 찾기 AJAX 요청을 보냅니다.
	    $.ajax({
	        type: 'POST',
	        url: '/trip/user/pwsearch.do', // form의 action과 동일한 주소
	        data: {
	            id: $('#id').val(),
	            email: $('#email').val()
	        },
	        dataType: 'json',
	        success: function(result) {
	            
	            // 3. 서버로부터 받은 결과(result)에 따라 안내창을 띄웁니다.
	            if (result.result == 1) {
	                // 성공했을 때 띄울 안내창
	                alert('입력하신 이메일로 새 비밀번호를 발송했습니다. 메일을 확인해주세요.');
	                location.href = '/trip/user/login.do'; // 로그인 페이지로 이동
	            } else {
	                // 실패했을 때 띄울 안내창
	                alert('일치하는 회원 정보가 없습니다. 이메일과 아이디를 다시 확인해주세요.');
	            }
	        },
	        error: function(a, b, c) {
	            console.log(a, b, c);
	            alert('비밀번호를 찾는 중 오류가 발생했습니다.');
	        }
	    });

	});

</script>
</body>
</html>