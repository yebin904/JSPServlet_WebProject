<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>여행 트렌드</title>
    <%@ include file="/WEB-INF/views/inc/asset.jsp" %>
    <style>
        main {
            max-width: 1200px;
            margin: 40px auto;
            padding: 0 30px;
        }
        .trend-header {
            text-align: center;
            margin-bottom: 50px;
        }
        .trend-header h2 {
            font-size: 2.5rem;
            font-weight: 700;
            color: var(--text);
            margin-bottom: 10px;
        }
        .trend-header .search-box {
            max-width: 500px;
            margin: 0 auto;
            position: relative;
        }
        .trend-header .search-box input {
            width: 100%;
            padding: 12px 20px;
            border: 1px solid var(--border);
            border-radius: 30px;
            background: var(--surface);
            transition: all 0.3s;
            font-size: 1rem;
        }
        .trend-header .search-box input:focus {
            outline: none;
            border-color: var(--primary);
            box-shadow: 0 0 0 3px rgba(108, 154, 139, 0.1);
        }

        .trend-gallery {
            display: grid;
            grid-template-columns: repeat(auto-fill, minmax(320px, 1fr));
            gap: 30px;
        }

        .trend-card {
            background-color: var(--surface);
            border-radius: var(--radius);
            box-shadow: var(--shadow-soft);
            overflow: hidden;
            transition: all 0.3s ease;
            cursor: pointer;
        }
        .trend-card:hover {
            transform: translateY(-5px);
            box-shadow: 0 12px 40px rgba(0,0,0,0.08);
        }

        .trend-card .card-img-top {
            width: 100%;
            height: 200px;
            object-fit: cover;
        }

        .trend-card .card-body {
            padding: 25px;
        }

        .trend-card .card-title {
            font-size: 1.4rem;
            font-weight: 700;
            margin: 0 0 10px 0;
            color: var(--text);
        }

        .trend-card .card-text {
            font-size: 0.95rem;
            color: var(--text-light);
            margin-bottom: 20px;
            height: 4.5em; /* 약 3줄 높이 */
            overflow: hidden;
            text-overflow: ellipsis;
            display: -webkit-box;
            -webkit-line-clamp: 3;
            -webkit-box-orient: vertical;
        }

        .trend-card .card-tags {
            display: flex;
            flex-wrap: wrap;
            gap: 8px;
        }

        .trend-card .tag {
            background-color: var(--bg);
            color: var(--primary);
            font-size: 0.8rem;
            font-weight: 500;
            padding: 5px 12px;
            border-radius: 20px;
            border: 1px solid var(--border);
        }
    </style>
</head>
<body>

    <%@ include file="/WEB-INF/views/inc/header.jsp" %>

    <main>
        <div class="trend-header">
            <h2>여행 트렌드</h2>
            <div class="search-box">
                <input type="text" id="tag-search" placeholder="키워드로 트렌드 검색...">
            </div>
        </div>

        <div class="trend-gallery" id="trend-gallery">
            <c:forEach var="item" items="${trendList}">
                <div class="trend-card" data-keywords="${item.place_name} <c:forEach var='k' items='${item.keywords}'>#${k}</c:forEach>">
                    <img src="${pageContext.request.contextPath}/${item.place_main_image_url}" alt="${item.place_name}" class="card-img-top">
                    <div class="card-body">
                        <h5 class="card-title">${item.place_name}</h5>
                        <p class="card-text">${item.place_description}</p>
                        <div class="card-tags">
                            <c:forEach var="keyword" items="${item.keywords}">
                                <span class="tag">#${keyword}</span>
                            </c:forEach>
                        </div>
                    </div>
                </div>
            </c:forEach>
        </div>
    </main>

    <script>
        document.addEventListener('DOMContentLoaded', function () {
            const isLoggedIn = ${not empty sessionScope.id};
            const searchInput = document.getElementById('tag-search');
            const gallery = document.getElementById('trend-gallery');
            const cards = gallery.getElementsByClassName('trend-card');

            searchInput.addEventListener('input', function () {
                if (!isLoggedIn) {
                    alert('로그인한 사용자만 사용 가능합니다.');
                    searchInput.value = '';
                    return;
                }

                const searchTerm = searchInput.value.toLowerCase();

                for (let i = 0; i < cards.length; i++) {
                    const card = cards[i];
                    const keywords = card.dataset.keywords.toLowerCase();
                    
                    if (keywords.includes(searchTerm)) {
                        card.style.display = 'block';
                    } else {
                        card.style.display = 'none';
                    }
                }
            });
        });
    </script>

</body>
</html>
