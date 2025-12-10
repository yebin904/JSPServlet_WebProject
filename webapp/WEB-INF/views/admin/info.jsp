<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>관리자 정보</title>
<%@ include file="/WEB-INF/views/inc/admin_asset.jsp" %>
</head>
<body>
    <%@ include file="/WEB-INF/views/inc/admin_header.jsp" %>
    
    <main id="admin-main">
        <h1>관리자 정보</h1>
        
        <div class="info-container">
            <img src="https://i.imgur.com/sMyA4sR.png" alt="Admin Avatar" class="profile-pic">
            
            <table class="info-table">
                <tr>
                    <th>이름</th>
                    <%-- ★★★★★ DTO의 필드명(camelCase)에 맞게 수정 ★★★★★ --%>
                    <td>${admin.adminRealName}</td>
                </tr>
                <tr>
                    <th>아이디</th>
                    <%-- ★★★★★ DTO의 필드명(camelCase)에 맞게 수정 ★★★★★ --%>
                    <td>${admin.adminName}</td>
                </tr>
                 <tr>
                    <th>이메일</th>
                    <%-- ★★★★★ DTO의 필드명(camelCase)에 맞게 수정 ★★★★★ --%>
                    <td>${admin.adminEmail}</td>
                </tr>
                 <tr>
                    <th>가입일</th>
                    <td>
                        <%-- ★★★★★ DTO의 필드명(camelCase)에 맞게 수정 ★★★★★ --%>
                        <fmt:formatDate value="${admin.adminRegdate}" pattern="yyyy-MM-dd" />
                    </td>
                </tr>
            </table>
        </div>

        <div class="button-container">
            <button type="button" class="btn primary" onclick="location.href='${pageContext.request.contextPath}/admin/main.do'">메인으로</button>
            <button type="button" class="btn danger" onclick="location.href='${pageContext.request.contextPath}/admin/logout.do'">로그아웃</button>
        </div>
    </main>
</body>
</html>