<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>콘텐츠 통계</title>
<%@ include file="/WEB-INF/views/inc/admin_asset.jsp" %>
<script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
</head>
<body>
    <div id="admin-container">
        <%@ include file="/WEB-INF/views/inc/admin_header.jsp" %>
        
        <main id="admin-main">
            <h1>DASHBOARD - 콘텐츠 통계</h1>
            
            <div class="content-stats-container">
                
                <div class="stat-card">
                    <h2 class="stat-card-title">가장 칼로리 소모가 많은 루트 (TOP 3)</h2>
                    <ol class="ranked-list">
                        <li>제주도 올레길</li>
                        <li>설악산 등반</li>
                        <li>한라산 등반</li>
                    </ol>
                </div>
                
                <div class="stat-card">
                    <h2 class="stat-card-title">인기 검색 키워드 (TOP 5)</h2>
                    <ol class="ranked-list">
                        <li>제주 흑돼지</li>
                        <li>부산 돼지국밥</li>
                        <li>부산 광안리</li>
                        <li>성수동 인스타 감성 카페</li>
                        <li>경복궁</li>
                    </ol>
                </div>

                <%-- ▼▼▼ [핵심] AI 추천 차트를 헬스케어 차트로 교체 ▼▼▼ --%>
                <div class="stat-card">
                    <h2 class="stat-card-title">헬스케어 활용 여부</h2>
                    <canvas id="healthcareChart"></canvas>
                </div>
            </div>
        </main>
    </div>

<script>
    // ▼▼▼ [핵심] AI 추천 차트 스크립트를 헬스케어 차트 스크립트로 교체 ▼▼▼
    new Chart(document.getElementById('healthcareChart'), {
        type: 'pie',
        data: {
            labels: ['사용', '미사용'],
            datasets: [{
                data: [78, 22], // 하드코딩된 데이터 값
                backgroundColor: ['#007BFF', '#343A40'],
                borderColor: 'white',
                borderWidth: 2
            }]
        },
        options: {
            responsive: true,
            plugins: {
                legend: {
                    position: 'bottom',
                }
            }
        }
    });
</script>

</body>
</html>