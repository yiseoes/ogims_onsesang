<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c"  uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8">
  <title>로그인</title>

  <!-- ✅ 공통/페이지 CSS만 링크 (site.css는 꼭 활성화) -->
  <link rel="stylesheet" href="<c:url value='/css/site.css'/>">

  <!-- (선택) 부트스트랩 제거 권장: 지금 페이지는 필요 없음
  <link rel="stylesheet" href="<c:url value='/css/bootstrap.min.css'/>">
  -->

  <!-- global JS (필요 시만 유지) -->
  <script src="<c:url value='/js/jquery-1.12.4.min.js'/>"></script>
  <script src="<c:url value='/js/bootstrap.min.js'/>"></script>

  <!-- 로그인 에러 팝업 -->
  <c:if test="${not empty loginError}">
    <script>
      alert('${fn:escapeXml(loginError)}');
      window.addEventListener('DOMContentLoaded', function(){
        var el = document.querySelector('input[name="userId"]');
        if(el) el.focus();
      });
    </script>
  </c:if>
</head>

<body class="login-page">
  <%@ include file="/common/top.jspf" %>

  <div class="login-card">
    <h2>로그인</h2>

    <form action="<c:url value='/login.do'/>" method="post">
      <div class="form-group">
        <label for="userId">아이디</label>
        <input id="userId" name="userId" maxlength="10" required
               pattern="[A-Za-z0-9]{1,10}"
               value="${not empty userId ? userId : param.userId}" />
      </div>

      <div class="form-group">
        <label for="password">비밀번호</label>
        <input id="password" type="password" name="password" required />
      </div>

      <!-- ✅ 버튼은 CSS에서 색/폭 통일 -->
      <button type="submit">로그인</button>

      <a class="link-join" href="<c:url value='/addUserView.do'/>">회원가입</a>
    </form>
  </div>
</body>
</html>
