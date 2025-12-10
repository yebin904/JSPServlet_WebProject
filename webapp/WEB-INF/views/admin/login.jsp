<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>관리자 로그인</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/asset/css/admin.css">
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@300;400;600&family=Noto+Sans+KR:wght@400;500&display=swap" rel="stylesheet">
</head>
<body class="login-page-body">
    <div class="login-container">
        <div class="login-form">
            <h2>Admin Login</h2>
            <p>관리자 계정으로 로그인해주세요.</p>
            
            <form method="POST" action="${pageContext.request.contextPath}/admin/login.do">
                <div class="input-group">
                    <label for="adminName">아이디</label>
                    <%-- ★★★★★ name 속성을 snake_case로 수정 ★★★★★ --%>
                    <input type="text" id="adminName" name="admin_name" required autocomplete="off">
                </div>
                <div class="input-group">
                    <label for="adminPassword">비밀번호</label>
                    <%-- ★★★★★ name 속성을 snake_case로 수정 ★★★★★ --%>
                    <input type="password" id="adminPassword" name="admin_password" required>
                </div>
                <button type="submit" class="btn primary">Login</button>
            </form>
        </div>
    </div>
</body>
</html>