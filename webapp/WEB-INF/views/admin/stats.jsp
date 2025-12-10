<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>회원 통계</title>
<%@ include file="/WEB-INF/views/inc/admin_asset.jsp" %>
<%-- 1. Chart.js 기본 라이브러리 --%>
<script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
<%-- ★★★★★ 2. 핵심 수정: 숫자 표시를 위한 데이터 레이블 플러그인 추가 ★★★★★ --%>
<script src="https://cdn.jsdelivr.net/npm/chartjs-plugin-datalabels@2.0.0"></script>


</head>
<body>
<div id="admin-container">
    <%@ include file="/WEB-INF/views/inc/admin_header.jsp" %>
    <main id="admin-main">
        <h1>DASHBOARD - 회원 통계</h1>
        <div class="stats-container">
            <div class="stat-box">
                <h3>방문 기록</h3>
                <canvas id="visitChart"></canvas>
            </div>
            <div class="stat-box">
                <h3>회원 성별 비율</h3>
                <canvas id="memberRatioChart"></canvas>
            </div>
            <div class="stat-box">
                <h3>가입자 수</h3>
                <canvas id="signUpChart"></canvas>
            </div>
            <div class="stat-box">
                <h3>탈퇴자 수</h3>
                <canvas id="withdrawalChart"></canvas>
            </div>
        </div>
    </main>
</div>

<script>
    // ★★★★★ 3. 핵심 수정: 다운로드한 플러그인을 Chart.js에 등록합니다. ★★★★★
    Chart.register(ChartDataLabels);

    // 샘플 데이터 로직은 그대로 유지합니다.
    const memberVisitsData = (${memberVisits == 0 && nonMemberVisits == 0}) ? 85 : ${memberVisits};
    const nonMemberVisitsData = (${memberVisits == 0 && nonMemberVisits == 0}) ? 120 : ${nonMemberVisits};
    const memberGenderStats = ${not empty memberGenderStatsJson and memberGenderStatsJson ne '[]' ? memberGenderStatsJson : "[{category:'남자', count:180}, {category:'여자', count:250}]"};
    const yearlySignUps = ${not empty yearlySignUpsJson and yearlySignUpsJson ne '[]' ? yearlySignUpsJson : "[{category:'2024', count:150}, {category:'2025', count:420}]"};
    const yearlyWithdrawals = ${not empty yearlyWithdrawalsJson and yearlyWithdrawalsJson ne '[]' ? yearlyWithdrawalsJson : "[{category:'2024', count:12}, {category:'2025', count:35}]"};

    // --- 각 차트의 options에 숫자 표시(plugins) 설정을 추가합니다. ---

    new Chart(document.getElementById('visitChart'), {
        type: 'bar', 
        data: { labels: ['방문'], datasets: [{ label: '회원', data: [memberVisitsData]}, { label: '비회원', data: [nonMemberVisitsData]}] },
        options: {
            plugins: {
                datalabels: { // 숫자 표시 설정
                    anchor: 'end', align: 'top', font: { weight: 'bold' }
                }
            }
        }
    });
    
    new Chart(document.getElementById('memberRatioChart'), {
        type: 'pie',
        data: {
            labels: memberGenderStats.map(item => item.category),
            datasets: [{ data: memberGenderStats.map(item => item.count) }]
        },
        options: {
            plugins: {
                datalabels: { // 숫자 표시 설정
                    color: 'white', font: { weight: 'bold' },
                    formatter: (value, ctx) => { // 숫자를 퍼센트로 변환해서 표시
                        const total = ctx.chart.data.datasets[0].data.reduce((a, b) => a + b, 0);
                        const percentage = (value / total * 100).toFixed(1) + '%';
                        return percentage;
                    }
                }
            }
        }
    });

    new Chart(document.getElementById('signUpChart'), {
        type: 'bar', 
        data: { labels: yearlySignUps.map(i => i.category), datasets: [{ label: '연도별 가입자', data: yearlySignUps.map(i => i.count) }] },
        options: {
            scales: { y: { beginAtZero: true } },
            plugins: {
                datalabels: { // 숫자 표시 설정
                    anchor: 'end', align: 'top', font: { weight: 'bold' }
                }
            }
        }
    });
    
    new Chart(document.getElementById('withdrawalChart'), {
        type: 'bar', 
        data: { labels: yearlyWithdrawals.map(i => i.category), datasets: [{ label: '연도별 탈퇴자', data: yearlyWithdrawals.map(i => i.count), backgroundColor: 'rgba(255, 99, 132, 0.5)' }] },
        options: {
            scales: { y: { beginAtZero: true } },
            plugins: {
                datalabels: { // 숫자 표시 설정
                    anchor: 'end', align: 'top', color: '#c44558', font: { weight: 'bold' }
                }
            }
        }
    });
</script>
</body>
</html>