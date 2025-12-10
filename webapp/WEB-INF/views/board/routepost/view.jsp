<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="ko">
<head>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/asset/css/routepost.css">
  <meta charset="UTF-8">
  <title>루트 추천 게시판_view</title>
  <%@ include file="/WEB-INF/views/inc/asset.jsp" %>
  <style>
    .comment-item{border-bottom:1px solid #ddd; padding:10px 0;}
    .page-btn{margin:0 2px;}
    .page-btn.active{background:#333; color:#fff;}
  </style>
</head>
<body>
<%@ include file="/WEB-INF/views/inc/header.jsp" %>


<div class="container">

  <!-- 제목 / 작성자 / 조회수 -->
  <div class="header-area">
    <div>
      <h2><c:out value="${dto.routepostTitle}"/></h2>
      <div class="post-info">
        작성자: <c:out value="${dto.nickname}"/> |
        작성일: <c:out value="${dto.routepostRegdate}"/> |
        조회수: <c:out value="${dto.routepostViewCount}"/>
      </div>
    </div>
  </div>

  <!-- 루트 선택 -->
  <label for="route_id">루트 선택</label>
  <select id="route_id" name="route_id" required>
    <option value="">-- 여행 루트를 선택하세요 --</option>
    <option value="1">서울 → 강릉 1박 2일 루트</option>
    <option value="2">부산 해운대 당일치기 루트</option>
    <option value="3">제주도 2박 3일 루트</option>
  </select>

  <!-- 내용 -->
  <div class="content-area">
    <c:out value="${dto.routepostContent}" escapeXml="false"/>
  </div>

		<div class="image-area-wrapper">
	  <span class="image-nav-arrow left">&#10094;</span>
  
	  <div class="image-area">
	    <c:forEach var="img" items="${dto.images}">
	      <img class="routepost-img"
	           src="${pageContext.request.contextPath}/uploads/routepost/${img.routepostImageUrl}"
	           alt="게시글 이미지">
	    </c:forEach>
	  </div>
	
	  <span class="image-nav-arrow right">&#10095;</span>
	</div>

	
	<!-- 모달 -->
	<div class="image-modal" id="imageModal">
	  <span class="modal-close">&times;</span>
	  <span class="modal-arrow left">&#10094;</span>
	  <img id="modalImage" src="">
	  <span class="modal-arrow right">&#10095;</span>
	  <div class="modal-counter"></div>
	</div>




  <!--  추천 & 스크랩 (본문 바로 아래, 중앙) -->
	<div class="btn-action-area">
	  <button id="btn-like" data-id="${dto.routepostId}">👍 추천</button>
	  <button id="btn-scrap" data-id="${dto.routepostId}">📌 스크랩</button>
	</div>
	
	<!--  목록 / 수정 / 삭제 (댓글 위, 오른쪽 정렬) -->
	<div class="btn-area">
  <a href="${pageContext.request.contextPath}/routepost/list.do" class="btn-list">목록</a>

  <div class="btn-edit-group">
    <c:if test="${sessionScope.user != null and sessionScope.user.userId == dto.userId}">
      <a href="${pageContext.request.contextPath}/routepost/edit.do?id=${dto.routepostId}" class="btn-edit">수정</a>
      <a href="${pageContext.request.contextPath}/routepost/delete.do?id=${dto.routepostId}" class="btn-delete"
         onclick="return confirm('정말 삭제하시겠습니까?');">삭제</a>
    </c:if>
  </div>
</div>




  <!-- ======================= 댓글 영역 시작 ======================= -->
  <div class="comment-section" style="margin-top:40px; border-top:1px solid #ccc; padding-top:20px;">

    <!-- 제목 -->
    <h3>댓글 💬 <span id="comment-count">(0)</span></h3>

    <!-- 댓글 목록 -->
    <div id="comment-list" style="margin-top:20px;"></div>

    <!-- 페이지 버튼 -->
    <div id="comment-pagination" style="margin-top:15px; text-align:center;"></div>

    <!-- 댓글 입력창 -->
    <div class="comment-input" style="margin-top:25px;">
      <textarea id="comment-content" placeholder="댓글을 입력하세요" rows="3"
                style="width:100%; resize:none;"></textarea>
      <button id="btn-comment-add" data-id="${dto.routepostId}"
              style="margin-top:5px; float:right;">등록</button>
      <div style="clear:both;"></div>
    </div>

  </div>
  <!-- ======================= 댓글 영역 끝 ======================= -->

</div>

	<script src="https://code.jquery.com/jquery-3.7.1.min.js"></script>
	<script>
	// 로그인된 사용자 ID (비로그인 시 null)
	const currentUserId = ${sessionScope.user != null ? sessionScope.user.userId : 'null'};
	const postId = '${dto.routepostId}';

	//  페이지 진입 시 상태 조회
	$(function(){
	  $.getJSON('${pageContext.request.contextPath}/routepost/status.do', { id: postId }, function(res){
	    if (res.liked) $('#btn-like').addClass('active').text('👍 추천됨');
	    if (res.scrapped) $('#btn-scrap').addClass('active').text('📌 스크랩됨');
	  });
	});
	
	//✅ 페이지 진입 시 상태 조회 (추천/스크랩 상태 초기 표시)
	$(function() {
	  $.getJSON('${pageContext.request.contextPath}/routepost/status.do', { id: postId }, function(res) {
	    if (res.liked) $('#btn-like').addClass('active').text('👍 추천됨');
	    if (res.scrapped) $('#btn-scrap').addClass('active').text('📌 스크랩됨');
	  });
	});
	
	// ✅ 추천 기능
	$(document).on('click', '#btn-like', function(e) {
	  e.preventDefault();
	  const postId = $(this).data('id');
	
	  $.ajax({
	    type: 'GET',
	    url: '${pageContext.request.contextPath}/routepost/like.do',
	    data: { id: postId },
	    dataType: 'json',
	    success: function(res) {
	      if (!res.success) {
	        alert(res.message);
	        location.href = '${pageContext.request.contextPath}/user/login.do';
	        return;
	      }
	
	      // ✅ 토글 반영
	      if (res.liked) {
	        $('#btn-like').addClass('active').text('👍 추천됨');
	      } else {
	        $('#btn-like').removeClass('active').text('👍 추천');
	      }
	
	      alert(res.message);
	    },
	    error: function(a,b,c){
	      console.log(a,b,c);
	      alert('추천 처리 중 오류 발생 ❌');
	    }
	  });
	});
	
	// ✅ 스크랩
	$(document).on('click', '#btn-scrap', function(e){
	  e.preventDefault();
	  $.ajax({
	    type: 'GET',
	    url: '${pageContext.request.contextPath}/routepost/scrap.do',
	    data: { id: postId },
	    dataType: 'json',
	    success: function(res) {
	      if (!res.success) {
	        alert(res.message);
	        location.href = '${pageContext.request.contextPath}/user/login.do';
	        return;
	      }
	      if (res.scrapped) {
	        $('#btn-scrap').addClass('active').text('📌 스크랩됨');
	      } else {
	        $('#btn-scrap').removeClass('active').text('📌 스크랩');
	      }
	      alert(res.message);
	    }
	  });
	});
	</script>
	
	<script>
	/* =================== 댓글 기능 =================== */
	
	/* 서버 값 → JS 변환(EL 충돌 방지를 위해 c:out 사용) */
	
	let currentPage = 1;
	const commentsPerPage = 20;
	
	/* 출력 안전화(간단 XSS 방지) */
	function esc(s){
	  return String(s).replace(/[&<>"']/g, function(m){
	    return ({'&':'&amp;','<':'&lt;','>':'&gt;','"':'&quot;',"'":'&#39;'}[m]);
	  });
	}
	
	/* 댓글 불러오기 (수정된 버전) */
	function loadComments(page = 1) {
	  $.getJSON('/trip/comment/list.do', { postId: postId, page: page }, function(res) {
	    const list = res.list || [];
	    const totalCount = res.totalCount || 0;
	
	    $('#comment-count').text('(' + totalCount + ')');
	
	    const totalPages = Math.ceil(totalCount / commentsPerPage);
	    currentPage = Math.min(page, totalPages || 1);
	
	    let html = '';
	    if (list.length === 0) {
	      html = '<p>등록된 댓글이 없습니다 😶</p>';
	    } else {
	    	list.forEach(function(c){
	    		  html += '<div class="comment-item" data-id="' + c.commentId + '">'
	    		        +   '<b>' + esc(c.nickname) + '</b> '
	    		        +   '<small style="color:#999;">' + esc(c.regdate) + '</small>'
	    		        +   '<div class="comment-content" style="margin:5px 0;">' + esc(c.content) + '</div>';

	    		  // ✅ 작성자 본인만 수정/삭제 버튼 보이기
	    		  if (currentUserId !== null && Number(currentUserId) === Number(c.userId)) {
	    		    html += '<button class="btn-edit" style="font-size:11px;">수정</button> '
	    		          + '<button class="btn-delete" style="font-size:11px;">삭제</button>';
	    		  }

	    		  html += '</div>';
	    		});

	    }
	    $('#comment-list').html(html);
	
	    // 페이지 버튼 렌더링
	    let pageHtml = '';
	    for (var i = 1; i <= totalPages; i++) {
	      pageHtml += '<button class="page-btn' + (i === currentPage ? ' active' : '') + '" data-page="' + i + '">' + i + '</button>';
	    }
	    $('#comment-pagination').html(pageHtml);
	  });
	}
	
	
	/* 댓글 등록 */
	$(document).on('click', '#btn-comment-add', function(e) {
	  e.preventDefault();
	  const content = $('#comment-content').val().trim();
	  if (!content) return alert('내용을 입력하세요.');
	
	  $.post('/trip/comment/add.do', { postId: postId, content: content }, function(res) {
	    if (!res.success) {
	      alert(res.message);
	      if (res.message && res.message.indexOf('로그인') !== -1) {
	        location.href = '/trip/user/login.do';
	      }
	      return;
	    }
	    alert(res.message);
	    $('#comment-content').val('');
	    loadComments(1);
	  }, 'json');
	});
	
	/* 댓글 삭제 */
	$(document).on('click', '.btn-delete', function() {
	  const commentId = $(this).closest('.comment-item').data('id');
	  $.post('/trip/comment/delete.do', { commentId: commentId }, function(res) {
	    alert(res.message);
	    if (res.success) {
	      loadComments(currentPage);
	    }
	  }, 'json');
	});

	/* 댓글 수정(입력창 변환) */
	$(document).on('click', '.btn-edit', function() {
	  const $comment = $(this).closest('.comment-item');
	  const $content = $comment.find('.comment-content');
	  const originalText = $content.text().trim();
	
	  // 이미 수정 중이면 무시
	  if ($comment.find('textarea.edit-area').length > 0) return;
	
	  $content.hide();
	  const editBox =
	      '<textarea class="edit-area" style="width:100%; resize:none;">'
	    + esc(originalText)
	    + '</textarea>'
	    + '<button class="btn-save" style="font-size:11px; margin-top:5px;">저장</button> '
	    + '<button class="btn-cancel" style="font-size:11px; margin-top:5px;">취소</button>';
	  $content.after(editBox);
	});

/* 수정 저장 */
	$(document).on('click', '.btn-save', function() {
	  const $comment = $(this).closest('.comment-item');
	  const commentId = $comment.data('id');
	  const newContent = $comment.find('.edit-area').val().trim();
	
	  if (!newContent) return alert('내용을 입력하세요.');
	
	  $.post('/trip/comment/edit.do', { commentId: commentId, content: newContent }, function(res) {
	    alert(res.message);
	    if (res.success) {
	      loadComments(currentPage);
	    }
	  }, 'json');
	});

	/* 수정 취소 */
	$(document).on('click', '.btn-cancel', function() {
	  const $comment = $(this).closest('.comment-item');
	  $comment.find('.edit-area, .btn-save, .btn-cancel').remove();
	  $comment.find('.comment-content').show();
	});
	
	/* 페이지 버튼 클릭 */
	$(document).on('click', '.page-btn', function() {
	  const page = parseInt($(this).data('page'), 10);
	  loadComments(page);
	});

/* 첫 진입 시 댓글 로드 */
	$(function(){ loadComments(1); });
	/* =================== 댓글 기능 끝 =================== */
	 
	// ===================== 이미지 클릭 확대 =====================

		window.addEventListener("load", function () {
	  
	  const modal = document.getElementById("imageModal");
	  const modalImg = document.getElementById("modalImage");
	  const closeBtn = document.querySelector(".modal-close");
	  const leftArrow = document.querySelector(".modal-arrow.left");
	  const rightArrow = document.querySelector(".modal-arrow.right");
	  const counter = document.querySelector(".modal-counter");
	  const images = Array.from(document.querySelectorAll(".routepost-img"));
	
	  if (!images.length) {
		    if (modal) modal.style.display = "none";
		    console.warn("이미지가 없습니다.");
		    return;
		  }
	
	  let currentIndex = 0;
	
	  function openModal(index) {
	    modal.style.display = "flex";
	    currentIndex = index;
	    updateModalImage();
	    document.body.style.overflow = "hidden";
	  }
	
	  function closeModal() {
	    modal.style.display = "none";
	    document.body.style.overflow = "";
	  }
	
	  function updateModalImage() {
		  modalImg.src = images[currentIndex].src;
		  if (counter) counter.textContent = (currentIndex + 1) + ' / ' + images.length;
		}

	
	  leftArrow.addEventListener("click", (e) => {
	    e.stopPropagation();
	    currentIndex = (currentIndex - 1 + images.length) % images.length;
	    updateModalImage();
	  });
	
	  rightArrow.addEventListener("click", (e) => {
	    e.stopPropagation();
	    currentIndex = (currentIndex + 1) % images.length;
	    updateModalImage();
	  });
	
	  closeBtn.addEventListener("click", closeModal);
	
	  window.addEventListener("keydown", (e) => {
	    if (e.key === "Escape") closeModal();
	  });
	
	  modal.addEventListener("click", (e) => {
	    if (e.target === modal) closeModal();
	  });
	
	  images.forEach((img, i) => {
	    img.addEventListener("click", () => openModal(i));
	  });
	
	  // 초기 숨기기
	  modal.style.display = "none";
	});
	
		// ===================== 썸네일 화살표 이동 및 자동 숨김 =====================
		document.addEventListener("DOMContentLoaded", function () {
		  const imageArea = document.querySelector(".image-area");
		  const leftArrow = document.querySelector(".image-nav-arrow.left");
		  const rightArrow = document.querySelector(".image-nav-arrow.right");

		  if (!imageArea) return;

		  const scrollAmount = 250; // 클릭 시 이동 거리(px)
		  const images = imageArea.querySelectorAll("img");

		  // ✅ 이미지 개수 2장 이하일 경우 화살표 숨김
		  if (images.length <= 2) {
		    leftArrow.classList.add("hidden");
		    rightArrow.classList.add("hidden");
		    return;
		  }

		  // ✅ 클릭 시 스크롤 이동
		  leftArrow.addEventListener("click", () => {
		    imageArea.scrollBy({ left: -scrollAmount, behavior: "smooth" });
		    updateArrowVisibility();
		  });

		  rightArrow.addEventListener("click", () => {
		    imageArea.scrollBy({ left: scrollAmount, behavior: "smooth" });
		    updateArrowVisibility();
		  });

		  // ✅ 스크롤 상태에 따라 화살표 표시/숨김
		  function updateArrowVisibility() {
		    const maxScroll = imageArea.scrollWidth - imageArea.clientWidth;
		    const currentScroll = imageArea.scrollLeft;

		    if (currentScroll <= 10) {
		      leftArrow.classList.add("hidden");
		    } else {
		      leftArrow.classList.remove("hidden");
		    }

		    if (currentScroll >= maxScroll - 10) {
		      rightArrow.classList.add("hidden");
		    } else {
		      rightArrow.classList.remove("hidden");
		    }
		  }

		  // ✅ 스크롤 시에도 동적으로 감지
		  imageArea.addEventListener("scroll", updateArrowVisibility);

		  // 초기 상태 반영
		  updateArrowVisibility();
		});




	
	


</script>

</body>
<%@ include file="/WEB-INF/views/inc/route_footer.jsp" %>
</html>
