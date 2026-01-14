<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" isELIgnored="false" %>
<%@ taglib prefix="c"  uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<%@ include file="/common/top.jspf" %>

<%-- ===== 검색/페이징 파라미터 바인딩 (기존 변수/이름 유지) ===== --%>
<c:set var="page"          value="${empty param.page ? (empty page ? 1 : page) : param.page}" />
<c:set var="pageSize"      value="${empty param.pageSize ? (empty pageSize ? 10 : pageSize) : param.pageSize}" />
<c:set var="searchKey"     value="${empty param.searchKey ? (empty searchKey ? '' : searchKey) : param.searchKey}" />
<c:set var="q"             value="${empty param.q ? (empty q ? '' : q) : param.q}" />

<c:set var="totalCount"    value="${empty totalCount ? 0 : totalCount}" />
<c:set var="totalPage"     value="${empty totalPage ? 1 : totalPage}" />
<c:set var="startPage"     value="${empty startPage ? 1 : startPage}" />
<c:set var="endPage"       value="${empty endPage ? 1 : endPage}" />

<c:if test="${page lt 1}"><c:set var="page" value="1"/></c:if>
<c:if test="${page gt totalPage}"><c:set var="page" value="${totalPage}"/></c:if>

<c:set var="items" value="${not empty noticeList ? noticeList : (not empty list ? list : null)}" />

<%-- 세션 사용자 → admin 식별 (기존 로직 보존) --%>
<c:set var="sessionUser"
       value="${empty sessionScope.user ? (empty sessionScope.loginUser ? (empty sessionScope.sessionUser ? null : sessionScope.sessionUser) : sessionScope.loginUser) : sessionScope.user}" />
<c:set var="sessionUserId"
       value="${empty sessionUser ? (empty sessionScope.userId ? '' : sessionScope.userId)
                 : (empty sessionUser.userId ? (empty sessionUser.id ? '' : sessionUser.id) : sessionUser.userId)}" />
<c:set var="isAdmin" value="${sessionUserId eq 'admin'}" />

