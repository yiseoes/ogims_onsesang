<%@ page contentType="text/html; charset=UTF-8" language="java"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8" />
  <title>봉사요청 목록</title>
  <!-- 공통 CSS (캐시버스터 옵션은 필요시) -->
  <link rel="stylesheet" href="<c:url value='/css/site.css?v=20250828'/>">
  <script defer src="<c:url value='/js/listVolRequest.js'/>"></script>
</head>

<body class="theme-warm page-bg">
  <%@ include file="/common/top.jspf"%>

  <main class="container-main">
    <div class="page-wrap">

      <!-- ✅ 유리 카드 한 장 (한 페이지 느낌) -->
      <section class="page-cover">
        <h1 class="page-title">마음부탁 (봉사요청)</h1>

        <!-- 검색 폼 (컨트롤/파라미터 규약 유지) -->
        <form id="searchForm" method="get" action="<c:url value='/listVolRequest.do'/>" class="form-grid">
          <div class="searchbar" style="margin-bottom:16px;">
            <!-- 검색항목 -->
            <div class="control">
              <label for="searchCondition" class="form-label">검색항목</label>
              <select name="searchCondition" id="searchCondition" class="ui-select">
                <option value="title"  <c:if test="${param.searchCondition=='title'}">selected</c:if>>제목</option>
                <%-- <option value="region" <c:if test="${param.searchCondition=='region'}">selected</c:if>>지역</option> --%>
                <option value="author" <c:if test="${param.searchCondition=='author'}">selected</c:if>>작성자</option>
                <option value="date"   <c:if test="${param.searchCondition=='date'}">selected</c:if>>날짜</option>
              </select>
            </div>

            <!-- 카테고리 (드롭다운) -->
            <c:set var="cats" value="동행,청소,장보기,배달,집수리,요리,밭일,미용,벌래잡기,소통대화,목욕,병원동행" />
            <div class="control">
              <label for="categorySelect" class="form-label">카테고리</label>
           <select name="category" id="categorySelect" class="ui-select"
        onchange="document.getElementById('searchForm').submit()">

                <option value="" <c:if test="${empty param.category}">selected</c:if>>전체</option>
                <c:forEach var="cname" items="${fn:split(cats, ',')}">
                  <option value="${cname}" <c:if test="${param.category==cname}">selected</c:if>>${cname}</option>
                </c:forEach>
              </select>
            </div>

            <!-- 검색어 -->
            <div class="control search-grow">
              <label for="keywordText" class="form-label">검색어</label>
              <input type="text" id="keywordText" name="searchKeyword"
                     class="ui-input" placeholder="검색어" value="${param.searchKeyword}">
            </div>

            <!-- 날짜 (date 선택 시 JS로 토글: listVolRequest.js) -->
            <div class="control search-grow" id="dateGroup" style="display:none;">
              <label class="form-label">날짜</label>
              <div class="inline">
                <input type="date" id="dateFrom" class="ui-input" style="max-width:200px;">
                <span>~</span>
                <input type="date" id="dateTo"   class="ui-input" style="max-width:200px;">
              </div>
            </div>

            <!-- 모집중만 -->
            <div class="push-right">
              <label class="ui-checkbox">
                <input type="checkbox" name="status" value="모집중"
                       <c:if test="${param.status=='모집중'}">checked</c:if> />
                <span>모집중만</span>
              </label>
            </div>

            <!-- 검색 버튼 -->
            <div>
              <button type="submit" class="btn">검색</button>
            </div>
          </div>

          <!-- 날짜 검색 시 조합해서 넘길 hidden (중복 name 방지용) -->
          <input type="hidden" id="searchKeywordHidden" name="searchKeyword" />
          <!-- 페이지 유지 -->
          <input type="hidden" name="page" value="${empty param.page ? 1 : param.page}" />
        </form>

        <!-- ✅ 리스트 테이블 (폼 바깥) -->
        <jsp:include page="/volRequest/_listTable.jsp" />

        <!-- ✅ 페이징 (폼 바깥) + 등록은 Request 전용으로 -->
        <jsp:include page="/volRequest/_paging.jsp">
          <jsp:param name="registerUrl" value="/addVolRequest.do"/>
        </jsp:include>

      </section>
      <!-- /page-cover -->

    </div>
  </main>
<script>
document.addEventListener('DOMContentLoaded', () => {
  const form    = document.getElementById('searchForm');
  const catSel  = document.getElementById('categorySelect');
  const condSel = document.getElementById('searchCondition');

  const pageInput     = form.querySelector('input[name="page"]');
  const hiddenKeyword = document.getElementById('searchKeywordHidden');
  const keywordText   = document.getElementById('keywordText');
  const dateFrom      = document.getElementById('dateFrom');
  const dateTo        = document.getElementById('dateTo');

  function buildDateKeyword(){
    const f = (dateFrom && dateFrom.value) || '';
    const t = (dateTo   && dateTo.value)   || '';
    return (f && t) ? `${f}~${t}` : '';
  }

  catSel.addEventListener('change', () => {
    // 카테고리 바꾸면 1페이지부터 보도록 리셋
    if (pageInput) pageInput.value = 1;

    // 날짜 모드면 hidden에 yyyy-MM-dd~yyyy-MM-dd 조합, 텍스트 name 제거
    if (condSel && condSel.value === 'date') {
      if (hiddenKeyword) hiddenKeyword.value = buildDateKeyword();
      if (keywordText)   keywordText.removeAttribute('name');
    } else {
      if (hiddenKeyword) hiddenKeyword.value = '';
      if (keywordText)   keywordText.setAttribute('name', 'searchKeyword');
    }
    form.submit();
  });
});
</script>

  <%@ include file="/common/footer.jspf"%>
</body>
</html>
