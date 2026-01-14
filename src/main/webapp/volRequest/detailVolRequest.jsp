<%-- /volRequest/detailVolRequest.jsp --%>
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c"   uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"  %>
<%@ taglib prefix="fn"  uri="http://java.sun.com/jsp/jstl/functions" %>

<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8"/>
  <title>봉사요청 상세</title>
  <link rel="stylesheet" href="<c:url value='/css/site.css'/>">
</head>

<body class="theme-warm page-bg">
  <%@ include file="/common/top.jspf"%>

  <%-- 상세 객체에서 volunteerId 뽑아오기 (프로젝트에 맞게 한 줄만 선택 사용) --%>
  <c:set var="volId" value="${volunteerId}" />
  <c:if test="${empty volId}">
    <c:set var="volId" value="${not empty volunteer ? volunteer.volunteerId : null}" />
  </c:if>

  <%-- 메인 컨테이너: 공통 레이아웃 클래스 적용 (기능 변경 없음) --%>
  <main class="container-main"
        data-volunteer-id="${volId}"
        data-context-path="${pageContext.request.contextPath}">

    <div class="page-wrap">
      <section class="page-cover">
        <h2 class="page-title">봉사요청 상세</h2>

        <%-- 상세 본문 --%>
        <div class="ui-card ui-card--lg" style="padding:20px;">
          <div class="form-grid cols-2">
            <div class="form-row">
              <label class="form-label"><strong>제목</strong></label>
              <div>${fn:escapeXml(volunteer.title)}</div>
            </div>

            <div class="form-row">
              <label class="form-label"><strong>작성자</strong></label>
              <div>${fn:escapeXml(volunteer.authorId)}</div>
            </div>

            <div class="form-row">
              <label class="form-label"><strong>연락처</strong></label>
              <div>${fn:escapeXml(volunteer.phone)}</div>
            </div>

            <div class="form-row">
              <label class="form-label"><strong>지역</strong></label>
              <div>${fn:escapeXml(volunteer.region)}</div>
            </div>

            <div class="form-row">
              <label class="form-label"><strong>카테고리</strong></label>
              <div>${fn:escapeXml(volunteer.category)}</div>
            </div>

            <div class="form-row">
              <label class="form-label"><strong>기간</strong></label>
              <div>
                <fmt:formatDate value="${startTime}" pattern="yyyy-MM-dd HH:mm" />
                ~
                <fmt:formatDate value="${endTime}"   pattern="yyyy-MM-dd HH:mm" />
              </div>
            </div>
          </div>

        <%-- 이미지 영역: 원본보기 + 자르지 않고 전체 보기 --%>
<c:if test="${not empty volunteer.image}">
  <c:url var="imgUrl" value="/images/${volunteer.image}" />
  <div class="seg"></div>

  <div class="media-frame detail-img">
    <a href="${pageContext.request.contextPath}${imgUrl}" target="_blank" rel="noopener" title="원본 보기(새 탭)">
      <img src="${pageContext.request.contextPath}${imgUrl}" alt="이미지">
    </a>
  </div>