<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8" />
  <title>공지 목록</title>
  <link rel="stylesheet" href="<c:url value='/css/site.css'/>"/>

  <style>
    /* 공통 CSS 유지 + 필요한 최소 보강만 */
    .tbl-fixed { table-layout: fixed; width: 100%; }
    /* 제목 말줄임 */
    .col-title { overflow: hidden; text-overflow: ellipsis; white-space: nowrap; }
    .col-title a { display:inline-block; max-width:100%; overflow:hidden; text-overflow:ellipsis; white-space:nowrap; }

    /* 검색 바: 공통 searchbar 클래스로 사이즈 일치 */
    .notice-searchbar { display:flex; gap:12px; align-items:flex-end; flex-wrap:wrap; }
    .notice-searchbar .control { display:grid; gap:6px; min-width:120px; }
    .notice-searchbar .search-grow { flex:1 1 420px; min-width:240px; }
    .notice-searchbar .ui-select,
    .notice-searchbar .ui-input { height:30px; font-size:13px; border-radius:6px; padding:0 8px; }
    .notice-searchbar .btn { height:30px; min-width:60px; padding:0 10px; font-size:13px; border-radius:6px; }

    /* 테이블 버튼(상세 등) 슬림 */
    .table .btn { height:28px; min-width:54px; padding:0 10px; font-size:12px; border-radius:8px; }

    /* 페이지네이션: 공통 .pagination 사용 + 버튼 스타일 */
    .pagination a.btn, .pagination span.btn { min-width:40px; text-align:center; }
    .pagination .current { background:var(--accent); color:#fff; border:1px solid var(--accent); border-radius:8px; padding:6px 10px; }
  </style>
</head>

<body class="theme-warm page-bg">
<main class="container-main">
  <div class="page-wrap">
    <section class="page-cover">
      <h2 class="page-title">공지 목록</h2>

      <%-- ===== 검색 폼 (공통 스타일) ===== --%>
      <form class="notice-searchbar" action="<c:url value='/listNotice.do'/>" method="get">
        <div class="control">
          <label class="ui-label" for="searchKey">검색항목</label>
          <select id="searchKey" name="searchKey" class="ui-select">
            <option value=""      ${empty searchKey ? 'selected' : ''}>선택</option>
            <option value="title" ${searchKey == 'title' ? 'selected' : ''}>제목</option>
          </select>
        </div>

        <div class="control search-grow">
          <label class="ui-label" for="q">검색어</label>
          <input id="q" type="text" name="q" value="${fn:escapeXml(q)}" class="ui-input" placeholder="검색어 입력"/>
        </div>

        <input type="hidden" name="page" value="1"/>
        <input type="hidden" name="pageSize" value="${pageSize}"/>
        <button type="submit" class="btn">검색</button>
      </form>

      <div class="seg"></div>

      <%-- ===== 목록 테이블 ===== --%>
      <c:choose>
        <c:when test="${empty items}">
          <div class="ui-alert">등록된 공지가 없습니다.</div>
        </c:when>
        <c:otherwise>
          <table class="table tbl-fixed">
            <colgroup>
              <col style="width:12%"><!-- 번호 -->
              <col><!-- 제목 (가장 넓게) -->
              <col style="width:18%"><!-- 작성일 -->
            </colgroup>
            <thead>
              <tr>
                <th>No</th>
                <th>제목</th>
                <th>작성일</th>
              </tr>
            </thead>
            <tbody>
              <c:forEach var="n" items="${items}">
                <tr>
                  <td style="text-align:center;">${n.noticeId}</td>
                  <td class="col-title">
                    <a class="title-link"
                       href="<c:url value='/detailNotice.do'>
                               <c:param name='noticeId' value='${n.noticeId}'/>
                               <c:param name='page'      value='${page}'/>
                               <c:param name='pageSize'  value='${pageSize}'/>
                               <c:param name='searchKey' value='${searchKey}'/>
                               <c:param name='q'         value='${q}'/>
                             </c:url>">
                      ${fn:escapeXml(n.title)}
                    </a>
                  </td>

                  <td style="text-align:center;">${n.createdAtString}</td>
                </tr>
              </c:forEach>
            </tbody>
          </table>
        </c:otherwise>
      </c:choose>

      <div class="seg"></div>

    <!-- ===== 푸터: 페이지네이션 + 등록 버튼 ===== -->
<div class="actions btn-row" style="justify-content:space-between; align-items:center;">
  <!-- 왼쪽: 페이징 (btn 금지) -->
  <nav class="pagination" aria-label="공지 페이징">
    <c:forEach var="p" begin="1" end="${totalPage > 5 ? 5 : totalPage}">
      <c:choose>
        <c:when test="${p == page}">
          <span class="current" aria-current="page">${p}</span>
        </c:when>
        <c:otherwise>
          <a href="<c:url value='/listNotice.do'>
                     <c:param name='page' value='${p}'/>
                     <c:param name='pageSize' value='${pageSize}'/>
                     <c:param name='q' value='${fn:escapeXml(q)}'/>
                     <c:param name='searchKey' value='${searchKey}'/>
                   </c:url>">${p}</a>
        </c:otherwise>
      </c:choose>
    </c:forEach>

    <!-- 다음(») -->
    <c:if test="${totalPage > 5 && page < totalPage}">
      <a href="<c:url value='/listNotice.do'>
                 <c:param name='page' value='${page+1}'/>
                 <c:param name='pageSize' value='${pageSize}'/>
                 <c:param name='q' value='${fn:escapeXml(q)}'/>
                 <c:param name='searchKey' value='${searchKey}'/>
               </c:url>">&raquo;</a>
    </c:if>
  </nav>

  <!-- 오른쪽: 등록 버튼 (관리자만) -->
  <c:if test="${isAdmin}">
    <a class="btn" href="<c:url value='/addNoticeView.do'/>">등록</a>
  </c:if>
</div>


</main>

<%@ include file="/common/footer.jspf"%>
</body>
</html>
