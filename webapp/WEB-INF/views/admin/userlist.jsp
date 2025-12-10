<%-- 파일 경로: src/main/webapp/WEB-INF/views/admin/userlist.jsp --%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>회원 정보 조회</title>
<%@ include file="/WEB-INF/views/inc/admin_asset.jsp" %>
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.2/css/all.min.css"/>
</head>
<body>
    <div id="admin-container">
        <%@ include file="/WEB-INF/views/inc/admin_header.jsp" %>
        <main id="admin-main">
            <h1>회원 정보 조회</h1>

            <form method="GET" action="${pageContext.request.contextPath}/admin/user/list.do">
                <div class="search-bar">
                    <select name="searchType">
                        <option value="real_name" <c:if test="${param.searchType == 'real_name'}">selected</c:if>>이름</option>
                        <%-- ★★★★★ 핵심 수정: </c-if>를 </c:if>로 변경 ★★★★★ --%>
                        <option value="email" <c:if test="${param.searchType == 'email'}">selected</c:if>>이메일</option>
                    </select>
                    <input type="text" name="keyword" placeholder="검색어를 입력하세요" value="${param.keyword}">
                    <select name="status">
                        <option value="">전체 상태</option>
                        <option value="1" <c:if test="${param.status == '1'}">selected</c:if>>활동중</option>
                        <option value="2" <c:if test="${param.status == '2'}">selected</c:if>>정지</option>
                    </select>
                    <button type="submit" class="btn primary"><i class="fa-solid fa-magnifying-glass"></i> 검색</button>
                </div>
            </form>

            <table class="admin-table">
                <thead>
                    <tr>
                        <th>회원번호</th>
                        <th>닉네임</th>
                        <th>이름</th>
                        <th>이메일</th>
                        <th>가입일</th>
                        <th>상태</th>
                        <th>관리</th>
                    </tr>
                </thead>
                <tbody>
                    <c:if test="${empty userlist}">
                        <tr>
                            <td colspan="7" style="text-align:center;">조건에 맞는 회원이 없습니다.</td>
                        </tr>
                    </c:if>
                    <c:forEach items="${userlist}" var="user">
                        <tr>
                            <td>${user.userId}</td>
                            <td>${user.nickname}</td>
                            <td>${user.realName}</td>
                            <td>${user.email}</td>
                            <td><fmt:formatDate value="${user.regdate}" pattern="yyyy-MM-dd"/></td>
                            <td>${user.status}</td>
                            <td>
                                <c:if test="${user.status == '활동중'}">
                                    <button type="button" class="btn danger" style="padding: 5px 10px;"
                                        onclick="openSuspendModal('${user.userId}', '${user.nickname}')">정지</button>
                                </c:if>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </main>
    </div>

    <div id="suspendModal" class="modal">
        <div class="modal-content">
            <span class="close-button" onclick="closeSuspendModal()">&times;</span>
            <h2>회원 정지 처리</h2>
            <p><strong id="nicknameToSuspend"></strong> 회원을 정지하시겠습니까?</p>
            <form method="POST" action="${pageContext.request.contextPath}/admin/user/suspend.do">
                <input type="hidden" name="userId" id="userIdToSuspend">
                <div class="input-group">
                    <label for="reason">정지 사유</label>
                    <input type="text" name="reason" id="reason" class="form-control" required>
                </div>
                <div class="input-group">
                    <label for="duration">정지 기간 (일)</label>
                    <input type="number" name="duration" id="duration" class="form-control" value="7" required>
                </div>
                <button type="submit" class="btn primary">정지 실행</button>
            </form>
        </div>
    </div>
    <script>
        const modal = document.getElementById('suspendModal');
        function openSuspendModal(userId, nickname) {
            document.getElementById('userIdToSuspend').value = userId;
            document.getElementById('nicknameToSuspend').innerText = nickname;
            modal.style.display = 'block';
        }
        function closeSuspendModal() {
            modal.style.display = 'none';
        }
    </script>
</body>
</html>