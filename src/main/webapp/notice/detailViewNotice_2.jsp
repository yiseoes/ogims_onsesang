<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" isELIgnored="false" %>
<%@ taglib prefix="c"  uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<%-- 공통 상단: 탑바 include --%>
<%@ include file="/common/top.jspf" %>

<%-- ===== 검색/페이징 파라미터 (기존 이름 유지) ===== --%>
<c:set var="page"          value="${empty param.page ? (empty page ? 1 : page) : param.page}" />
<c:set var="pageSize"      value="${empty param.pageSize ? (empty pageSize ? 10 : pageSize) : param.pageSize}" />
<c:set var="searchKey"     value="${empty param.searchKey ? (empty searchKey ? '' : searchKey) : param.searchKey}" />
<c:set var="q"             value="${empty param.q ? (empty q ? '' : q) : param.q}" />

<%-- ===== 상세 모델 바인딩 (notice/vo/item 중 하나) ===== --%>
<c:set var="notice" value="${empty notice ? (empty vo ? (empty item ? null : item) : vo) : notice}" />

<%-- ===== admin 식별 (기존 로직 유지) ===== --%>
<c:set var="sessionUser"
       value="${empty sessionScope.user ? (empty sessionScope.loginUser ? (empty sessionScope.sessionUser ? null : sessionScope.sessionUser) : sessionScope.loginUser) : sessionScope.user}" />
<c:set var="sessionUserId"
       value="${empty sessionUser ? (empty sessionScope.userId ? '' : sessionScope.userId)
                 : (empty sessionUser.userId ? (empty sessionUser.id ? '' : sessionUser.id) : sessionUser.userId)}" />
<c:set var="isAdmin" value="${sessionUserId eq 'admin'}" />

<%-- ===== 목록 복귀 URL(검색/페이징 유지) ===== --%>
<c:url var="backToListUrl" value="/listNotice.do">
  <c:param name="page"      value="${page}"/>
  <c:param name="pageSize"  value="${pageSize}"/>
  <c:param name="searchKey" value="${searchKey}"/>
  <c:param name="q"         value="${q}"/>
</c:url>

<%-- ===== 수정/삭제 URL(파라미터 유지) ===== --%>
<c:url var="updateUrl" value="/updateNoticeView.do">
  <c:param name="noticeId"  value="${notice.noticeId}"/>
  <c:param name="page"      value="${page}"/>
  <c:param name="pageSize"  value="${pageSize}"/>
  <c:param name="searchKey" value="${searchKey}"/>
  <c:param name="q"         value="${q}"/>
</c:url>

<c:url var="deleteUrl" value="/deleteNotice.do">
  <c:param name="noticeId"  value="${notice.noticeId}"/>
  <c:param name="page"      value="${page}"/>
  <c:param name="pageSize"  value="${pageSize}"/>
  <c:param name="searchKey" value="${searchKey}"/>
  <c:param name="q"         value="${q}"/>
</c:url>

<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8" />
  <title>공지 상세</title>
  <link rel="stylesheet" href="<c:url value='/css/site.css'/>"/>
  <style>
    /* 공통 CSS 사용, 필요한 최소만 보강 */
    .detail-meta { color: var(--subtle-ink); font-size: 13px; margin-top: 6px; }
    .detail-title { margin: 0 0 8px; font-size: 20px; font-weight: 700; }
    .detail-content { white-space: pre-wrap; line-height: 1.7; }
  </style>
</head>

<body class="theme-warm page-bg">
<main class="container-main">
  <div class="page-wrap">
    <section class="page-cover">
      <div class="actions btn-row" style="justify-content:space-between; margin-bottom:8px;">
        <h2 class="page-title" style="margin:0;">공지 상세</h2>
        <a class="btn" href="${backToListUrl}">목록</a>
      </div>

      <c:choose>
        <c:when test="${empty notice}">
          <div class="ui-alert">해당 공지를 찾을 수 없습니다.</div>
        </c:when>
        <c:otherwise>
          <div class="ui-card ui-card--lg">
            <h3 class="detail-title">${fn:escapeXml(notice.title)}</h3>

            <%-- 작성일: yyyy-MM-dd (EL instanceof 없이 안전 처리) --%>
            <div class="detail-meta">
              작성일:
              <c:set var="__outDate" value=""/>
              <c:catch var="__fmtErr">
                <fmt:formatDate value="${notice.createdAt}" pattern="yyyy-MM-dd" var="__outDate"/>
              </c:catch>

              <c:choose>
                <c:when test="${empty __fmtErr and not empty __outDate}">
                  ${__outDate}
                </c:when>
                <c:otherwise>
                  <c:set var="__raw" value="${notice.createdAt}"/>
                  <c:set var="__num"
                         value="${fn:replace(fn:replace(fn:replace(fn:replace(__raw,'-',''),'/',''),' ',''),':','')}"/>
                  <c:choose>
                    <c:when test="${not empty __num and fn:length(__num) >= 8}">
                      <c:set var="__yyyy" value="${fn:substring(__num,0,4)}"/>
                      <c:set var="__mm"   value="${fn:substring(__num,4,6)}"/>
                      <c:set var="__dd"   value="${fn:substring(__num,6,8)}"/>
                      ${__yyyy}-${__mm}-${__dd}
                    </c:when>
                    <c:otherwise>
                      ${fn:escapeXml(__raw)}
                    </c:otherwise>
                  </c:choose>
                </c:otherwise>
              </c:choose>
            </div>

            <div class="seg"></div>

            <div class="detail-content">
              ${fn:escapeXml(notice.content)}
            </div>
          </div>

          <div class="actions btn-row justify-end" style="margin-top:12px;">
            <c:if test="${isAdmin}">
              <a class="btn" href="${updateUrl}">수정</a>
              <a class="btn" href="${deleteUrl}" onclick="return confirm('정말 삭제하시겠습니까?');">삭제</a>
            </c:if>
          </div>
        </c:otherwise>
      </c:choose>
    </section>
  </div>
</main>

<%@ include file="/common/footer.jspf"%>
</body>
</html>
