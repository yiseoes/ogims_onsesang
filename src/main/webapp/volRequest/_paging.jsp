<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<div class="pagination">

  <%-- ====== URL 기본 파라미터 유지 ====== --%>
  <c:url var="baseUrl" value="/listVolRequest.do">
    <c:param name="searchCondition" value="${param.searchCondition}"/>
    <c:param name="searchKeyword"   value="${param.searchKeyword}"/>
    <c:param name="category"        value="${param.category}"/>
    <c:param name="status"          value="${param.status}"/>
  </c:url>

  <%-- ====== 이전 묶음 ====== --%>
  <c:if test="${page.maxPage > 1}">
    <c:if test="${page.beginUnitPage > 1}">
      <a href="${baseUrl}&page=${page.beginUnitPage - 1}">&laquo;</a>
    </c:if>

    <%-- 페이지 번호들 --%>
    <c:forEach var="p" begin="${page.beginUnitPage}" end="${page.endUnitPage}">
      <c:choose>
        <c:when test="${p == page.currentPage}">
          <span class="current">${p}</span>
        </c:when>
        <c:otherwise>
          <a href="${baseUrl}&page=${p}">${p}</a>
        </c:otherwise>
      </c:choose>
    </c:forEach>

    <%-- 다음 묶음 --%>
    <c:if test="${page.endUnitPage < page.maxPage}">
      <a href="${baseUrl}&page=${page.endUnitPage + 1}">&raquo;</a>
    </c:if>
  </c:if>
</div>

<%-- ====== 등록 버튼 (오른쪽 배치) ====== --%>
<div class="actions btn-row justify-end" style="margin-top:12px;">
  <c:if test="${not empty sessionScope.user}">
    <a href="<c:url value='/addVolRequestView.do'/>" class="btn ui-btn--primary">등록</a>
  </c:if>
</div>
