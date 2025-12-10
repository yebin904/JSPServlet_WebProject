<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

 
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>여행 추천 루트 게시글 작성</title>
<%@ include file="/WEB-INF/views/inc/asset.jsp" %>
<!-- 외부 CSS 연결 -->
<link rel="stylesheet" href="${pageContext.request.contextPath}/asset/css/routepost.css">

</head>
<body>
<%@ include file="/WEB-INF/views/inc/header.jsp" %>

<div class="container">
    <h2>여행 추천 루트 게시글 작성 ✈️</h2>

    <!-- ✅ POST 요청 시 RoutePostAdd 서블릿으로 이동 -->
    <form method="post" 
          action="${pageContext.request.contextPath}/routepost/add.do" 
          enctype="multipart/form-data">
        
        <!-- 제목 -->
        <label for="title">제목</label>
        <input type="text" id="title" name="title" placeholder="제목을 입력하세요." required>

        <!-- 루트 선택 -->
        <label for="route_id">루트 선택</label>
        <select id="route_id" name="route_id" required>
            <option value="">-- 여행 루트를 선택하세요 --</option>
            <option value="1">서울 → 강릉 1박 2일 루트</option>
            <option value="2">부산 해운대 당일치기 루트</option>
            <option value="3">제주도 2박 3일 루트</option>
        </select>

        <!-- 내용 -->
        <label for="content">여행 후기 내용</label>
        <textarea id="content" name="content" placeholder="여행에 대한 후기를 입력하세요." required></textarea>

        <!-- 만족도 -->
        <label for="satisfaction">만족도 (0.0 ~ 5.0)</label>
        <input type="number" id="satisfaction" name="satisfaction" min="0" max="5" step="0.1" value="5.0" required>

        <!-- 이미지 업로드 -->
        <label for="images">여행 사진 업로드 (여러장 선택 가능)</label>
        <input type="file" id="images" name="images" accept="image/*" multiple>

        <!-- 버튼 영역 -->
        <div class="btn-area">
            <!-- ✅ 등록 시 RoutePostAdd 서블릿이 처리 후 list.do로 redirect -->
            <button type="submit" class="btn btn-submit">등록하기</button>
            
            <!-- ✅ 취소 시 list.do로 바로 이동 -->
            <button type="button" class="btn btn-cancel"
                    onclick="location.href='${pageContext.request.contextPath}/routepost/list.do'">
                취소
            </button>
        </div>
    </form>
</div>

</body>
<%@ include file="/WEB-INF/views/inc/route_footer.jsp" %>
</html>
