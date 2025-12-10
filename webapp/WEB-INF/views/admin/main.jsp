<%-- 파일 경로: src/main/webapp/WEB-INF/views/admin/main.jsp --%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>관리자 메인</title>
    <%@ include file="/WEB-INF/views/inc/admin_asset.jsp" %>
</head>
<body>
    
    <%@ include file="/WEB-INF/views/inc/admin_header.jsp" %>
    
    <main id="admin-main">
        <h1>관리자 페이지</h1>
        
        <div id="dashboard">
        
            <%-- 1. 신고 내역 관리 --%>
            <div class="admin-card">
                <h2 class="card-title">1. 신고 내역 관리</h2>
                <ul class="card-links">
                    <li><a href="${pageContext.request.contextPath}/admin/report.do">신고 접수 목록</a></li>
                    <li><a href="${pageContext.request.contextPath}/admin/report/history.do">처리 내역 보기</a></li>
                </ul>
            </div>
            
            <%-- 2. 회원 관리 --%>
            <div class="admin-card">
                <h2 class="card-title">2. 회원 관리</h2>
                <ul class="card-links">
                    <li><a href="${pageContext.request.contextPath}/admin/user/list.do">회원 정보 조회</a></li>
                    <li><a href="${pageContext.request.contextPath}/admin/board.do">전체 게시글 조회</a></li>
                    <li><a href="${pageContext.request.contextPath}/admin/user/suspendedList.do">정지 계정 리스트</a></li>
                </ul>
            </div>
           
            <%-- 3. 예약 관리 --%>
           <div class="admin-card">
		    	<h2 class="card-title">3. 예약 관리</h2>
    			<ul class="card-links">
        			<li><a href="${pageContext.request.contextPath}/admin/accom/list.do">숙소 관리</a></li>
        			<li><a href="${pageContext.request.contextPath}/admin/car/list.do">렌터카 관리</a></li>
    			</ul>
		   </div>
            
            <div class="admin-card">
                <h2 class="card-title">4. 통계 관리</h2>
                <ul class="card-links">
                    <li><a href="${pageContext.request.contextPath}/admin/stats/member.do">회원 통계</a></li>
      			    <li><a href="${pageContext.request.contextPath}/admin/stats/usage.do">이용 통계</a></li>      			  
      			    <li><a href="${pageContext.request.contextPath}/admin/stats/content.do">콘텐츠 통계</a></li>
                </ul>
            </div>
            
        </div>
    </main>
</body>
</html>