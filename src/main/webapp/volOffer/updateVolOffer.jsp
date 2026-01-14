<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8"/>
  <title>봉사제공 수정</title>
  <!-- ✅ 캐시버스터 포함 -->
  <link rel="stylesheet" href="<c:url value='/css/site.css?v=20250828'/>">
</head>

<body class="theme-warm page-bg">
  <%@ include file="/common/top.jspf" %>

  <main class="container-main">
    <div class="page-wrap">
      <section class="page-cover">
        <h2 class="page-title">봉사제공 수정</h2>

        <!-- 데이터 바인딩 -->
        <c:set var="item" value="${volOffer}"/>

        <!-- datetime-local 값 준비 -->
        <c:set var="startVal" value=""/>
        <c:if test="${not empty item.startTime}">
          <c:set var="startVal" value="${fn:replace(item.startTime, ' ', 'T')}"/>
          <c:if test="${fn:length(startVal) >= 16}">
            <c:set var="startVal" value="${fn:substring(startVal, 0, 16)}"/>
          </c:if>
        </c:if>
        <c:if test="${empty startVal and not empty item.startDate}">
          <fmt:formatDate value="${item.startDate}" pattern="yyyy-MM-dd'T'HH:mm" var="startVal"/>
        </c:if>

        <c:set var="endVal" value=""/>
        <c:if test="${not empty item.endTime}">
          <c:set var="endVal" value="${fn:replace(item.endTime, ' ', 'T')}"/>
          <c:if test="${fn:length(endVal) >= 16}">
            <c:set var="endVal" value="${fn:substring(endVal, 0, 16)}"/>
          </c:if>
        </c:if>
        <c:if test="${empty endVal and not empty item.endDate}">
          <fmt:formatDate value="${item.endDate}" pattern="yyyy-MM-dd'T'HH:mm" var="endVal"/>
        </c:if>

        <!-- 수정 폼 -->
        <form method="post" action="<c:url value='/updateVolOffer.do'/>" enctype="multipart/form-data"
              class="form-wrap form-grid cols-2">

          <!-- hidden 값 -->
          <input type="hidden" name="postId"   value="${item.postId}"/>
          <input type="hidden" name="authorId" value="${not empty item.authorId ? item.authorId : sessionScope.user.userId}"/>

          <!-- 제목 -->
          <div class="form-row" style="grid-column:1/-1">
            <label class="form-label">제목</label>
            <input type="text" name="title" class="input" required
                   value="${fn:escapeXml(item.title)}" placeholder="제목을 입력하세요">
          </div>

          <!-- 시작/종료일시 -->
          <div class="form-row">
            <label class="form-label">시작일시</label>
            <input type="datetime-local" name="startTime" class="input" value="${startVal}">
          </div>
          <div class="form-row">
            <label class="form-label">종료일시</label>
            <input type="datetime-local" name="endTime" class="input" value="${endVal}">
          </div>

          <!-- 연락처 -->
          <div class="form-row">
            <label class="form-label">연락처</label>
            <input type="text" name="phone" class="input"
                   value="${fn:escapeXml(item.phone)}" placeholder="010-0000-0000">
          </div>

          <!-- 지역 -->
          <div class="form-row">
            <label class="form-label">지역</label>
            <input type="text" name="region" class="input" required
                   value="${fn:escapeXml(item.region)}" placeholder="예: 서울시 구로구">
          </div>

          <!-- 카테고리 -->
          <div class="form-row">
            <label class="form-label">카테고리</label>
            <input type="text" name="category" class="input"
                   value="${fn:escapeXml(item.category)}" placeholder="예: 병원동행">
          </div>

          <!-- 이미지 -->
          <div class="form-row">
            <label class="form-label">이미지</label>
            <input type="file" name="image" class="input" accept="image/*">
            <c:if test="${not empty item.image}">
              <div class="form-help" style="margin-top:6px">
                <img src="${pageContext.request.contextPath}/images/${item.image}"
                     alt="현재 이미지" class="img-fluid" style="max-width:200px; border-radius:10px;">
                <p class="form-help">새 파일을 선택하지 않으면 기존 이미지를 유지합니다.</p>
              </div>
            </c:if>
          </div>

          <!-- 미리보기 -->
          <div class="form-row" style="grid-column:1/-1">
            <div class="ui-card">
              <div class="ui-label">미리보기</div>
              <div id="preview" style="margin-top:6px; white-space:pre-line;"></div>
            </div>
          </div>

          <!-- 상세내용 -->
          <div class="form-row" style="grid-column:1/-1">
            <label class="form-label">상세내용</label>
            <textarea name="content" rows="8" class="textarea"
                      placeholder="상세 내용을 입력하세요">${fn:escapeXml(item.content)}</textarea>
          </div>

          <!-- 버튼 -->
          <div class="actions btn-row justify-end" style="grid-column:1/-1">
            <a href="javascript:history.back()" class="btn secondary">취소</a>
            <button type="submit" class="btn">적용하기</button>
          </div>
        </form>
      </section>
    </div>
  </main>

  <%@ include file="/common/footer.jspf" %>

  <script>
    // 미리보기
    function val(sel){ var el=document.querySelector(sel); return el?el.value:""; }
    function syncPreview(){
      var s=val('[name="startTime"]'), e=val('[name="endTime"]'),
          t=val('[name="title"]'), rg=val('[name="region"]'), ct=val('[name="category"]');
      var lines=[];
      if(t)  lines.push("제목: "+t);
      if(rg) lines.push("지역: "+rg);
      if(ct) lines.push("카테고리: "+ct);
      if(s)  lines.push("시작: "+s.replace("T"," "));
      if(e)  lines.push("종료: "+e.replace("T"," "));
      document.getElementById("preview").textContent = lines.join("\n");
    }
    document.addEventListener("input", syncPreview);
    document.addEventListener("DOMContentLoaded", syncPreview);
  </script>
</body>
</html>
