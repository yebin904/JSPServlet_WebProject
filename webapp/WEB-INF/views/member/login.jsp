<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>로그인</title>
</head>
<body>
    <h1>회원 로그인</h1>
    <p>테스트 계정: seokyong / 1111 또는 seungwoo / 1111</p>
    <form method="POST" action="/trip/member/login.do">
        <div>
            아이디: <input type="text" name="username" required value="seokyong">
        </div>
        <div>
            비밀번호: <input type="password" name="password" required value="1111">
        </div>
        <button type="submit">로그인</button>
    </form>
</body>
</html>