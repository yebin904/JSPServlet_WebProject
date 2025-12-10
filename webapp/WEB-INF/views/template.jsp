<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%-- 
    [공용 리스트 뷰 템플릿]
    '여행 트렌드', '여행지 뉴스', '축제/행사' 등 동일한 레이아웃을 가진
    모든 목록 페이지의 화면을 그리는 템플릿입니다.
    
    Controller로부터 "itemList", "pageTitle" 등의 이름으로 데이터를 받아 화면을 완성합니다.
--%>

<%-- 공통 헤더 include --%>
<jsp:include page="/WEB-INF/views/inc/header.jsp" />


<main class="list-page-container">
    
    <%-- 1. 페이지 상단: 제목, 서브메뉴, 검색 옵션 --%>
    <section class="list-header">
        
        <%-- Controller에서 전달받은 페이지 제목 출력 --%>
        <h2 class="page-title">${pageTitle}</h2>

        <%-- (선택) 서브메뉴: Controller에서 subMenuList를 전달한 경우에만 표시 --%>
        <c:if test="${not empty subMenuList}">
            <nav class="sub-menu">
                <c:forEach var="menu" items="${subMenuList}">
                    <a href="${pageContext.request.contextPath}${menu.url}" class="${menu.active ? 'active' : ''}">${menu.name}</a>
                </c:forEach>
            </nav>
        </c:if>

    </section>

    <%-- 2. 콘텐츠 목록 --%>
    <section class="list-body">
        
        <div class="list-info-bar">
            <div class="total-count">총 <strong>${totalCount}</strong>건</div>
            <div class="sort-options">
                <%-- 정렬 기능 (필요시 구현) --%>
                <a href="#" class="active">최신순</a>
                <a href="#">인기순</a>
            </div>
        </div>

        <div class="item-list">
            <%-- Controller에서 전달받은 itemList가 비어있는지 확인 --%>
            <c:if test="${empty itemList}">
                <div class="no-result">
                    <p>표시할 항목이 없습니다.</p>
                </div>
            </c:if>

            <%-- itemList를 반복하며 각 항목을 출력 --%>
            <c:forEach var="item" items="${itemList}">
                <div class="list-item">
                    
                    <%-- DTO의 place_type_id를 확인하여 링크 동적 생성 --%>
                    <c:choose>
                        <c:when test="${item.place_type_id == 2}"> <%-- 맛집 --%>
                            <a href="${pageContext.request.contextPath}/restaurant/detail.do?id=${item.place_id}">
                        </c:when>
                        <c:otherwise> <%-- 관광지 등 나머지 --%>
                            <a href="${pageContext.request.contextPath}/place/detail.do?id=${item.place_id}">
                        </c:otherwise>
                    </c:choose>

                        <div class="item-thumbnail">
                            <img src="${pageContext.request.contextPath}${item.imageUrl}" alt="${item.title}" 
                                 onerror="this.src='https://placehold.co/200x150/E9ECEF/495057?text=No+Image'">
                        </div>
                        <div class="item-info">
                            <h3 class="item-title">${item.title}</h3>
                            <p class="item-description">${item.description}</p>
                            <div class="item-tags">${item.tags}</div>
                        </div>
                    </a>
                </div>
            </c:forEach>
        </div>

    </section>

    <%-- 3. 페이지네이션 --%>
    <nav class="pagination-container">
        <div class="pagination">
            <%-- 페이지네이션 로직 (PagingDTO 등 활용하여 구현) --%>
            <a href="#" class="active">1</a>
            <a href="#">2</a>
            <a href="#">3</a>
            <a href="#">4</a>
            <a href="#">5</a>
        </div>
    </nav>

</main>



