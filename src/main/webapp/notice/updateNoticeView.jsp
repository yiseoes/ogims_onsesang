<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>
<%@ taglib prefix="c"  uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<%@ include file="/common/top.jspf" %>

<%-- 상세 모델 바인딩 (기존 변수명 유지) --%>
<c:set var="n" value="${empty notice ? (empty vo ? (empty item ? null : item) : vo) : notice}" />

<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8" />
  <title>공지 수정</title>
  <link rel="stylesheet" href="<c:url value='/css/site.css'/>"/>
  <style>
    /* 공통 CSS 사용, 보조 여백만 최소 보강 */
    .edit-actions { display:flex; gap:8px; justify-content:flex-end; }
    .field { display:flex; flex-direction:column; gap:6px; margin-bottom:14px; }
    .field label { font-weight:700; color: var(--subtle-ink); }
    /* 공통 규격: 슬림 버튼/필드 (프로젝트 규약) */
    .form-wrap .input { height: 34px; }
    .actions.btn-row .btn,
    .edit-actions .btn { height:30px; min-width:60px; padding:0 10px; font-size:13px; border-radius:6px; }
  </style>
</head>

<body class="theme-warm page-bg">
<main class="container-main">
  <div class="page-wrap">
    <section class="page-cover">

      <div class="actions btn-row" style="justify-content:space-between; margin-bottom:8px;">
        <h2 class="page-title" style="margin:0;">공지 수정</h2>
        <a class="btn" href="<c:url value='/listNotice.do'/>">목록</a>
      </div>

      <c:choose>
        <c:when test="${empty n}">
          <div class="ui-alert">수정할 공지를 찾을 수 없습니다.</div>
        </c:when>
        <c:otherwise>
          <div class="form-wrap">
            <form action="<c:url value='/updateNotice.do'/>" method="post" accept-charset="UTF-8">
              <input type="hidden" name="noticeId" value="${n.noticeId}" />

              <div class="field">
                <label for="title">제목</label>
                <input id="title" name="title" type="text" maxlength="100"
                       class="input"
                       value="${fn:escapeXml(n.title)}" required />
              </div>

              <div class="field">
                <label for="content">내용</label>
                <textarea id="content" name="content" maxlength="4000" required
                          class="textarea" style="min-height:220px;">${fn:escapeXml(n.content)}</textarea>
              </div>

              <div class="edit-actions">
                <a class="btn"
                   href="<c:url value='/detailNotice.do'><c:param name='noticeId' value='${n.noticeId}'/></c:url>">취소</a>
                <button type="submit" class="btn">수정완료</button>
              </div>
            </form>
          </div>
        </c:otherwise>
      </c:choose>

    </section>
  </div>
</main>

<jsp:include page="/common/footer.jspf" />
</body>
</html>
