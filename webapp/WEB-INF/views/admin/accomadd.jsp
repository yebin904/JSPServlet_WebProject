<%-- 파일 경로: /WEB-INF/views/admin/accomadd.jsp --%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>신규 숙소 등록</title>
<%@ include file="/WEB-INF/views/inc/admin_asset.jsp" %>
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.2/css/all.min.css"/>
</head>
<body>
    <div id="admin-container">
        <%@ include file="/WEB-INF/views/inc/admin_header.jsp" %>
        <main id="admin-main">
            <h1><i class="fa-solid fa-plus"></i> 신규 숙소 등록</h1>
            
            <div class="form-container">
                <%-- ★★★★★ 핵심 수정 1: onsubmit 이벤트 추가 ★★★★★ --%>
                <form method="POST" action="${pageContext.request.contextPath}/admin/accom/add.do" onsubmit="return removeCommasBeforeSubmit(this)">
                     
                    <h3># 숙소 정보</h3>
                    <div class="form-group">
                        <label for="placeName">숙소명</label>
                        <input type="text" id="placeName" name="placeName" required>
                    </div>
                    <div class="form-group">
                        <label for="accomType">숙소 유형</label>
                        <select id="accomType" name="accomType">
                            <option value="호텔">호텔</option>
                            <option value="민박">민박</option>
                            <option value="펜션">펜션</option>
                            <option value="캠핑">캠핑</option>
                            <option value="모텔">모텔</option>
                            <option value="풀빌라">풀빌라</option>
                        </select>
                    </div>
                    <div class="form-group">
                        <label for="placeAddress">주소</label>
                        <input type="text" id="placeAddress" name="placeAddress" required>
                    </div>

                    <h3># 객실 정보 (기본 1개)</h3>
                     <div class="form-group">
                        <label for="roomName">객실명</label>
                        <input type="text" id="roomName" name="roomName" required>
                    </div>
                     <div class="form-row">
                        <div class="form-group">
                            <label for="capacity">수용인원 (명)</label>
                            <input type="number" id="capacity" name="capacity" required min="1">
                        </div>
                        <div class="form-group">
                             <label for="pricePerNight">1박 요금 (원)</label>
                             <%-- ★★★★★ 핵심 수정 2: type, id, onkeyup 속성 변경 및 추가 ★★★★★ --%>
                            <input type="text" id="pricePerNight" name="pricePerNight" required onkeyup="formatPrice(this)">
                        </div>
                    </div>
                      
                    <div class="button-container">
                        <button type="button" class="btn" onclick="location.href='${pageContext.request.contextPath}/admin/accom/list.do'">취소</button>
                        <button type="submit" class="btn primary">등록하기</button>
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
    </script>
</body>
</html>