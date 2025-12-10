<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>신고 접수 목록</title>
<%@ include file="/WEB-INF/views/inc/admin_asset.jsp" %>
</head>
<body>
    <div id="admin-container">
        <%@ include file="/WEB-INF/views/inc/admin_header.jsp" %>
        <main id="admin-main">
            <h1>신고 접수 목록</h1>
            <table class="admin-table">
                <thead>
                    <tr>
                        <th>신고 번호</th>
                        <th>신고된 게시글</th>
                        <th>신고자</th>
                        <th>신고 대상</th>
                        <th>신고 사유</th>
                        <th>신고일</th>
                        <th>처리</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach items="${list}" var="dto">
                        <tr>
                            <td>${dto.reportId}</td>
<%-- ✅ [수정 후] 이 코드로 교체해주세요 --%>
<td>
    <c:choose>
        <%-- 1. 동행찾기 게시판인 경우 --%>
        <c:when test="${dto.reportTargetType == 'findboard'}">
            <a href="${pageContext.request.contextPath}/findboard/view.do?id=${dto.reportTargetId}" target="_blank">
                ${dto.postTitle}
            </a>
        </c:when>

        <%-- 2. 질문 게시판인 경우 --%>
        <c:when test="${dto.reportTargetType == 'question'}">
            <a href="${pageContext.request.contextPath}/question/view.do?id=${dto.reportTargetId}" target="_blank">
                ${dto.postTitle}
            </a>
        </c:when>
        
        <%-- 3. 여행 후기 게시판인 경우 --%>
        <c:when test="${dto.reportTargetType == 'review'}">
            <a href="${pageContext.request.contextPath}/review/view.do?id=${dto.reportTargetId}" target="_blank">
                ${dto.postTitle}
            </a>
        </c:when>

        <%-- 4. 핫딜 게시판인 경우 --%>
        <c:when test="${dto.reportTargetType == 'hotdeal'}">
            <a href="${pageContext.request.contextPath}/hotdeal/view.do?id=${dto.reportTargetId}" target="_blank">
                ${dto.postTitle}
            </a>
        </c:when>

        <%-- 5. 그 외 (삭제되었거나 알 수 없는 경우) --%>
        <c:otherwise>
            ${dto.postTitle}
        </c:otherwise>
    </c:choose>
</td>                            <td>${dto.reporterNickname}</td>
                            <td>${dto.reportedUserNickname}</td>
                            <td>${dto.reportReasonType}</td>
                            <td><fmt:formatDate value="${dto.reportRegdate}" pattern="yyyy-MM-dd"/></td>
                            <td style="display: flex; gap: 5px;">
                                <form method="POST" action="${pageContext.request.contextPath}/admin/processReport.do" onsubmit="return confirm('이 게시글을 숨김 처리하시겠습니까?');">
                                    <%-- DTO 변경에 따라 input name과 value의 변수명을 모두 camelCase로 수정 --%>
                                    <input type="hidden" name="reportId" value="${dto.reportId}">
                                    <input type="hidden" name="targetId" value="${dto.reportTargetId}">
                                    <input type="hidden" name="targetType" value="${dto.reportTargetType}">
                                    <input type="hidden" name="action" value="approve">
                                    <button type="submit" class="btn primary" style="padding: 5px 10px;">숨김</button>
                                </form>
                                <form method="POST" action="${pageContext.request.contextPath}/admin/processReport.do" onsubmit="return confirm('이 신고를 반려 처리하시겠습니까?');">
                                    <input type="hidden" name="reportId" value="${dto.reportId}">
                                    <input type="hidden" name="action" value="reject">
                                    <button type="submit" class="btn danger" style="padding: 5px 10px;">반려</button>
                                </form>
                            </td>
                        </tr>
                    </c:forEach>
                    <c:if test="${empty list}">
                        <tr>
                            <td colspan="7" style="text-align: center;">처리할 신고 내역이 없습니다.</td>
                        </tr>
                    </c:if>
                </tbody>
            </table>
        </main>
    </div>
</body>
</html>