<%-- 파일 경로: /WEB-INF/views/admin/editcar.jsp --%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>렌터카 수정</title>
<%@ include file="/WEB-INF/views/inc/admin_asset.jsp" %>
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.2/css/all.min.css"/>
</head>
<body>
    <div id="admin-container">
        <%@ include file="/WEB-INF/views/inc/admin_header.jsp" %>
        <main id="admin-main">
            <h1><i class="fa-solid fa-pen-to-square"></i> 렌터카 수정</h1>
            
            <form method="POST" action="${pageContext.request.contextPath}/admin/car/edit.do" class="form-container" onsubmit="return removeCommasBeforeSubmit(this)">
                
                <input type="hidden" name="carId" value="${dto.carId}">

                <div class="form-group">
                    <label for="carName">차량 이름</label>
                    <input type="text" id="carName" name="carName" required value="${dto.carName}">
                </div>
                
                <div class="form-row">
                    <div class="form-group">
                        <label for="carType">차종</label>
                        <select id="carType" name="carType">
                            <option value="승용차" <c:if test="${dto.carType == '승용차'}">selected</c:if>>승용차</option>
                            <option value="SUV" <c:if test="${dto.carType == 'SUV'}">selected</c:if>>SUV</option>
                            <option value="승합차" <c:if test="${dto.carType == '승합차'}">selected</c:if>>승합차</option>
                        </select>
                    </div>

                    <div class="form-group">
                        <label for="fuelType">연료 종류</label>
                        <select id="fuelType" name="fuelType">
                            <option value="가솔린" <c:if test="${dto.fuelType == '가솔린'}">selected</c:if>>가솔린</option>
                            <option value="디젤" <c:if test="${dto.fuelType == '디젤'}">selected</c:if>>디젤</option>
                            <option value="전기차" <c:if test="${dto.fuelType == '전기차'}">selected</c:if>>전기차</option>
                            <option value="LPG" <c:if test="${dto.fuelType == 'LPG'}">selected</c:if>>LPG</option>
                        </select>
                    </div>
                </div>

                <div class="form-group">
                    <label for="pricePerDay">하루 대여 요금 (원)</label>
                    <input type="text" id="pricePerDay" name="pricePerDay" required value="${dto.pricePerDay}" onkeyup="formatPrice(this)">
                </div>

                <div class="button-container">
                    <button type="button" class="btn" onclick="location.href='${pageContext.request.contextPath}/admin/car/list.do'">취소</button>
                    <button type="submit" class="btn primary">수정하기</button>
                </div>
            </form>
        </main>
    </div>

<script>
    function formatPrice(input) {
        let value = input.value.replace(/[^\d]/g, '');
        if (value === '') {
            input.value = '';
            return;
        }
        input.value = Number(value).toLocaleString('en-US');
    }

    function removeCommasBeforeSubmit(form) {
        const priceInput = form.querySelector('#pricePerDay');
        if (priceInput) {
            priceInput.value = priceInput.value.replace(/,/g, '');
        }
        return true;
    }

    document.addEventListener('DOMContentLoaded', function() {
        const priceInput = document.getElementById('pricePerDay');
        if (priceInput && priceInput.value) {
            formatPrice(priceInput);
        }
    });
</script>
</body>
</html>