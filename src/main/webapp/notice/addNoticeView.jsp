<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>
<%@ taglib prefix="c"  uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<%@ include file="/common/top.jspf" %>

<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8" />
  <title>공지 등록</title>
  <link rel="stylesheet" href="<c:url value='/css/site.css'/>"/>
  <script>
    // (선택) 간단 유효성—기능 변경 없음
    function onSubmitAddNotice(e){
      const f=e.target, t=f.title.value.trim(), c=f.content.value.trim();
      if(!t){ alert("제목은 필수입니다."); f.title.focus(); e.preventDefault(); return false; }
      if(!c){ alert("내용은 필수입니다."); f.content.focus(); e.preventDefault(); return false; }
      return true;
    }
  </script>
</head>

<body class="theme-warm page-bg">
<main class="container-main">
  <div class="page-wrap">
    <section class="page-cover">
      <div class="actions btn-row" style="justify-content:space-between;">
        <h2 class="page-title">공지 등록</h2>
        <a class="btn" href="<c:url value='/listNotice.do'/>">목록</a>
      </div>

      <%-- 로그인/관리자 여부 --%>
      <c:set var="isLogin" value="${not empty sessionScope.loginUser}" />
      <c:set var="isAdmin" value="${isLogin and sessionScope.loginUser.userId eq 'admin'}" />

      <%-- 관리자 전용: 등록 폼 --%>
      <c:if test="${isAdmin}">
        <div class="form-wrap">
          <p class="form-sub">공지 글은 서비스 전체에 노출됩니다. 정확하고 간결하게 작성해주세요.</p>

          <form method="post" action="<c:url value='/addNotice.do'/>" onsubmit="return onSubmitAddNotice(event)">
            <div class="form-grid">
              <div class="form-row">
                <label for="title" class="form-label">제목</label>
                <input id="title" name="title" type="text" maxlength="100"
                       class="input ui-input"
                       value="${fn:escapeXml(param.title)}" required />
              </div>

              <div class="form-row">
                <label for="content" class="form-label">내용</label>
                <textarea id="content" name="content" maxlength="4000" rows="10"
                          class="textarea ui-textarea"
                          placeholder="공지 내용을 입력하세요.">${fn:escapeXml(param.content)}</textarea>
              </div>

              <div class="actions btn-row justify-end">
                <button class="btn" type="submit">등록</button>
                <a class="btn" href="<c:url value='/listNotice.do'/>">목록</a>
              </div>

              <c:if test="${not empty requestScope.error}">
                <div class="ui-alert">${requestScope.error}</div>
              </c:if>
            </div>
          </form>
        </div>
      </c:if>

      <%-- 비관리자: 안내 박스 --%>
      <c:if test="${not isAdmin}">
        <div class="ui-card ui-card--lg">
          <div class="ui-label" style="font-weight:700; margin-bottom:6px;">권한 안내</div>
          <div>공지 등록은 관리자만 가능합니다.</div>
          <c:choose>
            <c:when test="${isLogin}">
              <div style="margin-top:4px;">현재 계정: <strong>${sessionScope.loginUser.userId}</strong></div>
            </c:when>
            <c:otherwise>
              <div style="margin-top:4px;">현재 로그인되어 있지 않습니다.</div>
            </c:otherwise>
          </c:choose>
          <div class="actions btn-row" style="margin-top:10px;">
            <a class="btn" href="<c:url value='/listNotice.do'/>">공지 목록으로</a>
          </div>
        </div>
      </c:if>
    </section>
  </div>
</main>

<jsp:include page="/common/footer.jspf" />
</body>
</html>
