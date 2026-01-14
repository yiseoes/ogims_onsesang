<%-- /volOffer/detailVolOffer.jsp --%>
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c"   uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"  %>
<%@ taglib prefix="fn"  uri="http://java.sun.com/jsp/jstl/functions" %>

<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8"/>
  <title>봉사제공 상세</title>
  <link rel="stylesheet" href="<c:url value='/css/site.css'/>">
</head>

<body class="theme-warm page-bg">
<%@ include file="/common/top.jspf"%>

<%-- 상세 객체/키값 바인딩(기능 유지) --%>
<c:set var="volId" value="${volunteerId}" />
<c:if test="${empty volId}">
  <c:set var="volId" value="${not empty volOffer ? volOffer.postId : null}" />
</c:if>

<%-- 화면에서 사용할 offer 바인딩 --%>
<c:set var="offer" value="${not empty volOffer ? volOffer : null}" />

<main class="container-main"
      data-volunteer-id="${volId}"
      data-context-path="${pageContext.request.contextPath}">
  <div class="page-wrap">
    <section class="page-cover">
      <h2 class="page-title">손길나눔 (봉사제공) 상세</h2>

      <div class="ui-card ui-card--lg" style="padding:20px;">

        <div class="form-grid cols-2">
          <div class="form-row">
            <label class="form-label"><strong>제목</strong></label>
            <div>${fn:escapeXml(offer.title)}</div>
          </div>

          <div class="form-row">
            <label class="form-label"><strong>작성자</strong></label>
            <div>${fn:escapeXml(offer.authorId)}</div>
          </div>

          <div class="form-row">
            <label class="form-label"><strong>연락처</strong></label>
            <div>${fn:escapeXml(offer.phone)}</div>
          </div>

          <div class="form-row">
            <label class="form-label"><strong>지역</strong></label>
            <div>${fn:escapeXml(offer.region)}</div>
          </div>

          <div class="form-row">
            <label class="form-label"><strong>카테고리</strong></label>
            <div>${fn:escapeXml(offer.category)}</div>
          </div>

          <div class="form-row">
            <label class="form-label"><strong>기간</strong></label>
            <div>
              <%-- 로컬데이트 규약: 어떤 타입이 와도 'yyyy-MM-dd HH:mm'로 안전 표시 --%>
              <c:set var="__s0" value="${offer.startTime}" />
              <c:set var="__e0" value="${offer.endTime}" />

              <c:set var="__s1" value="${fn:replace(__s0, 'T', ' ')}" />
              <c:set var="__e1" value="${fn:replace(__e0, 'T', ' ')}" />
              <%-- 밀리초/UTC 꼬리 제거 대비 간단 치환 --%>
              <c:set var="__s2" value="${fn:replace(__s1, 'Z', '')}" />
              <c:set var="__e2" value="${fn:replace(__e1, 'Z', '')}" />
              <c:set var="__s3" value="${fn:replace(__s2, '.0', '')}" />
              <c:set var="__e3" value="${fn:replace(__e2, '.0', '')}" />

              <c:choose>
                <c:when test="${not empty __s3}">
                  <c:choose>
                    <c:when test="${fn:length(__s3) >= 16}">
                      ${fn:substring(__s3, 0, 16)}
                    </c:when>
                    <c:otherwise>${__s3}</c:otherwise>
                  </c:choose>
                </c:when>
                <c:otherwise>-</c:otherwise>
              </c:choose>
              ~
              <c:choose>
                <c:when test="${not empty __e3}">
                  <c:choose>
                    <c:when test="${fn:length(__e3) >= 16}">
                      ${fn:substring(__e3, 0, 16)}
                    </c:when>
                    <c:otherwise>${__e3}</c:otherwise>
                  </c:choose>
                </c:when>
                <c:otherwise>-</c:otherwise>
              </c:choose>
            </div>
          </div>
        </div>

        <%-- 이미지(자르지 않고 보기 + 원본 새탭) --%>
        <c:if test="${not empty offer.image}">
          <c:url var="imgUrl" value="/images/${offer.image}" />
          <div class="seg"></div>
          <div class="media-frame detail-img">
            <a href="${pageContext.request.contextPath}${imgUrl}" target="_blank" rel="noopener" title="원본 보기(새 탭)">
              <img src="${pageContext.request.contextPath}${imgUrl}" alt="이미지">
            </a>
          </div>
        </c:if>
         <%-- ✅ 상세내용 출력 (이미지 아래에 위치) --%>
        <div class="seg"></div>
        <div class="form-row" style="grid-column:1/-1">
          <label class="form-label"><strong>상세내용</strong></label>
          <div class="ui-card" style="white-space:pre-line;">
            ${fn:escapeXml(offer.content)}
          </div>
        </div>
        
        

        <%-- 상단 액션(기능 유지) --%>
        <div class="actions btn-row justify-end" style="margin-top:12px;">
          <c:if test="${sessionScope.user.userId == offer.authorId}">
            <%-- 수정 --%>
            <c:url var="editUrl" value="/updateVolOfferView.do">
              <c:param name="postId" value="${offer.postId}"/>
              <c:if test="${not empty param.region}"><c:param name="region" value="${param.region}"/></c:if>
              <c:if test="${not empty param.category}"><c:param name="category" value="${param.category}"/></c:if>
              <c:if test="${not empty param.page}"><c:param name="page" value="${param.page}"/></c:if>
              <c:if test="${not empty param.status}"><c:param name="status" value="${param.status}"/></c:if>
              <c:if test="${not empty param.searchCondition}"><c:param name="searchCondition" value="${param.searchCondition}"/></c:if>
              <c:if test="${not empty param.searchKeyword}"><c:param name="searchKeyword" value="${param.searchKeyword}"/></c:if>
            </c:url>
            <a class="btn" href="${editUrl}">수정</a>

            <%-- 삭제 --%>
            <form method="post" action="<c:url value='/deleteVolOffer.do'/>" style="display:inline;"
                  onsubmit="return confirm('게시글을 삭제하시겠습니까?');">
              <input type="hidden" name="postId" value="${offer.postId}"/>
              <button class="btn" type="submit">삭제</button>
            </form>
          </c:if>
          
          <!-- 작성자 외 사용자: 신청 버튼 -->
          <c:if test="${sessionScope.user.userId != offer.authorId}">
            <c:choose>
              <c:when test="${offer.status == '모집중'}">
                <form method="post" action="<c:url value='/processVolOffer.do'/>" style="display:inline;">
                  <input type="hidden" name="volunteerId" value="${volId}"/>
                  <button class="btn" type="submit">신청하기</button>
                </form>
              </c:when>
              <c:otherwise>
                <button class="btn" type="button" disabled>신청불가 (${offer.status})</button>
              </c:otherwise>
            </c:choose>
          </c:if>
          
        </div>

        <div class="seg"></div>

        <%-- 댓글 --%>
        <a id="comment-area"></a>
        <h3 class="form-title">댓글</h3>

        <%-- 작성 폼 (기능/엔드포인트/파라미터 유지) --%>
        <form id="comment-add-form" method="post" action="<c:url value='/addComment.do'/>"
              class="form-wrap" style="margin-bottom:12px;">
          <input type="hidden" name="volunteerId" value="${volId}"/>

          <div class="form-row">
            <textarea name="content" rows="3" maxlength="4000" required
                      class="ui-textarea"
                      placeholder="댓글을 입력하세요"></textarea>
          </div>

          <div class="actions btn-row justify-end">
            <button type="submit" class="btn">등록</button>
          </div>
        </form>

        <%-- 목록 --%>
        <div id="comment-list-container">
          <c:import url="/listComment.do">
            <c:param name="volunteerId" value="${volId}" />
            <c:param name="page" value="${param.commentPage != null ? param.commentPage : 1}" />
            <c:param name="pageSize"
                     value="${initParam['comment.pageSize'] != null ? initParam['comment.pageSize'] : initParam['pageSize']}" />
          </c:import>
        </div>

        <div class="seg"></div>

        <%-- 뒤로가기(검색조건 유지) --%>
        <div class="actions btn-row">
          <c:url var="backUrl" value="/listVolOffer.do">
            <c:if test="${not empty param.region}">
              <c:param name="region" value="${param.region}" />
            </c:if>
            <c:if test="${not empty param.category}">
              <c:param name="category" value="${param.category}" />
            </c:if>
            <c:if test="${not empty param.page}">
              <c:param name="page" value="${param.page}" />
            </c:if>
            <c:if test="${not empty param.status}">
              <c:param name="status" value="${param.status}" />
            </c:if>
            <c:if test="${not empty param.searchCondition}">
              <c:param name="searchCondition" value="${param.searchCondition}" />
            </c:if>
            <c:if test="${not empty param.searchKeyword}">
              <c:param name="searchKeyword" value="${param.searchKeyword}" />
            </c:if>
            <c:param name="pageSize" value="${initParam['pageSize']}" />
          </c:url>
          <a class="btn" href="${backUrl}">목록으로</a>
        </div>

      </div>
    </section>
  </div>
</main>

<%@ include file="/common/footer.jspf"%>

<%-- 댓글 스크립트(기능 그대로) --%>
<script src="<c:url value='/js/comment.js'/>"></script>
</body>
</html>
