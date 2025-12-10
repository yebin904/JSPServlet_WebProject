<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/inc/asset.jsp" %>
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8">
<title>AI 추천 여행 경로</title>
<link rel="stylesheet" href="<%= request.getContextPath() %>/asset/css/aiMapView.css">

<!-- ✅ Kakao Maps SDK -->
<script src="//dapi.kakao.com/v2/maps/sdk.js?appkey=95f06e859388fb23abc3ac05fa370f48&libraries=services"></script>

</head>
<body>

<div class="container">
    <h1 class="route-title">AI 추천 여행 경로</h1>
    <p class="route-description">아래의 날짜 버튼을 클릭하여 일차별 경로를 확인해보세요.</p>

    <div id="day-buttons"></div>
    <div id="map"></div>
    <div id="stop-list"></div>

    <button id="save-route-btn">이 경로 저장하기</button>
</div>

<script>
window.addEventListener('DOMContentLoaded', async () => {

    const aiRouteId = '<%= request.getAttribute("id") %>';
    const contextPath = '<%= request.getContextPath() %>';
    const fetchURL = contextPath + '/route/aiMapView.do?id=' + aiRouteId;

    try {
        const res = await fetch(fetchURL);
        if (!res.ok) throw new Error("HTTP " + res.status);
        const data = await res.json();

        if (!data || data.length === 0) {
            document.getElementById('map').innerHTML = "<h4 style='text-align:center;'>경로 데이터가 없습니다.</h4>";
            return;
        }

        const mapContainer = document.getElementById('map');
        const map = new kakao.maps.Map(mapContainer, { 
            center: new kakao.maps.LatLng(data[0].lat, data[0].lng),
            level: 8 
        });

        const stopsByDay = data.reduce((acc, stop) => {
            (acc[stop.day] = acc[stop.day] || []).push(stop);
            return acc;
        }, {});
        const dayKeys = Object.keys(stopsByDay).sort((a, b) => a - b);

        const mapElements = {};
        const stopListContainer = document.getElementById('stop-list');

        // 날짜별로 지도 요소(오버레이, 폴리라인)와 목록 아이템 생성
        dayKeys.forEach(day => {
            const stops = stopsByDay[day];
            const linePath = [];
            mapElements[day] = { overlays: [], polyline: null };

            const dayGroup = document.createElement('div');
            dayGroup.id = 'day-group-' + day;
            dayGroup.className = 'day-group';
            const dayTitle = document.createElement('h3');
            dayTitle.className = 'day-title';
            dayTitle.textContent = 'Day ' + day;
            dayGroup.appendChild(dayTitle);
            stopListContainer.appendChild(dayGroup);

            stops.forEach(stop => {
                const latlng = new kakao.maps.LatLng(stop.lat, stop.lng);
                linePath.push(latlng);

                // 숫자 오버레이 생성
                const content = '<div class="custom-overlay-marker">' + stop.order + '</div>';
                const customOverlay = new kakao.maps.CustomOverlay({
                    position: latlng,
                    content: content,
                    yAnchor: 1.2
                });
                mapElements[day].overlays.push(customOverlay);

                // 목록 아이템 생성 및 클릭 이벤트 추가
                const item = document.createElement('div');
                item.className = 'stop-item';
                item.innerHTML = '<span class="stop-order" style="color: white !important; background-color: #4a6cf7 !important;">' + stop.order + '</span><span class="stop-name" style="color: black !important; font-weight: bold !important;">' + stop.name + '</span>';
                item.addEventListener('click', () => {
                    map.panTo(latlng);
                });
                dayGroup.appendChild(item);
            });

            const polyline = new kakao.maps.Polyline({
                path: linePath,
                strokeWeight: 4,
                strokeColor: '#888',
                strokeStyle: 'dashed',
                strokeOpacity: 0.8
            });
            mapElements[day].polyline = polyline;
        });

        const dayButtonsContainer = document.getElementById('day-buttons');
        dayKeys.forEach(day => {
            const button = document.createElement('button');
            button.id = 'day-btn-' + day;
            button.className = 'day-btn';
            button.textContent = 'Day ' + day;
            button.dataset.day = day;
            dayButtonsContainer.appendChild(button);
        });

        let currentDay = null;
        function displayDay(day) {
            currentDay = day;

            dayKeys.forEach(d => {
                mapElements[d].overlays.forEach(o => o.setMap(null));
                mapElements[d].polyline.setMap(null);
            });
            document.querySelectorAll('#day-buttons .day-btn').forEach(b => b.classList.remove('active'));

            const activeButton = document.getElementById('day-btn-' + day);
            if (activeButton) {
                activeButton.classList.add('active');
            }

            mapElements[day].overlays.forEach(o => o.setMap(map));
            mapElements[day].polyline.setMap(map);

            const bounds = new kakao.maps.LatLngBounds();
            stopsByDay[day].forEach(stop => bounds.extend(new kakao.maps.LatLng(stop.lat, stop.lng)));
            map.setBounds(bounds);
        }

        dayButtonsContainer.addEventListener('click', e => {
            if (e.target.matches('.day-btn')) {
                const day = e.target.dataset.day;
                displayDay(day);
            }
        });

        if (dayKeys.length > 0) {
            displayDay(dayKeys[0]);
        }

        // 8. "이 경로 저장하기" 버튼 이벤트 리스너
        const saveBtn = document.getElementById('save-route-btn');
        saveBtn.addEventListener('click', async () => {
            
            if (!confirm('이 경로를 내 경로로 저장하시겠습니까?')) {
                return;
            }

            saveBtn.disabled = true;
            saveBtn.textContent = '저장 중...';

            const params = new URLSearchParams();
            params.append('aiRouteId', aiRouteId);

            try {
                const saveRes = await fetch(contextPath + '/route/saveUserRoute.do', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/x-www-form-urlencoded'
                    },
                    body: params
                });

                const result = await saveRes.json();

                if (saveRes.ok && result.success) {
                    alert('경로가 성공적으로 저장되었습니다!');
                    // 생성된 내 경로 보기 페이지로 이동 (3단계에서 생성할 페이지)
                    window.location.href = contextPath + '/route/userRouteView.do?id=' + result.newUserRouteId;
                } else {
                    throw new Error(result.message || '알 수 없는 이유로 저장에 실패했습니다.');
                }

            } catch (error) {
                alert('경로 저장 중 오류가 발생했습니다: ' + error.message);
                saveBtn.disabled = false;
                saveBtn.textContent = '이 경로 저장하기';
            }
        });

    } catch (err) {
        console.error("❌ 지도 로드 중 오류:", err);
        document.getElementById('map').innerHTML = "<h4 style='text-align:center;'>지도를 불러오는 중 오류가 발생했습니다.</h4>";
    }
});
</script>
</body>
</html>
