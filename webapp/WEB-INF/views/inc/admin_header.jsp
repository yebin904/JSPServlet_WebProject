<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%-- ▼▼▼ [핵심 수정] 사용자 헤더와 비슷한 구조와 클래스 이름으로 변경 ▼▼▼ --%>
<header id="admin-header-tuned">
    <div class="header-inner">
        <div class="header-column">
            <div class="logo">
                <a href="/trip/admin/main.do">Admin LOGO</a>
            </div>
        </div>

        <nav class="nav-menu">
            <a href="${pageContext.request.contextPath}/admin/report.do">신고 관리</a>
            <a href="${pageContext.request.contextPath}/admin/user/list.do">회원 관리</a>
            <a href="${pageContext.request.contextPath}/admin/accom/list.do">예약 관리</a>
            <a href="${pageContext.request.contextPath}/admin/stats/member.do">통계 관리</a>
        </nav>
        
        <div class="header-column header-right">
            <div class="user-info">
                <c:if test="${not empty adminName}">
                    <span><strong>${adminName}</strong> 관리자님</span>
                </c:if>
                <a href="/trip/admin/logout.do" class="btn-logout">로그아웃</a>
            </div>
        </div>
    </div>
</header>