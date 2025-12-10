<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <%@ include file="/WEB-INF/views/inc/asset.jsp" %>
    <style>
        /* (스타일 코드는 이전과 동일하므로 생략) */
        /* 지도 페이지 레이아웃 */
        body {
            padding-top: 70px;
            overflow: hidden;
        }
        .map-container {
            display: flex;
            height: calc(100vh - 70px); 
        }
        .sidebar {
            width: 380px;
            min-width: 380px;
            background-color: #fff;
            box-shadow: 2px 0 10px rgba(0,0,0,0.05);
            display: flex;
            flex-direction: column;
            z-index: 10;
        }
        .sidebar-header {
            padding: 20px;
            border-bottom: 1px solid var(--border);
            flex-shrink: 0;
        }
        #search-input {
            width: 100%;
            padding: 12px; 
            border-radius: 8px; 
            border: 1px solid var(--border); 
            box-sizing: border-box;
            margin-bottom: 15px;
        }
        .place-list-container {
            flex-grow: 1;
            overflow-y: auto;
        }
        
        /* 메인 콘텐츠 영역 (지도 + 랭킹) */
        .main-content {
            flex-grow: 1;
            display: flex;
            flex-direction: column;
            background-color: #f8f9fa; /* 전체 배경색 */
            padding: 20px;
            gap: 20px;
        }
        .map-area {
            flex-grow: 1;
            position: relative;
            border-radius: 12px;
            overflow: hidden; /* 둥근 모서리 적용을 위해 */
            box-shadow: 0 4px 15px rgba(0,0,0,0.1);
        }
        #map {
            width: 100%;
            height: 100%;
        }

        /* 하단 랭킹 카드 영역 */
        .ranking-container {
            flex-shrink: 0;
            height: 220px;
            background-color: #ffffff;
            border: 1px solid var(--border);
            border-radius: 12px;
            padding: 20px;
            display: flex;
            justify-content: center;
            align-items: center;
            gap: 20px;
            box-shadow: 0 4px 15px rgba(0,0,0,0.05);
        }
        .rank-card {
             flex: 1;
             max-width: 250px;
             text-align: center;
             cursor: pointer;
             background-color: #fff;
             border-radius: 16px;
             padding: 15px;
             transition: all 0.3s ease;
             border: 1px solid var(--border);
        }
        .rank-card:hover {
            transform: translateY(-5px);
            box-shadow: 0 8px 25px rgba(0,0,0,0.12);
        }
        .rank-card img {
            width: 100%;
            height: 120px;
            object-fit: cover;
            border-radius: 12px;
            margin-bottom: 10px;
        }
        .rank-card h5 {
            font-size: 1.1rem;
            margin: 0;
            white-space: nowrap;
            overflow: hidden;
            text-overflow: ellipsis;
        }

        /* 사이드바 버튼 그룹 스타일 */
        .button-group {
            display: grid;
            grid-template-columns: repeat(3, 1fr);
            gap: 10px;
            margin-bottom: 15px;
        }
        .button-group.restaurant-filters {
            grid-template-columns: repeat(4, 1fr);
        }
        .button-group.filter-group {
            grid-template-columns: repeat(3, 1fr);
        }
        .group-btn {
            padding: 10px;
            border: 1px solid var(--border);
            background-color: #fff;
            border-radius: 8px;
            cursor: pointer;
            transition: all 0.2s;
        }
        .group-btn.active {
            background-color: var(--primary-dark);
            color: white;
            border-color: var(--primary-dark);
            font-weight: bold;
        }

        /* 중앙 고정 마커 */
        #center-marker {
            position: absolute;
            top: 50%;
            left: 50%;
            width: 50px;
            height: 50px;
            transform: translate(-50%, -50%);
            z-index: 5;
            pointer-events: none;
            background-image: url('/trip/asset/images/free-icon-my-location-7233773.png');
            background-size: contain;
            background-repeat: no-repeat;
            animation: pulse 2s infinite;
        }
        @keyframes pulse {
            0% { transform: translate(-50%, -50%) scale(0.95); opacity: 0.8; }
            70% { transform: translate(-50%, -50%) scale(1.05); opacity: 1; }
            100% { transform: translate(-50%, -50%) scale(0.95); opacity: 0.8; }
        }
        
        /* 사이드바 목록 아이템 */
        .place-item {
            display: flex;
            padding: 15px; border-bottom: 1px solid var(--border);
            cursor: pointer; transition: background-color 0.2s;
        }
        .place-item:hover { background-color: #f8f9fa; }
        .place-item.selected { 
            background-color: #e6f3f0;
            border-left: 4px solid var(--primary-dark);
        }
        .place-item img {
            width: 100px;
            height: 100px; border-radius: 12px;
            object-fit: cover; margin-right: 15px; flex-shrink: 0;
        }
        .place-info h4 { margin: 0 0 5px 0; font-size: 1.1rem; }
        .place-info p { margin: 0; font-size: 0.9rem; color: #868e96; }
        .place-info .distance { font-weight: bold; color: var(--primary-dark); margin-top: 8px; }
    </style>
</head>
<body class="map-page">

    <%@ include file="/WEB-INF/views/inc/header.jsp" %>

    <div class="map-container">
        
        <aside class="sidebar">
            <div class="sidebar-header">
                
                <div class="button-group">
                    <button class="group-btn active" data-type="0">전체</button>
                    <button class="group-btn" data-type="1">관광지</button>
                    <button class="group-btn" data-type="2">맛집</button>
                </div>

                <input type="text" id="search-input" placeholder="이름 또는 주소로 검색">

                <div class="button-group filter-group" id="default-filters">
                    <button class="group-btn active" data-sort="distance">거리순</button>
                    <button class="group-btn" data-sort="popularity">인기순</button>
                    <button class="group-btn" data-sort="review">리뷰순</button>
                </div>
                
                <div class="button-group restaurant-filters" id="restaurant-filters" style="display: none;">
                    <button class="group-btn" data-sort="rating">평점순</button>
                    <button class="group-btn" data-sort="review">리뷰순</button>
                    <button class="group-btn" data-sort="distance">거리순</button>
                    <button class="group-btn" data-sort="price">가격순</button>
                </div>
            </div>
            
            <div class="place-list-container" id="place-list"></div>
        </aside>

        <main class="main-content">
            <section class="map-area">
                <div id="map"></div>
                <div id="center-marker"></div>
            </section>
            
            <section class="ranking-container" id="ranking-cards"></section>
        </main>
        
    </div>

    <script type="text/javascript" src="//dapi.kakao.com/v2/maps/sdk.js?appkey=09d09e9035bb509e8f002c6fab6b12ac&libraries=services"></script>
    <script>
        document.addEventListener("DOMContentLoaded", function() {

            // 1. --- 상태 및 상수 관리 ---
            const isLoggedIn = ${not empty sessionScope.id};

            const state = {
                allData: [],
                currentMarkers: [],
                selectedListItem: null,
                map: null,
                currentTypeId: 0,
                currentSortBy: 'distance'
            };

            const dom = {
                listContainer: document.getElementById('place-list'),
                rankingContainer: document.getElementById('ranking-cards'),
                searchInput: document.getElementById('search-input')
            };
            
            const markerImageInfo = {
                travel: { src: '/trip/asset/images/travel.png' },
                restaurant: { src: '/trip/asset/images/restaurant.png' }
            };

            // 2. --- 초기화 ---
            function init() {
                state.allData = ${empty allPlaceListJson ? '[]' : allPlaceListJson};
                if (state.allData.length === 0) {
                    console.warn("지도에 표시할 데이터가 없습니다.");
                }
                
                initMap();
                initEventListeners();
                render(); // 초기 렌더링
            }

            function initMap() {
                const mapContainer = document.getElementById('map');
                const mapOption = {
                    center: new kakao.maps.LatLng(37.498085, 127.027977),
                    level: 7
                };
                state.map = new kakao.maps.Map(mapContainer, mapOption);
            }

            function initEventListeners() {
                // 지도의 드래그, 줌 등 모든 움직임이 끝나면('idle') 화면 내 콘텐츠를 다시 계산하고 렌더링합니다.
                kakao.maps.event.addListener(state.map, 'idle', render);

                // 검색창 입력 이벤트
                dom.searchInput.addEventListener('input', () => {
                    if (!isLoggedIn) {
                        alert('로그인한 사용자만 사용 가능합니다.');
                        dom.searchInput.value = '';
                        return;
                    } 
                    render(true); // isSearch = true
                });
                
                // 모든 필터 버튼 이벤트
                document.querySelectorAll('.button-group .group-btn').forEach(button => {
                    button.addEventListener('click', handleFilterClick);
                });
            }

            // 3. --- 이벤트 핸들러 ---
            function handleFilterClick(e) {
                if (!isLoggedIn) {
                    alert('로그인한 사용자만 사용 가능합니다.');
                    return;
                }

                const button = e.currentTarget;
                const group = button.parentElement;

                if (group.querySelector('.group-btn.active')) {
                    group.querySelector('.group-btn.active').classList.remove('active');
                }
                button.classList.add('active');

                if (button.dataset.type) {
                    state.currentTypeId = parseInt(button.dataset.type, 10);
                } else if (button.dataset.sort) {
                    state.currentSortBy = button.dataset.sort;
                }
                
                render();
            }

            // 4. --- 렌더링 주 함수 ---
            function render(isSearch = false) {
                
                const level = state.map.getLevel();

                // 4.1 줌 레벨 9 이상이면 모든걸 숨기고 종료
                if (level >= 9) {
                    clearOldRenders();
                    dom.listContainer.innerHTML = '<p style="text-align: center; color: #868e96; padding-top: 20px;">지도를 확대하면 장소가 표시됩니다.</p>';
                    dom.rankingContainer.innerHTML = '<p style="color: #868e96;">지도를 확대해주세요.</p>';
                    return;
                }

                // 4.2 데이터 필터링 (타입, 검색어, 지도 영역)
                const filteredData = getFilteredData();

                // 4.3 데이터 정렬
                const sortedData = getSortedData(filteredData);

                // 4.4 화면 그리기
                clearOldRenders();

                if (sortedData.length === 0) {
                    dom.listContainer.innerHTML = '<p style="text-align: center; color: #868e96; padding-top: 20px;">현재 화면에 표시할 장소가 없습니다.</p>';
                    dom.rankingContainer.innerHTML = '<p style="color: #868e96;">표시할 항목이 없습니다.</p>';
                    return;
                }

                renderMarkers(sortedData);
                renderRankingCards(sortedData.slice(0, 3));
                renderPlaceList(sortedData);

                if (isSearch && sortedData.length > 0) {
                    state.map.panTo(new kakao.maps.LatLng(sortedData[0].lat, sortedData[0].lng));
                }
            }

            // 5. --- 데이터 처리 보조 함수 ---
            function getFilteredData() {
                const searchTerm = dom.searchInput.value.toLowerCase();
                const bounds = state.map.getBounds();
                
                return state.allData.filter(item => {
                    // 타입 필터
                    const typeMatch = (state.currentTypeId === 0 || item.typeId === state.currentTypeId);
                    if (!typeMatch) return false;

                    // 지도 영역 필터
                    const position = new kakao.maps.LatLng(item.lat, item.lng);
                    const boundsMatch = bounds.contain(position);
                    if (!boundsMatch) return false;

                    // 검색어 필터
                    if (searchTerm) {
                        const nameMatch = item.name && item.name.toLowerCase().includes(searchTerm);
                        const addressMatch = item.address && item.address.toLowerCase().includes(searchTerm);
                        return nameMatch || addressMatch;
                    }
                    
                    return true;
                });
            }

            function getSortedData(data) {
                const center = state.map.getCenter();
                const currentCenter = { lat: center.getLat(), lng: center.getLng() };

                data.forEach(item => {
                    item.distance = getDistance(currentCenter, item);
                });

                return data.sort((a, b) => {
                    switch (state.currentSortBy) {
                        case 'popularity': return b.popularity - a.popularity;
                        case 'review': return b.reviews - a.reviews;
                        case 'rating': return b.rating - a.rating;
                        case 'price': return a.price - b.price;
                        case 'distance':
                        default: return a.distance - b.distance;
                    }
                });
            }

            function getDistance(loc1, loc2) {
                if (!loc1 || !loc2 || !loc1.lat || !loc1.lng || !loc2.lat || !loc2.lng) return 0;
                const R = 6371; // km
                const dLat = (loc2.lat - loc1.lat) * Math.PI / 180;
                const dLon = (loc2.lng - loc1.lng) * Math.PI / 180;
                const a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                          Math.cos(loc1.lat * Math.PI / 180) * Math.cos(loc2.lat * Math.PI / 180) *
                          Math.sin(dLon / 2) * Math.sin(dLon / 2);
                const c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
                return R * c;
            }

            // 6. --- 렌더링 보조 함수 ---
            function clearOldRenders() {
                state.currentMarkers.forEach(marker => marker.setMap(null));
                state.currentMarkers = [];
                dom.listContainer.innerHTML = '';
                dom.rankingContainer.innerHTML = '';
            }

            function renderMarkers(data) {
                const level = state.map.getLevel();
                data.forEach(item => {
                    const markerImage = getMarkerImageForLevel(item.typeId, level);
                    if (!markerImage) return; // 9레벨 이상이거나 타입 없으면 생성 안함

                    const marker = new kakao.maps.Marker({
                        map: state.map,
                        position: new kakao.maps.LatLng(item.lat, item.lng),
                        title: item.name,
                        image: markerImage
                    });
                    state.currentMarkers.push(marker);
                });
            }

            function getMarkerImageForLevel(typeId, level) {
                let src, size;
                const isTravel = typeId === 1;

                switch (level) {
                    case 1: case 2: case 3: case 4: case 5:
                        size = new kakao.maps.Size(isTravel ? 44 : 38, isTravel ? 44 : 38);
                        break;
                    case 6:
                        size = new kakao.maps.Size(isTravel ? 36 : 30, isTravel ? 36 : 30);
                        break;
                    case 7:
                        size = new kakao.maps.Size(isTravel ? 30 : 24, isTravel ? 30 : 24);
                        break;
                    case 8:
                        size = new kakao.maps.Size(22, 22);
                        break;
                    default: return null;
                }

                if (isTravel) {
                    src = markerImageInfo.travel.src;
                } else if (typeId === 2) {
                    src = markerImageInfo.restaurant.src;
                } else {
                    return null;
                }

                const options = { offset: new kakao.maps.Point(size.width / 2, size.height) };
                return new kakao.maps.MarkerImage(src, size, options);
            }

            function renderRankingCards(data) {
                data.forEach(item => {
                    const rankCard = document.createElement('div');
                    rankCard.className = 'rank-card';
                    rankCard.innerHTML = createCardHTML(item);
                    rankCard.onclick = () => {
                        /* if (!isLoggedIn) { alert('로그인한 사용자만 사용 가능합니다.'); return; } */
                        state.map.panTo(new kakao.maps.LatLng(item.lat, item.lng));
                    };
                    dom.rankingContainer.appendChild(rankCard);
                });
            }

            function renderPlaceList(data) {
                data.forEach(item => {
                    const listItem = document.createElement('div');
                    listItem.className = 'place-item';
                    listItem.innerHTML = createListItemHTML(item);
                    listItem.onclick = () => handleListItemClick(listItem, item);
                    dom.listContainer.appendChild(listItem);
                });
            }
            
            function handleListItemClick(listItem, item) {
                if (!isLoggedIn) { alert('로그인한 사용자만 사용 가능합니다.'); return; }
                const isAlreadySelected = listItem.classList.contains('selected');
                if (state.selectedListItem) state.selectedListItem.classList.remove('selected');
                if (isAlreadySelected) {
                    state.selectedListItem = null;
                } else {
                    listItem.classList.add('selected');
                    state.selectedListItem = listItem;
                    state.map.panTo(new kakao.maps.LatLng(item.lat, item.lng));
                }
            }

            // 7. --- HTML 생성 ---
            function createCardHTML(item) {
                const defaultImg = "https://placehold.co/250x120/E9ECEF/495057?text=No+Image";
                return `<img src="${pageContext.request.contextPath}/\${item.imageUrl || defaultImg}" alt="\${item.name || ''}" onerror="this.src='\${defaultImg}'"><h5>\${item.name || ''}</h5>`;
            }

            function createListItemHTML(item) {
                const defaultImg = "https://placehold.co/100x100/E9ECEF/495057?text=No+Image";
                return `<img src="${pageContext.request.contextPath}/\${item.imageUrl || defaultImg}" alt="\${item.name || ''}" onerror="this.src='\${defaultImg}'"><div class="place-info"><h4>\${item.name || ''}</h4><p>\${item.address || ''}</p><p class="distance">중심 위치로부터 약 \${item.distance ? item.distance.toFixed(1) : 'N/A'}km</p></div>`;
            }

            // 8. --- 앱 실행 ---
            init();
        });
    </script>
</body>
</html>