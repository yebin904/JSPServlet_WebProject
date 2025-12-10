<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/inc/asset.jsp" %>
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8">
<title>내 여행 경로</title>
<link rel="stylesheet" href="<%= request.getContextPath() %>/asset/css/aiMapView.css">
<script src="//dapi.kakao.com/v2/maps/sdk.js?appkey=95f06e859388fb23abc3ac05fa370f48&libraries=services"></script>
</head>
<body>

<div class="container">
    <h1 class="route-title">내 여행 경로</h1>
    <p class="route-description">아래의 날짜 버튼을 클릭하여 일차별 여행 루트를 확인하거나 수정할 수 있습니다.</p>

    <div id="day-buttons"></div>
    <div id="map"></div>
    <div id="stop-list"></div>

    <div id="user-route-controls" style="text-align:center;margin-top:40px;">
        <button id="list-btn" class="day-btn">목록으로</button>
        <button id="edit-route-btn" class="day-btn active">수정 저장</button>
    </div>
</div>

<script>
window.addEventListener('DOMContentLoaded', async () => {

    const userRouteId = '<%= request.getAttribute("userRouteId") %>';
    const contextPath = '<%= request.getContextPath() %>';
    const fetchURL = contextPath + '/route/getUserRouteData.do?id=' + userRouteId;

    let map, stopsByDay = {}, mapElements = {}, dayKeys = [], currentDay = null;
    const stopListContainer = document.getElementById('stop-list');

    try {
        const res = await fetch(fetchURL);
        if (!res.ok) throw new Error("HTTP " + res.status);
        const data = await res.json();

        if (!data || data.length === 0) {
            document.getElementById('map').innerHTML = "<h4 style='text-align:center;'>저장된 경로가 없습니다.</h4>";
            return;
        }

        console.log("Stops Data:", data); // ✅ 디버그 로그

        // ✅ 지도 초기화
        const mapContainer = document.getElementById('map');
        map = new kakao.maps.Map(mapContainer, {
            center: new kakao.maps.LatLng(data[0].lat, data[0].lng),
            level: 8
        });

        initializeRoute(data);

        // ✅ 초기 표시 (지도 렌더 완료 후 표시)
        if (dayKeys.length > 0) {
            setTimeout(() => displayDay(dayKeys[0]), 200);
        }

        // ✅ 드래그 앤 드롭
        let draggingItem = null;

        stopListContainer.addEventListener('dragstart', e => {
            if (e.target.classList.contains('stop-item')) {
                draggingItem = e.target;
                e.dataTransfer.effectAllowed = 'move';
                setTimeout(() => e.target.classList.add('dragging'), 0);
            }
        });

        stopListContainer.addEventListener('dragend', e => {
            if (draggingItem) {
                draggingItem.classList.remove('dragging');
                draggingItem = null;
                handleDropComplete();
            }
        });

        stopListContainer.addEventListener('dragover', e => e.preventDefault());

        stopListContainer.addEventListener('drop', e => {
            e.preventDefault();
            const dropTarget = e.target.closest('.stop-item, .day-group');
            if (!draggingItem || !dropTarget) return;

            const targetGroup = dropTarget.classList.contains('day-group')
                ? dropTarget
                : dropTarget.parentElement;
            const afterElement = getDragAfterElement(targetGroup, e.clientY);

            if (!afterElement) targetGroup.appendChild(draggingItem);
            else targetGroup.insertBefore(draggingItem, afterElement);

            handleDropComplete();
        });

        function getDragAfterElement(container, y) {
            const draggableElements = [...container.querySelectorAll('.stop-item:not(.dragging)')];
            return draggableElements.reduce((closest, child) => {
                const box = child.getBoundingClientRect();
                const offset = y - box.top - box.height / 2;
                if (offset < 0 && offset > closest.offset) {
                    return { offset, element: child };
                } else {
                    return closest;
                }
            }, { offset: Number.NEGATIVE_INFINITY }).element;
        }

        async function handleDropComplete() {
            const updatedDaysData = {};

            document.querySelectorAll('.day-group').forEach(group => {
                const day = parseInt(group.dataset.day);
                const stopItems = group.querySelectorAll('.stop-item');
                const stopIds = [];
                stopsByDay[day] = [];

                stopItems.forEach((item, i) => {
                    const stopId = parseInt(item.dataset.stopId);
                    const stop = findStopById(stopId);
                    if (stop) {
                        stop.order = i + 1;
                        stop.day = day;
                        stopsByDay[day].push(stop);
                    }
                    item.querySelector('.stop-order').textContent = i + 1;
                    stopIds.push(stopId);
                });
                updatedDaysData[day] = stopIds;
            });

           

            try {
                const saveRes = await fetch(contextPath + '/route/updateUserRouteOrder.do', {
                    method: 'POST',
                    headers: { 'Content-Type': 'application/json' },
                    body: JSON.stringify({
                        userRouteId: parseInt(userRouteId),
                        updatedDays: updatedDaysData
                    })
                });
                const result = await saveRes.json();
                if (!result.success) throw new Error(result.message);
                console.log("✅ 순서 서버 저장 성공");
                
                setTimeout(() => {
                    window.location.reload();
                }, 1000);
              
            } catch (err) {
                console.warn("⚠️ 서버 저장 실패:", err);
                alert("순서 저장 중 오류가 발생했습니다. (화면은 유지됩니다)");
            }
        }

        function findStopById(id) {
            for (const day in stopsByDay) {
                const stop = stopsByDay[day].find(s => s.stopId == id);
                if (stop) return stop;
            }
            return null;
        }

        function initializeRoute(data) {
            stopsByDay = data.reduce((acc, stop) => {
                (acc[stop.day] = acc[stop.day] || []).push(stop);
                return acc;
            }, {});

            // ✅ 빈 Day도 유지
            const maxDay = Math.max(...data.map(stop => stop.day));
            for (let d = 1; d <= maxDay; d++) {
                if (!stopsByDay[d]) stopsByDay[d] = [];
            }

            dayKeys = Object.keys(stopsByDay).sort((a, b) => a - b);
            mapElements = {};

            document.getElementById('day-buttons').innerHTML = '';
            stopListContainer.innerHTML = '';

            dayKeys.forEach(day => {
                const stops = stopsByDay[day];
                const linePath = [];
                mapElements[day] = { overlays: [], polyline: null };

                const group = document.createElement('div');
                group.className = 'day-group';
                group.dataset.day = day;
                group.innerHTML = `<h3 class="day-title">Day ${day}</h3>`;
                stopListContainer.appendChild(group);

                stops.forEach(stop => {
                    const latlng = new kakao.maps.LatLng(stop.lat, stop.lng);
                    linePath.push(latlng);

                    const overlay = new kakao.maps.CustomOverlay({
                        position: latlng,
                        content: '<div class="custom-overlay-marker">' + stop.order + '</div>',
                        yAnchor: 1.2
                    });

                    mapElements[day].overlays.push(overlay);

                    const item = document.createElement('div');
                    item.className = 'stop-item';
                    item.draggable = true;
                    item.dataset.stopId = stop.stopId;
                    item.dataset.day = day;
                    item.innerHTML =
                        '<span class="stop-order">' + stop.order + '</span>' +
                        '<span class="stop-name">' + stop.name + '</span>';

                    item.addEventListener('click', () => map.panTo(latlng));
                    group.appendChild(item);
                });

                if (linePath.length > 0) {
                    const polyline = new kakao.maps.Polyline({
                        path: linePath,
                        strokeWeight: 4,
                        strokeColor: '#4a6cf7',
                        strokeOpacity: 0.9
                    });
                    mapElements[day].polyline = polyline;
                }
            });

            // 버튼 생성
            const btnContainer = document.getElementById('day-buttons');
            dayKeys.forEach(day => {
                const btn = document.createElement('button');
                btn.className = 'day-btn';
                btn.textContent = 'Day ' + day;
                btn.dataset.day = day;
                btnContainer.appendChild(btn);
            });
            btnContainer.addEventListener('click', e => {
                if (e.target.matches('.day-btn')) displayDay(e.target.dataset.day);
            });
        }

        function updateMapElements() {
            Object.values(mapElements).forEach(d => {
                d.overlays.forEach(o => o.setMap(null));
                if (d.polyline) d.polyline.setMap(null);
            });
            mapElements = {};

            for (const day of Object.keys(stopsByDay)) {
                const stops = stopsByDay[day];
                const linePath = [];
                mapElements[day] = { overlays: [], polyline: null };

                stops.forEach(stop => {
                    const latlng = new kakao.maps.LatLng(stop.lat, stop.lng);
                    linePath.push(latlng);
                    const overlay = new kakao.maps.CustomOverlay({
                        position: latlng,
                        content: '<div class="custom-overlay-marker">' + stop.order + '</div>',
                        yAnchor: 1.2
                    });

                    mapElements[day].overlays.push(overlay);
                });

                if (linePath.length > 0) {
                    const polyline = new kakao.maps.Polyline({
                        path: linePath,
                        strokeWeight: 4,
                        strokeColor: '#4a6cf7',
                        strokeOpacity: 0.9
                    });
                    mapElements[day].polyline = polyline;
                }
            }

            for (const day of Object.keys(mapElements)) {
                mapElements[day].overlays.forEach(o => o.setMap(map));
                if (mapElements[day].polyline) mapElements[day].polyline.setMap(map);
            }

            if (currentDay) displayDay(currentDay);
            kakao.maps.event.trigger(map, 'resize');
        }

        function displayDay(day) {
            currentDay = day;

            for (const d of Object.keys(mapElements)) {
                mapElements[d].overlays.forEach(o => o.setMap(null));
                if (mapElements[d].polyline) mapElements[d].polyline.setMap(null);
            }

            if (mapElements[day]) {
                mapElements[day].overlays.forEach(o => o.setMap(map));
                if (mapElements[day].polyline) mapElements[day].polyline.setMap(map);

                const bounds = new kakao.maps.LatLngBounds();
                stopsByDay[day].forEach(s => bounds.extend(new kakao.maps.LatLng(s.lat, s.lng)));
                if (!bounds.isEmpty()) map.setBounds(bounds);
            }

            const activeBtn = document.querySelector(`[data-day="${day}"]`);
            if (activeBtn) {
                document.querySelectorAll('.day-btn').forEach(b => b.classList.remove('active'));
                activeBtn.classList.add('active');
            }

            kakao.maps.event.trigger(map, 'resize');
        }

    } catch (err) {
        console.error("❌ 지도 로드 중 오류:", err);
        document.getElementById('map').innerHTML = "<h4 style='text-align:center;'>지도를 불러오는 중 오류가 발생했습니다.</h4>";
    }
});
</script>

</body>
</html>
