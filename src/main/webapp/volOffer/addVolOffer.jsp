<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8"/>
  <title>봉사제공 등록</title>
  <!-- 캐시버스터 포함 -->
  <link rel="stylesheet" href="<c:url value='/css/site.css?v=20250828'/>">
</head>

<body class="theme-warm page-bg">
  <%@ include file="/common/top.jspf"%>

  <main class="container-main">
    <div class="page-wrap">
      <section class="page-cover">
        <h2 class="page-title">봉사제공 등록</h2>

        <!-- 공통 form-wrap -->
        <form method="post" action="<c:url value='/addVolOffer.do'/>" enctype="multipart/form-data"
              class="form-wrap">

          <!-- 제목 -->
          <div class="form-row">
            <label class="form-label">제목</label>
            <input type="text" name="title" class="input" required placeholder="제목을 입력하세요">
          </div>

          <!-- 시작/종료일시 -->
          <div class="form-grid cols-2">
            <div class="form-row">
              <label class="form-label">시작일시</label>
              <input type="datetime-local" name="startTime" class="input" required>
              <span class="form-help">전송 시 서버 규약(yyyy-MM-dd HH:mm:ss)으로 변환됩니다</span>
            </div>
            <div class="form-row">
              <label class="form-label">종료일시</label>
              <input type="datetime-local" name="endTime" class="input" required>
            </div>
          </div>

          <!-- 연락처 -->
          <div class="form-row">
            <label class="form-label">연락처</label>
            <input type="text" name="phone" class="input" placeholder="010-0000-0000">
          </div>

          <!-- 지역 -->
          <div class="form-row">
            <label class="form-label">지역</label>
            <input type="text" name="region" class="input" placeholder="예: 서울시 구로구">
          </div>

          <!-- 카테고리 -->
          <div class="form-row">
            <label class="form-label">카테고리</label>
            <input type="text" name="category" class="input" placeholder="예: 병원동행">
          </div>

          <!-- 이미지 -->
          <div class="form-row">
            <label class="form-label">이미지</label>
            <input type="file" name="image" class="input" accept="image/*">
          </div>

          <!-- 상세내용 -->
          <div class="form-row">
            <label class="form-label">봉사제공 상세내용</label>
            <textarea name="content" rows="6" class="textarea" placeholder="상세 내용을 입력하세요"></textarea>
          </div>

          <!-- 버튼 -->
          <div class="actions btn-row justify-end">
            <a href="<c:url value='/listVolOffer.do'/>" class="btn secondary">취소</a>
            <button type="submit" class="btn">등록하기</button>
          </div>
        </form>
      </section>
    </div>
  </main>

  <%@ include file="/common/footer.jspf"%>
</body>
</html>
