<%-- 파일 경로: src/main/webapp/WEB-INF/views/admin/suspendedlist.jsp --%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>정지된 회원 목록</title>
<%@ include file="/WEB-INF/views/inc/admin_asset.jsp" %>
</head>
<body>
    <div id="admin-container">
        <%@ include file="/WEB-INF/views/inc/admin_header.jsp" %>
        <main id="admin-main">
            <h1>정지된 회원 목록</h1>
            <table class="admin-table">
                <thead>
                    <tr>
                        <th>정지 번호</th>
                        <th>닉네임</th>
                        <th>정지 사유</th>
                        <th>정지 시작일</th>
                        <th>정지 종료일</th>
                        <th>관리</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach items="${suspendedlist}" var="user">
                        <tr>
                            <td>${user.memsuspendedId}</td>
                            <td>${user.nickname}</td>
                            <td>${user.suspendedReason}</td>
                            <td><fmt:formatDate value="${user.suspendedStartDate}" pattern="yyyy-MM-dd"/></td>
                            <td><fmt:formatDate value="${user.suspendedEndDate}" pattern="yyyy-MM-dd"/></td>
                            <td>
                                <form method="POST" action="${pageContext.request.contextPath}/admin/user/restore.do" onsubmit="return confirm('[${user.nickname}] 회원을 정말로 복구하시겠습니까?');">
                                    <input type="hidden" name="userId" value="${user.userId}">
                                    <button type="submit" class="btn primary" style="padding: 5px 10px;">복구</button>
                                </form>
                            </td>
                        </tr>
                    </c:forEach>
                    <c:if test="${suspendedlist.size() == 0}">
                        <tr>
                            <td colspan="6" style="text-align: center;">정지된 회원이 없습니다.</td>
                        </tr>
                    </c:if>
                </tbody>
            </table>
        </main>
    </div>
</body>
</html>