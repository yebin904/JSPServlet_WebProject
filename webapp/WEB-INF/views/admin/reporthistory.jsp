<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>신고 처리 내역</title>
<%@ include file="/WEB-INF/views/inc/admin_asset.jsp" %>
</head>
<body>
    <div id="admin-container">
        <%@ include file="/WEB-INF/views/inc/admin_header.jsp" %>
        <main id="admin-main">
            <h1>신고 처리 내역</h1>
            <table class="admin-table">
                <thead>
                    <tr>
                        <th>신고 번호</th>
                        <th>신고된 게시글</th>
                        <th>신고자</th>
                        <th>신고 대상</th>
                        <th>신고 사유</th>
                        <th>신고일</th>
                        <th>처리 상태</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach items="${list}" var="dto">
                        <tr>
                            <%-- DTO 변경에 따라 모든 변수명을 camelCase로 수정 --%>
                            <td>${dto.reportId}</td>
                            <td>${dto.postTitle}</td>
                            <td>${dto.reporterNickname}</td>
                            <td>${dto.reportedUserNickname}</td>
                            <td>${dto.reportReasonType}</td>
                            <td><fmt:formatDate value="${dto.reportRegdate}" pattern="yyyy-MM-dd"/></td>
                            <td>
                                <c:if test="${dto.reportStatus == 'APPROVED'}">
                                    <span style="color: blue; font-weight: bold;">승인(숨김)</span>
                                </c:if>
                                <c:if test="${dto.reportStatus == 'REJECTED'}">
                                    <span style="color: red; font-weight: bold;">반려</span>
                                </c:if>
                            </td>
                        </tr>
                    </c:forEach>
                    <c:if test="${empty list}">
                        <tr>
                            <td colspan="7" style="text-align: center;">처리된 신고 내역이 없습니다.</td>
                        </tr>
                    </c:if>
                </tbody>
            </table>
        </main>
    </div>
</body>
</html>