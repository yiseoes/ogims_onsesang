<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c"  uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<html>
<head>
  <title>내 봉사 요청 이력</title>
  <!-- 공통 CSS만 사용 -->
  <link rel="stylesheet" href="<c:url value='/css/site.css'/>">
</head>
<body>

<div class="form-wrap">
  <h2 class="form-title">내 손길요청 흔적 보기</h2>
  <p class="form-sub">내가 작성한 봉사요청 글 목록입니다.</p>

  <!-- 페이징 기본값 (규약 준수: page / pageSize) -->
  <c:set var="p"        value="${empty page ? 1 : page}" />
  <c:set var="size"     value="${empty pageSize ? 10 : pageSize}" />
  <c:set var="total"    value="${empty total ? 0 : total}" />
  <c:set var="last"     value="${ total <= 0 ? 1 : ((total + size - 1) / size) }" />
  <c:set var="p"        value="${p > last ? last : (p < 1 ? 1 : p)}" />
  <c:set var="startNo"  value="${total - ((p-1) * size)}" />

  <table class="table mt-16">
    <thead>
      <tr>
        <th style="width:80px;">순번</th>
        <th>타이틀</th>
        <th style="width:160px;">작성일</th>
      </tr>
    </thead>
    <tbody>
      <c:choose>
        <c:when test="${empty list}">
          <tr><td colspan="3" class="text-center">작성한 요청이 없습니다.</td></tr>
        </c:when>
        <c:otherwise>
          <c:forEach var="r" items="${list}" varStatus="st">
            <tr>
              <td><c:out value="${startNo - st.index}"/></td>
              <td>
                <a href="<c:url value='/detailVolRequest.do'>
                           <c:param name='volunteerId' value='${r.postId}'/>
                         </c:url>">
                  <c:out value="${r.title}"/>
                </a>
              </td>
              <td><c:out value="${fn:substring(r.createdAt, 0, 10)}"/></td>
            </tr>
          </c:forEach>
        </c:otherwise>
      </c:choose>
    </tbody>
  </table>

  <!-- 페이지네이션 (공통 버튼/레이아웃 클래스만 사용) -->
 <!-- 이전 -->
<a class="btn" href="<c:url value='/myVolHistory.do'>
                       <c:param name='page' value='${p-1}'/>
                       <c:param name='pageSize' value='${size}'/>
                     </c:url>">이전</a>

<!-- 다음 -->
<a class="btn" href="<c:url value='/myVolHistory.do'>
                       <c:param name='page' value='${p+1}'/>
                       <c:param name='pageSize' value='${size}'/>
                     </c:url>">다음</a>


</body>
</html>
