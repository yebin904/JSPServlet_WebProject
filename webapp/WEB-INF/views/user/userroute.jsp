<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <%@ include file="/WEB-INF/views/inc/asset.jsp" %>
    <%-- jQuery 라이브러리를 꼭 추가해주세요 --%>
    <script src="https://code.jquery.com/jquery-3.7.1.min.js"></script>
    
    <%-- ▼▼▼ 분리한 CSS 파일을 연결합니다 ▼▼▼ --%>
    <link rel="stylesheet" href="/trip/asset/css/userroute.css">

</head>
<body>

    <%@ include file="/WEB-INF/views/inc/header.jsp" %>

    <div id="main">
        <h1>내 여행 루트</h1>

        <div id="route-list-body">
            <c:forEach items="${list}" var="dto">
                <div class="route-item" onclick="location.href='/trip/route/userRouteView.do?id=${dto.seq}'">
                    <div class="route-title">${dto.userroutetitle}</div>
                    <div class="route-details">
                        <span>인원수: ${dto.userroutedays}</span>
                        <span>여행시작일: ${dto.userroutestartdate}</span>
                        <span>여행종료일: ${dto.userrouteenddate}</span>
                    </div>
                    
                </div>
                <form action="${pageContext.request.contextPath}/reservation/accomList.do" method="get">
    <input type="hidden" name="start_date" value="${dto.userroutestartdate}">
    <input type="hidden" name="end_date" value="${dto.userrouteenddate}">
    <input type="hidden" name="people" value="${dto.userroutedays}">

    <button type="submit" class="btn btn-primary">예약하기</button>
</form>
            
            </c:forEach>
        </div>

        <c:if test="${nowPage < totalPage}">
            <div class="load-more-container">
                <button id="loadMoreBtn">더보기</button>
            </div>
        </c:if>
    </div>

    <%-- JavaScript 부분은 변경 없이 그대로 둡니다. --%>
    <script>
        let currentPage = ${nowPage};
        const totalPage = ${totalPage};

        $('#loadMoreBtn').on('click', function() {
            currentPage++;

            $.ajax({
                type: 'GET',
                url: '/trip/user/userroute.do',
                data: {
                    page: currentPage,
                    ajax: 'true'
                },
                dataType: 'json',
                success: function(newList) {
                    if (newList.length > 0) {
                        newList.forEach(function(dto) {
                            const newRow = `
                                <div class="route-item" onclick="location.href='/trip/user/userroute.do?seq=\${dto.seq}'">
                                    <div class="route-title">\${dto.userroutetitle}</div>
                                    <div class="route-details">
                                        <span>인원수: \${dto.userroutedays}</span>
                                        <span>여행시작일: \${dto.userroutestartdate}</span>
                                        <span>여행종료일: \${dto.userrouteenddate}</span>
                                    </div>
                                </div>
                            `;
                            $('#route-list-body').append(newRow);
                        });
                    }

                    if (currentPage >= totalPage) {
                        $('#loadMoreBtn').parent().hide();
                    }
                },
                error: function(err) {
                    console.log('데이터를 불러오는 데 실패했습니다.', err);
                    alert('오류가 발생했습니다. 다시 시도해주세요.');
                }
            });
        });
    </script>

</body>
</html>