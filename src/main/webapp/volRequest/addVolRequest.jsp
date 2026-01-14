<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8"/>
  <title>봉사요청 등록</title>
  <link rel="stylesheet" href="<c:url value='/css/site.css?v=20250827'/>">
</head>

<body class="theme-warm page-bg">
  <%@ include file="/common/top.jspf"%>

  <main class="container-main">
    <div class="page-wrap">
      <section class="page-cover">
        <h2 class="page-title">봉사요청 등록</h2>

        <form method="post"
              action="<c:url value='/addVolRequest.do'/>"
              enctype="multipart/form-data"
              class="form-grid cols-2">

          <!-- 제목 (한 줄 꽉) -->
          <div class="form-row" style="grid-column:1/-1">
            <label class="form-label">제목</label>
            <input type="text" name="title" required class="input" placeholder="제목을 입력하세요">
          </div>

          <!-- 시작/종료 일시 -->
          <div class="form-row">
            <label class="form-label">시작일시</label>
            <input type="datetime-local" name="start" required class="input">
          </div>
          <div class="form-row">
            <label class="form-label">종료일시</label>
            <input type="datetime-local" name="end" required class="input">
          </div>

          <!-- 연락처 / 지역 -->
          <div class="form-row">
            <label class="form-label">연락처</label>
            <input type="text" name="phone" class="input" placeholder="010-0000-0000">
          </div>
          <div class="form-row">
            <label class="form-label">지역</label>
            <input type="text" name="region" required class="input" placeholder="예: 서울시 구로구">
          </div>

          <!-- 카테고리 / 이미지 -->
          <div class="form-row">
            <label class="form-label">카테고리</label>
            <input type="text" name="category" class="input" placeholder="예: 병원동행">
          </div>
          <div class="form-row">
            <label class="form-label">이미지</label>
            <input type="file" name="image" accept="image/*" class="input">
          </div>

          <!-- 미리보기 카드 -->
          <div class="ui-card ui-card--sm" style="grid-column:1/-1">
            <div class="ui-label" style="margin-bottom:6px;">미리보기</div>
            <pre id="preview" class="ui-help" style="min-height:140px; white-space:pre-line;"></pre>
          </div>

          <!-- 상세 내용 (한 줄 꽉) -->
          <div class="form-row" style="grid-column:1/-1">
            <label class="form-label">봉사요청 상세내용</label>
            <textarea name="content" rows="8" class="textarea" placeholder="상세 내용을 입력하세요"></textarea>
          </div>

          <!-- 버튼 -->
          <div class="actions btn-row" style="grid-column:1/-1; justify-content:flex-end; margin-top:8px">
            <a href="<c:url value='/listVolRequest.do'/>" class="btn secondary">취소</a>
            <button type="submit" class="btn">등록하기</button>
          </div>
        </form>
      </section>
    </div>
  </main>

  <jsp:include page="/common/footer.jspf"/>
  
  <script>
    // 간단 미리보기: 핵심 필드만 합쳐서 보여줌
    const $ = (s)=>document.querySelector(s);
    function syncPreview(){
      const start   = $('[name=start]').value || '';
      const end     = $('[name=end]').value || '';
      const phone   = $('[name=phone]').value || '';
      const region  = $('[name=region]').value || '';
      const category= $('[name=category]').value || '';
      const content = $('[name=content]').value || '';
      $('#preview').textContent =
        (start && '시작: '+start+'\n') +
        (end && '종료: '+end+'\n') +
        (phone && '연락처: '+phone+'\n') +
        (region && '지역: '+region+'\n') +
        (category && '카테고리: '+category+'\n') +
        (content ? '\n'+content : '');
    }
    document.addEventListener('input', (e)=>{
      if(['start','end','content','phone','region','category','title'].includes(e.target.name)){
        syncPreview();
      }
    });
    document.addEventListener('DOMContentLoaded', syncPreview);
  </script>
</body>
</html>
