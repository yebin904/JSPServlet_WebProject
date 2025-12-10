<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>게시글 신고</title>
<style>
    :root { 
        --primary: #6C9A8B;
        --danger: #DC3545;
        --danger-dark: #C82333;
        --border: #E9ECEF;
        --surface: #FFFFFF;
        --text: #212529;
        --radius: 12px;
    }
    body { 
        font-family: 'Noto Sans KR', sans-serif;
        padding: 30px; 
        margin: 0;
        background-color: #F8F9FA;
    }
    h1 { 
        margin-top: 0;
        margin-bottom: 25px;
        text-align: center;
        color: var(--text);
    }
    form div { 
        margin-bottom: 20px; 
    }
    label { 
        font-weight: bold; 
        display: block;
        margin-bottom: 8px;
    }
    select { 
        width: 100%; 
        padding: 12px; 
        box-sizing: border-box;
        border-radius: var(--radius);
        border: 1px solid var(--border);
        font-size: 1rem;
    }
    button { 
        width: 100%; 
        padding: 12px; 
        background-color: var(--danger); 
        color: white; 
        border: none; 
        cursor: pointer;
        border-radius: var(--radius);
        font-size: 1.1rem;
        font-weight: bold;
        transition: background-color 0.2s;
    }
    button:hover {
        background-color: var(--danger-dark);
    }
</style>
</head>
<body>
    <h1>게시글 신고</h1>

    <%-- ▼▼▼ [핵심 수정] onsubmit 이벤트 제거 ▼▼▼ --%>
    <form method="POST" action="/trip/findboard/report.do">
        <input type="hidden" name="boardSeq" value="${param.boardSeq}">
        <input type="hidden" name="reportedUserId" value="${param.reportedUserId}">
        
        <div>
            <label for="reason">신고 사유</label>
            <select id="reason" name="reason">
                <option value="스팸/홍보물">스팸/홍보물</option>
                <option value="음란물">음란물</option>
                <option value="도배">도배</option>
                <option value="정치글">정치글</option>
                <option value="기타">기타</option>
            </select>
        </div>
        
        <button type="submit">신고하기</button>
    </form>
</body>
</html>