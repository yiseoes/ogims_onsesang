<%@ page contentType="text/html; charset=UTF-8" language="java"%>
<%@ taglib prefix="c"  uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8" />
  <title>손길나눔 목록</title>
  <link rel="stylesheet" href="<c:url value='/css/site.css'/>">
  <script defer src="<c:url value='/js/listVolOffer.js'/>"></script>
</head>

<body class="theme-warm page-bg">
  <%@ include file="/common/top.jspf"%>

  <main class="container-main">
    <div class="page-wrap">
      <section class="page-cover">
        <h2 class="page-title">손길나눔 (봉사제공)</h2>

        <%-- 날짜 모드 여부 & 전달된 키워드 분해(yyyy-MM-dd~yyyy-MM-dd) --%>
        <c:set var="isDate" value="${param.searchCondition eq 'date'}" />
        <c:set var="kw" value="${param.searchKeyword}" />
        <c:set var="dateFromVal" value="${isDate ? fn:substringBefore(kw,'~') : ''}" />
        <c:set var="dateToVal"   value="${isDate ? fn:substringAfter(kw,'~')  : ''}" />

        <!-- ===== 검색 폼 ===== -->
        <form id="searchForm" method="get" action="<c:url value='/listVolOffer.do'/>" class="form-wrap" style="max-width:1100px;">

        <!-- 검색바 -->
<div class="searchbar" style="flex-wrap:wrap;">
  <!-- 검색항목 -->
  <div class="control">
    <label class="ui-label" for="searchCondition">검색항목</label>
    <select name="searchCondition" id="searchCondition" class="ui-select">
      <option value="title"  <c:if test="${param.searchCondition=='title'}">selected</c:if>>제목</option>
      <%-- <option value="region" <c:if test="${param.searchCondition=='region'}">selected</c:if>>지역</option> --%>
      <option value="author" <c:if test="${param.searchCondition=='author'}">selected</c:if>>작성자</option>
      <option value="date"   <c:if test="${param.searchCondition=='date'}">selected</c:if>>날짜</option>
    </select>
  </div>

  <!-- 카테고리 -->
  <c:set var="cats" value="동행,청소,장보기,배달,집수리,요리,밭일,미용,벌래잡기,소통대화,목욕,병원동행" />
  <div class="control">
    <label class="ui-label" for="categorySelect">카테고리</label>
    <select name="category" id="categorySelect" class="ui-select">
      <option value="" <c:if test="${empty param.category}">selected</c:if>>전체</option>
      <c:forEach var="cname" items="${fn:split(cats, ',')}">
        <option value="${cname}" <c:if test="${param.category==cname}">selected</c:if>>${cname}</option>
      </c:forEach>
    </select>
  </div>

  <!-- 텍스트 검색 (날짜모드가 아니면 표시 + name 부여) -->
  <div class="control search-grow" id="textGroup" style="${isDate ? 'display:none;' : ''}">
    <label class="ui-label" for="keywordText">검색어</label>
    <input
      type="text"
      id="keywordText"
      class="ui-input"
      <c:if test="${not isDate}">name="searchKeyword"</c:if>
      placeholder="검색어"
      value="<c:out value='${isDate ? "" : param.searchKeyword}'/>">
  </div>

  <!-- 날짜 검색 (밑줄 전체 차지) -->
  <div class="control" id="dateGroup"
       style="flex:1 1 100%; ${isDate ? '' : 'display:none;'}">
    <label class="form-label">기간</label>
    <div class="inline">
      <input type="date" id="dateFrom" class="ui-input" value="${dateFromVal}" style="width:auto;">
      <span>~</span>
      <input type="date" id="dateTo"   class="ui-input" value="${dateToVal}" style="width:auto;">
    </div>
  </div>

  <!-- 날짜를 searchKeyword로 합쳐 넘기기 -->
  <input type="hidden" id="searchKeywordHidden" name="searchKeyword" />

  <!-- 모집중만 -->
  <label class="chk" style="margin-left:8px; align-self:end;">
    <input type="checkbox" name="status" value="모집중"
           <c:if test="${param.status=='모집중'}">checked</c:if> />
    모집중만
  </label>

  <!-- 페이지 유지 -->
  <input type="hidden" name="page" value="${empty param.page ? 1 : param.page}" />

  <!-- 검색 버튼 -->
  <button type="submit" class="btn" style="align-self:end;">검색</button>
</div>


          <!-- 안내 분리선 -->
          <div class="seg"></div>

          <!-- ===== 목록/페이징 include (기존 파일 유지) ===== -->
          <jsp:include page="/volOffer/_listTable.jsp" />
          <jsp:include page="/volOffer/_paging.jsp" />
        </form>
      </section>
    </div>
  </main>
  
  <!-- ✅ 날짜 토글 스크립트 추가 -->
<script>
document.addEventListener("DOMContentLoaded", () => {
  const form          = document.getElementById("searchForm");
  const select        = document.getElementById("searchCondition");
  const textGroup     = document.getElementById("textGroup");
  const dateGroup     = document.getElementById("dateGroup");
  const keywordText   = document.getElementById("keywordText");
  const hiddenKeyword = document.getElementById("searchKeywordHidden");
  const dateFrom      = document.getElementById("dateFrom");
  const dateTo        = document.getElementById("dateTo");

  function setHiddenFromDates() {
    const from = dateFrom?.value || "";
    const to   = dateTo?.value   || "";
    hiddenKeyword.value = (from && to) ? (from + "~" + to) : "";
  }

  function toggleInput() {
    if (select.value === "date") {
      // 날짜 모드: 텍스트 name 제거, 날짜 보이기
      if (keywordText) keywordText.removeAttribute("name");
      textGroup.style.display = "none";
      dateGroup.style.display = "";

      // hidden에 날짜 조합값 유지
      setHiddenFromDates();
    } else {
      // 텍스트 모드: 텍스트 name 부여, 날짜 숨기기
      if (keywordText) keywordText.setAttribute("name", "searchKeyword");
      textGroup.style.display = "";
      dateGroup.style.display = "none";

      // hidden은 비워서 중복 제출 방지
      hiddenKeyword.value = "";
    }
  }

  // 드롭다운 변경 시 즉시 반영
  select.addEventListener("change", toggleInput);

  // 날짜 변경 시 hidden 즉시 반영
  dateFrom?.addEventListener("change", setHiddenFromDates);
  dateTo?.addEventListener("change", setHiddenFromDates);

  // 제출 시에도 마지막으로 한 번 더 안전하게 반영
  form.addEventListener("submit", () => {
    if (select.value === "date") setHiddenFromDates();
  });

  // 초기 로딩 상태 반영 (뒤로가기/새로고침 포함)
  toggleInput();
});
</script>

  <%@ include file="/common/footer.jspf"%>
</body>
</html>
