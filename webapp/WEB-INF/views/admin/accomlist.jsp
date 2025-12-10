<%-- 파일 경로: src/main/webapp/WEB-INF/views/admin/accomlist.jsp --%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>숙소 관리</title>
<%@ include file="/WEB-INF/views/inc/admin_asset.jsp" %>
<%-- 아이콘 및 슬라이더 라이브러리 추가 --%>
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.2/css/all.min.css"/>
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/noUiSlider/15.7.1/nouislider.min.css" />
<style>
    /* ★★★★★ 핵심 수정 1: 버튼 정렬 및 디자인을 위한 스타일 추가 ★★★★★ */
    .action-buttons {
        display: flex;
        gap: 8px; /* 버튼 사이 간격 */
        align-items: center; 
    }
    .action-buttons form, .action-buttons a {
        display: inline-block;
        margin: 0;
    }
    .btn.secondary {
        background-color: #6c757d; /* 수정 버튼 색상 (회색 계열) */
        color: white;
        text-decoration: none; /* a 태그의 밑줄 제거 */
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
            <h1>숙소 목록</h1>
            
            <form id="filterForm" method="GET" action="${pageContext.request.contextPath}/admin/accom/list.do">
                
                <input type="hidden" name="sort" id="sortInput" value="${sortOrder}">

                <div class="controls-bar">
                    <div class="filter-buttons">
                        <button type="button" class="btn" onclick="openFilterModal()"><i class="fa-solid fa-filter"></i> 필터</button>
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
                            <h3>숙소 유형</h3>
                            <label><input type="checkbox" name="type" value="호텔" <c:if test="${selectedTypes.contains('호텔')}">checked</c:if>> 호텔</label>
                            <label><input type="checkbox" name="type" value="민박" <c:if test="${selectedTypes.contains('민박')}">checked</c:if>> 민박</label>
                            <label><input type="checkbox" name="type" value="캠핑" <c:if test="${selectedTypes.contains('캠핑')}">checked</c:if>> 캠핑</label>
                            <label><input type="checkbox" name="type" value="펜션" <c:if test="${selectedTypes.contains('펜션')}">checked</c:if>> 펜션</label>
                            <label><input type="checkbox" name="type" value="모텔" <c:if test="${selectedTypes.contains('모텔')}">checked</c:if>> 모텔</label>
                            <label><input type="checkbox" name="type" value="풀빌라" <c:if test="${selectedTypes.contains('풀빌라')}">checked</c:if>> 풀빌라</label>
                        </div>
                        <div class="filter-group">
                             <h3>가격 범위 (1박 기준)</h3>
                            <div id="price-display" class="price-display"></div>
                            <div id="price-slider-container">
                                <div id="price-slider"></div>
                            </div>
                            <input type="hidden" name="minPrice" id="minPriceInput" value="${minPrice}">
                            <input type="hidden" name="maxPrice" id="maxPriceInput" value="${maxPrice}">
                        </div>
                        <div class="modal-actions">
                            <a href="${pageContext.request.contextPath}/admin/accom/list.do" class="btn btn-reset"><i class="fa-solid fa-rotate-left"></i> 초기화</a>
                             <button type="submit" class="btn primary"><i class="fa-solid fa-check"></i> 적용하기</button>
                        </div>
                    </div>
                </div>
            </form>

            <table class="admin-table">
                <thead>
                    <%-- ★★★★★ 핵심 수정 2: 테이블 헤더 텍스트 변경 ★★★★★ --%>
                    <tr><th>숙소명</th><th>숙소 유형</th><th>객실명</th><th>1박 요금</th><th>수용인원</th><th>예약 여부</th><th>수정 및 삭제</th></tr>
                </thead>
                <tbody>
                    <c:forEach items="${list}" var="dto">
                         <tr>
                            <td>${dto.accomName}</td>
                            <td>${dto.accomType}</td>
                             <td>${dto.roomName}</td>
                            <td><fmt:formatNumber value="${dto.pricePerNight}" pattern="#,###원"/></td>
                            <td>${dto.capacity}명</td>
                            <td>
                                <c:if test="${dto.reserved}"><span class="reserved">예약 불가</span></c:if>
                                <c:if test="${!dto.reserved}"><span class="available">예약 가능</span></c:if>
                            </td>
                            <td>
                                <%-- ★★★★★ 핵심 수정 3: 수정 버튼에 secondary 클래스 추가 ★★★★★ --%>
                                <div class="action-buttons">
                                    <a href="${pageContext.request.contextPath}/admin/accom/edit.do?roomId=${dto.roomId}" class="btn secondary">수정</a>
                                    <form method="POST" action="${pageContext.request.contextPath}/admin/accom/delete.do" onsubmit="return confirm('[${dto.accomName} - ${dto.roomName}] 객실을 정말 삭제하시겠습니까?');">
                                        <input type="hidden" name="roomId" value="${dto.roomId}">
                                        <button type="submit" class="btn danger">삭제</button>
                                    </form>
                                </div>
                            </td>
                         </tr>
                    </c:forEach>
                    <c:if test="${empty list}">
                        <tr><td colspan="7" style="text-align: center;">조건에 맞는 숙소가 없습니다.</td></tr>
                     </c:if>
                </tbody>
            </table>
            
            <div class="button-container">
                <button type="button" class="btn primary" onclick="location.href='${pageContext.request.contextPath}/admin/accom/add.do'"><i class="fa-solid fa-plus"></i> 숙소 등록</button>
            </div>
         </main>
    </div>

<script src="https://cdnjs.cloudflare.com/ajax/libs/noUiSlider/15.7.1/nouislider.min.js"></script>
<script>
    const modal = document.getElementById('filterModal');
    function openFilterModal() { modal.style.display = 'block'; }
    function closeFilterModal() { modal.style.display = 'none'; }
    window.onclick = function(event) {
        if (event.target == modal) { closeFilterModal(); }
    }

    function submitSort(sortValue) {
        document.getElementById('sortInput').value = sortValue;
        document.getElementById('filterForm').submit();
    }

    const priceSlider = document.getElementById('price-slider');
    const minPriceInput = document.getElementById('minPriceInput');
    const maxPriceInput = document.getElementById('maxPriceInput');
    const priceDisplay = document.getElementById('price-display');
    
    // ▼▼▼ [핵심 수정] 서블릿에서 넘겨준 DB 최고가를 Javascript 변수로 받습니다. ▼▼▼
    const dbMaxPrice = <c:out value="${maxPriceFromDB}" default="2000000" />;

    if (priceSlider) {
        noUiSlider.create(priceSlider, {
            // 슬라이더 시작점 설정: 필터가 적용된 값이 있으면 그 값을, 없으면 0과 DB 최고가를 사용
            start: [ 
                <c:out value="${minPrice}" default="0"/>, 
                <c:out value="${maxPrice == maxPriceFromDB ? maxPriceFromDB : maxPrice}" default="${maxPriceFromDB}"/> 
            ],
            connect: true,
            step: 10000,
            // 슬라이더 전체 범위 설정: 0부터 DB 최고가까지
            range: { 
                'min': 0, 
                'max': dbMaxPrice
            }
        });
        
        priceSlider.noUiSlider.on('update', function (values, handle) {
            const minPrice = parseInt(values[0]);
            const maxPrice = parseInt(values[1]);
            
            // '최대' 가격일 경우 '+'를 붙여서 표시
            let maxPriceText = maxPrice.toLocaleString();
            if (maxPrice === dbMaxPrice) {
                
            }
            
            priceDisplay.innerHTML = minPrice.toLocaleString() + '원 - ' + maxPriceText + '원';
            
            minPriceInput.value = minPrice;
            maxPriceInput.value = maxPrice;
        });
    }
</script>
</body>
</html>
