<%-- 파일 경로: src/main/webapp/WEB-INF/views/admin/carlist.jsp --%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>렌터카 관리</title>
<%@ include file="/WEB-INF/views/inc/admin_asset.jsp" %>
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.2/css/all.min.css"/>
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/noUiSlider/15.7.1/nouislider.min.css" />
<script src="https://cdnjs.cloudflare.com/ajax/libs/noUiSlider/15.7.1/nouislider.min.js"></script>
<%-- ★★★★★ 핵심 수정 1: 버튼 정렬 및 디자인을 위한 스타일 추가 ★★★★★ --%>
<style>
    .action-buttons {
        display: flex;
        gap: 8px;
        align-items: center;
    }
    .action-buttons form, .action-buttons a {
        display: inline-block;
        margin: 0;
    }
    .btn.secondary {
        background-color: #6c757d;
        color: white;
        text-decoration: none;
    }
    .btn.secondary:hover {
        background-color: #5a6268;
    }
</style>
</head>
<body>
    <div id="admin-container">
        <%@ include file="/WEB-INF/views/inc/admin_header.jsp" %>
        <main id="admin-main">
            <h1>렌터카 목록</h1>
            
            <form id="filterForm" method="GET" action="${pageContext.request.contextPath}/admin/car/list.do">
                <input type="hidden" name="sort" id="sortInput" value="${sortOrder}">
                <div class="controls-bar">
                    <div class="filter-buttons">
                        <button type="button" class="btn" onclick="openFilterModal()"><i class="fa-solid fa-filter"></i>필터</button>
                    </div>
                    <div class="sort-dropdown">
                        <button type="button" class="dropbtn">
                            <span id="sort-text">
                                <c:choose>
                                    <c:when test="${sortOrder == 'price_asc'}">낮은 가격순</c:when>
                                    <c:when test="${sortOrder == 'price_desc'}">높은 가격순</c:when>
                                    <c:otherwise>기본 정렬</c:otherwise>
                                </c:choose>
                            </span>
                            <i class="fa-solid fa-caret-down"></i>
                        </button>
                        <div class="dropdown-content">
                            <a href="#" onclick="submitSort('')">기본 정렬</a>
                            <a href="#" onclick="submitSort('price_asc')">낮은 가격순</a>
                            <a href="#" onclick="submitSort('price_desc')">높은 가격순</a>
                        </div>
                    </div>
                </div>
                <div id="filterModal" class="modal">
                    <div class="modal-content">
                        <div class="modal-header">
                            <h2>상세 검색 필터</h2>
                            <span class="close-button" onclick="closeFilterModal()">&times;</span>
                        </div>
                        <div class="filter-group">
                            <h3>연료 종류</h3>
                            <label><input type="checkbox" name="fuel" value="가솔린" <c:if test="${selectedFuels.contains('가솔린')}">checked</c:if>> 가솔린</label>
                            <label><input type="checkbox" name="fuel" value="디젤" <c:if test="${selectedFuels.contains('디젤')}">checked</c:if>> 디젤</label>
                            <label><input type="checkbox" name="fuel" value="전기차" <c:if test="${selectedFuels.contains('전기차')}">checked</c:if>> 전기차</label>
                        </div>
                        <div class="filter-group">
                            <h3>가격 범위 (1일 기준)</h3>
                            <div id="price-display" class="price-display"></div>
                            <div id="price-slider-container"><div id="price-slider"></div></div>
                            <input type="hidden" name="minPrice" id="minPriceInput">
                            <input type="hidden" name="maxPrice" id="maxPriceInput">
                        </div>
                        <div class="modal-actions">
                            <a href="${pageContext.request.contextPath}/admin/car/list.do" class="btn btn-reset"><i class="fa-solid fa-rotate-left"></i>초기화</a>
                            <button type="submit" class="btn primary"><i class="fa-solid fa-check"></i>적용하기</button>
                        </div>
                    </div>
                </div>
            </form>

            <table class="admin-table">
                <thead>
                    <%-- ★★★★★ 핵심 수정 2: 테이블 헤더 텍스트 변경 ★★★★★ --%>
                    <tr><th>차종</th><th>차량</th><th>하루 대여 요금</th><th>연료 종류</th><th>예약 여부</th><th>수정 및 삭제</th></tr>
                </thead>
                <tbody>
                    <c:forEach items="${list}" var="dto">
                        <tr>
                            <td>${dto.carType}</td>
                            <td>${dto.carName}</td>
                            <td><fmt:formatNumber value="${dto.pricePerDay}" pattern="#,###원"/></td>
                            <td>${dto.fuelType}</td>
                            <td>
                                <c:if test="${dto.reserved}"><span class="reserved">예약 불가</span></c:if>
                                <c:if test="${!dto.reserved}"><span class="available">예약 가능</span></c:if>
                            </td>
                            <td>
                                <%-- ★★★★★ 핵심 수정 3: 수정 버튼 추가 및 버튼 그룹화 ★★★★★ --%>
                                <div class="action-buttons">
                                    <a href="${pageContext.request.contextPath}/admin/car/edit.do?carId=${dto.carId}" class="btn secondary">수정</a>
                                    <form method="POST" action="${pageContext.request.contextPath}/admin/car/delete.do" onsubmit="return confirm('[${dto.carName}] 차량을 정말 삭제하시겠습니까?');">
                                        <input type="hidden" name="carId" value="${dto.carId}">
                                        <button type="submit" class="btn danger">삭제</button>
                                    </form>
                                </div>
                            </td>
                        </tr>
                    </c:forEach>
                    <c:if test="${empty list}">
                        <tr><td colspan="6" style="text-align: center;">조건에 맞는 차량이 없습니다.</td></tr>
                    </c:if>
                </tbody>
            </table>
            <div class="button-container">
                <button type="button" class="btn primary" onclick="location.href='${pageContext.request.contextPath}/admin/car/add.do'"><i class="fa-solid fa-plus"></i>차량 등록</button>
            </div>
        </main>
    </div>

<script>
    const modal = document.getElementById('filterModal');
    function openFilterModal() { modal.style.display = 'block'; }
    function closeFilterModal() { modal.style.display = 'none'; }
    window.onclick = function(event) { if (event.target == modal) { modal.style.display = 'none'; } }
    
    function submitSort(sortValue) {
        document.getElementById('sortInput').value = sortValue;
        document.getElementById('filterForm').submit();
    }
    
    const priceSlider = document.getElementById('price-slider');
    const minPriceInput = document.getElementById('minPriceInput');
    const maxPriceInput = document.getElementById('maxPriceInput');
    const priceDisplay = document.getElementById('price-display');

    if (priceSlider) {
        noUiSlider.create(priceSlider, {
            start: [ <c:out value="${minPrice}" default="0"/>, <c:out value="${maxPrice == 9999999 ? 500000 : maxPrice}" default="500000"/> ],
            connect: true, step: 10000,
            range: { 'min': 0, 'max': 500000 },
            format: { to: value => Math.round(value), from: value => Number(value) }
        });

        priceSlider.noUiSlider.on('update', function (values) {
            const [minPrice, maxPrice] = values;
            minPriceInput.value = minPrice;
            maxPriceInput.value = maxPrice;
            const formatter = new Intl.NumberFormat('ko-KR');
            priceDisplay.innerHTML = formatter.format(minPrice) + '원 - ' + formatter.format(maxPrice) + '원';
        });
    }
</script>
</body>
</html>