</c:if>

   <%-- ✅ 상세내용 추가 --%>
          <div class="seg"></div>
          <div class="form-row" style="grid-column:1/-1">
            <label class="form-label"><strong>상세내용</strong></label>
            <div class="ui-card" style="white-space:pre-line;">
              ${fn:escapeXml(volunteer.content)}
            </div>
          </div>




        <!-- 상단 액션 영역 (버튼 줄 공통 클래스 적용) -->
        <div class="actions btn-row justify-end" style="margin-top:12px;">
          <!-- 작성자 본인: 수정 / 삭제 -->
          <c:if test="${sessionScope.user.userId == volunteer.authorId 
                     and volunteer.status ne '봉사완료' 
                     and volunteer.status ne '만료'}">

            <!-- 수정 -->
            <c:url var="editUrl" value="/updateVolRequestView.do">
              <c:param name="volunteerId" value="${volId}"/>
              <c:if test="${not empty param.region}"><c:param name="region" value="${param.region}"/></c:if>
              <c:if test="${not empty param.category}"><c:param name="category" value="${param.category}"/></c:if>
              <c:if test="${not empty param.page}"><c:param name="page" value="${param.page}"/></c:if>
            </c:url>
            <a class="btn" href="${editUrl}">수정</a>

            <!-- 삭제 -->
            <form method="post" action="<c:url value='/deleteVolRequest.do'/>" style="display:inline;"
                  onsubmit="return confirm('게시글을 삭제하시겠습니까?');">
              <input type="hidden" name="volunteerId" value="${volId}"/>
              <button class="btn" type="submit">삭제</button>
            </form>
          </c:if>

          <!-- 작성자 외 사용자: 신청 버튼 -->
          <c:if test="${sessionScope.user.userId != volunteer.authorId}">
            <c:choose>
              <c:when test="${volunteer.status == '모집중'}">
                <form method="post" action="<c:url value='/processVolRequest.do'/>" style="display:inline;">
                  <input type="hidden" name="volunteerId" value="${volId}"/>
                  <button class="btn" type="submit">신청하기</button>
                </form>
              </c:when>
              <c:otherwise>
                <button class="btn" type="button" disabled>신청불가 (${volunteer.status})</button>
              </c:otherwise>
            </c:choose>
          </c:if>
        </div>

        <div class="seg"></div>

        <%-- 댓글 섹션 --%>
        <a id="comment-area"></a>
        <h3 class="form-title">댓글</h3>

        <%-- 댓글 작성 폼 --%>
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

        <%-- 댓글 목록 컨테이너 --%>
        <div id="comment-list-container">
          <c:import url="/listComment.do">
            <c:param name="volunteerId" value="${volId}" />
            <c:param name="page" value="${param.commentPage != null ? param.commentPage : 1}" />
            <c:param name="pageSize"
                     value="${initParam['comment.pageSize'] != null ? initParam['comment.pageSize'] : initParam['pageSize']}" />
          </c:import>
        </div>

        <div class="seg"></div>

        <%-- 뒤로가기(목록 검색조건 유지) --%>
        <div class="actions btn-row">
          <c:url var="backUrl" value="/listVolRequest.do">
            <c:if test="${not empty (empty param.region ? requestScope.region : param.region)}">
              <c:param name="region" value="${empty param.region ? requestScope.region : param.region}" />
            </c:if>

            <c:if test="${not empty (empty param.category ? requestScope.category : param.category)}">
              <c:param name="category" value="${empty param.category ? requestScope.category : param.category}" />
            </c:if>

            <c:if test="${not empty (empty param.page ? requestScope.page : param.page)}">
              <c:param name="page" value="${empty param.page ? requestScope.page : param.page}" />
            </c:if>

            <c:if test="${not empty (empty param.searchCondition ? requestScope.searchCondition : param.searchCondition)}">
              <c:param name="searchCondition" value="${empty param.searchCondition ? requestScope.searchCondition : param.searchCondition}" />
            </c:if>

            <c:if test="${not empty (empty param.searchKeyword ? requestScope.searchKeyword : param.searchKeyword)}">
              <c:param name="searchKeyword" value="${empty param.searchKeyword ? requestScope.searchKeyword : param.searchKeyword}" />
            </c:if>

            <%-- 모집중 체크 상태일 때만 status 유지 --%>
            <c:if test="${(empty param.status ? requestScope.status : param.status) == '모집중'}">
              <c:param name="status" value="모집중" />
            </c:if>

            <%-- pageSize: 요청값이 있으면 사용, 없으면 initParam 기본값 --%>
            <c:param name="pageSize" value="${not empty (empty param.pageSize ? requestScope.pageSize : param.pageSize)
                                              ? (empty param.pageSize ? requestScope.pageSize : param.pageSize)
                                              : initParam['pageSize']}" />
          </c:url>

          <a class="btn" href="${backUrl}">목록으로</a>
        </div>
      </section>
    </div>
  </main>

  <%@ include file="/common/footer.jspf"%>

  <%-- 스크립트 경로/파일명/기능 변경 없음 --%>
  <script src="<c:url value='/js/comment.js'/>"></script>
</body>
</html>
