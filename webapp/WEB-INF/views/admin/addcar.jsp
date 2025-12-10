<%-- 파일 경로: /WEB-INF/views/admin/addcar.jsp --%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>렌터카 등록</title>
<%@ include file="/WEB-INF/views/inc/admin_asset.jsp" %>
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.2/css/all.min.css"/>
</head>
<body>
    <div id="admin-container">
        <%@ include file="/WEB-INF/views/inc/admin_header.jsp" %>
        <main id="admin-main">
            <h1><i class="fa-solid fa-car"></i> 렌터카 등록</h1>
            
            <%-- ★★★★★ 핵심 수정 1: action 주소 변경 및 onsubmit 이벤트 추가 ★★★★★ --%>
            <form method="POST" action="${pageContext.request.contextPath}/admin/car/add.do" class="form-container" onsubmit="return removeCommasBeforeSubmit(this)">
                
                <div class="form-group">
                    <label for="carName">차량 이름</label>
                    <input type="text" id="carName" name="carName" required>
                </div>
                
                <div class="form-row">
                    <div class="form-group">
                        <label for="carType">차종</label>
                        <select id="carType" name="carType">
                            <option value="승용차">승용차</option>
                            <option value="SUV">SUV</option>
                            <option value="승합차">승합차</option>
                        </select>
                    </div>

                    <div class="form-group">
                        <label for="fuelType">연료 종류</label>
                        <select id="fuelType" name="fuelType">
                            <option value="가솔린">가솔린</option>
                            <option value="디젤">디젤</option>
                            <option value="전기차">전기차</option>
                            <option value="LPG">LPG</option>
                        </select>
                    </div>
                </div>

                <div class="form-group">
                    <label for="pricePerDay">하루 대여 요금 (원)</label>
                    <%-- ★★★★★ 핵심 수정 2: 자동 콤마 기능을 위해 input 속성 변경 ★★★★★ --%>
                    <input type="text" id="pricePerDay" name="pricePerDay" required onkeyup="formatPrice(this)">
                </div>

                <div class="button-container">
                    <button type="button" class="btn" onclick="location.href='${pageContext.request.contextPath}/admin/car/list.do'">취소</button>
                    <button type="submit" class="btn primary">등록하기</button>
                </div>
            </form>
        </main>
    </div>

<script>
    // 1. 가격 입력 필드에 자동으로 콤마(,)를 삽입하는 함수
    function formatPrice(input) {
        let value = input.value.replace(/[^\d]/g, '');
        if (value === '') {
            input.value = '';
            return;
        }
        input.value = Number(value).toLocaleString('en-US');
    }

    // 2. 폼을 제출하기 직전에 가격에서 콤마(,)를 제거하는 함수
    function removeCommasBeforeSubmit(form) {
        const priceInput = form.querySelector('#pricePerDay');
        if (priceInput) {
            priceInput.value = priceInput.value.replace(/,/g, '');
        }
        return true;
    }
</script>
</body>
</html>