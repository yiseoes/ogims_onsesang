<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:set var="login" value="${sessionScope.loginUser}" />
<section class="section">
  <h2 class="page-title">회원 탈퇴</h2>

  <c:choose>
    <c:when test="${empty login}">
      <p>로그인이 필요합니다.</p>
      <p><a class="btn" href="<c:url value='/loginView.do'/>">로그인 하러 가기</a></p>
    </c:when>
    <c:otherwise>
      <div class="card">
        <p>정말로 탈퇴하시겠습니까? 이 작업은 되돌릴 수 없습니다.</p>
        <form method="post" action="<c:url value='/deleteAcc.do'/>">
          <div class="btn-row">
            <button type="submit" class="btn secondary"
                    onclick="return confirm('정말 탈퇴하시겠습니까?');">회원 탈퇴</button>
            <a href="javascript:history.back()" class="btn">취소</a>
          </div>
        </form>
      </div>
    </c:otherwise>
  </c:choose>
</section>
