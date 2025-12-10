<%-- 파일 경로: /WEB-INF/views/admin/accomedit.jsp --%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>숙소 정보 수정</title>
<%@ include file="/WEB-INF/views/inc/admin_asset.jsp" %>
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.2/css/all.min.css"/>
</head>
<body>
    <div id="admin-container">
        <%@ include file="/WEB-INF/views/inc/admin_header.jsp" %>
        <main id="admin-main">
            <h1><i class="fa-solid fa-pen-to-square"></i> 숙소 정보 수정</h1>
            
            <div class="form-container">
                 <%-- ★★★★★ 핵심 수정 1: onsubmit 이벤트 추가 ★★★★★ --%>
                <form method="POST" action="${pageContext.request.contextPath}/admin/accom/edit.do" onsubmit="return removeCommasBeforeSubmit(this)">
                    
                    <input type="hidden" name="placeId" value="${dto.placeId}">
                    <input type="hidden" name="roomId" value="${dto.roomId}">

                    <h3># 숙소 정보</h3>
                    <div class="form-group">
                        <label for="placeName">숙소명</label>
                        <input type="text" id="placeName" name="placeName" required value="${dto.placeName}">
                    </div>
                    <div class="form-group">
                        <label for="accomType">숙소 유형</label>
                        <select id="accomType" name="accomType">
                            <option value="호텔" <c:if test="${dto.accomType == '호텔'}">selected</c:if>>호텔</option>
                            <option value="민박" <c:if test="${dto.accomType == '민박'}">selected</c:if>>민박</option>
                            <option value="펜션" <c:if test="${dto.accomType == '펜션'}">selected</c:if>>펜션</option>
                            <option value="캠핑" <c:if test="${dto.accomType == '캠핑'}">selected</c:if>>캠핑</option>
                            <option value="모텔" <c:if test="${dto.accomType == '모텔'}">selected</c:if>>모텔</option>
                            <option value="풀빌라" <c:if test="${dto.accomType == '풀빌라'}">selected</c:if>>풀빌라</option>
                        </select>
                    </div>
                    <div class="form-group">
                        <label for="placeAddress">주소</label>
                        <input type="text" id="placeAddress" name="placeAddress" required value="${dto.placeAddress}">
                    </div>

                    <h3># 객실 정보</h3>
                     <div class="form-group">
                        <label for="roomName">객실명</label>
                        <input type="text" id="roomName" name="roomName" required value="${dto.roomName}">
                    </div>
                     <div class="form-row">
                        <div class="form-group">
                            <label for="capacity">수용인원 (명)</label>
                            <input type="number" id="capacity" name="capacity" required min="1" value="${dto.capacity}">
                        </div>
                        <div class="form-group">
                             <label for="pricePerNight">1박 요금 (원)</label>
                             <%-- ★★★★★ 핵심 수정 2: type, id, onkeyup 속성 변경 및 추가 ★★★★★ --%>
                            <input type="text" id="pricePerNight" name="pricePerNight" required value="${dto.pricePerNight}" onkeyup="formatPrice(this)">
                        </div>
                    </div>
                     
                    <div class="button-container">
                        <button type="button" class="btn" onclick="location.href='${pageContext.request.contextPath}/admin/accom/list.do'">취소</button>
                        <button type="submit" class="btn primary">수정하기</button>
                     </div>
                </form>
            </div>
        </main>
    </div>

    <%-- ★★★★★ 핵심 수정 3: 페이지 하단에 스크립트 추가 ★★★★★ --%>
    <script>
        // 1. 가격 입력 필드에 자동으로 콤마(,)를 삽입하는 함수
        function formatPrice(input) {
            let value = input.value.replace(/[^\d]/g, ''); // 숫자 이외의 문자 제거
            if (value === '') {
                input.value = '';
                return;
            }
            input.value = Number(value).toLocaleString('en-US'); // 3자리마다 콤마 추가
        }
    
        // 2. 폼을 제출하기 직전에 가격에서 콤마(,)를 제거하는 함수
        function removeCommasBeforeSubmit(form) {
            const priceInput = form.querySelector('#pricePerNight');
            if (priceInput) {
                priceInput.value = priceInput.value.replace(/,/g, ''); // 모든 콤마 제거
            }
            return true; // 폼 제출 진행
        }

        // 3. 페이지가 처음 로드될 때, 기존 가격 값에 콤마를 적용
        document.addEventListener('DOMContentLoaded', function() {
            const priceInput = document.getElementById('pricePerNight');
            if (priceInput && priceInput.value) {
                formatPrice(priceInput);
            }
        });
    </script>
</body>
</html>