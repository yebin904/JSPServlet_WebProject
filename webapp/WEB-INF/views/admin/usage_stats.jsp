<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>이용 통계</title>
<%@ include file="/WEB-INF/views/inc/admin_asset.jsp" %>
<script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
</head>
<body>	
    <div id="admin-container">
        <%@ include file="/WEB-INF/views/inc/admin_header.jsp" %>
        
        <main id="admin-main">
            <h1>DASHBOARD - 이용 통계</h1>
            
            <div class="stats-grid-container">
                
                <div class="stat-card card-tour-keyword">
                    <h2 class="stat-card-title">관광지 인기 검색 키워드 (TOP 10)</h2>
                    <ol class="ranked-list" id="tour-keyword-list">
                        <li>제주도<span class="rank-change"></span></li>
                        <li>경주<span class="rank-change"></span></li>
                        <li>부산<span class="rank-change"></span></li>
                        <li>강릉<span class="rank-change"></span></li>
                        <li>속초<span class="rank-change"></span></li>
                        <li>여수<span class="rank-change"></span></li>
                        <li>전주<span class="rank-change"></span></li>
                        <li>서울<span class="rank-change"></span></li>
                        <li>가평<span class="rank-change"></span></li>
                        <li>포항<span class="rank-change"></span></li>
                    </ol>
                </div>
                
                <div class="stat-card card-food-keyword">
                    <h2 class="stat-card-title">맛집 인기 검색 키워드 (TOP 10)</h2>
                    <ol class="ranked-list" id="food-keyword-list">
                        <li>제주 흑돼지<span class="rank-change"></span></li>
                        <li>부산 돼지국밥<span class="rank-change"></span></li>
                        <li>강릉 순두부젤라또<span class="rank-change"></span></li>
                        <li>속초 물회<span class="rank-change"></span></li>
                        <li>전주 비빔밥<span class="rank-change"></span></li>
                        <li>여수 게장<span class="rank-change"></span></li>
                        <li>경주 황리단길<span class="rank-change"></span></li>
                        <li>서울 힙지로<span class="rank-change"></span></li>
                        <li>부산 해운대<span class="rank-change"></span></li>
                        <li>제주 고기국수<span class="rank-change"></span></li>
                    </ol>
                </div>


                <div class="stat-card card-tour-20s">
                    <h2 class="stat-card-title">20대 관광지 인기</h2>
                    <ol class="ranked-list" id="tour-20s-list">
                        <li>성수동 팝업스토어<span class="rank-change"></span></li>
                        <li>홍대/연남동<span class="rank-change"></span></li>
                        <li>부산 해리단길<span class="rank-change"></span></li>
                        <li>양양 서피비치<span class="rank-change"></span></li>
                        <li>경주 황리단길<span class="rank-change"></span></li>
                    </ol>
                </div>

                <div class="stat-card card-food-20s">
                    <h2 class="stat-card-title">20대 맛집 인기 순위</h2>
                    <ol class="ranked-list" id="food-20s-list">
                        <li>인스타 감성 카페<span class="rank-change"></span></li>
                        <li>탕후루 맛집<span class="rank-change"></span></li>
                        <li>연남동 파스타<span class="rank-change"></span></li>
                        <li>성수동 베이글<span class="rank-change"></span></li>
                        <li>마라탕<span class="rank-change"></span></li>
                    </ol>
                </div>

            </div>
        </main>
    </div>

<script>
    // 애니메이션 로직 (기존과 동일)
    function animateRankList(listId) {
        const listElement = document.getElementById(listId);
        if (!listElement || listElement.children.length < 3) return;
        const itemsWithOriginalIndex = Array.from(listElement.children).map((item, index) => ({ item, oldIndex: index }));
        const oldPositions = new Map();
        itemsWithOriginalIndex.forEach(({ item }) => { oldPositions.set(item, item.getBoundingClientRect()); });
        const thirdItemRef = itemsWithOriginalIndex.splice(2, 1)[0];
        itemsWithOriginalIndex.unshift(thirdItemRef);
        itemsWithOriginalIndex.forEach(({ item }) => listElement.appendChild(item));

        itemsWithOriginalIndex.forEach(({ item, oldIndex }, newIndex) => {
            const change = oldIndex - newIndex;
            const oldPos = oldPositions.get(item);
            const newPos = item.getBoundingClientRect();
            const dx = oldPos.left - newPos.left;
            const dy = oldPos.top - newPos.top;
            const indicator = item.querySelector('.rank-change');

            if (indicator) {
                indicator.textContent = '';
                indicator.className = 'rank-change';
                if (change !== 0) {
                    if (change > 0) {
                        indicator.textContent = '▲ ' + change;
                        indicator.classList.add('rank-up');
                    } else {
                        indicator.textContent = '▼ ' + Math.abs(change);
                        indicator.classList.add('rank-down');
                    }
                    setTimeout(() => indicator.classList.add('visible'), 800);
                    setTimeout(() => indicator.classList.remove('visible'), 4500);
                }
            }

            requestAnimationFrame(() => {
                item.style.transition = 'none';
                item.style.transform = 'translate(' + dx + 'px, ' + dy + 'px)';
                requestAnimationFrame(() => {
                    item.style.transition = 'transform 0.8s linear';
                    item.style.transform = '';
                });
            });
        });
    }

    let animationCount = 0;
    const maxAnimations = 3;
    const rankAnimationInterval = setInterval(() => {
        if (++animationCount > maxAnimations) {
            clearInterval(rankAnimationInterval);
            return;
        }
        animateRankList('tour-keyword-list');
        animateRankList('food-keyword-list');
        animateRankList('tour-20s-list');
        animateRankList('food-20s-list');
    }, 5000);
</script>
</body>
</html>